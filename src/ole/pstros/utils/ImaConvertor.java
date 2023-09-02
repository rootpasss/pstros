// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

public class ImaConvertor extends WaveConvertor
{
    private static final int ISSTMAX = 88;
    private static final int[] imaStepSizeTable;
    private static int[][] imaStateAdjustTable;
    
    static {
        imaStepSizeTable = new int[] { 7, 8, 9, 10, 11, 12, 13, 14, 16, 17, 19, 21, 23, 25, 28, 31, 34, 37, 41, 45, 50, 55, 60, 66, 73, 80, 88, 97, 107, 118, 130, 143, 157, 173, 190, 209, 230, 253, 279, 307, 337, 371, 408, 449, 494, 544, 598, 658, 724, 796, 876, 963, 1060, 1166, 1282, 1411, 1552, 1707, 1878, 2066, 2272, 2499, 2749, 3024, 3327, 3660, 4026, 4428, 4871, 5358, 5894, 6484, 7132, 7845, 8630, 9493, 10442, 11487, 12635, 13899, 15289, 16818, 18500, 20350, 22385, 24623, 27086, 29794, 32767 };
        ImaConvertor.imaStateAdjustTable = new int[89][8];
    }
    
    private static final int imaStateAdjust(final int c) {
        return (c < 4) ? -1 : (2 * c - 6);
    }
    
    private static void initImaTable() {
        for (int i = 0; i <= 88; ++i) {
            for (int j = 0; j < 8; ++j) {
                int k = i + imaStateAdjust(j);
                if (k < 0) {
                    k = 0;
                }
                else if (k > 88) {
                    k = 88;
                }
                ImaConvertor.imaStateAdjustTable[i][j] = k;
            }
        }
    }
    
    private static int getUChar(final byte[] arr, final int i) {
        final int b = arr[i];
        return (b & Integer.MIN_VALUE) >>> 24 | (b & 0x7F);
    }
    
    private static int imaExpandS(final int ch, final int chans, final byte[] ibuff, final int ibuffOffset, final byte[] obuff, final int obuffOffset, final int n, final int o_inc) {
        int ip = ibuffOffset + 4 * ch;
        final int i_inc = 4 * (chans - 1);
        int val = (short)(getUChar(ibuff, ip + 0) + (getUChar(ibuff, ip + 1) << 8));
        int state = getUChar(ibuff, ip + 2);
        if (state > 88) {
            System.out.println("IMA_ADPCM block ch" + ch + " initial-state " + state + " out of range");
            state = 0;
        }
        ip += 4 + i_inc;
        int op = obuffOffset;
        obuff[op] = (byte)(val >> 8 & 0xFF);
        obuff[op + 1] = (byte)(val & 0xFF);
        op += o_inc;
        for (int i = 1; i < n; ++i) {
            int cm;
            if ((i & 0x1) != 0x0) {
                cm = (getUChar(ibuff, ip) & 0xF);
            }
            else {
                cm = getUChar(ibuff, ip) >> 4;
                ++ip;
                if ((i & 0x7) == 0x0) {
                    ip += i_inc;
                }
            }
            final int step = ImaConvertor.imaStepSizeTable[state];
            final int c = cm & 0x7;
            state = ImaConvertor.imaStateAdjustTable[state][c];
            final int dp = (c + c + 1) * step >> 3;
            if (c != cm) {
                val -= dp;
                if (val < -32768) {
                    val = -32768;
                }
            }
            else {
                val += dp;
                if (val > 32767) {
                    val = 32767;
                }
            }
            obuff[op] = (byte)(val >> 8 & 0xFF);
            obuff[op + 1] = (byte)(val & 0xFF);
            op += o_inc;
        }
        return op - obuffOffset;
    }
    
    public static int imaSamplesIn(final int dataLen, final int chans, final int blockAlign, final int samplesPerBlock) {
        int n;
        int m;
        if (samplesPerBlock != 0) {
            n = dataLen / blockAlign * samplesPerBlock;
            m = dataLen % blockAlign;
        }
        else {
            n = 0;
            m = blockAlign;
        }
        if (m >= 4 * chans) {
            m -= 4 * chans;
            m /= 4 * chans;
            m = 8 * m + 1;
            if (samplesPerBlock != 0 && m > samplesPerBlock) {
                m = samplesPerBlock;
            }
            n += m;
        }
        return n;
    }
    
    int imaBytesPerBlock(final int chans, final int samplesPerBlock) {
        final int n = (samplesPerBlock + 14) / 8 * 4 * chans;
        return n;
    }
    
    public static WaveConvertor getInstance() {
        if (ImaConvertor.instance == null) {
            ImaConvertor.instance = createInstance();
        }
        return ImaConvertor.instance;
    }
    
    protected static WaveConvertor createInstance() {
        initImaTable();
        return new ImaConvertor();
    }
    
    private ImaConvertor() {
    }
    
    public byte[] convert(final byte[] srcData, final int[] dstParams) {
        int fmtIndex = this.findChunk(srcData, ImaConvertor.CHUNK_FMT, 0);
        if (fmtIndex == -1) {
            return srcData;
        }
        int chunkSize = this.getI4(srcData, fmtIndex + 4);
        fmtIndex += 8;
        final int numChannels = this.getChannelCount(srcData, fmtIndex);
        final int freq = this.getSamplingFreq(srcData, fmtIndex);
        final int bPerSec = this.getBytesPerSec(srcData, fmtIndex);
        final int align = this.getBlockAlign(srcData, fmtIndex);
        final int bits = this.getBitsPerSample(srcData, fmtIndex);
        final int samplesPerBlock = this.getSamplesPerBlock(srcData, fmtIndex);
        final int bytesPerBlock = this.imaBytesPerBlock(numChannels, samplesPerBlock);
        if (bytesPerBlock > align || samplesPerBlock % 8 != 1) {
            System.out.println("samplesPerBlock incompatible with blockAlign!  spb=" + samplesPerBlock + " ba=" + align + " bpb=" + bytesPerBlock);
            return srcData;
        }
        if (numChannels > 1) {
            System.out.println("Stereo sampled sounds? not supported...");
        }
        dstParams[0] = numChannels;
        dstParams[1] = freq;
        dstParams[2] = 2;
        int dataIndex = this.findChunk(srcData, ImaConvertor.CHUNK_DATA, fmtIndex + chunkSize);
        if (dataIndex == -1) {
            return srcData;
        }
        chunkSize = this.getI4(srcData, dataIndex + 4);
        dataIndex += 8;
        final byte[] dstBuf = new byte[chunkSize / align * samplesPerBlock * 2];
        int dataSize = chunkSize;
        int dstSize = 0;
        while (dataSize >= bytesPerBlock) {
            dstSize += imaExpandS(0, 1, srcData, dataIndex + (chunkSize - dataSize), dstBuf, dstSize, samplesPerBlock, 2);
            dataSize -= align;
        }
        return dstBuf;
    }
}
