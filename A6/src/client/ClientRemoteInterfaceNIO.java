package client;

import inputport.nio.manager.listeners.SocketChannelAcceptListener;
import inputport.nio.manager.listeners.SocketChannelConnectListener;
import inputport.nio.manager.listeners.SocketChannelReadListener;
import inputport.nio.manager.listeners.SocketChannelWriteListener;

public interface ClientRemoteInterfaceNIO extends SocketChannelConnectListener, SocketChannelWriteListener,
SocketChannelAcceptListener, SocketChannelReadListener{

}
