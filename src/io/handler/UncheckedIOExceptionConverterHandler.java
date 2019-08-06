package io.handler;

import java.io.IOException;
import java.io.UncheckedIOException;

public class UncheckedIOExceptionConverterHandler<S> implements Handler<S> {

	private final Handler<S> handler;

	public UncheckedIOExceptionConverterHandler(final Handler<S> handler) {
		this.handler = handler;
	}

	@Override
	public void handle(final S s) {
		try {
			handler.handle(s);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
