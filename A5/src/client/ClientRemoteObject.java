package client;

import util.annotations.Tags;
import util.interactiveMethodInvocation.IPCMechanism;
import util.misc.ThreadSupport;
import util.tags.DistributedTags;

import java.beans.PropertyChangeListener;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


import assignments.util.mainArgs.ClientArgsProcessor;
import coupledsims.AStandAloneTwoCoupledHalloweenSimulations;
import coupledsims.Simulation;
import coupledsims.Simulation1;
import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import main.BeauAndersonFinalProject;
import server.remote.ServerRemoteInterfaceRMI;
import server.remote.ServerRemoteObjectGIPC;
import server.remote.ServerRemoteInterfaceGIPC;
import stringProcessors.HalloweenCommandProcessor;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.PortTraceUtility;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.consensus.ProposalLearnedNotificationReceived;
import util.trace.port.consensus.ProposedStateSet;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.gipc.GIPCObjectLookedUp;
import util.trace.port.rpc.gipc.GIPCRPCTraceUtility;
import util.trace.port.rpc.gipc.GIPCRegistryLocated;
import util.trace.port.rpc.rmi.RMIObjectLookedUp;
import util.trace.port.rpc.rmi.RMIRegistryLocated;
import util.trace.port.rpc.rmi.RMITraceUtility;

@Tags({ DistributedTags.CLIENT_REMOTE_OBJECT, DistributedTags.RMI, DistributedTags.GIPC })
public class ClientRemoteObject extends AStandAloneTwoCoupledHalloweenSimulations implements ClientRemoteInterfaceRMI, ClientRemoteInterfaceGIPC {
	HalloweenCommandProcessor commandProcessor;
	protected int NUM_EXPERIMENT_COMMANDS = 500;
	public static final String EXPERIMENT_COMMAND_1 = "move 1 -1";
	public static final String EXPERIMENT_COMMAND_2 = "undo";
	protected PropertyChangeListener simulationCoupler;
	ServerRemoteInterfaceGIPC server = null;
	ServerRemoteInterfaceGIPC serverGIPC = null;
	

	private static String RMI_SERVER_HOST_NAME;
	private static int RMI_SERVER_PORT;
	private static String SERVER_NAME;
	private static String CLIENT_NAME;
	
	//A5
	private static int GIPC_SERVER_PORT;
	protected static GIPCRegistry gipcRegistry;
	private static String GIPC_SERVER_NAME ;
	private static boolean broadcastIPCMechansim = false;
	
	PropertyChangeListener clientOutCoupler;

	@Override
	public HalloweenCommandProcessor createSimulation(String aPrefix) {
		return BeauAndersonFinalProject.createSimulation(aPrefix, Simulation1.SIMULATION1_X_OFFSET,
				Simulation.SIMULATION_Y_OFFSET, Simulation.SIMULATION_WIDTH, Simulation.SIMULATION_HEIGHT,
				Simulation1.SIMULATION1_X_OFFSET, Simulation.SIMULATION_Y_OFFSET);
	}

	@Override
	public void processArgs(String[] args) {
		System.out.println("Registry host:" + ClientArgsProcessor.getRegistryHost(args));
		System.out.println("Registry port:" + ClientArgsProcessor.getRegistryPort(args));
		System.out.println("Server host:" + ClientArgsProcessor.getServerHost(args));
		System.out.println("Headless:" + ClientArgsProcessor.getHeadless(args));
		System.out.println("Client name:" + ClientArgsProcessor.getClientName(args));

		// Make sure you set this property when processing args
		System.setProperty("java.awt.headless", ClientArgsProcessor.getHeadless(args));

		RMI_SERVER_HOST_NAME = ClientArgsProcessor.getRegistryHost(args);
		RMI_SERVER_PORT = ClientArgsProcessor.getRegistryPort(args);
		SERVER_NAME = "SERVER";
				//ClientArgsProcessor.getServerHost(args);
		CLIENT_NAME = ClientArgsProcessor.getClientName(args);


		//A5
		GIPC_SERVER_PORT = ClientArgsProcessor.getGIPCPort(args);
		GIPC_SERVER_NAME = ClientArgsProcessor.getServerHost(args);
		
	}
	
	@Override
	protected void setTracing() {
		//A5
		FactoryTraceUtility.setTracing();
		BeanTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();
		GIPCRPCTraceUtility.setTracing();
		NIOTraceUtility.setTracing();

		//A4
		PortTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		FactoryTraceUtility.setTracing();		
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();
		trace(true);
	}

	@Override
	public void init(String[] args) {
		setTracing();

		this.processArgs(args);
		// Ideally the prefixes should be main args
		commandProcessor = createSimulation(Simulation1.SIMULATION1_PREFIX);
		
		//Locate GIPC Server
		gipcRegistry = GIPCLocateRegistry.getRegistry(GIPC_SERVER_NAME, GIPC_SERVER_PORT, CLIENT_NAME);
		GIPCRegistryLocated.newCase(this, GIPC_SERVER_NAME, GIPC_SERVER_PORT, CLIENT_NAME);
		
		//Get GIPC server here
		serverGIPC = (ServerRemoteInterfaceGIPC) gipcRegistry.lookup(ServerRemoteObjectGIPC.class, SERVER_NAME);
		GIPCObjectLookedUp.newCase(this, serverGIPC, ServerRemoteObjectGIPC.class, SERVER_NAME, gipcRegistry);
		
		//Register Client with GIPC Proxy
		try {
			serverGIPC.registerClientGIPC(this);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// Locate RMI Server
		Registry rmiRegistry = null;
		try {
			rmiRegistry = LocateRegistry.getRegistry(RMI_SERVER_HOST_NAME, RMI_SERVER_PORT);
			RMIRegistryLocated.newCase(this, RMI_SERVER_HOST_NAME, RMI_SERVER_PORT, rmiRegistry);
		} catch (RemoteException e3) {
			e3.printStackTrace();
		}
		// Get RMI server here
		
		try {
			server = (ServerRemoteInterfaceGIPC) rmiRegistry.lookup(SERVER_NAME);
			RMIObjectLookedUp.newCase(this, server, SERVER_NAME, rmiRegistry);
		} catch (AccessException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (RemoteException e2) {

			e2.printStackTrace();
		} catch (NotBoundException e2) {
			e2.printStackTrace();
		}

		// export client to create proxy
		try {
			UnicastRemoteObject.exportObject(this, 0);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}

		// have the server register the exported client
		try {
			server.registerClientRMI(this);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		
		clientOutCoupler = new ClientOutCoupler(server, this, CLIENT_NAME);
		//clientOutCoupler = new ClientOutCoupler(serverGIPC, this, CLIENT_NAME);
		// Add propertyChangeListener
		commandProcessor.addPropertyChangeListener(clientOutCoupler);
		
		System.out.println("added server as a property change listener of client");
	}

	@Override
	public void inCoupler(String aNewCommand, int proposalNumber) {
		//final int aProposalNumber = proposalNumber;
		System.out.println("recieved broadcased command: "+ aNewCommand);
		ProposalLearnedNotificationReceived.newCase(this, CLIENT_NAME, proposalNumber, aNewCommand);
		commandProcessor.processCommand(aNewCommand);
		ProposedStateSet.newCase(this, CLIENT_NAME, proposalNumber, aNewCommand);
		System.out.println("executed command");
		
	}
	
	@Override
	/*
	 * You will need to delay not command input but sends(non-Javadoc)
	 */
	public void simulationCommand(String aCommand) {
		//long aDelay = getDelay(); 
		//if (aDelay > 0) {
		//	ThreadSupport.sleep(aDelay);
		//}
		IPCMechanism mechanism = getIPCMechanism();
		System.out.println("IPC Mechanism");
		System.out.println(mechanism);
		
		
		if(mechanism.toString().equals("GIPC")) {
						
			commandProcessor.removePropertyChangeListener(clientOutCoupler);
			clientOutCoupler = new ClientOutCoupler(serverGIPC, this, CLIENT_NAME);
			commandProcessor.addPropertyChangeListener(clientOutCoupler);
			System.out.println("using gipc proxy server");
		}
		if(mechanism.toString().equals("RMI")) {
			commandProcessor.removePropertyChangeListener(clientOutCoupler);
			clientOutCoupler = new ClientOutCoupler(server, this, CLIENT_NAME);
			commandProcessor.addPropertyChangeListener(clientOutCoupler);
			System.out.println("using RMI proxy server");
		}
		commandProcessor.setInputString(aCommand); // all commands go to the first command window
	}
	
	@Override	
	public void quit(int aCode) {
		System.exit(aCode);
	}
	
	@Override
	public void localProcessingOnly(boolean newValue) {
		super.localProcessingOnly(newValue);
		if (isLocalProcessingOnly()) {
			commandProcessor.removePropertyChangeListener(clientOutCoupler);
			
		} else {
			commandProcessor.addPropertyChangeListener(clientOutCoupler);
			
		}
	}
	
	@Override
	public void broadcastMetaState(boolean broadcast) {
		broadcastIPCMechanism = broadcast;
		
	}
	
	@Override
	public void changeIPCMechanism(IPCMechanism mechanism, int proposalNumber) {
		ProposalLearnedNotificationReceived.newCase(this, CLIENT_NAME, proposalNumber, mechanism);
		setIPCMechanism(mechanism);
		ProposedStateSet.newCase(this, CLIENT_NAME, proposalNumber, mechanism);
		
	}


}
