// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.io;

public interface ContentConnection extends StreamConnection
{
    String getType();
    
    String getEncoding();
    
    long getLength();
}
