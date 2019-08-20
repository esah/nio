package io.handler;

import io.util.Util;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

public class PoolReadHandler implements Handler<SelectionKey> {
	private final ExecutorService pool;
	private final Queue<Runnable> actions;
	private final Map<SocketChannel, Queue<ByteBuffer>> data;

	public PoolReadHandler(final ExecutorService pool, final Queue<Runnable> actions,
						   final Map<SocketChannel, Queue<ByteBuffer>> data) {
		this.pool = pool;
		this.actions = actions;
		this.data = data;
	}

	@Override
	public void handle(final SelectionKey selectionKey) throws IOException {
		final SocketChannel sc = (SocketChannel) selectionKey.channel();

		ByteBuffer buffer = ByteBuffer.allocate(80);
		int read = sc.read(buffer);
		if (read == -1) {
			System.out.println("Disconnected from " + sc + " from pooled read");
			data.remove(sc);
			sc.close();
			return;
		}

		if (read > 0) {
			pool.submit(() -> {
				Util.transmogrify(buffer);
				data.get(sc).add(buffer);
				//write back so kind
				actions.add(() -> selectionKey.interestOps(SelectionKey.OP_WRITE));
				selectionKey.selector().wakeup(); //wakeup the main thread from selector.select();
			});
		}

	}
}
