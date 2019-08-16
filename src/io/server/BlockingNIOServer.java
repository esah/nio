package io.server;

import io.handler.BlockingChannelHandler;
import io.handler.ExecutorServiceHandler;
import io.handler.Handler;
import io.handler.LoggingHandler;
import io.handler.TransmogrifyChannelHandler;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;

public class BlockingNIOServer {

	public static void main(String[] args) throws IOException {

		final Handler<SocketChannel> handler =
				new ExecutorServiceHandler<>(
						new LoggingHandler<>(
								new BlockingChannelHandler(
										new TransmogrifyChannelHandler()
								))

						, Executors.newFixedThreadPool(10));

		new ChannelServer(handler, true).start();
	}

}
