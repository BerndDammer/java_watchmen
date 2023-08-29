package watchmen.httphandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class SnapHandler implements HttpHandler
{
    private StreamBuffer sb = new StreamBuffer();

    public SnapHandler()
    {
    }

    @Override
    public void handle(HttpExchange he) throws IOException
    {
        makePackage();
        sb.sswitch();
        he.getResponseHeaders().add("Content-Type", "application/octet-stream");
        he.getResponseHeaders().add("Content-Disposition", "attachment; filename=crashlog.zip");
        he.sendResponseHeaders(200, sb.getSize());
        OutputStream os = he.getResponseBody();
        sb.drainToOutputStream(os);
        os.close();
    }

    private static final String[] LOGNAMES = new String[]
    {
        "proca.log",
        "procb.log",
        "procc.log"
    };

    // //////////////////////////////////
    // /
    // /
    private void makePackage()
    {
        sb.reset();
        OutputStream os = sb.getPrintStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(os);
        for (String logname : LOGNAMES)
        {
            addLogEntry(zipOutputStream, logname);
        }
        try
        {
            zipOutputStream.flush();
            zipOutputStream.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static final int TRANS_SIZE = 1024;
    private final byte[] buf = new byte[TRANS_SIZE];

    private void addLogEntry(ZipOutputStream zipOutputStream, String logname)
    {
        ZipEntry zipEntry = new ZipEntry(logname);
        try
        {
            zipOutputStream.putNextEntry(zipEntry);
            FileInputStream fileInputStream = new FileInputStream("/var/log/" + logname );
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buf)) > 0)
            {
                zipOutputStream.write(buf, 0, bytesRead);
            }
            zipOutputStream.closeEntry();
            fileInputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
