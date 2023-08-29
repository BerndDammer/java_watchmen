package watchmen.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

import watchmen.httphandler.CommandHandler;
import watchmen.httphandler.DebugHandler;
import watchmen.httphandler.FileEcho;
import watchmen.httphandler.ResourceEcho;
import watchmen.httphandler.ResourcePathEcho;
import watchmen.httphandler.ShowProcesses;
import watchmen.httphandler.SnapHandler;
import watchmen.httphandler.StringEcho;
import www2_root.www2_root;

import com.sun.net.httpserver.HttpServer;

public class myHttpServer
{
    // private final HttpServerParent parent;
    private HttpServer myServer;

    public myHttpServer(HttpServerParent parent, int port)
    {
        List<String> cmd = new LinkedList<>();
        // this.parent = parent;
        InetSocketAddress isa = new InetSocketAddress(port);
        try
        {
            myServer = HttpServer.create(isa, 0);

            myServer.createContext("/", new ResourcePathEcho(www2_root.class));
            // /
            cmd = new LinkedList<>();
            cmd.add("ls");
            cmd.add("-l");
            cmd.add("/var/log");
            myServer.createContext("/dirlogs", new CommandHandler(cmd));

            cmd = new LinkedList<>();
            cmd.add("ls");
            cmd.add("-l");
            cmd.add("/var/run");
            myServer.createContext("/dirpids", new CommandHandler(cmd));

            cmd = new LinkedList<>();
            cmd.add("ls");
            cmd.add("-l");
            cmd.add("/spare");
            myServer.createContext("/dirspare", new CommandHandler(cmd));

            // ///////////////////////////////////////////////////////////////
            cmd = new LinkedList<>();
            cmd.add("/bin/journalctl");
            cmd.add("-b");
            myServer.createContext("/journalctl", new CommandHandler(cmd));

            cmd = new LinkedList<>();
            cmd.add("dmesg");
            myServer.createContext("/dmesg", new CommandHandler(cmd));

            cmd = new LinkedList<>();
            cmd.add("ps");
            myServer.createContext("/ps", new CommandHandler(cmd));

            cmd = new LinkedList<>();
            cmd.add("top");
            cmd.add("-b");
            cmd.add("-n");
            cmd.add("1");
            myServer.createContext("/top", new CommandHandler(cmd));

            cmd = new LinkedList<>();
            cmd.add("date");
            myServer.createContext("/date", new CommandHandler(cmd));

            cmd = new LinkedList<>();
            cmd.add("ipconfig");
            cmd.add("-a");
            myServer.createContext("/ifconfig", new CommandHandler(cmd));

            // ////////////////////////////////////////////////////////////
            myServer.createContext("/networklog", new FileEcho(parent.getLogFileName()));
            myServer.createContext("/showpids", new ShowProcesses(parent.getWatchedProcesses()));
            myServer.createContext("/snap", new SnapHandler());

            // ////////////////////////////////////////////////////////////////
            myServer.createContext("/echo", new StringEcho("Hello Hello its nice to be here!"));
            myServer.createContext("/logs", new ResourceEcho(getClass().getResource("logs.html")));
            myServer.createContext("/debug", new DebugHandler());
            // /////////////////////////////////
            myServer.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            myServer = null;
        }
    }
}
