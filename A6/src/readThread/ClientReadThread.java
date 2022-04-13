package readThread;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import client.ClientRemoteObjectNIO;
import inputport.nio.manager.NIOManager;
import inputport.nio.manager.NIOManagerFactory;
import nioExample.AnNIOManagerPrintClient;
import util.annotations.Tags;
import util.tags.DistributedTags;


@Tags({DistributedTags.NIO, DistributedTags.CLIENT_READ_THREAD})
public class ClientReadThread implements ReadThreadInterface{
	final ClientRemoteObjectNIO client;
	protected NIOManager nioManager = NIOManagerFactory.getSingleton();
	
	public ClientReadThread (final ClientRemoteObjectNIO aClient) {
		client = aClient;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void socketChannelRead(SocketChannel arg0, ByteBuffer arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public synchronized void notifyThread() {
		// TODO This should be when a line is read
		this.notify();
	}

}
