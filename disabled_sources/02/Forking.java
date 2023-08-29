package watchmen.statics;

import java.io.IOException;
import java.util.Properties;

public class Forking
{
    public static void doFork(String jarname)
    {
        Properties p = System.getProperties();
        String s = p.getProperty("file.separator");
        String command = p.getProperty("java.home") + s + "bin" + s + "java";
        ProcessBuilder pb;
        if( common.FORKING_PATH)
            jarname = "/home/" + jarname;
        pb = new ProcessBuilder( command, "-jar", jarname );
        try
        {
            terminal("Watchmen command " + command + " -jar " + jarname);
            Process pp = pb.start();
            terminal("Watchmen started !");
            System.exit(0);
        }
        catch (IOException e)
        {
            terminal("Exception while forking");
            e.printStackTrace();
            System.exit(2);
        }
    }
    private static final void terminal(String s)
    {
        //use terminal here because logging not started
        System.out.println( s );
    }
}
