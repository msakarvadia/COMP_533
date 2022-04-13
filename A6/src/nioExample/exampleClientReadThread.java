package nioExample;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import inputport.nio.manager.NIOManager;
import inputport.nio.manager.NIOManagerFactory;
import readThread.ReadThreadInterface;

public class exampleClientReadThread implements ReadThreadInterface{
	final AnNIOManagerPrintClient client;
	protected NIOManager nioManager = NIOManagerFactory.getSingleton();
	
	public exampleClientReadThread (final AnNIOManagerPrintClient aClient) {
		client = aClient;
	}

	@Override
	public void socketChannelRead(SocketChannel arg0, ByteBuffer arg1, int arg2) {
		// TODO Auto-generated method stub
		
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
			
			ArrayBlockingQueue<ByteBuffer> boundedBuffer = client.getBoundedBuffer();
			
			
					
			ByteBuffer originalMessage = null;
			try {
				originalMessage = boundedBuffer.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String aMessageString = new String(originalMessage.array());
			System.out.println("SERVER MESSAGE RECIEVED: "+aMessageString);
			
		
		}
		
	}
	
	@Override
	public synchronized void notifyThread() {
		// TODO This should be when a line is read
		this.notify();
	}

}
