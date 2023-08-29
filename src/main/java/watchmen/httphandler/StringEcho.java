package watchmen.httphandler;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class StringEcho implements HttpHandler
{

    private final String message;

    public StringEcho( String message )
    {
        this.message = message;
    }

    @Override
    public void handle(HttpExchange he) throws IOException
    {
        he.sendResponseHeaders(200, message.length());
        OutputStream os = he.getResponseBody();
        os.write( message.getBytes());
        os.close();
    }
}
