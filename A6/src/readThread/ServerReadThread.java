package readThread;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

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
		while(true) {
			synchronized (this) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break; // this breaks out of run loop then thread terminates
				}
			}
			
			System.out.println("IN RUN METHOD OF READ THREAD");
			
			ArrayBlockingQueue<ByteBuffer> boundedBuffer = server.getBoundedBuffer();
			List<SocketChannel> socketList = server.getSocketList();
			SocketChannel currentSocket = server.getSocketChannel();
			
					
			ByteBuffer originalMessage = null;
			try {
				originalMessage = boundedBuffer.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Echo recieve message to all clients (except original message sender)
			for (SocketChannel socket : socketList) {
				if (!socket.equals(currentSocket)) {
					nioManager.write(socket, originalMessage, server);
				}
			}
		
		}
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
