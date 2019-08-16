package io.server;

import io.handler.BlockingChannelHandler;
import io.handler.ExecutorServiceHandler;
import io.handler.Handler;
import io.handler.LoggingHandler;
import io.handler.TransmogrifyChannelHandler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;

public class SingleThreadedPollingNonBlockingNIOServer {

	public static void main(String[] args) throws IOException {

		ServerSocketChannel channel = ServerSocketChannel.open();
		channel.configureBlocking(false);
		channel.bind(new InetSocketAddress(8081));


		final Handler<SocketChannel> handler =
				new TransmogrifyChannelHandler();


		final Collection<SocketChannel> sockets = new ArrayList<>();

		while (true) {
			final SocketChannel sc = channel.accept(); //almost always null!
			if (sc != null) {
				sockets.add(sc);
				System.out.println("Connected to " + sc);
				sc.configureBlocking(false);
			}

			for (SocketChannel socket : sockets) {
				if (socket.isConnected()) {
					handler.handle(socket);
				}
			}
			sockets.removeIf(s -> !s.isConnected());
		}
	}

}
