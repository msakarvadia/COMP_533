package server.remote;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import assignments.util.mainArgs.ClientArgsProcessor;
import assignments.util.mainArgs.ServerArgsProcessor;
import client.ClientRemoteInterfaceRMI;
import util.annotations.Tags;
import util.misc.ThreadSupport;
import util.tags.DistributedTags;
import coupledsims.AStandAloneTwoCoupledHalloweenSimulations;
import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import port.ATracingConnectionListener;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.PortTraceUtility;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.consensus.ProposalLearnedNotificationSent;
import util.trace.port.consensus.RemoteProposeRequestReceived;
import util.trace.port.rpc.rmi.RMIRegistryLocated;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.rmi.RMITraceUtility;
import util.trace.port.rpc.gipc.GIPCRPCTraceUtility;
import util.trace.port.rpc.gipc.GIPCRegistryCreated;
import 	util.trace.port.rpc.rmi.RMIObjectRegistered;

@Tags({DistributedTags.SERVER_REMOTE_OBJECT, DistributedTags.RMI, DistributedTags.GIPC})
public class ServerRemoteObjectRMI extends AStandAloneTwoCoupledHalloweenSimulations implements ServerRemoteInterfaceRMI{
	List<ClientRemoteInterfaceRMI> clientList = new ArrayList<ClientRemoteInterfaceRMI>();
	
	private static  String RMI_SERVER_HOST_NAME;
	private static int RMI_SERVER_PORT;
	private static String SERVER_NAME;
	
	//A5
	private static int GIPC_SERVER_PORT;
	protected static GIPCRegistry gipcRegistry;
	
	//int aProposalNumber = 0;
	

	
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
				//ClientArgsProcessor.getServerHost(args);
		
		
	}
	
	@Override
	public void registerClient(ClientRemoteInterfaceRMI aClient) throws RemoteException {
		// TODO Auto-generated method stub
		clientList.add(aClient);
		System.out.println("Client registered");
		
	}

	@Override
	public void broadcast(String aNewCommand, ClientRemoteInterfaceRMI originalClient, int aProposalNumber) throws RemoteException {
		
		//TODO Check is this is where delay is needed
		long aDelay = getDelay(); 
		if (aDelay > 0) {
			ThreadSupport.sleep(aDelay);
		}
		
		System.out.println("Command recieved for broadcast: "+ aNewCommand);
		RemoteProposeRequestReceived.newCase(this, SERVER_NAME, aProposalNumber, aNewCommand);
		
		for (ClientRemoteInterfaceRMI client : clientList) {
			if(client.equals(originalClient)) {
				if (aNewCommand.charAt(0) == 'q') {
					//Need to quit
					this.quit(0);
				}
				continue;
			}
			
			client.inCoupler(aNewCommand, aProposalNumber);
			ProposalLearnedNotificationSent.newCase(this, SERVER_NAME, aProposalNumber, aNewCommand);
			
			if (aNewCommand.charAt(0) == 'q') {
				//Need to quit
				this.quit(0);
			}
		}
				
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
	protected void init(String[] args) {
		
		setTracing();

		this.processArgs(args);
		
		
		try {
			final Registry rmiRegistry = LocateRegistry.getRegistry(RMI_SERVER_HOST_NAME, RMI_SERVER_PORT);
			RMIRegistryLocated.newCase(this, RMI_SERVER_HOST_NAME, RMI_SERVER_PORT, rmiRegistry);
			//Create remote server object
			final ServerRemoteInterfaceRMI server = new ServerRemoteObjectRMI();
			//create proxy of remote server object
			//UnicastRemoteObject.exportObject(server, 0);
			UnicastRemoteObject.exportObject(this, 0);
			//send server to RMI server
			//rmiRegistry.rebind(SERVER_NAME, server);
			rmiRegistry.rebind(SERVER_NAME, this);
			
			RMIObjectRegistered.newCase(this, SERVER_NAME, (ServerRemoteInterfaceRMI) this, rmiRegistry);
			
			System.out.println("Server proxy sent to RMI Registry");
			
			
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void  start (String[] args) {
		init(args);
		

	}

	@Override
	public void fakeMethod(String stringOne, String stringTwo){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fakeMethodTwo(String stringOne, ClientRemoteInterfaceRMI client) {
		// TODO Auto-generated method stub
		
	}

}
