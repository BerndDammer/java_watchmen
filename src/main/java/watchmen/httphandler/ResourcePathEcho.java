package watchmen.httphandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import watchmen.root.SubRootHandler;
import watchmen.util.MiMeTypes;
import watchmen.util.StreamBuffer;

public class ResourcePathEcho extends SubRootHandler implements HttpHandler, MiMeTypes
{
    // Examples
    // http://www.programcreek.com/java-api-examples/index.php?api=com.sun.net.httpserver.HttpServer
    private final Logger logger = Logger.getAnonymousLogger();

    private final StreamBuffer sb = new StreamBuffer();
    // private final StreamBuffer req = new StreamBuffer();
    private final Class<?> rootReferenceClass;

    public ResourcePathEcho(final String description, final String path, final Class<?> rootReferenceClass)
    {
    	super(description, path);
        this.rootReferenceClass = rootReferenceClass;
    }

    @Override
    public void handle(HttpExchange he) throws IOException
    {
        sb.reset();
        try
        {
            URI uri = he.getRequestURI();
            String s = uri.getPath();
            if (s == null || s.length() == 0 || s.equals("/"))
                s = "/index.html";
            URL path = rootReferenceClass.getResource(s.substring(1));
            logger.info("URL path : " + path.toString());
            InputStream ris = path.openStream();
            assert ris != null;
            sb.getFromInputStream(ris);
            addContentType(he, s);
        }
        catch (Exception e)
        {
            sb.drainError(e);
            he.getResponseHeaders().add("Content-Type", "text/plain");
        }

        sb.sswitch();
        int answerSize = sb.getSize();
        logger.info("answerSize : " + answerSize);
        he.sendResponseHeaders(HttpURLConnection.HTTP_OK, answerSize);
        OutputStream os = he.getResponseBody();
        sb.drainToOutputStream(os);
        os.close();
    }
}
