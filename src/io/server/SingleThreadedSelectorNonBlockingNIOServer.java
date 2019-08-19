package io.server;

import io.handler.AcceptHandler;
import io.handler.Handler;
import io.handler.ReadHandler;
import io.handler.TransmogrifyChannelHandler;
import io.handler.WriteHandler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class SingleThreadedSelectorNonBlockingNIOServer {

	public static void main(String[] args) throws IOException {

		ServerSocketChannel channel = ServerSocketChannel.open();
		channel.configureBlocking(false);
		channel.bind(new InetSocketAddress(8081));

		Selector selector = Selector.open();
		channel.register(selector, SelectionKey.OP_ACCEPT);
		//ACCEPT - some client connected and we accepted the connection
		//READ - we are reading
		//WRITE - we are trying to write and the socket is ready for writing


		final Map<SocketChannel, Queue<ByteBuffer>> pendingData = new HashMap<>(); //not inter thread communication, only 1 thread

		final Handler<SelectionKey> acceptHandler = new AcceptHandler(pendingData);
		final Handler<SelectionKey> readHandler = new ReadHandler(pendingData);
		final Handler<SelectionKey> writeHandler = new WriteHandler(pendingData);


		while (true) {
			selector.select(); //blocks until smth happened

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

}
