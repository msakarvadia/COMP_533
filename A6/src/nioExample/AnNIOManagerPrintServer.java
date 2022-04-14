package nioExample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import assignments.util.MiscAssignmentUtils;

import client.ClientRemoteInterfaceGIPC;
import inputport.nio.manager.NIOManager;
import inputport.nio.manager.NIOManagerFactory;
import inputport.nio.manager.factories.classes.AReadingAcceptCommandFactory;
import inputport.nio.manager.factories.classes.AnAcceptCommandFactory;
import inputport.nio.manager.factories.selectors.AcceptCommandFactorySelector;
import inputport.nio.manager.listeners.SocketChannelAcceptListener;
import inputport.nio.manager.listeners.SocketChannelReadListener;
import inputport.nio.manager.listeners.SocketChannelWriteListener;
import inputport.nio.manager.listeners.WriteBoundedBufferListener;
import readThread.ReadThreadInterface;
import util.trace.factories.FactoryTraceUtility;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.nio.SocketChannelBound;

public class AnNIOManagerPrintServer implements NIOManagerPrintServer {

	List<SocketChannel> socketList = new ArrayList<SocketChannel>();
	ArrayBlockingQueue<ByteBuffer> boundedBuffer = new ArrayBlockingQueue<ByteBuffer>(500);
	ReadThreadInterface reader = null;
	Thread readThread = null;
	SocketChannel currentSocket = null;
	
	protected NIOManager nioManager = NIOManagerFactory.getSingleton();

	public AnNIOManagerPrintServer(int aServerPort) {
		setTracing();
		setFactories();
		initialize(aServerPort);
	}

	protected void setFactories() {
		AcceptCommandFactorySelector.setFactory(new AnAcceptCommandFactory(SelectionKey.OP_READ));
	}
	protected void setTracing() {
		FactoryTraceUtility.setTracing();
		NIOTraceUtility.setTracing();
	}

	protected void initialize(int aServerPort) {
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
		reader = new exampleServerReadThread(this);
				
		//Create new readThread
		readThread = new Thread(reader);
		
		//Start thread and do some action
		readThread.start();
	}

	@Override
	public void socketChannelAccepted(ServerSocketChannel aServerSocketChannel, SocketChannel aSocketChannel) {
		nioManager.addReadListener(aSocketChannel, this);

		// save aSocketChannel
		socketList.add(aSocketChannel);
	}

	@Override
	public void socketChannelRead(SocketChannel aSocketChannel, ByteBuffer aMessage, int aLength) {
		ByteBuffer copy = MiscAssignmentUtils.deepDuplicate(aMessage);
		boundedBuffer.add(copy);

		String aMessageString = new String(aMessage.array(), aMessage.position(), aLength);
		System.out.println(aMessageString + "<--" + aSocketChannel);

		currentSocket = aSocketChannel;
		/*
		// Pop value off the bounded buffer
		ByteBuffer originalMessage = null;
		try {
			originalMessage = boundedBuffer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Echo recieve message to all clients (except original message sender)
		for (SocketChannel socket : socketList) {
			if (!socket.equals(aSocketChannel)) {
				nioManager.write(socket, originalMessage, this);
			}
		}
		*/
		System.out.println("notifying reader!!");
		reader.notifyThread();
		System.out.println("thread has completed action and is now waiting");

	}
	
	@Override
	public ArrayBlockingQueue<ByteBuffer> getBoundedBuffer(){
		return boundedBuffer;
		
	}
	
	@Override
	public List<SocketChannel> getSocketList(){
		return socketList;
	}
	
	@Override
	public SocketChannel getSocketChannel() {
		return currentSocket;
	}

	@Override
	public void written(SocketChannel socket, ByteBuffer aMessage, int aLength) {
		// TODO Auto-generated method stub
		String aMessageString = new String(aMessage.array());
		System.out.println("SERVER SENT MESSAGE TO CLIENT: " + aMessageString + "-->" + socket);
	}

}
