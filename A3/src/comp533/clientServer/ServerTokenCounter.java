package comp533.clientServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import comp533.mvc.Controller;
import comp533.mvc.ControllerInterface;
import comp533.mvc.Model;
import comp533.mvc.View;
import comp533.mvc.ViewInterface;

public class ServerTokenCounter implements SimpleRegistryAndCounterServer{
 public static void main (final String[] args) {
	 try {
		final Registry rmiRegistry = LocateRegistry.createRegistry(SERVER_PORT);
		final Model model = new Model();
		UnicastRemoteObject.exportObject(model, 0);
		rmiRegistry.rebind(MODEL_NAME, model);
		
		//Same code from original main
		final ViewInterface view = new View();
		
		model.addPropertyChangeListener(view);
		
		final ControllerInterface controller = new Controller();
		controller.setModel(model);
		controller.processInput();
		
		System.exit(0);
		
	} catch (RemoteException e) {
		e.printStackTrace();
	}
	 
 }
}
