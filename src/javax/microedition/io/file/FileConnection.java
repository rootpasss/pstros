// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.io.file;

import java.util.Enumeration;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.StreamConnection;

public interface FileConnection extends StreamConnection
{
    boolean isOpen();
    
    InputStream openInputStream() throws IOException;
    
    DataInputStream openDataInputStream() throws IOException;
    
    OutputStream openOutputStream() throws IOException;
    
    DataOutputStream openDataOutputStream() throws IOException;
    
    OutputStream openOutputStream(final long p0) throws IOException;
    
    long totalSize();
    
    long availableSize();
    
    long usedSize();
    
    long directorySize(final boolean p0) throws IOException;
    
    long fileSize() throws IOException;
    
    boolean canRead();
    
    boolean canWrite();
    
    boolean isHidden();
    
    void setReadable(final boolean p0) throws IOException;
    
    void setWritable(final boolean p0) throws IOException;
    
    void setHidden(final boolean p0) throws IOException;
    
    Enumeration list() throws IOException;
    
    Enumeration list(final String p0, final boolean p1) throws IOException;
    
    void create() throws IOException;
    
    void mkdir() throws IOException;
    
    boolean exists();
    
    boolean isDirectory();
    
    void delete() throws IOException;
    
    void rename(final String p0) throws IOException;
    
    void truncate(final long p0) throws IOException;
    
    void setFileConnection(final String p0) throws IOException;
    
    String getName();
    
    String getPath();
    
    String getURL();
    
    long lastModified();
}
