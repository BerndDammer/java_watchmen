package watchmen.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

import com.sun.net.httpserver.HttpServer;

import watchmen.httphandler.CommandHandler;
import watchmen.httphandler.DebugHandler;
import watchmen.httphandler.FileEcho;
import watchmen.httphandler.ResourceEcho;
import watchmen.httphandler.ShowProcesses;
import watchmen.httphandler.SnapHandler;
import watchmen.httphandler.StringEcho;
import watchmen.logging.LoggingInit;
import watchmen.statics.IConst;
import watchmen.statics.ListWatchedProcesses;

public class myHttpServer {

	private HttpServer myServer;
	private final List<SubRootHandler> subHandlers = new LinkedList<>();

	public myHttpServer() {
		List<String> cmd = new LinkedList<>();
		final InetSocketAddress isa = new InetSocketAddress(IConst.HTTP_PORT);

		final String logFileName = LoggingInit.forceClassLoadingAndGetLogName();
		System.out.println("Using Log File : " + logFileName);

		try {
			myServer = HttpServer.create(isa, 0);

//            myServer.createContext("/", new ResourcePathEcho(www2_root.class));
			// /
//			cmd = new LinkedList<>();
//			cmd.add("ls");
//			cmd.add("-l");
//			cmd.add("/var/log");
//			myServer.createContext("/dirlogs", new CommandHandler(cmd));
			add(new CommandHandler("List loggings", "/dirlogs", List.of("ls", "-l", "/var/log")));

//			cmd = new LinkedList<>();
//			cmd.add("ls");
//			cmd.add("-l");
//			cmd.add("/var/run");
//			myServer.createContext("/dirpids", new CommandHandler(cmd));
			add(new CommandHandler("List process Id's", "/dirpids", List.of("ls", "-l", "/var/run")));

//			cmd = new LinkedList<>();
//			cmd.add("ls");
//			cmd.add("-l");
//			cmd.add("/spare");
//			myServer.createContext("/dirspare", new CommandHandler(cmd));

			// ///////////////////////////////////////////////////////////////
//			List.of("journalctl", "-b");
//			cmd = new LinkedList<>();
//			cmd.add("/bin/journalctl");
//			cmd.add("-b");
//			myServer.createContext("/journalctl", new CommandHandler(cmd));
			add(new CommandHandler("show bootlog", "/journalctl", List.of("journalctl", "-b")));

//			cmd = new LinkedList<>();
//			cmd.add("dmesg");
//			myServer.createContext("/dmesg", new CommandHandler(cmd));
			add(new CommandHandler("dmesg"));

//			cmd = new LinkedList<>();
//			cmd.add("ps");
//			myServer.createContext("/ps", new CommandHandler(cmd));
			add(new CommandHandler("ps"));

//			cmd = new LinkedList<>();
//			cmd.add("top");
//			cmd.add("-b");
//			cmd.add("-n");
//			cmd.add("1");
//			myServer.createContext("/top", new CommandHandler(cmd));
			add(new CommandHandler("once top", "/top", List.of("top", "-b", "-n", "1")));

//			cmd = new LinkedList<>();
//			cmd.add("date");
//			myServer.createContext("/date", new CommandHandler(cmd));
			add(new CommandHandler("date"));

//			cmd = new LinkedList<>();
//			cmd.add("ifconfig");
//			cmd.add("-a");
//			myServer.createContext("/ifconfig", new CommandHandler(cmd));
			add(new CommandHandler("show network configuration", "/ifconfig", List.of("ifconfig", "-a" )));
			add(new CommandHandler("show active servers", "/netstat", List.of("netstat", "-tan" )));

			// ////////////////////////////////////////////////////////////
			myServer.createContext("/networklog", new FileEcho(logFileName));
			myServer.createContext("/showpids", new ShowProcesses(ListWatchedProcesses.getListWatchedProcesses()));
			myServer.createContext("/snap", new SnapHandler());

			// ////////////////////////////////////////////////////////////////
			myServer.createContext("/echo", new StringEcho("Hello Hello its nice to be here!"));
			myServer.createContext("/logs", new ResourceEcho(getClass().getResource("logs.html")));
			myServer.createContext("/debug", new DebugHandler());
			// /////////////////////////////////

			myServer.createContext("/", new RootHandler(subHandlers));

			myServer.start();
		} catch (IOException e) {
			e.printStackTrace();
			myServer = null;
		}
	}

	private void add(final SubRootHandler subRootHandler) {
		subHandlers.add(subRootHandler);
		myServer.createContext(subRootHandler.getPath(), subRootHandler);

	}
}
