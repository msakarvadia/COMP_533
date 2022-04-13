package client;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;

import assignments.util.mainArgs.ServerArgsProcessor;
import inputport.nio.manager.NIOManager;
import inputport.nio.manager.NIOManagerFactory;
import nioExample.exampleClientReadThread;
import readThread.ClientReadThread;
import readThread.ReadThreadInterface;

public class ClientRemoteObjectNIO extends ClientRemoteObject implements ClientRemoteInterfaceNIO{
	protected NIOManager nioManager = NIOManagerFactory.getSingleton();
	int aServerPort;
	protected SocketChannel socketChannel;
	
	ArrayBlockingQueue<ByteBuffer> boundedBuffer = new ArrayBlockingQueue<ByteBuffer>(500);
	ReadThreadInterface reader = null;
	Thread readThread = null;
	
	@Override
	public void init(String[] args) {
		super.init(args);
		aServerPort = ServerArgsProcessor.getNIOServerPort(args);
		
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
	}

	@Override
	public void connected(SocketChannel arg0) {
		// TODO Auto-generated method stub
		
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
	public void socketChannelRead(SocketChannel arg0, ByteBuffer arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
