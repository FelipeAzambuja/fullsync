package net.sourceforge.fullsync.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.sourceforge.fullsync.buffer.EntryDescriptor;
import net.sourceforge.fullsync.fs.File;


/**
 * @author <a href="mailto:codewright@gmx.net">Jan Kopcsek</a>
 */
public class DirCreationEntryDescriptor implements EntryDescriptor
{
    //private Directory src;
    private File dst;
    
    public DirCreationEntryDescriptor( /*Directory src,*/ File dst )
    {
        //this.src = src;
        this.dst = dst;
        if( dst == null )
            throw new RuntimeException( "can't give me null !!" );
    }
    
    public long getLength()
    {
        return 0;
    }
    public InputStream getInputStream()
    	throws IOException
    {
        return null;
    }
    public OutputStream getOutputStream()
    	throws IOException
    {
        return null;
    }
    public void finishWrite()
    {
        try {
            dst.makeDirectory();
            dst.refresh();
            
        } catch( IOException e ) {
            e.printStackTrace();
        }
    }
    public void finishStore()
    {
        
    }
    public String getOperationDescription()
    {
        return "Making Directory "+dst.getPath();
    }
}
