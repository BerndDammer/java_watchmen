package watchmen.subroothandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map.Entry;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import watchmen.root.SubRootHandler;
import watchmen.util.StreamBuffer;

public class DebugHandler extends SubRootHandler {
	private final StreamBuffer sb = new StreamBuffer();

	public DebugHandler(final String description, final String path) {
		super(description, path);
	}

	@Override
	public void handle(HttpExchange he) throws IOException {
		sb.reset();
		final PrintStream p = sb.getPrintStream();

		p.println("----- Root -----");
		p.println("getRequestMethod() : " + he.getRequestMethod());
		p.println("getRemoteAddress() : " + he.getRemoteAddress());
		p.println("getLocalAddress() : " + he.getLocalAddress());
		p.println("getPrincipal() : " + he.getPrincipal());
		p.println("getProtocol() : " + he.getProtocol());
		p.println("getRequestURI() : " + he.getRequestURI());
		p.println("----- Request Headers -----");
		final Headers headers = he.getRequestHeaders();
		for (final Entry<String, List<String>> entry : headers.entrySet()) {
			final String tag = entry.getKey();
			for (final String value : entry.getValue()) {
				p.println("   " + tag + ":" + value);
			}
		}
		p.close();

		sb.sswitch();
		he.sendResponseHeaders(HttpURLConnection.HTTP_OK, sb.getSize());
		OutputStream os = he.getResponseBody();
		sb.drainToOutputStream(os);
		os.close();
	}
}
