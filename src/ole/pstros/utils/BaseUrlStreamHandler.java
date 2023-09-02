// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import java.io.IOException;
import java.net.URLConnection;
import java.net.URL;
import java.net.URLStreamHandler;

public class BaseUrlStreamHandler extends URLStreamHandler
{
    BaseClassLoader bcl;
    
    public BaseUrlStreamHandler(final BaseClassLoader bcl) {
        this.bcl = bcl;
    }
    
    protected URLConnection openConnection(final URL u) throws IOException {
        return new ClassLoaderConnection(u, this.bcl);
    }
}
