package comp533.client;

import util.annotations.Tags;
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
import comp533.server.ServerRemoteInterface;
import coupledsims.AStandAloneTwoCoupledHalloweenSimulations;
import coupledsims.Simulation;
import coupledsims.Simulation1;
import main.BeauAndersonFinalProject;
import stringProcessors.HalloweenCommandProcessor;

import util.trace.port.consensus.ProposalLearnedNotificationReceived;
import util.trace.port.consensus.ProposedStateSet;

@Tags({ DistributedTags.CLIENT_REMOTE_OBJECT, DistributedTags.RMI })
public class ClientRemoteObject extends AStandAloneTwoCoupledHalloweenSimulations implements ClientRemoteInterface {
	HalloweenCommandProcessor commandProcessor;
	protected int NUM_EXPERIMENT_COMMANDS = 500;
	public static final String EXPERIMENT_COMMAND_1 = "move 1 -1";
	public static final String EXPERIMENT_COMMAND_2 = "undo";
	protected PropertyChangeListener simulationCoupler;
	ServerRemoteInterface server = null;
	int aProposalNumber = 0;

	private static String RMI_SERVER_HOST_NAME;
	private static int RMI_SERVER_PORT;
	private static String SERVER_NAME;
	private static String CLIENT_NAME;
	
	ClientOutCoupler clientOutCoupler;

	protected HalloweenCommandProcessor createSimulation(String aPrefix) {
		return BeauAndersonFinalProject.createSimulation(aPrefix, Simulation1.SIMULATION1_X_OFFSET,
				Simulation.SIMULATION_Y_OFFSET, Simulation.SIMULATION_WIDTH, Simulation.SIMULATION_HEIGHT,
				Simulation1.SIMULATION1_X_OFFSET, Simulation.SIMULATION_Y_OFFSET);
	}

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
	protected void init(String[] args) {
		setTracing();

		this.processArgs(args);
		// Ideally the prefixes should be main args
		commandProcessor = createSimulation(Simulation1.SIMULATION1_PREFIX);

		// Locate Server
		Registry rmiRegistry = null;
		try {
			rmiRegistry = LocateRegistry.getRegistry(RMI_SERVER_HOST_NAME, RMI_SERVER_PORT);
		} catch (RemoteException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		// Get server here
		
		try {
			server = (ServerRemoteInterface) rmiRegistry.lookup(SERVER_NAME);
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

		clientOutCoupler = new ClientOutCoupler(server, this);
		// Add propertyChangeListener
		commandProcessor.addPropertyChangeListener(clientOutCoupler);
		
		System.out.println("added server as a property change listener of client");
	}

	@Override
	public void inCoupler(String aNewCommand) {
		System.out.println("recieved broadcased command: "+ aNewCommand);
		ProposalLearnedNotificationReceived.newCase(this, CLIENT_NAME, aProposalNumber, aNewCommand);
		commandProcessor.processCommand(aNewCommand);
		ProposedStateSet.newCase(this, CLIENT_NAME, aProposalNumber, aNewCommand);
		System.out.println("executed command");
		aProposalNumber++;
		
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
	public void localProcessingOnly(boolean newValue) {
		super.localProcessingOnly(newValue);
		if (isLocalProcessingOnly()) {
			commandProcessor.removePropertyChangeListener(clientOutCoupler);
			
		} else {
			commandProcessor.addPropertyChangeListener(clientOutCoupler);
			
		}
	}

}
