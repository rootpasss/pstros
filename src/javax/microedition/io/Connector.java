// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.io;

import ole.pstros.io.ConnectionManager;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import ole.pstros.io.ConnectionProvider;
import ole.pstros.MainApp;

public class Connector
{
    public static final int READ = 1;
    public static final int READ_WRITE = 3;
    public static final int WRITE = 2;
    private static boolean initialised;
    private static final String NOT_SUPPORTED = "pstros: not supported. Scheme=";
    
    public static Connection open(final String name) throws IOException {
        if (MainApp.verbose) {
            System.out.println("Connector.open name=" + name);
        }
        final ConnectionProvider cp = getProvider(name);
        return cp.open(name, 3, false);
    }
    
    public static Connection open(final String name, final int mode) throws IOException {
        if (MainApp.verbose) {
            System.out.println("Connector.open name=" + name + " mode=" + mode);
        }
        final ConnectionProvider cp = getProvider(name);
        return cp.open(name, mode, false);
    }
    
    public static Connection open(final String name, final int mode, final boolean timeouts) throws IOException {
        if (MainApp.verbose) {
            System.out.println("Connector.open name=" + name + " mode=" + mode + " timeouts=" + timeouts);
        }
        final ConnectionProvider cp = getProvider(name);
        return cp.open(name, mode, timeouts);
    }
    
    public static DataInputStream openDataInputStream(final String name) throws IOException {
        if (MainApp.verbose) {
            System.out.println("Connector.openDataInputStream name=" + name);
        }
        final ConnectionProvider cp = getProvider(name);
        return cp.openDataInputStream(name);
    }
    
    public static DataOutputStream openDataOutputStream(final String name) throws IOException {
        if (MainApp.verbose) {
            System.out.println("Connector.openDataOutputStream name=" + name);
        }
        final ConnectionProvider cp = getProvider(name);
        return cp.openDataOutputStream(name);
    }
    
    public static InputStream openInputStream(final String name) throws IOException {
        if (MainApp.verbose) {
            System.out.println("Connector.openInputStream name=" + name);
        }
        final ConnectionProvider cp = getProvider(name);
        return cp.openInputStream(name);
    }
    
    public static OutputStream openOutputStream(final String name) throws IOException {
        if (MainApp.verbose) {
            System.out.println("Connector.openOutputStream name=" + name);
        }
        final ConnectionProvider cp = getProvider(name);
        return cp.openOutputStream(name);
    }
    
    private static String getScheme(final String name) {
        final int i = name.indexOf(":");
        if (i < 0) {
            return null;
        }
        return name.substring(0, i);
    }
    
    private static ConnectionProvider getProvider(final String name) throws IOException {
        final String scheme = getScheme(name);
        final ConnectionProvider cp = ConnectionManager.getProvider(scheme);
        if (cp == null) {
            throw new IOException("pstros: not supported. Scheme=" + scheme);
        }
        return cp;
    }
}
