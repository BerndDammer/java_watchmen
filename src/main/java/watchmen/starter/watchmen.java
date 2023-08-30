package watchmen.starter;

import watchmen.root.myHttpServer;

public class watchmen {
	public watchmen(String[] args) {
		new myHttpServer();
	}

	public static void main(String[] args) {
		new watchmen(args);
	}
}
