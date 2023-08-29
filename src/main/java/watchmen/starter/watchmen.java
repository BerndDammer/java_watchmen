package watchmen.starter;

import java.util.LinkedList;
import java.util.List;

import watchmen.crashwatch.CrashWatchParent;
import watchmen.crashwatch.WatchedProcess;
import watchmen.httpserver.HttpServerParent;
import watchmen.httpserver.myHttpServer;
import watchmen.logging.LoggingInit;
import watchmen.networkinterfacewatch.NetworkInterfaceParent;
import watchmen.networkinterfacewatch.NetworkInterfaceWatcher;
import watchmen.statics.Forking;
import wefwefwe.common;

//
///////////////////////////////////////////////
//
// this project requires project specific setting
//
// windows -> preferences
//
// java -> compiler ->errors/warnings
//
// project specific settings
//
// Deprecated and restricted api
// Forbidden reference -> warning
//



public class watchmen implements CrashWatchParent, NetworkInterfaceParent, HttpServerParent
{
    private final String logFileName;
    private myHttpServer httpServer = null;
    private final BOARD board;

    private final List<WatchedProcess> watchedProcesses = new LinkedList<>();

    public watchmen(String[] args)
    {
//        board = args.length >= 1 ? BOARD.getByString(args[0]) : null;
        board = args.length >= 1 ? BOARD.getByString(args[0]) : BOARD.BOARDDEFAULT;
        if (board == null)
        {
            System.out.println("You must spec board");
            System.exit(2);
        }

        if (args.length >= 2)
        {
            switch (args[0])
            {
            case "FORKING":
                Forking.doFork(common.JAR);
                System.exit(3);
                break;
            }
        }
        this.logFileName = LoggingInit.forceClassLoadingAndGetLogName();
        System.out.println("Using Log File : " + logFileName);
        for (String n : board.getPids())
        {
            watchedProcesses.add(new WatchedProcess(n));
        }
        new NetworkInterfaceWatcher(this);
    }

    public static void main(String[] args)
    {
        new watchmen(args);
    }

    ////////////////////////////////////////////////////////////////////
    @Override
    public final List<WatchedProcess> getWatchedProcesses()
    {
        return watchedProcesses;
    }

    @Override
    public void habemusPapam()
    {
        if (httpServer == null)
            httpServer = new myHttpServer(this, common.HTTP_PORT);
    }

    @Override
    public final String getLogFileName()
    {
        return logFileName;
    }

    @Override
    public final BOARD getBoard()
    {
        return board;
    }
}
