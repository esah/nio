package io.server;

import io.handler.Handler;
import io.handler.LoggingHandler;
import io.handler.TransmogrifySocketHandler;
import java.io.IOException;
import java.net.Socket;

public class SingleThreadedBlockingServer {

	public static void main(String[] args) throws IOException {
		final Handler<Socket> handler = new LoggingHandler<>(new TransmogrifySocketHandler());

		new SocketServer(handler).start();
	}

}
