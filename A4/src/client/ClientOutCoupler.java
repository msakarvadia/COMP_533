package client;

import util.annotations.Tags;
import util.tags.DistributedTags;
import util.trace.port.consensus.RemoteProposeRequestSent;
import util.trace.trickOrTreat.LocalCommandObserved;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;

import server.ServerRemoteInterface;

@Tags({DistributedTags.CLIENT_OUT_COUPLER, DistributedTags.RMI})
public class ClientOutCoupler implements PropertyChangeListener{
	ServerRemoteInterface ObservingServer;
	ClientRemoteInterface originalClient;
	String ORIGINAL_CLIENT_NAME;
	
	int aProposalNumber = 0;
	
	public ClientOutCoupler (ServerRemoteInterface anObservingServer, ClientRemoteObject aClient, String aClientName) {
		ObservingServer = anObservingServer;
		originalClient = aClient;
		ORIGINAL_CLIENT_NAME = aClientName;
		
		
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent anEvent) {
		if (!anEvent.getPropertyName().equals("InputString")) return;
		String newCommand = (String) anEvent.getNewValue();
		LocalCommandObserved.newCase(this, newCommand);
		
		System.out.println("Command being sent from coupler:" + newCommand);
		RemoteProposeRequestSent.newCase(originalClient, ORIGINAL_CLIENT_NAME, aProposalNumber, newCommand);
		try {
			ObservingServer.broadcast(newCommand, originalClient, aProposalNumber);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		aProposalNumber++;
	
	}

}
