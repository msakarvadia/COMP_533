package server.remote;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import assignments.util.mainArgs.ServerArgsProcessor;
import inputport.nio.manager.NIOManager;
import inputport.nio.manager.NIOManagerFactory;
import nioExample.exampleServerReadThread;
import readThread.ReadThreadInterface;
import readThread.ServerReadThread;
import util.trace.port.nio.SocketChannelBound;

public class ServerRemoteObjectNIO extends ServerRemoteObjectGIPC implements ServerRemoteInterfaceNIO{
	protected NIOManager nioManager = NIOManagerFactory.getSingleton();
	int aServerPort;
	
	List<SocketChannel> socketList = new ArrayList<SocketChannel>();
	ArrayBlockingQueue<ByteBuffer> boundedBuffer = new ArrayBlockingQueue<ByteBuffer>(500);
	ReadThreadInterface reader = null;
	Thread readThread = null;
	SocketChannel currentSocket = null;
	
	@Override
	protected void init(String[] args) {
		super.init(args);
		aServerPort = ServerArgsProcessor.getNIOServerPort(args);
		
		try {
			ServerSocketChannel aServerFactoryChannel = ServerSocketChannel.open();
			InetSocketAddress anInternetSocketAddress = new InetSocketAddress(aServerPort);
			aServerFactoryChannel.socket().bind(anInternetSocketAddress);
			SocketChannelBound.newCase(this, aServerFactoryChannel, anInternetSocketAddress);
			nioManager.enableListenableAccepts(aServerFactoryChannel, SelectionKey.OP_READ, // allow incoming writes
																							// that can be read
					this);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Create new read thread Runnable
		reader = new ServerReadThread(this);
				
		//Create new readThread
		readThread = new Thread(reader);
		
		//Start thread and do some action
		readThread.start();
	}
	
	@Override
	public void socketChannelAccepted(ServerSocketChannel arg0, SocketChannel arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void socketChannelRead(SocketChannel arg0, ByteBuffer arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void written(SocketChannel arg0, ByteBuffer arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayBlockingQueue<ByteBuffer> getBoundedBuffer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SocketChannel> getSocketList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SocketChannel getSocketChannel() {
		// TODO Auto-generated method stub
		return null;
	}

}
