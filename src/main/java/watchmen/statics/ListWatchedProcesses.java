package watchmen.statics;

import java.util.ArrayList;
import java.util.List;

import watchmen.crashwatch.WatchedProcess;

public class ListWatchedProcesses extends ArrayList<WatchedProcess> {
	private static final long serialVersionUID = 1L;

	private ListWatchedProcesses()
	{
//		add( new WatchedProcess("iwioedjwodj"));
	}
	public static List<WatchedProcess> getListWatchedProcesses()
	{
		return new ListWatchedProcesses();
	}
}
