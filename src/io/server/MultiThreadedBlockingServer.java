package io.server;

import io.handler.LoggingHandler;
import io.handler.SocketHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadedBlockingServer {


	public static void main(String[] args) throws IOException {
		final ServerSocket ss = new ServerSocket(8081);
		final LoggingHandler<Socket> handler = new LoggingHandler<>(new SocketHandler());

		while (true) {
			final Socket socket = ss.accept();
			new Thread(() -> handler.handle(socket)).start();
		}
	}


}
