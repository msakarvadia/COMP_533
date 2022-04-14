package readThread;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;

import client.ClientRemoteObjectNIO;
import inputport.nio.manager.NIOManager;
import inputport.nio.manager.NIOManagerFactory;
import nioExample.AnNIOManagerPrintClient;
import stringProcessors.HalloweenCommandProcessor;
import util.annotations.Tags;
import util.tags.DistributedTags;
import util.trace.port.consensus.ProposalLearnedNotificationReceived;
import util.trace.port.consensus.ProposedStateSet;


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
			
			System.out.println("IN RUN METHOD OF CLIENT READ THREAD");
			
			ArrayBlockingQueue<ByteBuffer> boundedBuffer = client.getBoundedBuffer();
			
			
					
			ByteBuffer originalMessage = null;
			try {
				originalMessage = boundedBuffer.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String aMessageString = new String(originalMessage.array());
			System.out.println("SERVER MESSAGE RECIEVED: "+aMessageString);
			
			
			HalloweenCommandProcessor commandProcessor = client.getCommandProcessor();
			
			
			ProposalLearnedNotificationReceived.newCase(this, client.CLIENT_NAME, client.aProposalNumber, aMessageString);
			commandProcessor.processCommand(aMessageString);
			ProposedStateSet.newCase(this, client.CLIENT_NAME, client.aProposalNumber, aMessageString);
			client.aProposalNumber++;
		
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
