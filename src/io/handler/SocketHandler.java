package io.handler;

import io.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketHandler implements Handler<Socket> {

	@Override
	public void handle(Socket socket) throws IOException {
		try (socket;
			 InputStream in = socket.getInputStream();
			 OutputStream out = socket.getOutputStream()) {
			//in.transferTo(out);
			int data;
			while ((data = in.read()) != -1) {
				out.write(Util.transmogrify(data));
			}
		}
	}

}
