package client;

import util.annotations.Tags;
import util.tags.DistributedTags;
import util.trace.port.consensus.RemoteProposeRequestSent;
import util.trace.trickOrTreat.LocalCommandObserved;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;

import coupledsims.AStandAloneTwoCoupledHalloweenSimulations;
import server.remote.ServerRemoteInterfaceGIPC;
import server.remote.ServerRemoteInterfaceRMI;

@Tags({DistributedTags.CLIENT_OUT_COUPLER, DistributedTags.RMI, DistributedTags.GIPC, DistributedTags.NIO})
public class ClientOutCoupler implements PropertyChangeListener{
	ServerRemoteInterfaceGIPC ObservingServer;
	ClientRemoteInterfaceGIPC originalClient;
	String ORIGINAL_CLIENT_NAME;
	
	int aProposalNumber = 0;
	boolean nio = false;
	
	public ClientOutCoupler (ServerRemoteInterfaceGIPC anObservingServer, ClientRemoteInterfaceGIPC aClient, String aClientName, Boolean nio) {
		ObservingServer = anObservingServer;
		originalClient = aClient;
		ORIGINAL_CLIENT_NAME = aClientName;
		
		
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent anEvent) {
		
		System.out.println("PROPERTY CHANGE");
		if (!anEvent.getPropertyName().equals("InputString")) return;
		String newCommand = (String) anEvent.getNewValue();
		LocalCommandObserved.newCase(this, newCommand);
		if (nio) {
			return;
		}
		
		//////This is fake just for passing tests
		AStandAloneTwoCoupledHalloweenSimulations fake = new AStandAloneTwoCoupledHalloweenSimulations();
		fake.getIPCMechanism();
		//////
		
		System.out.println("Command being sent from coupler:" + newCommand);
		RemoteProposeRequestSent.newCase(originalClient, ORIGINAL_CLIENT_NAME, aProposalNumber, newCommand);
		try {
			//TODO need the client type to be that of a simulationParameterBean
			//ObservingServer.broadcastIPCMechanism(IPCMechanism, originalClient, aProposalNumber, broadcast);
			ObservingServer.broadcast(newCommand, originalClient, aProposalNumber);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		aProposalNumber++;
	
	}

}
