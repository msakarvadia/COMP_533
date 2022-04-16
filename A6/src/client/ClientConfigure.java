package client;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ArrayBlockingQueue;

import assignments.util.MiscAssignmentUtils;
import assignments.util.mainArgs.ClientArgsProcessor;
import coupledsims.AStandAloneTwoCoupledHalloweenSimulations;
import coupledsims.Simulation;
import coupledsims.Simulation1;
import inputport.nio.manager.NIOManager;
import inputport.nio.manager.NIOManagerFactory;
import inputport.nio.manager.factories.classes.AConnectCommandFactory;
import inputport.nio.manager.factories.selectors.ConnectCommandFactorySelector;
import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import main.BeauAndersonFinalProject;
import readThread.ClientReadThread;
import readThread.ReadThreadInterface;
import server.remote.ServerRemoteInterfaceGIPC;
import server.remote.ServerRemoteInterfaceRMI;
import server.remote.ServerRemoteObjectGIPC;
import server.remote.ServerRemoteObjectRMI;
import stringProcessors.HalloweenCommandProcessor;
import util.annotations.Tags;
import util.interactiveMethodInvocation.IPCMechanism;
import util.tags.DistributedTags;
import util.trace.Tracer;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.PerformanceExperimentEnded;
import util.trace.port.PerformanceExperimentStarted;
import util.trace.port.PortTraceUtility;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.consensus.ProposalLearnedNotificationReceived;
import util.trace.port.consensus.ProposedStateSet;
import util.trace.port.consensus.RemoteProposeRequestSent;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.gipc.GIPCObjectLookedUp;
import util.trace.port.rpc.gipc.GIPCRPCTraceUtility;
import util.trace.port.rpc.gipc.GIPCRegistryLocated;
import util.trace.port.rpc.rmi.RMIObjectLookedUp;
import util.trace.port.rpc.rmi.RMIRegistryLocated;
import util.trace.port.rpc.rmi.RMITraceUtility;

@Tags({DistributedTags.CLIENT_CONFIGURER, DistributedTags.RMI, DistributedTags.GIPC, DistributedTags.NIO})
public class ClientConfigure  extends ClientRemoteObject implements ClientRemoteInterfaceNIO {
	
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
	private static boolean broadcastIPCMechanism = false;
	private static int aProposalNumber;
	
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
	public void atomicBroadcast(boolean fake) {
		return;
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
			serverGIPC.registerClientGIPC((ClientRemoteInterfaceGIPC) this);
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
			server.registerClientRMI((ClientRemoteInterfaceGIPC) this);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		
		clientOutCoupler = new ClientOutCoupler(server, (ClientRemoteInterfaceGIPC) this, CLIENT_NAME);
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
	
	//@Override
	/*
	 * You will need to delay not command input but sends(non-Javadoc)
	 */
	public void simulationCommand1(String aCommand) {
		//long aDelay = getDelay(); 
		//if (aDelay > 0) {
		//	ThreadSupport.sleep(aDelay);
		//}
		IPCMechanism mechanism = getIPCMechanism();
		System.out.println("IPC Mechanism");
		System.out.println(mechanism);
		
		
		if(mechanism.toString().equals("GIPC")) {
						
			commandProcessor.removePropertyChangeListener(clientOutCoupler);
			clientOutCoupler = new ClientOutCoupler(serverGIPC, (ClientRemoteInterfaceGIPC) this, CLIENT_NAME);
			commandProcessor.addPropertyChangeListener(clientOutCoupler);
			System.out.println("using gipc proxy server");
		}
		if(mechanism.toString().equals("RMI")) {
			commandProcessor.removePropertyChangeListener(clientOutCoupler);
			clientOutCoupler = new ClientOutCoupler(server, (ClientRemoteInterfaceGIPC) this, CLIENT_NAME);
			commandProcessor.addPropertyChangeListener(clientOutCoupler);
			System.out.println("using RMI proxy server");
		}
		
		//IPC Mechanism Change
		ProposedStateSet.newCase(this, CLIENT_NAME, aProposalNumber, mechanism);
		try {
			server.broadcastIPCMechanism(mechanism, (ClientRemoteInterfaceGIPC) this, aProposalNumber, broadcastIPCMechanism);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		commandProcessor.setInputString(aCommand); // all commands go to the first command window
	}
	
	@Override	
	public void quit(int aCode) {
		//commandProcessor.setInputString("quit");
		
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
		setBroadcastMetaState(broadcast);
		
	}
	
	@Override
	public void changeIPCMechanism(IPCMechanism mechanism) {
		ProposalLearnedNotificationReceived.newCase(this, CLIENT_NAME, aProposalNumber, mechanism);
		setIPCMechanism(mechanism);
		ProposedStateSet.newCase(this, CLIENT_NAME, aProposalNumber, mechanism);
		aProposalNumber++;
	}
	
	protected NIOManager nioManager = NIOManagerFactory.getSingleton();
	int aServerPort;
	protected SocketChannel socketChannel;
	protected boolean broadcastIPCMechanism1 = false;
	
	ArrayBlockingQueue<ByteBuffer> boundedBuffer = new ArrayBlockingQueue<ByteBuffer>(500);
	ReadThreadInterface reader = null;
	Thread readThread = null;
	
	//@Override
	public void nioInit(String[] args) {
		setTracing();
		setFactories();
		
		aServerPort = ClientArgsProcessor.getNIOServerPort(args);
		System.out.println("NIO SERVER PORT: "+aServerPort);
		
		try {
			socketChannel = SocketChannel.open();
			InetAddress aServerAddress = InetAddress.getByName("localhost");
			
			nioManager.connect(socketChannel, aServerAddress, aServerPort, 
					//0, // do not allow any incoming messages
					SelectionKey.OP_READ,
					this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Create new read thread Runnable
		//reader = new ClientReadThread(this);
						
		//Create new readThread
		readThread = new Thread(reader);
				
		//Start thread and do some action
		readThread.start();
		
		//String aNextLine = "a new client has been initialized";
		// wrap writes to the buffer and then flips it
		//ByteBuffer aWriteMessage = ByteBuffer.wrap(aNextLine.getBytes());
		//nioManager.write(socketChannel, aWriteMessage, this);
		super.init(args);
	}
	
	@Override
	public void setFactories() {
		ConnectCommandFactorySelector.setFactory(new AConnectCommandFactory(0));
	}

	@Override
	public void connected(SocketChannel aSocketChannel) {
		// TODO Auto-generated method stub
		nioManager.addReadListener(aSocketChannel, this);
		System.out.println("New Client connected to server!!!");
		
	}

	@Override
	public void notConnected(SocketChannel arg0, Exception arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void written(SocketChannel arg0, ByteBuffer arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void socketChannelAccepted(ServerSocketChannel arg0, SocketChannel arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void socketChannelRead(SocketChannel arg0, ByteBuffer aMessage, int arg2) {
		// TODO Auto-generated method stub
		ByteBuffer copy = MiscAssignmentUtils.deepDuplicate(aMessage);
		boundedBuffer.add(copy);
		
		reader.notifyThread();	
		
	}
	
	@Override
	public ArrayBlockingQueue<ByteBuffer> getBoundedBuffer() {
		// TODO Auto-generated method stub
		return boundedBuffer;
	}
	
	@Override
	public void simulationCommand(String aCommand) {

		IPCMechanism mechanism = getIPCMechanism();
		System.out.println("IPC Mechanism: " + mechanism.toString());

		// IPC Mechanism Change
		ProposedStateSet.newCase(this, super.CLIENT_NAME, super.aProposalNumber, mechanism);
		try {

			server.broadcastIPCMechanism(mechanism, this, aProposalNumber, broadcastIPCMechanism);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!mechanism.toString().equals("NIO")) {
			System.out.println("IPC Mechanism is GIPC or RMI");
			super.simulationCommand(aCommand);
			return;
		}

		commandProcessor.removePropertyChangeListener(clientOutCoupler);
		
		ByteBuffer bufferCommand = ByteBuffer.wrap(aCommand.getBytes());
		RemoteProposeRequestSent.newCase(this, CLIENT_NAME, aProposalNumber, aCommand);
		nioManager.write(socketChannel, bufferCommand, this);

		
		
		
		commandProcessor.setInputString(aCommand); // all commands go to the first command window
		
		commandProcessor.addPropertyChangeListener(clientOutCoupler);
		aProposalNumber = 1 + aProposalNumber;
	}
	
	@Override
	public HalloweenCommandProcessor getCommandProcessor() {
		return commandProcessor;
	}

}
