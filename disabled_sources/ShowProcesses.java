package watchmen.httphandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintStream;

import watchmen.starter.BOARD;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ShowProcesses implements HttpHandler
{
    private final BOARD board;


    private final StreamBuffer sb = new StreamBuffer();

    public ShowProcesses(BOARD board)
    {
        this.board = board;
    }

    @Override
    public void handle(HttpExchange he) throws IOException
    {
        sb.reset();
        PrintStream p = sb.getPrintStream();
        for (String progname : board.getPids())
        {
            addProgLine(progname, p);
        }

        sb.sswitch();

        he.getResponseHeaders().add("Content-Type", "text/plain");
        he.sendResponseHeaders(200, sb.getSize());
        OutputStream os = he.getResponseBody();
        sb.drainToOutputStream(os);
        os.close();
    }

    private void addProgLine(String progname, PrintStream p)
    {
        String pidfilename = "/var/run/" + progname + ".pid";
        try
        {
            FileReader fr;
            fr = new FileReader(pidfilename);
            LineNumberReader lnr = new LineNumberReader(fr);
            String spid = lnr.readLine();
            int pid = Integer.decode(spid);
            File processDirectory = new File("/proc/" + pid);
            if (processDirectory.exists() && processDirectory.isDirectory())
            {
                p.println("Process " + progname + " has PID : " + pid + " and is running");
            } else
            {
                p.println("Process " + progname + " has no process directory");
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace(p);
        }
        catch (IOException e)
        {
            e.printStackTrace(p);
        }
    }
}
