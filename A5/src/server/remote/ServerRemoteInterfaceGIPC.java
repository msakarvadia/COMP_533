package server.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.ClientRemoteInterfaceGIPC;
import client.ClientRemoteInterfaceRMI;
import util.annotations.Tags;
import util.interactiveMethodInvocation.IPCMechanism;
import util.tags.DistributedTags;

@Tags({DistributedTags.SERVER_REMOTE_INTERFACE, DistributedTags.GIPC})
public interface ServerRemoteInterfaceGIPC  extends Remote{
	public void registerClient(ClientRemoteInterfaceRMI aClient) throws RemoteException;
	
	void registerClientGIPC(ClientRemoteInterfaceGIPC aClient) throws RemoteException;
	
	public void broadcast(String aNewCommand, ClientRemoteInterfaceGIPC originalClient, int aProposalNumber) throws RemoteException;
	
	void processArgs(String[] args) throws RemoteException;
	public void start(String[] args) throws RemoteException;
	void fakeMethod(String stringOne, String stringTwo) throws RemoteException;
	void fakeMethodTwo(String stringOne, ClientRemoteInterfaceRMI client) throws RemoteException;

	void createGIPCRegistry() throws RemoteException;

	void broadcastIPCMechanism(IPCMechanism mechanism, ClientRemoteInterfaceGIPC originalClient, int aProposalNumber,
			boolean broadcast) throws RemoteException;

	void registerClientRMI(ClientRemoteInterfaceGIPC aClient) throws RemoteException;
	
	

}
