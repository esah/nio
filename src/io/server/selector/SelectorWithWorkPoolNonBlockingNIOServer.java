package io.server.selector;

import io.handler.nio.selector.AcceptHandler;
import io.handler.Handler;
import io.handler.nio.selector.PoolReadHandler;
import io.handler.nio.selector.WriteHandler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SelectorWithWorkPoolNonBlockingNIOServer {

	public static void main(String[] args) throws IOException {

		ServerSocketChannel channel = ServerSocketChannel.open();
		channel.configureBlocking(false);
		channel.bind(new InetSocketAddress(8081));

		Selector selector = Selector.open();
		channel.register(selector, SelectionKey.OP_ACCEPT);


		ExecutorService pool = Executors.newFixedThreadPool(10);
		Queue<Runnable> selectorActions = new ConcurrentLinkedQueue<>();
		final Map<SocketChannel, Queue<ByteBuffer>> pendingData = new ConcurrentHashMap<>();

		final Handler<SelectionKey> acceptHandler = new AcceptHandler(pendingData);
		final Handler<SelectionKey> readHandler = new PoolReadHandler(pool, selectorActions, pendingData);
		final Handler<SelectionKey> writeHandler = new WriteHandler(pendingData);

		while (true) {
			selector.select();

			processSelectorActions(selectorActions);


			Set<SelectionKey> keys = selector.selectedKeys();
			for (final Iterator<SelectionKey> it = keys.iterator(); it.hasNext(); ) {
				final SelectionKey key = it.next();
				it.remove();

				try {
					if (key.isValid()) {
						if (key.isAcceptable()) {
							acceptHandler.handle(key);
						} else if (key.isReadable()) {
							readHandler.handle(key);
						} else if (key.isWritable()) {
							writeHandler.handle(key);
						}
					}
				} catch (CancelledKeyException e) {
					System.out.println("Key cancelled " + key);

				}
			}


		}

	}

	private static void processSelectorActions(final Queue<Runnable> actions) {
		Runnable action;
		while ((action = actions.poll()) != null) {
			action.run();
		}
	}

}
