package io.handler;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class LoggingHandler<S> extends DecoratedHandler<S> {
	private final static AtomicInteger connections = new AtomicInteger();

	public LoggingHandler(final Handler<S> handler) {
		super(handler);
	}

	public void handle(S s) throws IOException {
		System.out.println("Connected " + connections.incrementAndGet() + " from " + s);
		super.handle(s);
		System.out.println("Disconnected " + connections.getAndDecrement() + "from " + s);
	}

}
