package watchmen.subroothandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import com.sun.net.httpserver.HttpExchange;

import watchmen.root.SubRootHandler;
import watchmen.util.StreamBuffer;

public class DebugHandler extends SubRootHandler
{
    private final StreamBuffer sb = new StreamBuffer();

    public DebugHandler(final String description, final String path)
    {
    	super(description, path);
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
