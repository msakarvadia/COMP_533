package comp533.clientServer;

import java.rmi.registry.LocateRegistry;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import comp533.mvc.RemoteModelInterface;

public class RemoteClientProcess extends AMapReduceTracer implements SimpleRegistryAndCounterServer{

	public static void main (final String[] args) {
		try {
			final Registry rmiRegistry = LocateRegistry.getRegistry(SERVER_HOST_NAME, SERVER_PORT);
			//Get model here
			final RemoteModelInterface model = (RemoteModelInterface) rmiRegistry.lookup(MODEL_NAME);
			
			//create client object
			final RemoteClientInterface client = new RemoteClientObject();
			
			//export client to create proxy
			UnicastRemoteObject.exportObject(client, 0);
			
			//have the model register the client
			
			model.register(client);
			client.clientWait();
			
			
			
			traceExit(RemoteClientProcess.class);
			System.exit(0);
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
	

	}

}
