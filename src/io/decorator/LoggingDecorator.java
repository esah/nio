package io.decorator;

import io.handler.Handler;
import java.util.concurrent.atomic.AtomicInteger;

public class LoggingDecorator {
	private final static AtomicInteger connections = new AtomicInteger();

	private final Handler handler;

	public LoggingDecorator(final Handler handler) {
		this.handler = handler;
	}

	public static LoggingDecorator of(final Handler handler) {
		return new LoggingDecorator(handler);
	}

	public void handle() {
		System.out.println("Connected " + connections.incrementAndGet() + " from " + handler.getName());
		handler.handle();
		System.out.println("Disconnected " + connections.getAndDecrement() + "from " + handler.getName());
	}

}
