package io.handler.nio.selector;

import io.handler.Handler;
import io.util.Util;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;

public class ReadHandler implements Handler<SelectionKey> {

	private final Map<SocketChannel, Queue<ByteBuffer>> data;

	public ReadHandler(final Map<SocketChannel, Queue<ByteBuffer>> data) {
		this.data = data;
	}

	@Override
	public void handle(final SelectionKey selectionKey) throws IOException {
		final SocketChannel sc = (SocketChannel) selectionKey.channel();

		ByteBuffer buffer = ByteBuffer.allocate(80);
		int read = sc.read(buffer);
		if (read == -1) {
			System.out.println("Disconnected from " + sc + " from read");
			data.remove(sc);
			sc.close();
			return;
		}

		if (read > 0) {
			Util.transmogrify(buffer);
			//write back so kind
			data.get(sc).add(buffer);
			selectionKey.interestOps(SelectionKey.OP_WRITE);
		}

	}
}
