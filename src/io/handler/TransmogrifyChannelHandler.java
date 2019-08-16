package io.handler;

import io.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TransmogrifyChannelHandler implements Handler<SocketChannel> {

	@Override
	public void handle(SocketChannel channel) throws IOException {
		final ByteBuffer buffer = ByteBuffer.allocate(80);
		int read = channel.read(buffer);
		if (read > 0) {
			Util.transmogrify(buffer);
			while (buffer.hasRemaining()) {
				channel.write(buffer);
			}
			buffer.compact();
		}
		if (read == -1) {
			channel.close();
		}
	}

}
