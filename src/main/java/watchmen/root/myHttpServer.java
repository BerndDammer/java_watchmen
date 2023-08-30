package watchmen.root;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

import com.sun.net.httpserver.HttpServer;

import watchmen.logging.LoggingInit;
import watchmen.statics.IConst;
import watchmen.statics.ListWatchedProcesses;
import watchmen.subroothandler.CommandHandler;
import watchmen.subroothandler.DebugHandler;
import watchmen.subroothandler.FileEcho;
import watchmen.subroothandler.ResourceEcho;
import watchmen.subroothandler.ShowProcesses;
import watchmen.subroothandler.SnapHandler;
import watchmen.subroothandler.StartupPlotHandler;
import watchmen.subroothandler.StringEcho;

public class myHttpServer {

	private HttpServer myServer;
	private final List<SubRootHandler> subHandlers = new LinkedList<>();

	public myHttpServer() {
//		List<String> cmd = new LinkedList<>();
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
			add(new CommandHandler("List loggings", "/dirlogs", List.<String>of("ls", "-l", "/var/log")));

//			cmd = new LinkedList<>();
//			cmd.add("ls");
//			cmd.add("-l");
//			cmd.add("/var/run");
//			myServer.createContext("/dirpids", new CommandHandler(cmd));
			add(new CommandHandler("List process Id's", "/dirpids", List.<String>of("ls", "-l", "/var/run")));

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
			add(new CommandHandler("show bootlog", "/journalctl", List.<String>of("journalctl", "-b")));

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
			add(new CommandHandler("once top", "/top", List.<String>of("top", "-b", "-n", "1")));

//			cmd = new LinkedList<>();
//			cmd.add("date");
//			myServer.createContext("/date", new CommandHandler(cmd));
			add(new CommandHandler("date"));

//			cmd = new LinkedList<>();
//			cmd.add("ifconfig");
//			cmd.add("-a");
//			myServer.createContext("/ifconfig", new CommandHandler(cmd));
			add(new CommandHandler("show network configuration", "/ifconfig", List.<String>of("ifconfig", "-a")));
			add(new CommandHandler("show active servers", "/netstat", List.<String>of("netstat", "-tan")));

			// ////////////////////////////////////////////////////////////
			add(new FileEcho("log of this server", "/networklog", logFileName));
			add(new ShowProcesses("showpids", "/showpids", ListWatchedProcesses.getListWatchedProcesses()));
			add(new SnapHandler("snap", "/snap"));

			// ////////////////////////////////////////////////////////////////
			add(new StringEcho("echo", "/echo", "Hello Hello its nice to be here!"));
			add(new ResourceEcho("logs", "/logs", getClass().getResource("test.html")));
			add(new DebugHandler("protocol test echo", "/debug"));
			add(new StartupPlotHandler("startup plot", "/plot"));
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
