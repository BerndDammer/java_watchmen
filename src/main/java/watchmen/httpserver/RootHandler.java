package watchmen.httpserver;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RootHandler implements HttpHandler {

	final List<SubRootHandler> subHandlers;
	
	public RootHandler(final List<SubRootHandler> subHandlers)
	{
		this.subHandlers = subHandlers;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
	    final StringBuilder stringBuilder = new StringBuilder();
	    
	    stringBuilder.append("hfiufeuiwwwhfuiwehfiuhewuifhuwiehfieuw\n");
        byte[] answer = stringBuilder.toString().getBytes();

        exchange.getResponseHeaders().add("Content-Type", "text/plain");
        exchange.sendResponseHeaders(200, answer.length);

        final OutputStream os = exchange.getResponseBody();
        os.write( stringBuilder.toString().getBytes());
        os.close();
	}
}
