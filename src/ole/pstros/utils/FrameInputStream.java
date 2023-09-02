// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import java.io.IOException;
import java.io.InputStream;

public class FrameInputStream extends InputStream
{
    byte[] data;
    int frameSize;
    int position;
    int markPosition;
    boolean fixResetBug;
    int resetCount;
    
    public FrameInputStream(final byte[] data, final int frameSize, final boolean fixResetBug) {
        this.data = data;
        this.frameSize = frameSize;
        this.fixResetBug = fixResetBug;
    }
    
    public int available() throws IOException {
        int rest = this.data.length - this.position;
        if (rest > this.frameSize) {
            rest = this.frameSize;
        }
        return rest;
    }
    
    public void close() throws IOException {
        this.data = null;
    }
    
    public synchronized void mark(final int readlimit) {
        this.markPosition = this.position;
    }
    
    public boolean markSupported() {
        return true;
    }
    
    public int read() throws IOException {
        if (this.position >= this.data.length) {
            return -1;
        }
        final int result = this.data[this.position] & 0xFF;
        ++this.position;
        return result;
    }
    
    public int read(final byte[] b, final int off, final int len) throws IOException {
        int size = (len > this.frameSize) ? this.frameSize : len;
        if (this.position + len > this.data.length) {
            size = this.data.length - this.position;
        }
        System.arraycopy(this.data, this.position, b, off, size);
        this.position += size;
        System.out.println("read b[], off=" + off + " len=" + len + " size=" + size + " position=" + this.position + " data size=" + this.data.length);
        return size;
    }
    
    public int read(final byte[] b) throws IOException {
        final int size = (b.length > this.frameSize) ? this.frameSize : b.length;
        System.arraycopy(this.data, this.position, b, 0, size);
        this.position += size;
        return size;
    }
    
    public synchronized void reset() throws IOException {
        if (this.fixResetBug && this.resetCount == 0) {
            this.position = 44;
        }
        else {
            this.position = this.markPosition;
        }
        ++this.resetCount;
    }
    
    public long skip(final long n) throws IOException {
        final int oldPos = this.position;
        this.position += (int)n;
        if (this.position <= this.data.length) {
            return n;
        }
        this.position = this.data.length;
        final int result = this.position - oldPos;
        if (result == 0) {
            return -1L;
        }
        return result;
    }
}
