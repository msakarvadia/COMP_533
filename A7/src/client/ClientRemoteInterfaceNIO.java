package client;

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;

import inputport.nio.manager.listeners.SocketChannelAcceptListener;
import inputport.nio.manager.listeners.SocketChannelConnectListener;
import inputport.nio.manager.listeners.SocketChannelReadListener;
import inputport.nio.manager.listeners.SocketChannelWriteListener;
import stringProcessors.HalloweenCommandProcessor;

public interface ClientRemoteInterfaceNIO extends SocketChannelConnectListener, SocketChannelWriteListener,
SocketChannelAcceptListener, SocketChannelReadListener{

	ArrayBlockingQueue<ByteBuffer> getBoundedBuffer();

	HalloweenCommandProcessor getCommandProcessor();

	void setFactories();

	//For A6 checks
	void simulationCommand1(String aCommand);
	void nioInit(String[] args);

}
