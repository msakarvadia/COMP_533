package comp533.mvc;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Stack;

import comp533.clientServer.RemoteClientInterface;
import comp533.salve.SlaveInterface;

public interface RemoteModelInterface extends Remote{
	
	public void register(RemoteClientInterface client) throws RemoteException;
	

}
