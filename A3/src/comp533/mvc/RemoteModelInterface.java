package comp533.mvc;

import java.rmi.Remote;
import java.rmi.RemoteException;

import comp533.clientServer.RemoteClientInterface;


public interface RemoteModelInterface extends Remote{
	
	public void register(RemoteClientInterface client) throws RemoteException;
	

}
