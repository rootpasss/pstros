// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.io;

import java.io.IOException;
import java.io.DataOutput;
import java.io.DataInput;

public interface Datagram extends DataInput, DataOutput
{
    String getAddress();
    
    byte[] getData();
    
    int getLength();
    
    int getOffset();
    
    void reset();
    
    void setAddress(final Datagram p0);
    
    void setAddress(final String p0) throws IOException;
    
    void setData(final byte[] p0, final int p1, final int p2);
    
    void setLength(final int p0);
}
