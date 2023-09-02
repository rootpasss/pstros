// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public interface InputConnection extends Connection
{
    InputStream openInputStream() throws IOException;
    
    DataInputStream openDataInputStream() throws IOException;
}
