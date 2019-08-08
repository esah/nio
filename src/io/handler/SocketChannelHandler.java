package io.handler;

import io.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketChannelHandler implements Handler<SocketChannel> {

	@Override
	public void handle(SocketChannel channel) throws IOException {
		final ByteBuffer buffer = ByteBuffer.allocate(80);
		int read;
		while ((read = channel.read(buffer)) != -1) {
			if (read > 0) {
				Util.transmogrify(buffer);
				while (buffer.hasRemaining()) {
					channel.write(buffer);
				}
				buffer.compact();
			}
		}
		channel.close();
	}

}
