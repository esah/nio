package io.handler;

public class ThreadedHandler<S> extends UncheckedIOExceptionConverterHandler<S> {

	public ThreadedHandler(final Handler<S> handler) {
		super(handler);
	}

	@Override
	public void handle(final S s) {
		new Thread(() -> super.handle(s)).start();
	}
}
