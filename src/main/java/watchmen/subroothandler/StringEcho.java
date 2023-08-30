package watchmen.subroothandler;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;

import watchmen.root.SubRootHandler;

public class StringEcho extends SubRootHandler {

	private final String message;

	public StringEcho(final String description, final String path,String message) {
		super(description, path);
		this.message = message;
	}

	@Override
	public void handle(HttpExchange he) throws IOException {
		he.getResponseHeaders().add("Content-Type", "text/plain");

		final byte[] b = message.getBytes();
		he.sendResponseHeaders(200, b.length);
		OutputStream os = he.getResponseBody();
		os.write(b);
		os.close();
	}
}
