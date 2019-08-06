package io.server;

import io.handler.Handler;
import io.handler.LoggingHandler;
import io.handler.SocketHandler;
import java.io.IOException;
import java.net.Socket;

public class SingleThreadedBlockingServer {

	public static void main(String[] args) throws IOException {
		final Handler<Socket> handler = new LoggingHandler<>(new SocketHandler());

		new SocketServer(handler).start();
	}

}
