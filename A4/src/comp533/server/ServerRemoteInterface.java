package comp533.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import comp533.client.ClientRemoteInterface;
import util.annotations.Tags;
import util.tags.DistributedTags;

@Tags({DistributedTags.SERVER_REMOTE_INTERFACE, DistributedTags.RMI})
public interface ServerRemoteInterface  extends Remote{
	public void registerClient(ClientRemoteInterface aClient) throws RemoteException;
	public void broadcast(String aNewCommand, ClientRemoteInterface originalClient) throws RemoteException;
	
	

}
