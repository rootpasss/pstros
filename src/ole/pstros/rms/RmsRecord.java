// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.rms;

public class RmsRecord
{
    private byte[] data;
    private int id;
    
    public RmsRecord(final int id, final byte[] data) {
        this.data = data;
        this.id = id;
    }
    
    public RmsRecord(final int id, final byte[] src, final int start, final int size) {
        this.id = id;
        if (src == null || size < 1) {
            this.data = new byte[0];
        }
        else {
            System.arraycopy(src, start, this.data = new byte[size], 0, size);
        }
    }
    
    public int getId() {
        return this.id;
    }
    
    public byte[] getData() {
        return this.data;
    }
    
    public void setData(final byte[] src, final int pos, final int length) {
        this.data = new byte[length];
        if (length > 0) {
            System.arraycopy(src, pos, this.data, 0, length);
        }
    }
}
