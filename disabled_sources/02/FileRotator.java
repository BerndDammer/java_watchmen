package wefwefwe;

import java.io.File;

public class FileRotator
{
    private static final char[] rotTags;
    private static final String srotTags = "ABCDEFGHIJ";

    static
    {
        rotTags = new char[srotTags.length()];
        for (int i = 0; i < srotTags.length(); i++)
        {
            rotTags[i] = srotTags.charAt(i);
        }
    }

    public FileRotator(String preface, int numRotations)
    {
        
    }
    
    public File getNew()
    {
        return null;
    }
    public File rotateAndGiveNewFile()
    {
        return null;
    }
}
