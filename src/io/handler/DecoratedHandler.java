package io.handler;

import java.io.IOException;

//Decorator common abstract superclass
public abstract class DecoratedHandler<S> implements Handler<S> {

	private final Handler<S> handler;

	DecoratedHandler(final Handler<S> handler) {
		this.handler = handler;
	}

	@Override
	public void handle(final S s) throws IOException {
		handler.handle(s);
	}
}
