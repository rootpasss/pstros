// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import java.net.URL;
import java.io.InputStream;
import ole.pstros.MainApp;
import java.io.FileInputStream;
import java.util.StringTokenizer;
import java.io.File;

public class DirectoryClassLoader extends BaseClassLoader
{
    File baseDirectory;
    String[] dirs;
    
    public DirectoryClassLoader(final ClassLoader masterCl) {
        super(masterCl);
        String baseDir = System.getProperty("pstros.dcl.dir");
        if (baseDir == null) {
            baseDir = "./;./data";
        }
        this.setBaseDir(baseDir);
    }
    
    public DirectoryClassLoader(final String baseDir) {
        this.setBaseDir(baseDir);
    }
    
    private void setBaseDir(final String baseDir) {
        final StringTokenizer tokenizer = new StringTokenizer(baseDir, ";");
        final int size = tokenizer.countTokens();
        this.dirs = new String[size];
        for (int i = 0; i < size; ++i) {
            this.dirs[i] = tokenizer.nextToken();
        }
        try {
            this.baseDirectory = new File("./");
            if (!this.baseDirectory.exists() || !this.baseDirectory.isDirectory()) {
                this.baseDirectory = null;
            }
        }
        catch (Exception e) {
            System.out.println("Error:" + this.getClass().toString() + " : " + e);
            System.out.println(" dir=" + baseDir);
        }
    }
    
    private String getFilename(final String name) {
        try {
            for (int i = 0; i < this.dirs.length; ++i) {
                String path = this.baseDirectory.getCanonicalPath();
                final String dir = this.dirs[i];
                if (dir.charAt(0) != '/' || dir.charAt(0) != '\\') {
                    path = String.valueOf(path) + '/';
                }
                path = String.valueOf(path) + dir;
                if (name.charAt(0) != '/' || name.charAt(0) != '\\') {
                    path = String.valueOf(path) + '/';
                }
                path = String.valueOf(path) + name;
                final File file = new File(path);
                if (file.exists() && !file.isDirectory()) {
                    return path;
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error:" + this.getClass().toString() + " : " + e);
            System.out.println("  loadData: name=" + name);
        }
        return null;
    }
    
    protected byte[] loadData(final String name) {
        if (this.dirs == null) {
            return null;
        }
        try {
            final String path = this.getFilename(name);
            if (path == null) {
                return null;
            }
            final File file = new File(path);
            int size = (int)file.length();
            final InputStream stream = new FileInputStream(file);
            int offset = 0;
            int red = 0;
            final byte[] result = new byte[size];
            while (size > 0) {
                red = stream.read(result, offset, size);
                size -= red;
                offset += red;
            }
            if (MainApp.verbose) {
                System.out.println("DCL loadData name= " + name);
            }
            return result;
        }
        catch (Exception e) {
            System.out.println("Error:" + this.getClass().toString() + " : " + e);
            System.out.println("  loadData: name=" + name);
            return null;
        }
    }
    
    public URL getResource(final String name) {
        if (MainApp.verbose) {
            System.out.println("Pstros DirectoryClassLoader: getResource called name=" + name);
        }
        return this.getUrl(name);
    }
    
    protected URL findResource(final String name) {
        if (MainApp.verbose) {
            System.out.println("Pstros DirectoryClassLoader: findResource called name=" + name);
        }
        return this.getUrl(name);
    }
    
    private URL getUrl(final String name) {
        final String filename = this.getFilename(name);
        if (filename == null) {
            return null;
        }
        URL url = null;
        try {
            url = new URL("pstros:///" + name);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (MainApp.verbose) {
            System.out.println("url=" + url);
        }
        return url;
    }
}
