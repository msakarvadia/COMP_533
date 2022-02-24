package comp533.client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import comp533.mvc.KeyValueInterface;

public interface RemoteClientInterface extends Remote {
	
	public Map<String, Integer> reduce (List<KeyValueInterface<String, Integer>> aList) throws RemoteException;
	

}
