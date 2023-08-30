package watchmen.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;

public class StreamBuffer extends OutputStream
{
    private static final int CAP = 2048;
    private ByteBuffer buffer;
    private final byte[] bb = new byte[CAP];

    public StreamBuffer()
    {
        buffer = ByteBuffer.allocate(CAP);
        buffer.clear();
    }
    public void getFromInputStream( InputStream in)
    {
        int len;
        //buffer.clear();
        try
        {
            len = in.read( bb);
            while (len != -1)
            {
                ensureCapacity( len );
                buffer.put(bb, 0, len);
                len = in.read( bb);
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            drainError(e);
        }
    }
    public void appendFromInputStream( InputStream in)
    {
        int len;
        //buffer.clear();
        try
        {
            len = in.read( bb);
            while (len != -1)
            {
                ensureCapacity( len );
                buffer.put(bb, 0, len);
                len = in.read( bb);
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            drainError(e);
        }
    }
    public void drainToOutputStream(OutputStream os)
    {
        try
        {
            os.write(buffer.array(), 0, buffer.limit());
        }
        catch (IOException e)
        {
            //this should not happen
            e.printStackTrace();
        }
    }
    public String asString()
    {
        return new String( buffer.array(), 0, buffer.limit() );
    }
    public int getSize()
    {
        return buffer.limit();
    }
    public void sswitch()
    {
        buffer.flip();
    }
    public void reset()
    {
        buffer.clear();
    }
    public PrintStream getPrintStream()
    {
        //buffer.clear();
        return new PrintStream(this);
    }
    //////////////////////////////////////////////////////////////
    //
    //private helper
    private void ensureCapacity(int size)
    {
        if( buffer.remaining() < size )
        {
            int requested = buffer.position() + size;
            int newBufferSize = buffer.capacity();
            while( newBufferSize < requested)
            {
                newBufferSize *= 2;
            }
            ByteBuffer newBuffer = ByteBuffer.allocate( newBufferSize );
            newBuffer.clear();
            buffer.flip();
            newBuffer.put(buffer);
            buffer = newBuffer; 
        }
    }
    public void drainError(Exception e)
    {
        PrintStream p = getPrintStream();
        e.printStackTrace( p );
        p.flush();
    }
    ///////////////////////////////////////////// OutputStream
    @Override
    public void write(byte[] b, int off, int len) throws IOException
    {
        ensureCapacity( len );
        buffer.put(b,off,len);
    }
    @Override
    public void write(byte[] b) throws IOException
    {
        ensureCapacity(b.length);
        buffer.put(b);
    }
    @Override
    public void write(int b) throws IOException
    {
        ensureCapacity(1);
        buffer.put((byte)b);
    }
    @Override
    public void close()
    {
        //buffer.flip();
    }
}
