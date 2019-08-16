package io.handler;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class BlockingChannelHandler extends DecoratedHandler<SocketChannel>{

	public BlockingChannelHandler(final Handler<SocketChannel> other) {
		super(other);
	}


	@Override
	public void handle(final SocketChannel channel) throws IOException {
		while (channel.isConnected()) {
			super.handle(channel);
		}

	}
}
