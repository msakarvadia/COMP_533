package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import util.annotations.Tags;
import util.tags.DistributedTags;

@Tags({DistributedTags.CLIENT_REMOTE_INTERFACE, DistributedTags.RMI})
public interface ClientRemoteInterface  extends Remote{

	//void processArgs(String[] args);
	
	void inCoupler(String aNewCommand, int aProposalNumber) throws RemoteException;

}
