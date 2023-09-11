package watchmen.subroothandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.sun.net.httpserver.HttpExchange;

import watchmen.root.SubRootHandler;
import watchmen.util.MiMeTypes;
import watchmen.util.StreamBuffer;

public class ResourceEcho extends SubRootHandler implements MiMeTypes
{
    private final StreamBuffer sb = new StreamBuffer();
    private final URL resourceUrl;

    public ResourceEcho(final String description, final String path, URL resourceUrl)
    {
    	super(description, path);
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
        he.sendResponseHeaders(HttpURLConnection.HTTP_OK, sb.getSize());
        OutputStream os = he.getResponseBody();
        sb.drainToOutputStream(os);
        os.close();
    }
}
