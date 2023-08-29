package watchmen.httphandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class CommandHandler implements HttpHandler
{
    private final Logger logger = Logger.getAnonymousLogger();

    private final List<String> command;
    private final ProcessBuilder pb;
    
    private StreamBuffer sb = new StreamBuffer();

    public CommandHandler(final List<String> command)
    {
        this.command = command;
        pb = new ProcessBuilder(command);
    }

    @Override
    public void handle(HttpExchange he) throws IOException
    {
        try
        {
            Process pp = pb.start();
            final InputStream consoleOutput = pp.getInputStream();
            final InputStream consoleErrorOutput = pp.getErrorStream();

            sb.reset();
            final PrintStream p = sb.getPrintStream();
            p.println("---- Executing command --- " + command + " ----");
            p.println("****** OUTPUT ******");
            p.flush();
            sb.appendFromInputStream( consoleOutput );
            p.println("AAAA OUTPUT AAAAA");
            p.println("****** ERROR ******");
            p.flush();
            sb.appendFromInputStream( consoleErrorOutput );
            p.println("END  ERROR END");
            p.close();
        }
        catch (IOException e)
        {
            sb.reset();
            sb.drainError(e);
        }
        sb.sswitch();
        he.getResponseHeaders().add("Content-Type", "text/plain");
        he.sendResponseHeaders(200, sb.getSize());
        OutputStream os = he.getResponseBody();
        sb.drainToOutputStream(os);
        os.close();
    }
}
