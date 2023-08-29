package watchmen.logging;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class LoggingInit
{
    //private static final long serialVersionUID = common.fap.svuid;

    private static final Logger anonymousLogger;
    private static final String filename;

    
    public static final String forceClassLoadingAndGetLogName()
    {
        return filename;
    }

    static
    {
        /////////create logging properties
        Properties myLogPoperties = new Properties();

        filename = findLoggingPath();
        
        //myLogPoperties.setProperty(".level", "INFO");
        myLogPoperties.setProperty(".level", "FINER");
        myLogPoperties.setProperty("handlers", "java.util.logging.ConsoleHandler,java.util.logging.FileHandler");
        myLogPoperties.setProperty("java.util.logging.ConsoleHandler.level", "ALL");
        myLogPoperties.setProperty("java.util.logging.ConsoleHandler.formatter", "java.util.logging.SimpleFormatter");
        myLogPoperties.setProperty("java.util.logging.FileHandler.level", "ALL");
        myLogPoperties.setProperty("java.util.logging.FileHandler.formatter", "java.util.logging.SimpleFormatter");
        myLogPoperties.setProperty("java.util.logging.FileHandler.pattern", filename );
        myLogPoperties.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: [%1$tc]  <<<%5$s>>>%n");

        /////////make input stream of properties
        try
        {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            myLogPoperties.store(output, null);
            ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
            LogManager.getLogManager().readConfiguration(input);
        }
        catch (IOException xe)
        {
            // TODO what to do here
            end();
        }
        anonymousLogger = Logger.getAnonymousLogger();
    }

    public static void exception(Exception e)
    {
        anonymousLogger.throwing("error classs", "error.throwing", e);
        end();
    }

    public static void exit(String text)
    {
        anonymousLogger.severe(text);
        end();
    }

    private static void end()
    {
        System.exit(1);
    }
    private static final String findLoggingPath()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY_MM_dd__HH_mm");

        StringBuffer result = new StringBuffer();
        StringBuffer templateLogFilenamname = new StringBuffer();
        templateLogFilenamname.append("PROGG_Log_Network_");
        templateLogFilenamname.append( sdf.format( new Date() ));
        templateLogFilenamname.append( ".log" );
        
        //writeable spare partition
        result.setLength(0);
        result.append( "/spare/");
        result.append( templateLogFilenamname );
        
        if( checkFileWriteable(result.toString()))
        {
            return(result.toString() );
        }
        
        //var log
        result.setLength(0);
        result.append( "/var/log/");
        result.append( templateLogFilenamname );
        
        if( checkFileWriteable(result.toString()))
        {
            return(result.toString() );
        }
        
        //last check
        result.setLength(0);
        result.append( System.getProperty("user.home"));
        result.append( System.getProperty("file.separator"));
        result.append( templateLogFilenamname );
        if( checkFileWriteable(result.toString()))
        {
            return(result.toString() );
        }
        //this should not happen
        return null;
    }
    private static final boolean checkFileWriteable(String filename)
    {
        PrintWriter pw = null;
        File file = new File( filename);
        try
        {
            pw = new PrintWriter( file );
        }
        catch (FileNotFoundException e)
        {
            return false;
        }
        pw.println("---test for writeability---");
        pw.close();
        return true;
    }
}
