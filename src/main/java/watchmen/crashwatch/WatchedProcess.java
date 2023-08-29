package watchmen.crashwatch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class WatchedProcess
{
    private final String progname;

    private boolean sawRunning = false;
    private final String pidfilename;
    private int pid = 0;
    
    public WatchedProcess(String progname)
    {
        this.progname = progname;
        pidfilename = "/var/run/" + progname + ".pid";
    }
    public boolean isRunning()
    {
        try
        {
            FileReader fr;
            fr = new FileReader(pidfilename);
            LineNumberReader lnr = new LineNumberReader(fr);
            String spid = lnr.readLine();
            pid = Integer.decode(spid);
            File processDirectory = new File("/proc/" + pid);
            if (processDirectory.exists() && processDirectory.isDirectory())
            {
                // good case
            } else
            {
                return false;
            }
            lnr.close();
            fr.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return true;
    }
    public boolean isCrashed()
    {
        if( isRunning() )
        {
            sawRunning = true;
            return false;
        }
        else
        {
            return sawRunning;
        }
    }
    public String getRunningMessage()
    {
        if(isRunning())
        {
            return("Process " + progname + " has PID : " + pid + " and is running");
        }
        else
        {
            return("Process " + progname + " has no process directory");
        }
    }
}
