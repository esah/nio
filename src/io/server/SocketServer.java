package io.server;

import io.handler.Handler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class SocketServer {
	private final int port = 8081;
	private final Handler<Socket> handler;

	public SocketServer(final Handler<Socket> handler) {
		this.handler = handler;
	}

	void start() throws IOException {
		final ServerSocket ss = new ServerSocket(port);

		while (true) {
			try {
				handler.handle(ss.accept());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
