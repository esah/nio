package io.handler;

import java.io.IOException;
import java.io.UncheckedIOException;

public class UncheckedIOExceptionConverterHandler<S> extends DecoratedHandler<S> {

	public UncheckedIOExceptionConverterHandler(final Handler<S> handler) {
		super(handler);
	}

	@Override
	public void handle(final S s) {
		try {
			super.handle(s);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
