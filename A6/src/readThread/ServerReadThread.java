package readThread;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import inputport.nio.manager.NIOManager;
import inputport.nio.manager.NIOManagerFactory;
import nioExample.NIOManagerPrintServer;
import server.remote.ServerRemoteObjectNIO;
import util.annotations.Tags;
import util.tags.DistributedTags;


@Tags({DistributedTags.NIO, DistributedTags.SERVER_READ_THREAD})
public class ServerReadThread implements ReadThreadInterface{
	
	final ServerRemoteObjectNIO server;
	protected NIOManager nioManager = NIOManagerFactory.getSingleton();
	
	public ServerReadThread (final ServerRemoteObjectNIO aServer) {
		server = aServer;
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
