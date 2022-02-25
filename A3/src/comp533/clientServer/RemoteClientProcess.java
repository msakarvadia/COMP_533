package comp533.clientServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import comp533.mvc.RemoteModelInterface;

public class RemoteClientProcess implements SimpleRegistryandCounterServer{

	public static void main (String[] args) {
		try {
			Registry rmiRegistry = LocateRegistry.getRegistry(SERVER_HOST_NAME, SERVER_PORT);
			//Get model here
			RemoteModelInterface model = (RemoteModelInterface) rmiRegistry.lookup(MODEL_NAME);
			
			//create client object
			RemoteClientInterface client = new RemoteClientObject();
			
			//export client to create proxy
			UnicastRemoteObject.exportObject(client, 0);
			
			//have the model register the client
			model.register(client);
		} catch(Exception e){
			e.printStackTrace();
		}
		
	

	}

}
