// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.io;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.DataOutputStream;
import javax.microedition.io.file.FileConnection;
import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.io.Connection;
import java.util.StringTokenizer;

public class FileConnectionProvider implements ConnectionProvider
{
    private static final String SCHEME = "file";
    private String[] drives;
    private String[] paths;
    private int size;
    private boolean init;
    
    public boolean init(final String param) {
        if (param == null) {
            return false;
        }
        final StringTokenizer st = new StringTokenizer(param, ";");
        this.size = st.countTokens();
        if (this.size < 2) {
            this.size = 0;
            return false;
        }
        this.size /= 2;
        this.drives = new String[this.size];
        this.paths = new String[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.drives[i] = st.nextToken();
            this.paths[i] = st.nextToken();
        }
        return this.init = true;
    }
    
    public String getScheme() {
        return "file";
    }
    
    public Connection open(final String name, final int mode, final boolean timeouts) throws IOException {
        final String localName = this.remapFileName(name);
        return new FileConnectionImpl(localName, name, mode);
    }
    
    public DataInputStream openDataInputStream(final String name) throws IOException {
        final String localName = this.remapFileName(name);
        final FileConnection fc = new FileConnectionImpl(localName, name, 1);
        return fc.openDataInputStream();
    }
    
    public DataOutputStream openDataOutputStream(final String name) throws IOException {
        final String localName = this.remapFileName(name);
        final FileConnection fc = new FileConnectionImpl(localName, name, 2);
        return fc.openDataOutputStream();
    }
    
    public InputStream openInputStream(final String name) throws IOException {
        final String localName = this.remapFileName(name);
        final FileConnection fc = new FileConnectionImpl(localName, name, 1);
        return fc.openInputStream();
    }
    
    public OutputStream openOutputStream(final String name) throws IOException {
        final String localName = this.remapFileName(name);
        final FileConnection fc = new FileConnectionImpl(localName, name, 2);
        return fc.openOutputStream();
    }
    
    private String remapFileName(final String name) throws IOException {
        for (int i = 0; i < this.size; ++i) {
            int index = name.indexOf(this.drives[i]);
            if (index > 0) {
                index += this.drives[i].length();
                return String.valueOf(this.paths[i]) + name.substring(index);
            }
        }
        throw new IOException("mapping not found for filename=" + name);
    }
    
    public String[] getRoots() {
        return this.drives;
    }
}
