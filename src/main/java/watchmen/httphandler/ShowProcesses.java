package watchmen.httphandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import watchmen.crashwatch.WatchedProcess;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ShowProcesses implements HttpHandler
{

    private final StreamBuffer sb = new StreamBuffer();
    private final List<WatchedProcess> watchedProcesses;

    public ShowProcesses(List<WatchedProcess> watchedProcesses)
    {
        this.watchedProcesses = watchedProcesses;
    }

    @Override
    public void handle(HttpExchange he) throws IOException
    {
        sb.reset();
        PrintStream p = sb.getPrintStream();
        for ( WatchedProcess wp : watchedProcesses)
        {
            addProgLine(wp, p);
        }

        sb.sswitch();

        he.getResponseHeaders().add("Content-Type", "text/plain");
        he.sendResponseHeaders(200, sb.getSize());
        OutputStream os = he.getResponseBody();
        sb.drainToOutputStream(os);
        os.close();
    }

    private void addProgLine(WatchedProcess wp, PrintStream p)
    {
        String msg = wp.getRunningMessage();
        p.println( msg );
    }
}
