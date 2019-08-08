package io.handler;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

public class ExecutorServiceHandler<S> extends DecoratedHandler<S> {

	private final ExecutorService pool;
	private final Thread.UncaughtExceptionHandler exceptionHandler;

	public ExecutorServiceHandler(final Handler<S> handler,
								  final ExecutorService pool,
								  final Thread.UncaughtExceptionHandler exceptionHandler) {
		super(handler);
		this.pool = pool;
		this.exceptionHandler = exceptionHandler;
	}

	public ExecutorServiceHandler(final Handler<S> handler,
								  final ExecutorService pool) {
		this(handler, pool, (t, e) -> System.out.println(t + ": " + e));
	}

	@Override
	public void handle(final S s) throws IOException {
		pool.submit(new FutureTask<>(() -> {
						super.handle(s);
						return null;
					}){
						@Override
						protected void setException(final Throwable t) {
							exceptionHandler.uncaughtException(Thread.currentThread(), t);
						}
					}
		);
	}
}
