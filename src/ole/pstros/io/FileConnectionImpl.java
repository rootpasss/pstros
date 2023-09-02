// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.io;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.util.Vector;
import java.io.FilenameFilter;
import java.util.Enumeration;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.File;
import javax.microedition.io.file.FileConnection;

public class FileConnectionImpl implements FileConnection
{
    private File file;
    private int mode;
    private String name;
    private InputStream is;
    private OutputStream os;
    
    public FileConnectionImpl(final String localName, String name, final int mode) {
        this.name = name;
        this.mode = mode;
        this.file = new File(localName);
        if (this.file.isDirectory() && !name.endsWith("/")) {
            name = String.valueOf(name) + "/";
        }
    }
    
    public long availableSize() {
        return 0L;
    }
    
    public boolean canRead() {
        return this.file != null && this.file.canRead();
    }
    
    public boolean canWrite() {
        return this.file != null && this.file.canWrite();
    }
    
    public void create() throws IOException {
        this.file.createNewFile();
    }
    
    public void delete() throws IOException {
        this.file.delete();
    }
    
    public long directorySize(final boolean flag) throws IOException {
        return 0L;
    }
    
    public boolean exists() {
        return this.file != null && this.file.exists();
    }
    
    public long fileSize() throws IOException {
        return this.file.length();
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getPath() {
        return this.name;
    }
    
    public String getURL() {
        return this.name;
    }
    
    public boolean isDirectory() {
        return this.file != null && this.file.isDirectory();
    }
    
    public boolean isHidden() {
        return this.file != null && this.file.isHidden();
    }
    
    public boolean isOpen() {
        return false;
    }
    
    public long lastModified() {
        if (this.file == null) {
            return 0L;
        }
        return this.file.lastModified();
    }
    
    public Enumeration list() throws IOException {
        return this.list("*", true);
    }
    
    public Enumeration list(final String s, final boolean flag) throws IOException {
        final File[] files = this.file.listFiles(new StarFilenameFilter(s));
        if (files == null) {
            return null;
        }
        final Vector v = new Vector();
        for (int i = 0; i < files.length; ++i) {
            v.addElement(String.valueOf(files[i].getName()) + (files[i].isDirectory() ? "/" : ""));
        }
        return v.elements();
    }
    
    public void setFileConnection(final String path) throws IOException {
        this.name = String.valueOf(this.name) + path;
        this.file = new File(this.file, path);
        if (this.file.isDirectory() && !this.name.endsWith("/")) {
            this.name = String.valueOf(this.name) + "/";
        }
    }
    
    public void mkdir() throws IOException {
        this.file.mkdir();
    }
    
    public DataInputStream openDataInputStream() throws IOException {
        final DataInputStream dis = new DataInputStream(this.openInputStream());
        return (DataInputStream)(this.is = dis);
    }
    
    public DataOutputStream openDataOutputStream() throws IOException {
        final DataOutputStream dos = new DataOutputStream(this.openOutputStream());
        return (DataOutputStream)(this.os = dos);
    }
    
    public InputStream openInputStream() throws IOException {
        return this.is = new BufferedInputStream(new FileInputStream(this.file));
    }
    
    public OutputStream openOutputStream() throws IOException {
        return this.os = new BufferedOutputStream(new FileOutputStream(this.file));
    }
    
    public OutputStream openOutputStream(final long l) throws IOException {
        return this.os = new FileOutputStream(this.file);
    }
    
    public void rename(final String s) throws IOException {
    }
    
    public void setHidden(final boolean flag) throws IOException {
    }
    
    public void setReadable(final boolean flag) throws IOException {
    }
    
    public void setWritable(final boolean flag) throws IOException {
    }
    
    public long totalSize() {
        if (this.file == null) {
            return 0L;
        }
        return this.file.length();
    }
    
    public void truncate(final long l) throws IOException {
    }
    
    public long usedSize() {
        return 0L;
    }
    
    public void close() throws IOException {
        if (this.is != null) {
            this.is.close();
        }
        if (this.os != null) {
            this.os.close();
        }
    }
}
