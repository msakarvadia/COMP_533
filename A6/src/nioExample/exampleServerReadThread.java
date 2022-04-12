package nioExample;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import readThread.ReadThreadInterface;

public class exampleServerReadThread implements ReadThreadInterface{

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
		
		}
		
	}
	
	@Override
	public synchronized void notifyThread() {
		// TODO This should be when a line is read
		this.notify();
	}

}
