package watchmen.subroothandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import com.sun.net.httpserver.HttpExchange;

import watchmen.root.SubRootHandler;
import watchmen.util.StreamBuffer;

public class FileEcho extends SubRootHandler {

	private final StreamBuffer sb = new StreamBuffer();
	private final String filename;

	public FileEcho(final String description, final String path, final String filename) {
		super(description, path);
		this.filename = filename;
	}

	@Override
	public void handle(HttpExchange he) throws IOException {
		sb.reset();
		try {
			FileInputStream fis = new FileInputStream(filename);
			assert fis != null;
			sb.getFromInputStream(fis);
		} catch (IOException e) {
			sb.drainError(e);
		}

		sb.sswitch();
		he.getResponseHeaders().add("Content-Type", "text/plain");
		he.sendResponseHeaders(HttpURLConnection.HTTP_OK, sb.getSize());
		OutputStream os = he.getResponseBody();
		sb.drainToOutputStream(os);
		os.close();
	}
}
