package watchmen.util;

import com.sun.net.httpserver.HttpExchange;

public interface MiMeTypes
{
    static final String MIME_DEFAULT = "text/plain";
    static final String[][] MIME_TYPES = new String[][]
    {
            {
                    ".htm", "text/html"
            },
            {
                    ".html", "text/html"
            },
            {
                    ".css", "text/css"
            },
            {
                    ".cgi", "text/plain"
            },
    };

    // /////////////////////////////////////////////////////////////////
    // http://wiki.selfhtml.org/wiki/Referenz:MIME-Typen
    default void addContentType(HttpExchange he, String filename)
    {
        String result = null;
        for (String[] line : MIME_TYPES)
        {
            if (filename.endsWith(line[0]))
            {
                result = line[1];
                break;
            }
        }
        if (result == null)
            result = MIME_DEFAULT;
        he.getResponseHeaders().add("Content-Type", result);
    }

}
