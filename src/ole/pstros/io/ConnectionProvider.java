// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.io;

import javax.microedition.io.Connection;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;

public interface ConnectionProvider
{
    boolean init(final String p0);
    
    InputStream openInputStream(final String p0) throws IOException;
    
    OutputStream openOutputStream(final String p0) throws IOException;
    
    DataInputStream openDataInputStream(final String p0) throws IOException;
    
    DataOutputStream openDataOutputStream(final String p0) throws IOException;
    
    Connection open(final String p0, final int p1, final boolean p2) throws IOException;
    
    String getScheme();
}
