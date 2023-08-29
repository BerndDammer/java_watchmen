package watchmen.httphandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class FileEcho extends MiMeTypes implements HttpHandler
{
    // Examples
    // http://www.programcreek.com/java-api-examples/index.php?api=com.sun.net.httpserver.HttpServer
    //private final Logger logger = Logger.getAnonymousLogger();

    private final StreamBuffer sb = new StreamBuffer();
    //private final StreamBuffer req = new StreamBuffer();
    private final String filename;

    public FileEcho(String filename)
    {
        this.filename = filename;
    }

    @Override
    public void handle(HttpExchange he) throws IOException
    {
        sb.reset();
        try
        {
            FileInputStream fis = new FileInputStream( filename );
            assert fis != null;
            sb.getFromInputStream( fis);
        }
        catch (IOException e)
        {
            sb.drainError(e);
        }

        sb.sswitch();
        he.getResponseHeaders().add("Content-Type", "text/plain");
        he.sendResponseHeaders(200, sb.getSize() );
        OutputStream os = he.getResponseBody();
        sb.drainToOutputStream(os);
        os.close();
    }
}
