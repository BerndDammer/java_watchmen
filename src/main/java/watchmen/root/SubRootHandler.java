package watchmen.root;

import com.sun.net.httpserver.HttpHandler;

public abstract class SubRootHandler implements HttpHandler {
	final String description;
	final String path;
	
	public SubRootHandler(final String description, final String path)
	{
		this.description = description;
		this.path = path;
	}

	public String getDescription() {
		return description;
	}

	public String getPath() {
		return path;
	}

}
