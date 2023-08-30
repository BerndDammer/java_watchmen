package watchmen.subroothandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;

import watchmen.crashwatch.WatchedProcess;
import watchmen.root.SubRootHandler;
import watchmen.util.StreamBuffer;

public class ShowProcesses extends SubRootHandler {

	private final StreamBuffer sb = new StreamBuffer();
	private final List<WatchedProcess> watchedProcesses;

	public ShowProcesses(final String description, final String path, final List<WatchedProcess> watchedProcesses) {
		super(description, path);
		this.watchedProcesses = watchedProcesses;
	}

	@Override
	public void handle(HttpExchange he) throws IOException {
		sb.reset();
		PrintStream p = sb.getPrintStream();
		for (WatchedProcess wp : watchedProcesses) {
			addProgLine(wp, p);
		}

		sb.sswitch();

		he.getResponseHeaders().add("Content-Type", "text/plain");
		he.sendResponseHeaders(200, sb.getSize());
		OutputStream os = he.getResponseBody();
		sb.drainToOutputStream(os);
		os.close();
	}

	private void addProgLine(WatchedProcess wp, PrintStream p) {
		String msg = wp.getRunningMessage();
		p.println(msg);
	}
}
