package io.handler;

import io.util.Util;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;

public class WriteHandler implements Handler<SelectionKey> {

	private final Map<SocketChannel, Queue<ByteBuffer>> data;

	public WriteHandler(final Map<SocketChannel, Queue<ByteBuffer>> data) {
		this.data = data;
	}

	@Override
	public void handle(final SelectionKey selectionKey) throws IOException {
		final SocketChannel sc = (SocketChannel) selectionKey.channel();

		final Queue<ByteBuffer> queue = data.get(sc);

		while (!queue.isEmpty()) {
			ByteBuffer buf = queue.peek();
			final int written = sc.write(buf);

			if (written == -1) {
				System.out.println("Disconnected from " + sc + " from write");
				sc.close();
				data.remove(sc); //clear queue
				return;
			}

			//written only half and block other half

			if (buf.hasRemaining()) {
				return;
			}
			queue.remove();
		}

		selectionKey.interestOps(SelectionKey.OP_READ);

	}
}
