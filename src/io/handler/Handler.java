package io.handler;

import java.io.IOException;

public interface Handler<S> {

	void handle(S s) throws IOException;

}
