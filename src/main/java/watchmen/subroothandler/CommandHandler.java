package watchmen.subroothandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;

import watchmen.root.SubRootHandler;
import watchmen.util.StreamBuffer;

public class CommandHandler extends SubRootHandler {
	private static final Logger logger = Logger.getLogger(CommandHandler.class.getName());

	private final List<String> command;
	private final ProcessBuilder pb;

	private final StreamBuffer sb = new StreamBuffer();

	public CommandHandler(final String allinone) {
		this(allinone, "/" + allinone, List.of(allinone));
	}

	public CommandHandler(final String description, final String path, final List<String> command) {
		super(description, path);
		this.command = command;
		pb = new ProcessBuilder(command);
	}

	@Override
	public void handle(final HttpExchange he) throws IOException {
		try {
			final Process pp = pb.start();
			final InputStream consoleOutput = pp.getInputStream();
			final InputStream consoleErrorOutput = pp.getErrorStream();

			sb.reset();
			final PrintStream p = sb.getPrintStream();
			p.println("---- Executing command --- " + command + " ----");
			p.println("*********************************************** OUTPUT **************************************");
			p.flush();
			sb.appendFromInputStream(consoleOutput);
			p.println("**********************************************************************************************");
			p.println("************************************************* ERROR *************************************");
			p.flush();
			sb.appendFromInputStream(consoleErrorOutput);
			p.println("**********************************************************************************************");
			p.close();
		} catch (IOException e) {
			logger.info(e.getMessage());
			sb.reset();
			sb.drainError(e);
		}
		sb.sswitch();
		// WORKAROUND : avoid downloading
		he.getResponseHeaders().add("X-Content-Type-Options", "nosniff");
		he.getResponseHeaders().add("Content-Type", "text/plain");
		he.sendResponseHeaders(HttpURLConnection.HTTP_OK, sb.getSize());
		final OutputStream os = he.getResponseBody();
		sb.drainToOutputStream(os);
		os.close();
	}
}
