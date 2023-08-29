package watchmen.crashwatch;

import java.util.logging.Logger;


public class CrashWatch
{
    private final Logger logger = Logger.getAnonymousLogger();

    private final CrashWatchParent parent;

    public CrashWatch(CrashWatchParent parent)
    {
        this.parent = parent;
        switch (parent.getBoard())
        {
        case BOARDA:
            logger.info("Im on A Board");
            break;
        case BOARDB:
            logger.info("Im on B Board");
            break;
        default:
            logger.severe("You should not be here");
            break;
        }
    }
}
