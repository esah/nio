package io.handler;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class LoggingHandler<S> implements Handler<S> {
	private final static AtomicInteger connections = new AtomicInteger();

	private final Handler<S> handler;

	public LoggingHandler(final Handler<S> handler) {
		this.handler = handler;
	}

	public void handle(S s) throws IOException {
		System.out.println("Connected " + connections.incrementAndGet() + " from " + s);
		handler.handle(s);
		System.out.println("Disconnected " + connections.getAndDecrement() + "from " + s);
	}

}
