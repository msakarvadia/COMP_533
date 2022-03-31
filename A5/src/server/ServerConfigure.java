package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import assignments.util.mainArgs.ClientArgsProcessor;
import assignments.util.mainArgs.ServerArgsProcessor;
import client.ClientRemoteInterfaceGIPC;
import client.ClientRemoteInterfaceRMI;
import coupledsims.AStandAloneTwoCoupledHalloweenSimulations;
import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import port.ATracingConnectionListener;
import server.remote.ServerRemoteInterfaceRMI;
import server.remote.ServerRemoteObjectGIPC;
import server.remote.ServerRemoteObjectRMI;
import util.annotations.Tags;
import util.interactiveMethodInvocation.IPCMechanism;
import util.interactiveMethodInvocation.SimulationParametersControllerFactory;
import util.misc.ThreadSupport;
import util.tags.DistributedTags;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.PortTraceUtility;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.consensus.ProposalLearnedNotificationSent;
import util.trace.port.consensus.RemoteProposeRequestReceived;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.gipc.GIPCObjectRegistered;
import util.trace.port.rpc.gipc.GIPCRPCTraceUtility;
import util.trace.port.rpc.gipc.GIPCRegistryCreated;
import util.trace.port.rpc.rmi.RMIObjectRegistered;
import util.trace.port.rpc.rmi.RMIRegistryLocated;
import util.trace.port.rpc.rmi.RMITraceUtility;

@Tags({DistributedTags.SERVER_CONFIGURER, DistributedTags.RMI, DistributedTags.GIPC})
public class ServerConfigure extends AStandAloneTwoCoupledHalloweenSimulations implements ServerRemoteInterfaceRMI{
	List<ClientRemoteInterfaceGIPC> clientListGIPC = new ArrayList<ClientRemoteInterfaceGIPC>();
	List<ClientRemoteInterfaceGIPC> clientListRMI = new ArrayList<ClientRemoteInterfaceGIPC>();

	private static String RMI_SERVER_HOST_NAME;
	private static int RMI_SERVER_PORT;
	private static String SERVER_NAME;

	// A5
	private static int GIPC_SERVER_PORT;
	protected static GIPCRegistry gipcRegistry;

	// int aProposalNumber = 0;

	@Override
	public void processArgs(String[] args) {
		System.out.println("Registry host:" + ClientArgsProcessor.getRegistryHost(args));
		System.out.println("Registry port:" + ClientArgsProcessor.getRegistryPort(args));
		System.out.println("Server host:" + ClientArgsProcessor.getServerHost(args));
		System.out.println("Headless:" + ClientArgsProcessor.getHeadless(args));
		System.out.println("Client name:" + ClientArgsProcessor.getClientName(args));

		// Make sure you set this property when processing args
		System.setProperty("java.awt.headless", ClientArgsProcessor.getHeadless(args));

		RMI_SERVER_HOST_NAME = ServerArgsProcessor.getRegistryHost(args);
		RMI_SERVER_PORT = ServerArgsProcessor.getRegistryPort(args);
		SERVER_NAME = "SERVER";
		GIPC_SERVER_PORT = ServerArgsProcessor.getGIPCServerPort(args);
		// ClientArgsProcessor.getServerHost(args);

	}

	@Override
	public void registerClientGIPC(ClientRemoteInterfaceGIPC aClient) {
		
		clientListGIPC.add(aClient);
		System.out.println("Client registered GIPC");
		System.out.println(aClient);
		System.out.println(clientListGIPC);
	}
	
	@Override
	public void registerClientRMI(ClientRemoteInterfaceGIPC aClient) {
		
		clientListRMI.add(aClient);
		System.out.println("Client registered RMI");
		System.out.println(aClient);
		System.out.println(clientListRMI);
	}

	@Override
	public void broadcast(String aNewCommand, ClientRemoteInterfaceGIPC originalClient, int aProposalNumber){
		List<ClientRemoteInterfaceGIPC> clientList = clientListGIPC;
		
		// TODO Check is this is where delay is needed
		long aDelay = getDelay();
		if (aDelay > 0) {
			ThreadSupport.sleep(aDelay);
		}

		System.out.println("Command recieved for broadcast: " + aNewCommand);
		RemoteProposeRequestReceived.newCase(this, SERVER_NAME, aProposalNumber, aNewCommand);
		ProposalLearnedNotificationSent.newCase(this, SERVER_NAME, aProposalNumber, aNewCommand);
		
		if(clientListGIPC.isEmpty()) {
			clientList = clientListRMI;
			System.out.println("USING RMI IN SERVER");
		}
		else {
			clientList = clientListGIPC;
			System.out.println("USING GIPC IN SERVER");
		}
		System.out.println(clientList);
		System.out.println(clientList.size());
		for (ClientRemoteInterfaceGIPC client : clientList) {
			System.out.println(client);
			if (client.equals(originalClient)) {
				if (aNewCommand.charAt(0) == 'q') {
					// Need to quit
					try {
						client.quit(0);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				continue;
			}
			
			if (aNewCommand.charAt(0) == 'q') {

				try {
					client.quit(0);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				continue;
			}

			try {
				client.inCoupler(aNewCommand, aProposalNumber);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//ProposalLearnedNotificationSent.newCase(this, SERVER_NAME, aProposalNumber, aNewCommand);

			//if (aNewCommand.charAt(0) == 'q') {
				// Need to quit
			//	this.quit(0);
			//}
		}
		if (aNewCommand.charAt(0) == 'q') {
			// Need to quit
			this.quit(0);
		}

	}

	@Override
	public void broadcastIPCMechanism(IPCMechanism mechanism, ClientRemoteInterfaceGIPC originalClient, int aProposalNumber, boolean broadcast) {
		List<ClientRemoteInterfaceGIPC> clientList;
		
		// TODO Check is this is where delay is needed
		long aDelay = getDelay();
		if (aDelay > 0) {
			ThreadSupport.sleep(aDelay);
		}

		System.out.println("IPC Mechanism recieved for broadcast: " + mechanism);
		setIPCMechanism(mechanism);
		setBroadcastMetaState(broadcast);
		
		

		if(clientListGIPC.isEmpty()) {
			clientList = clientListRMI;
			System.out.println("USING RMI IN SERVER");
		}
		else {
			clientList = clientListGIPC;
			System.out.println("USING GIPC IN SERVER");
		}
		
		if (broadcast) {
			RemoteProposeRequestReceived.newCase(this, SERVER_NAME, aProposalNumber, mechanism);
			
			for (ClientRemoteInterfaceGIPC client : clientList) {
				if (client.equals(originalClient)) {
					continue;
				}

				try {
					client.changeIPCMechanism(mechanism);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ProposalLearnedNotificationSent.newCase(this, SERVER_NAME, aProposalNumber, mechanism);
			}
		}

	}

	@Override
	protected void setTracing() {
		// A5
		FactoryTraceUtility.setTracing();
		BeanTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();
		GIPCRPCTraceUtility.setTracing();
		NIOTraceUtility.setTracing();

		// A4
		PortTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		FactoryTraceUtility.setTracing();
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();
		trace(true);
	}

	@Override
	protected void init(String[] args) {

		setTracing();

		this.processArgs(args);
		createGIPCRegistry();

		try {
			final Registry rmiRegistry = LocateRegistry.getRegistry(RMI_SERVER_HOST_NAME, RMI_SERVER_PORT);
			RMIRegistryLocated.newCase(this, RMI_SERVER_HOST_NAME, RMI_SERVER_PORT, rmiRegistry);
			// Create remote server object

			// create proxy of remote server object
			// UnicastRemoteObject.exportObject(server, 0);
			UnicastRemoteObject.exportObject(this, 0);
			// send server to RMI server
			// rmiRegistry.rebind(SERVER_NAME, server);
			rmiRegistry.rebind(SERVER_NAME, this);

			RMIObjectRegistered.newCase(this, SERVER_NAME, (ServerRemoteInterfaceRMI) this, rmiRegistry);

			System.out.println("Server proxy sent to RMI Registry");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void start(String[] args) {
		init(args);
		// register a callback to process actions denoted by the user commands
		SimulationParametersControllerFactory.getSingleton().addSimulationParameterListener(this);
		// use the calling back library
		SimulationParametersControllerFactory.getSingleton().processCommands();		
		//init(args);

	}

	@Override
	public void fakeMethod(String stringOne, String stringTwo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fakeMethodTwo(String stringOne, ClientRemoteInterfaceRMI client) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void fakeMethodThree(String stringOne, ClientRemoteInterfaceGIPC client) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void fakeMethodFour(String stringOne, boolean trueFalse) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void fakeMethodFive(String stringOne, IPCMechanism mechanism) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void createGIPCRegistry() {
		System.out.println("GIPC_SERVER_PORT: ");
		System.out.println(GIPC_SERVER_PORT);
		gipcRegistry = GIPCLocateRegistry.createRegistry(GIPC_SERVER_PORT);
		GIPCRegistryCreated.newCase(this, GIPC_SERVER_PORT);

		final ServerRemoteInterfaceRMI server = new ServerRemoteObjectGIPC();
		gipcRegistry.rebind(SERVER_NAME, server);
		GIPCObjectRegistered.newCase(this, SERVER_NAME, this, gipcRegistry);
		gipcRegistry.getInputPort().addConnectionListener(new ATracingConnectionListener(gipcRegistry.getInputPort()));
	}

	@Override
	public void registerClient(ClientRemoteInterfaceRMI aClient) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void broadcast(String aNewCommand, ClientRemoteInterfaceRMI originalClient, int aProposalNumber)
			throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
