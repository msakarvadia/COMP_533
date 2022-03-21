package comp533.client;

import util.annotations.Tags;
import util.tags.DistributedTags;
import util.trace.trickOrTreat.LocalCommandObserved;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;

import comp533.server.ServerRemoteInterface;

@Tags({DistributedTags.CLIENT_OUT_COUPLER, DistributedTags.RMI})
public class ClientOutCoupler implements PropertyChangeListener{
	ServerRemoteInterface ObservingServer;
	ClientRemoteInterface originalClient;
	
	public ClientOutCoupler (ServerRemoteInterface anObservingServer, ClientRemoteObject aClient) {
		ObservingServer = anObservingServer;
		originalClient = aClient;
		
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent anEvent) {
		if (!anEvent.getPropertyName().equals("InputString")) return;
		String newCommand = (String) anEvent.getNewValue();
		LocalCommandObserved.newCase(this, newCommand);
		
		System.out.println("Command being sent from coupler:" + newCommand);
		try {
			ObservingServer.broadcast(newCommand, originalClient);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	
	}

}
