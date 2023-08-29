package watchmen.httpserver;

import java.util.List;

import watchmen.crashwatch.WatchedProcess;

public interface HttpServerParent
{
    String getLogFileName();
//    BOARD getBoard();
    List<WatchedProcess> getWatchedProcesses();

}
