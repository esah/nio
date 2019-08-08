package io.server;

import io.handler.ExecutorServiceHandler;
import io.handler.Handler;
import io.handler.LoggingHandler;
import io.handler.SocketChannelHandler;
import io.handler.SocketHandler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioBlockingServer {

	public static void main(String[] args) throws IOException {

		ServerSocketChannel channel = ServerSocketChannel.open();
		channel.bind(new InetSocketAddress(8081));

		final ExecutorService pool = Executors.newFixedThreadPool(10);

		final Handler<SocketChannel> handler =
				new ExecutorServiceHandler<>(new LoggingHandler<>(new SocketChannelHandler()), pool);

		while (true) {
			try {
				handler.handle(channel.accept());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
