package io.handler.nio.selector;

import io.handler.Handler;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AcceptHandler implements Handler<SelectionKey> {
	private final Map<SocketChannel, Queue<ByteBuffer>> data;

	public AcceptHandler(final Map<SocketChannel, Queue<ByteBuffer>> data) {
		this.data = data;
	}

	@Override
	public void handle(final SelectionKey selectionKey) throws IOException {
		ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
		final SocketChannel socket = ssc.accept();// never null
		System.out.println("Connected to " + socket);

		//data.put(socket, new ArrayDeque<>()); //we don't need thread safety with a queue so the quickest arrayDeque
		data.put(socket, new ConcurrentLinkedQueue<>());
		socket.configureBlocking(false);
		socket.register(selectionKey.selector(), SelectionKey.OP_READ);

	}
}
