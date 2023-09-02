// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.io;

import ole.pstros.ConfigData;
import java.util.Vector;

public class ConnectionManager
{
    private static Vector providers;
    
    private static void init() {
        if (ConnectionManager.providers != null) {
            return;
        }
        ConnectionManager.providers = new Vector();
        if (ConfigData.fileConnectionMapping != null) {
            final ConnectionProvider cp = new FileConnectionProvider();
            if (cp.init(ConfigData.fileConnectionMapping)) {
                addConnectionProvider(cp, true);
            }
        }
    }
    
    public static void addConnectionProvider(final ConnectionProvider p, final boolean replace) {
        if (p == null) {
            throw new IllegalArgumentException("provider is null!");
        }
        init();
        final String scheme = p.getScheme();
        if (scheme == null) {
            throw new IllegalArgumentException("provider scheme is null!");
        }
        final int size = ConnectionManager.providers.size();
        int i = 0;
        while (i < size) {
            final ConnectionProvider cp = (ConnectionProvider)ConnectionManager.providers.get(i);
            if (scheme.equals(cp.getScheme())) {
                if (replace) {
                    ConnectionManager.providers.remove(i);
                    ConnectionManager.providers.add(p);
                    return;
                }
                throw new IllegalArgumentException("provider with the same scheme already registered!");
            }
            else {
                ++i;
            }
        }
        ConnectionManager.providers.add(p);
    }
    
    public static void addConnectionProvider(final String className, final String params, final boolean replace) {
        try {
            final Class c = Class.forName(className);
            final Object o = c.newInstance();
            if (o instanceof ConnectionProvider) {
                final ConnectionProvider cp = (ConnectionProvider)o;
                if (cp.init(params)) {
                    addConnectionProvider(cp, replace);
                }
            }
            else {
                System.out.println("Pstros: class=" + className + " is not instance of the ConnectionProvider!");
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static ConnectionProvider getProvider(final String scheme) {
        if (scheme == null) {
            throw new IllegalArgumentException("scheme is null!");
        }
        init();
        for (int size = ConnectionManager.providers.size(), i = 0; i < size; ++i) {
            final ConnectionProvider cp = (ConnectionProvider)ConnectionManager.providers.get(i);
            if (scheme.equals(cp.getScheme())) {
                return cp;
            }
        }
        return null;
    }
}
