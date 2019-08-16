package io.server;

import io.handler.Handler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

class ChannelServer {
	private static final int port = 8081;
	private final Handler<SocketChannel> handler;
	private final boolean blocking;

	public ChannelServer(final Handler<SocketChannel> handler, final boolean blocking) {
		this.handler = handler;
		this.blocking = blocking;
	}

	void start() throws IOException {
		ServerSocketChannel channel = ServerSocketChannel.open();
		channel.configureBlocking(blocking);
		channel.bind(new InetSocketAddress(port));

		while (true) {
			try {
				handler.handle(channel.accept());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
