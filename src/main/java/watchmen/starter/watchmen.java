package watchmen.starter;

import watchmen.httpserver.myHttpServer;

public class watchmen {
	public watchmen(String[] args) {
		new myHttpServer();
	}

	public static void main(String[] args) {
		new watchmen(args);
	}
}
