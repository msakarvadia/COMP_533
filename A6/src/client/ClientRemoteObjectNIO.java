package client;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.rmi.RemoteException;
import java.util.concurrent.ArrayBlockingQueue;

import assignments.util.MiscAssignmentUtils;
import assignments.util.mainArgs.ClientArgsProcessor;
import assignments.util.mainArgs.ServerArgsProcessor;
import inputport.nio.manager.NIOManager;
import inputport.nio.manager.NIOManagerFactory;
import inputport.nio.manager.factories.classes.AConnectCommandFactory;
import inputport.nio.manager.factories.classes.AnAcceptCommandFactory;
import inputport.nio.manager.factories.selectors.AcceptCommandFactorySelector;
import inputport.nio.manager.factories.selectors.ConnectCommandFactorySelector;
import nioExample.exampleClientReadThread;
import readThread.ClientReadThread;
import readThread.ReadThreadInterface;
import stringProcessors.HalloweenCommandProcessor;
import util.interactiveMethodInvocation.IPCMechanism;
import util.trace.port.consensus.ProposedStateSet;

public class ClientRemoteObjectNIO extends ClientRemoteObject implements ClientRemoteInterfaceNIO{
	protected NIOManager nioManager = NIOManagerFactory.getSingleton();
	int aServerPort;
	protected SocketChannel socketChannel;
	
	ArrayBlockingQueue<ByteBuffer> boundedBuffer = new ArrayBlockingQueue<ByteBuffer>(500);
	ReadThreadInterface reader = null;
	Thread readThread = null;
	
	@Override
	public void init(String[] args) {
		setTracing();
		setFactories();
		
		aServerPort = ClientArgsProcessor.getNIOServerPort(args);
		System.out.println("NIO SERVER PORT: "+aServerPort);
		
		try {
			socketChannel = SocketChannel.open();
			InetAddress aServerAddress = InetAddress.getByName("localhost");
			
			nioManager.connect(socketChannel, aServerAddress, aServerPort, 
					//0, // do not allow any incoming messages
					SelectionKey.OP_READ,
					this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Create new read thread Runnable
		reader = new ClientReadThread(this);
						
		//Create new readThread
		readThread = new Thread(reader);
				
		//Start thread and do some action
		readThread.start();
		
		//String aNextLine = "a new client has been initialized";
		// wrap writes to the buffer and then flips it
		//ByteBuffer aWriteMessage = ByteBuffer.wrap(aNextLine.getBytes());
		//nioManager.write(socketChannel, aWriteMessage, this);
		super.init(args);
	}
	
	@Override
	public void setFactories() {
		ConnectCommandFactorySelector.setFactory(new AConnectCommandFactory(0));
	}

	@Override
	public void connected(SocketChannel aSocketChannel) {
		// TODO Auto-generated method stub
		nioManager.addReadListener(aSocketChannel, this);
		System.out.println("New Client connected to server!!!");
		
	}

	@Override
	public void notConnected(SocketChannel arg0, Exception arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void written(SocketChannel arg0, ByteBuffer arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void socketChannelAccepted(ServerSocketChannel arg0, SocketChannel arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void socketChannelRead(SocketChannel arg0, ByteBuffer aMessage, int arg2) {
		// TODO Auto-generated method stub
		ByteBuffer copy = MiscAssignmentUtils.deepDuplicate(aMessage);
		boundedBuffer.add(copy);
		
		reader.notifyThread();	
		
	}
	
	@Override
	public ArrayBlockingQueue<ByteBuffer> getBoundedBuffer() {
		// TODO Auto-generated method stub
		return boundedBuffer;
	}
	
	@Override
	public void simulationCommand(String aCommand) {

		IPCMechanism mechanism = getIPCMechanism();
		System.out.println("IPC Mechanism: "+ mechanism.toString());

		if (!mechanism.toString().equals("NIO")) {
			System.out.println("IPC Mechanism is GIPC or RMI");
			super.simulationCommand(aCommand);
			return;
		}
		
		commandProcessor.removePropertyChangeListener(clientOutCoupler);
		
		ByteBuffer bufferCommand = ByteBuffer.wrap(aCommand.getBytes());
		nioManager.write(socketChannel, bufferCommand, this);

		// IPC Mechanism Change
		ProposedStateSet.newCase(this, super.CLIENT_NAME, super.aProposalNumber, mechanism);
		try {
			server.broadcastIPCMechanism(mechanism, this, aProposalNumber, super.broadcastIPCMechanism);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		commandProcessor.setInputString(aCommand); // all commands go to the first command window
		
		commandProcessor.addPropertyChangeListener(clientOutCoupler);
	}
	
	@Override
	public HalloweenCommandProcessor getCommandProcessor() {
		return commandProcessor;
	}
		

}
