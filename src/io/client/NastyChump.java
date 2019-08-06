package io.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NastyChump {

	public static void main(String[] args) throws IOException, InterruptedException {
		final Socket[] sockets = new Socket[5000];
		for (int i = 0; i < sockets.length; i++) {
			try {
				sockets[i] = new Socket("localhost", 8081);
			} catch (IOException e) {
				e.printStackTrace();
			}
			//read(sockets[i]);
			System.out.println("Started " + sockets[i]);
		}

		Thread.sleep(10_000);

	}

	private static void read(final Socket socket) throws IOException {
		try (
				final OutputStream out = socket.getOutputStream();
				final InputStream in = socket.getInputStream()

		) {
			out.write('A');
			in.read();
		}
	}
}
