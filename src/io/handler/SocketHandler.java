package io.handler;

import io.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.Socket;

public class SocketHandler implements Handler {

	private final Socket socket;

	public SocketHandler(final Socket socket) {
		this.socket = socket;
	}

	public static Handler of(final Socket socket) {
		return new SocketHandler(socket);
	}

	@Override
	public void handle() {
		try (socket;
			 InputStream in = socket.getInputStream();
			 OutputStream out = socket.getOutputStream()) {
			//in.transferTo(out);
			int data;
			while ((data = in.read()) != -1) {
				out.write(Util.transmogrify(data));
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	@Override
	public String getName() {
		return socket.toString();
	}
}
