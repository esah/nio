package io.server;

import io.decorator.LoggingDecorator;
import io.handler.SocketHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadedBlockingServer {


	public static void main(String[] args) throws IOException {
		final ServerSocket ss = new ServerSocket(8081);
		while (true) {
			final Socket socket = ss.accept();
			new Thread(() -> LoggingDecorator.of(SocketHandler.of(socket)).handle()).start();
		}
	}


}
