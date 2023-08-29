package watchmen.httpserver;

import java.util.List;

import watchmen.crashwatch.WatchedProcess;
import watchmen.starter.BOARD;

public interface HttpServerParent
{
    String getLogFileName();
    BOARD getBoard();
    List<WatchedProcess> getWatchedProcesses();

}
