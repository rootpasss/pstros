// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.io.file;

import ole.pstros.io.ConnectionProvider;
import ole.pstros.io.FileConnectionProvider;
import ole.pstros.io.ConnectionManager;
import java.util.Enumeration;
import java.util.Vector;

public class FileSystemRegistry
{
    private static Vector listeners;
    
    static {
        FileSystemRegistry.listeners = new Vector();
    }
    
    public static Enumeration listRoots() {
        final ConnectionProvider cp = ConnectionManager.getProvider("file");
        final Vector v = new Vector();
        if (cp == null) {
            return v.elements();
        }
        if (cp instanceof FileConnectionProvider) {
            final String[] roots = ((FileConnectionProvider)cp).getRoots();
            if (roots != null) {
                for (int i = 0; i < roots.length; ++i) {
                    v.add(roots[i]);
                }
            }
        }
        return v.elements();
    }
    
    public static boolean addFileSystemListener(final FileSystemListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (FileSystemRegistry.listeners.indexOf(listener) >= 0) {
            return false;
        }
        FileSystemRegistry.listeners.add(listener);
        return true;
    }
    
    public static boolean removeFileSystemListener(final FileSystemListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        return FileSystemRegistry.listeners.remove(listener);
    }
}
