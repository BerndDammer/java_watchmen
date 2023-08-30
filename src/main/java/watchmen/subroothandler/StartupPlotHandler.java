package watchmen.subroothandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;

import watchmen.root.SubRootHandler;
import watchmen.util.StreamBuffer;

public class StartupPlotHandler extends SubRootHandler {
	private static final Logger logger = Logger.getLogger(StartupPlotHandler.class.getName());

	private StreamBuffer sb = new StreamBuffer();

	public StartupPlotHandler(final String description, final String path) {
		super(description, path);
	}

	@Override
	public void handle(HttpExchange he) throws IOException {
		try {
			final ProcessBuilder pb = new ProcessBuilder(List.<String>of("systemd-analyze", "plot"));
			final Process pp = pb.start();

			final InputStream consoleOutput = pp.getInputStream();

			sb.reset();
			final PrintStream p = sb.getPrintStream();
			sb.appendFromInputStream(consoleOutput);
			p.close();
		} catch (IOException e) {
			logger.info(e.getMessage());
			sb.reset();
			sb.drainError(e);
		}
		sb.sswitch();
		he.getResponseHeaders().add("Content-Type", "image/svg+xml");
		he.sendResponseHeaders(200, sb.getSize());
		OutputStream os = he.getResponseBody();
		sb.drainToOutputStream(os);
		os.close();
	}
}
