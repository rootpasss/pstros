// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

public class WaveConvertor
{
    protected static final byte[] CHUNK_FMT;
    protected static final byte[] CHUNK_DATA;
    protected static WaveConvertor instance;
    
    static {
        CHUNK_FMT = new byte[] { 102, 109, 116, 32 };
        CHUNK_DATA = new byte[] { 100, 97, 116, 97 };
    }
    
    public byte[] convert(final byte[] srcData, final int[] dstParams) {
        return srcData;
    }
    
    protected int findChunk(final byte[] data, final byte[] chunk, int offset) {
        final int size = data.length;
        if (offset < 0) {
            offset = 0;
        }
        final byte c0 = chunk[0];
        final byte c2 = chunk[1];
        final byte c3 = chunk[2];
        final byte c4 = chunk[3];
        for (int i = offset; i < size; ++i) {
            if (data[i] == c0 && data[i + 1] == c2 && data[i + 2] == c3 && data[i + 3] == c4) {
                return i;
            }
        }
        return -1;
    }
    
    protected int getChannelCount(final byte[] data, final int offset) {
        return this.getI2(data, offset + 2);
    }
    
    protected int getSamplingFreq(final byte[] data, final int offset) {
        return this.getI4(data, offset + 4);
    }
    
    protected int getBytesPerSec(final byte[] data, final int offset) {
        return this.getI4(data, offset + 8);
    }
    
    protected int getBlockAlign(final byte[] data, final int offset) {
        return this.getI2(data, offset + 12);
    }
    
    protected int getBitsPerSample(final byte[] data, final int offset) {
        return this.getI2(data, offset + 14);
    }
    
    protected int getExtraBytes(final byte[] data, final int offset) {
        return this.getI2(data, offset + 16);
    }
    
    protected int getSamplesPerBlock(final byte[] data, final int offset) {
        return this.getI2(data, offset + 18);
    }
    
    protected int getI4(final byte[] data, int offset) {
        int i4;
        int i3;
        int i2;
        int i0 = i2 = (i3 = (i4 = 0));
        i0 |= data[offset++];
        i2 |= data[offset++];
        i3 |= data[offset++];
        i4 |= data[offset];
        i0 &= 0xFF;
        i2 &= 0xFF;
        i3 &= 0xFF;
        i4 &= 0xFF;
        return i0 | i2 << 8 | i3 << 16 | i4 << 24;
    }
    
    protected int getI2(final byte[] data, int offset) {
        int i2;
        int i0 = i2 = 0;
        i0 |= data[offset++];
        i2 |= data[offset];
        i0 &= 0xFF;
        i2 &= 0xFF;
        return i0 | i2 << 8;
    }
}
