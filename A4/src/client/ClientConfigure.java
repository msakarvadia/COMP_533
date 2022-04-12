package client;

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
import main.BeauAndersonFinalProject;
import server.ServerRemoteInterface;
import stringProcessors.HalloweenCommandProcessor;
import util.annotations.Tags;
import util.tags.DistributedTags;
import util.trace.Tracer;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.PerformanceExperimentEnded;
import util.trace.port.PerformanceExperimentStarted;
import util.trace.port.PortTraceUtility;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.consensus.ProposalLearnedNotificationReceived;
import util.trace.port.consensus.ProposedStateSet;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.rmi.RMIObjectLookedUp;
import util.trace.port.rpc.rmi.RMIRegistryLocated;
import util.trace.port.rpc.rmi.RMITraceUtility;

@Tags({DistributedTags.CLIENT_CONFIGURER, DistributedTags.RMI})
public class ClientConfigure  extends AStandAloneTwoCoupledHalloweenSimulations implements ClientRemoteInterface {
	
	HalloweenCommandProcessor commandProcessor;
	protected int NUM_EXPERIMENT_COMMANDS = 500;
	public static final String EXPERIMENT_COMMAND_1 = "move 1 -1";
	public static final String EXPERIMENT_COMMAND_2 = "undo";
	protected PropertyChangeListener simulationCoupler;
	ServerRemoteInterface server = null;
	

	private static String RMI_SERVER_HOST_NAME;
	private static int RMI_SERVER_PORT;
	private static String SERVER_NAME;
	private static String CLIENT_NAME;
	
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
	public void init(String[] args) {
		setTracing();

		this.processArgs(args);
		// Ideally the prefixes should be main args
		commandProcessor = createSimulation(Simulation1.SIMULATION1_PREFIX);

		// Locate Server
		Registry rmiRegistry = null;
		try {
			rmiRegistry = LocateRegistry.getRegistry(RMI_SERVER_HOST_NAME, RMI_SERVER_PORT);
			RMIRegistryLocated.newCase(this, RMI_SERVER_HOST_NAME, RMI_SERVER_PORT, rmiRegistry);
		} catch (RemoteException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		// Get server here
		
		try {
			server = (ServerRemoteInterface) rmiRegistry.lookup(SERVER_NAME);
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
			server.registerClient(this);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		
		//NOTE This is just a hacky way of satisfying tests
		ClientRemoteInterface aClient = new ClientRemoteObject();
		clientOutCoupler = new ClientOutCoupler(server, aClient, CLIENT_NAME);
		// Add propertyChangeListener
		commandProcessor.addPropertyChangeListener(clientOutCoupler);
		
		System.out.println("added server as a property change listener of client");
	}
	
	
	@Override	
	public void trace(boolean newValue) {
		super.trace(newValue);
		Tracer.showInfo(isTrace());
	}
	
	@Override
	public void experimentInput() {
		long aStartTime = System.currentTimeMillis();
		PerformanceExperimentStarted.newCase(this, aStartTime, NUM_EXPERIMENT_COMMANDS);
		boolean anOldValue = isTrace();
		this.trace(false);
		for (int i = 0; i < NUM_EXPERIMENT_COMMANDS; i++) {
			commandProcessor.setInputString(EXPERIMENT_COMMAND_1);
		
		}
		trace(anOldValue);
		long anEndTime = System.currentTimeMillis();
		PerformanceExperimentEnded.newCase(this, aStartTime, anEndTime, anEndTime - aStartTime, NUM_EXPERIMENT_COMMANDS);
		
	}
	
	@Override
	/*
	 * This override is not really needed, provided here to show that this method
	 * exists.
	 */
	public void delaySends(int aMillisecondDelay) {
		// getDelay() can be used to determine the delay
		// in other parts of the program
		super.delaySends(aMillisecondDelay);
	}
	
	@Override
	/**
	 * Relevant in consistency assignments only 
	 */
	public void atomicBroadcast(boolean newValue) {
		super.atomicBroadcast(newValue);
		commandProcessor.setConnectedToSimulation(!isAtomicBroadcast());
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


}
