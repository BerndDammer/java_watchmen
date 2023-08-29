package watchmen.httphandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings("restriction")
public class DebugHandler implements HttpHandler
{
    private final Logger logger = Logger.getAnonymousLogger();

    private final StreamBuffer sb = new StreamBuffer();

    public DebugHandler()
    {

    }

    @Override
    public void handle(HttpExchange he) throws IOException
    {
        sb.reset();
        PrintStream p = sb.getPrintStream();

        p.println("----- Debug Echo -----");
        p.println("getRequestMethod() : " + he.getRequestMethod());
        p.println("getRequestURI() : " + he.getRequestURI());
        p.close();
        
        sb.sswitch();
        he.sendResponseHeaders(200, sb.getSize());
        OutputStream os = he.getResponseBody();
        sb.drainToOutputStream(os);
        os.close();
    }
}
