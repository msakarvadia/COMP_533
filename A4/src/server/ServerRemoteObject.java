package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import assignments.util.mainArgs.ClientArgsProcessor;
import assignments.util.mainArgs.ServerArgsProcessor;
import client.ClientRemoteInterface;
import util.annotations.Tags;
import util.misc.ThreadSupport;
import util.tags.DistributedTags;
import coupledsims.AStandAloneTwoCoupledHalloweenSimulations;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.PortTraceUtility;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.consensus.ProposalLearnedNotificationSent;
import util.trace.port.consensus.RemoteProposeRequestReceived;
import util.trace.port.rpc.rmi.RMIRegistryLocated;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.rmi.RMITraceUtility;
import 	util.trace.port.rpc.rmi.RMIObjectRegistered;

@Tags({DistributedTags.SERVER_REMOTE_OBJECT, DistributedTags.RMI})
public class ServerRemoteObject extends AStandAloneTwoCoupledHalloweenSimulations implements ServerRemoteInterface{
	List<ClientRemoteInterface> clientList = new ArrayList<ClientRemoteInterface>();
	
	private static  String RMI_SERVER_HOST_NAME;
	private static int RMI_SERVER_PORT;
	private static String SERVER_NAME;
	
	//int aProposalNumber = 0;
	
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
				//ClientArgsProcessor.getServerHost(args);
		
		
	}
	
	@Override
	public void registerClient(ClientRemoteInterface aClient) throws RemoteException {
		// TODO Auto-generated method stub
		clientList.add(aClient);
		System.out.println("Client registered");
		
	}

	@Override
	public void broadcast(String aNewCommand, ClientRemoteInterface originalClient, int aProposalNumber) throws RemoteException {
		
		//TODO Check is this is where delay is needed
		long aDelay = getDelay(); 
		if (aDelay > 0) {
			ThreadSupport.sleep(aDelay);
		}
		
		System.out.println("Command recieved for broadcast: "+ aNewCommand);
		RemoteProposeRequestReceived.newCase(this, SERVER_NAME, aProposalNumber, aNewCommand);
		
		for (ClientRemoteInterface client : clientList) {
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
			final ServerRemoteInterface server = new ServerRemoteObject();
			//create proxy of remote server object
			//UnicastRemoteObject.exportObject(server, 0);
			UnicastRemoteObject.exportObject(this, 0);
			//send server to RMI server
			//rmiRegistry.rebind(SERVER_NAME, server);
			rmiRegistry.rebind(SERVER_NAME, this);
			
			RMIObjectRegistered.newCase(this, SERVER_NAME, (ServerRemoteInterface) this, rmiRegistry);
			
			System.out.println("Server proxy sent to RMI Registry");
			
			
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void  start (String[] args) {
		init(args);
		

	}

}
