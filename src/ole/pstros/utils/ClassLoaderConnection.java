// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import ole.pstros.MainApp;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class ClassLoaderConnection extends URLConnection
{
    BaseClassLoader bcl;
    
    public ClassLoaderConnection(final URL u, final BaseClassLoader bcl) {
        super(u);
        this.bcl = bcl;
    }
    
    public void connect() throws IOException {
    }
    
    public InputStream getInputStream() throws IOException {
        if (MainApp.verbose) {
            System.out.println("ClassLoaderConnection: Get input stream! " + this.getURL());
        }
        return this.bcl.getResourceAsStream(this.getURL().getPath());
    }
}
