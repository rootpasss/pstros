// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.io;

import java.io.IOException;

public interface SecureConnection extends SocketConnection
{
    SecurityInfo getSecurityInfo() throws IOException;
}
