package io.server;

import io.handler.Handler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

class ChannelServer {
	private static final int port = 8081;
	private final Handler<SocketChannel> handler;

	public ChannelServer(final Handler<SocketChannel> handler) {
		this.handler = handler;
	}

	void start() throws IOException {
		ServerSocketChannel channel = ServerSocketChannel.open();
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
