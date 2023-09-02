// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import java.net.URL;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import ole.pstros.MainApp;
import java.util.jar.JarFile;

public class JarClassLoader extends BaseClassLoader
{
    private static JarClassLoader instance;
    private JarFile jarFile;
    private boolean loading;
    
    public static JarClassLoader getInstance(final String jarFileName) {
        if (JarClassLoader.instance == null) {
            JarClassLoader.instance = new JarClassLoader(jarFileName);
        }
        else {
            JarClassLoader.instance.setJarFilename(jarFileName);
        }
        return JarClassLoader.instance;
    }
    
    public JarClassLoader(final ClassLoader masterCl) {
        super(masterCl);
        JarClassLoader.instance = this;
    }
    
    public JarClassLoader(final String jarFileName) {
        this.setJarFilename(jarFileName);
    }
    
    public void setJarFilename(final String jarFileName) {
        try {
            this.jarFile = new JarFile(jarFileName);
        }
        catch (Exception e) {
            System.out.println("Error:" + this.getClass().toString() + " : " + e);
            System.out.println(" file=" + jarFileName);
        }
    }
    
    protected byte[] loadData(String name) {
        if (this.jarFile == null) {
            return null;
        }
        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        final ZipEntry entry = this.jarFile.getEntry(name);
        try {
            final InputStream stream = this.jarFile.getInputStream(entry);
            int size = (int)entry.getSize();
            int offset = 0;
            int red = 0;
            final byte[] result = new byte[size];
            while (size > 0) {
                red = stream.read(result, offset, size);
                size -= red;
                offset += red;
            }
            if (MainApp.verbose) {
                System.out.println("loadData name= " + name);
            }
            return result;
        }
        catch (Exception e) {
            if (MainApp.verbose) {
                System.out.println("Warning:" + this.getClass().toString() + " : " + e);
                System.out.println("  loadData: name=" + name);
            }
            return null;
        }
    }
    
    public URL getResource(final String name) {
        if (MainApp.verbose) {
            System.out.println("Pstros JarClassLoader: getResource called name=" + name);
        }
        return this.getUrl(name);
    }
    
    public static URL getSystemResource(final String name) {
        if (MainApp.verbose) {
            System.out.println("Pstros JarClassLoader: getSystemResource called name=" + name);
        }
        if (JarClassLoader.instance == null) {
            return null;
        }
        return JarClassLoader.instance.getUrl(name);
    }
    
    public static InputStream getSystemResourceAsStream(final String name) {
        if (MainApp.verbose) {
            System.out.println("Pstros JarClassLoader: getSystemResourceAsStream called name=" + name);
        }
        return null;
    }
    
    protected URL findResource(final String name) {
        if (MainApp.verbose) {
            System.out.println("Pstros JarClassLoader: findResource called name=" + name);
        }
        return this.getUrl(name);
    }
    
    private URL getUrl(String name) {
        if (this.jarFile == null) {
            return null;
        }
        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        final ZipEntry entry = this.jarFile.getEntry(name);
        try {
            final InputStream stream = this.jarFile.getInputStream(entry);
        }
        catch (Exception e) {
            System.out.println("Error: + entry=" + entry + " name=" + name);
            e.printStackTrace();
            return null;
        }
        URL url = null;
        try {
            url = new URL("pstros:///" + name);
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        if (MainApp.verbose) {
            System.out.println("url=" + url);
        }
        return url;
    }
}
