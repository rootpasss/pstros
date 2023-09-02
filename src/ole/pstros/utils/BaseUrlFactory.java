// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import ole.pstros.MainApp;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public class BaseUrlFactory implements URLStreamHandlerFactory
{
    BaseClassLoader bcl;
    
    public BaseUrlFactory(final BaseClassLoader classLoader) {
        this.bcl = classLoader;
    }
    
    public URLStreamHandler createURLStreamHandler(final String protocol) {
        if (MainApp.verbose) {
            System.out.println("protocol=" + protocol);
        }
        if (protocol.equals("pstros")) {
            return new BaseUrlStreamHandler(this.bcl);
        }
        return null;
    }
}
