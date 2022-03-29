package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import stringProcessors.HalloweenCommandProcessor;
import util.annotations.Tags;
import util.tags.DistributedTags;

@Tags({DistributedTags.CLIENT_REMOTE_INTERFACE, DistributedTags.GIPC})
public interface ClientRemoteInterfaceGIPC  extends Remote{

	//void processArgs(String[] args);
	
	void inCoupler(String aNewCommand, int aProposalNumber) throws RemoteException;

	void start(String[] args) throws RemoteException;

	void processArgs(String[] args) throws RemoteException;

	HalloweenCommandProcessor createSimulation(String aPrefix) throws RemoteException;

	void broadcastMetaState(boolean broadcast) throws RemoteException;

}