// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.io;

import java.io.IOException;

public interface HttpsConnection extends HttpConnection
{
    int getPort();
    
    SecurityInfo getSecurityInfo() throws IOException;
}
