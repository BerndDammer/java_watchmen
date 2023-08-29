package watchmen.starter;

public enum BOARD {
	BOARDA(new String[] { "proca", "procb", "procc", "procd",
			"lockcontrolsoftware" }),
	BOARDB(new String[] { "", "", "" }), BOARDDEFAULT(new String[] { });

	private final String[] pids;

	BOARD(String[] pids) {
		this.pids = pids;
	}

	public static BOARD getByString(String board) {
		BOARD result = BOARD.BOARDDEFAULT;
		for (BOARD test : values()) {
			if (board.equalsIgnoreCase(test.name()))
				result = test;
		}
		return result;
	}

	public String[] getPids() {
		return pids;
	}
}
