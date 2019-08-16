package io.server;

import io.handler.Handler;
import io.handler.LoggingHandler;
import io.handler.TransmogrifySocketHandler;
import io.handler.ThreadedHandler;
import io.handler.UncheckedIOExceptionConverterHandler;
import java.io.IOException;
import java.net.Socket;

public class MultiThreadedBlockingServer {


	public static void main(String[] args) throws IOException {
		final Handler<Socket> handler =
				new ThreadedHandler<>(
						new UncheckedIOExceptionConverterHandler<>(
								new LoggingHandler<>(
										new TransmogrifySocketHandler()
								)
						)
				);

		new SocketServer(handler).start();
	}


}
