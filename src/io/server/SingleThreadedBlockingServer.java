package io.server;

import io.decorator.LoggingDecorator;
import io.handler.SocketHandler;
import java.io.IOException;
import java.net.ServerSocket;

public class SingleThreadedBlockingServer {

	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(8081);
		while (true) {
			LoggingDecorator.of(SocketHandler.of(ss.accept())).handle();
		}
	}

}
