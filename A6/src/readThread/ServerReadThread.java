package readThread;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import util.annotations.Tags;
import util.tags.DistributedTags;


@Tags({DistributedTags.NIO, DistributedTags.SERVER_READ_THREAD})
public class ServerReadThread implements ReadThreadInterface{

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
