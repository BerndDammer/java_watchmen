package watchmen.httphandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ResourceEcho extends MiMeTypes implements HttpHandler
{

    private final StreamBuffer sb = new StreamBuffer();
    private final URL resourceUrl;

    public ResourceEcho(URL resourceUrl)
    {
        this.resourceUrl = resourceUrl;
    }

    @Override
    public void handle(HttpExchange he) throws IOException
    {
        sb.reset();
        try
        {
            InputStream is = resourceUrl.openStream();
            sb.getFromInputStream(is);
            addContentType(he, resourceUrl.getPath() );
        }
        catch (IOException e)
        {
            sb.drainError(e);
            he.getResponseHeaders().add("Content-Type", "text/plain");
        }
        
        sb.sswitch();
        he.sendResponseHeaders(200, sb.getSize());
        OutputStream os = he.getResponseBody();
        sb.drainToOutputStream(os);
        os.close();
    }
}
