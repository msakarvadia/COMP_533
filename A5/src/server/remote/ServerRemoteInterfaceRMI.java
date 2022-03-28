package server.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.ClientRemoteInterfaceRMI;
import util.annotations.Tags;
import util.tags.DistributedTags;

@Tags({DistributedTags.SERVER_REMOTE_INTERFACE, DistributedTags.RMI})
public interface ServerRemoteInterfaceRMI  extends Remote{
	public void registerClient(ClientRemoteInterfaceRMI aClient) throws RemoteException;
	public void broadcast(String aNewCommand, ClientRemoteInterfaceRMI originalClient, int aProposalNumber) throws RemoteException;
	void processArgs(String[] args) throws RemoteException;
	public void start(String[] args) throws RemoteException;
	void fakeMethod(String stringOne, String stringTwo) throws RemoteException;
	void fakeMethodTwo(String stringOne, ClientRemoteInterfaceRMI client) throws RemoteException;
	
	

}
