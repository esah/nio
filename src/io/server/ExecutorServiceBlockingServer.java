package io.server;

import io.handler.ExecutorServiceHandler;
import io.handler.Handler;
import io.handler.LoggingHandler;
import io.handler.TransmogrifySocketHandler;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceBlockingServer {

	public static void main(String[] args) throws IOException {

		final ExecutorService pool = Executors.newFixedThreadPool(10);

		final Handler<Socket> handler =
				new ExecutorServiceHandler<>(new LoggingHandler<>(new TransmogrifySocketHandler()), pool);

		new SocketServer(handler).start();
	}

}
