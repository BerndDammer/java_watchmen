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
		final InetSocketAddress isa = new InetSocketAddress(IConst.HTTP_PORT);

		final String logFileName = LoggingInit.forceClassLoadingAndGetLogName();
		System.out.println("Using Log File : " + logFileName);

		try {
			myServer = HttpServer.create(isa, 0);

			add(new CommandHandler("apps status", "/appstats",
					List.<String>of("systemctl", "status", "kiosk.service", "watchmen.service")));
			add(new CommandHandler("serial numbers of picoprobes", "/picoprobes", List.<String>of("lsusb", "-vvv")));
			add(new CommandHandler("pi version 1", "/piv1",
					List.<String>of("cat", "/sys/firmware/devicetree/base/model")));
			add(new CommandHandler("pi version 2", "/piv2", List.<String>of("cat", "/proc/cpuinfo")));
//			add(new CommandHandler("pi version 4", "/piv3",
//					List.<String>of("grep", "-E", "\"Model|Revision\"", "/proc/cpuinfo")));
			add(new CommandHandler("pi version 3", "/piv4", List.<String>of("vcgencmd", "otp_dump")));
/////////////////////
			add(new CommandHandler("show serial interfaces", "/showtty",
					List.<String>of("sudo", "find", "/", "-wholename", "/dev/*tty*")));

			add(new CommandHandler("show serial interfaces2", "/showtty2",
					List.<String>of("sudo", "ls", "-hl", "/dev/*tty*")));

			add(new CommandHandler("show serial interfaces3", "/showtty3",
					List.<String>of("tree", "-P", "*tty*", "-L", "1", "/dev")));
///////////////////////////////////			
			add(new CommandHandler("List loggings", "/dirlogs", List.<String>of("ls", "-l", "/var/log")));
			add(new CommandHandler("List process Id's", "/dirpids", List.<String>of("ls", "-l", "/var/run")));
			add(new CommandHandler("show bootlog", "/journalctl", List.<String>of("journalctl", "-b")));
			add(new CommandHandler("dmesg"));
			add(new CommandHandler("ps"));
			add(new CommandHandler("once top", "/top", List.<String>of("top", "-b", "-n", "1")));
			add(new CommandHandler("date"));
			add(new CommandHandler("show network configuration", "/ifconfig", List.<String>of("ifconfig", "-a")));
			add(new CommandHandler("show active servers", "/netstat", List.<String>of("netstat", "-tan")));
			add(new CommandHandler("arp", "/arp", List.<String>of("arp", "-a")));
			add(new CommandHandler("traceroute", "/traceroute", List.<String>of("traceroute", "openwrt.org")));

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
