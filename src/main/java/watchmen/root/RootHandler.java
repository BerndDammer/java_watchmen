package watchmen.root;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RootHandler implements HttpHandler {
	private static final String HEAD = //
			"" + //
					"<!DOCTYPE html>\n" + //
					"<html>\n" + //
					"<head>\n" + //
					"<h1>WATCHMEN</h1>\n" + //
					"<h2>Select</h2>\n" + //
					"</head>\n" + //
					"<body>\n" + //
					"";
	private static final String TAIL = //
			"" + //
					"</body>\n" + //
					"</html>\n" + //
					"" + //
					"";

	final List<SubRootHandler> subHandlers;

	public RootHandler(final List<SubRootHandler> subHandlers) {
		this.subHandlers = subHandlers;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		final StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append(HEAD);
		for (SubRootHandler s : subHandlers) {
			stringBuilder.append("<p><a href=\"" + s.getPath() + "\">" + s.getDescription() + "</a></p>\n");
		}
		stringBuilder.append(TAIL);

		// push out answer / result
		byte[] answer = stringBuilder.toString().getBytes();

		exchange.getResponseHeaders().add("Content-Type", "text/html");
		exchange.sendResponseHeaders(200, answer.length);

		final OutputStream os = exchange.getResponseBody();
		os.write(stringBuilder.toString().getBytes());
		os.close();
	}
}
