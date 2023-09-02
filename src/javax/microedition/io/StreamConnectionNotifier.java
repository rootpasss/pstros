// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.io;

import java.io.IOException;

public interface StreamConnectionNotifier extends Connection
{
    StreamConnection acceptAndOpen() throws IOException;
}
