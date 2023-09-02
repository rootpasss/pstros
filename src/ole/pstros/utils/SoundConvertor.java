// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import java.io.ByteArrayInputStream;
import ole.pstros.MainApp;
import java.io.DataInputStream;
import java.io.InputStream;

public class SoundConvertor
{
    public static final int FORMAT_UNKNOWN = 0;
    public static final int FORMAT_WAVE_PCM = 1;
    public static final int FORMAT_WAVE_IMA_ADPCM = 17;
    private static byte[] RIFF_HEADER;
    private static int[] soundParams;
    
    static {
        SoundConvertor.RIFF_HEADER = new byte[] { 82, 73, 70, 70, 0, 0, 0, 0, 87, 65, 86, 69, 102, 109, 116, 32, 16, 0, 0, 0, 1, 0, 1, 0, 64, 31, 0, 0, 0, 125, 0, 0, 2, 0, 16, 0, 100, 97, 116, 97, 0, 0, 0 };
        SoundConvertor.soundParams = new int[3];
    }
    
    public static InputStream convertData(final InputStream stream) {
        InputStream bs = null;
        final DataInputStream dis = new DataInputStream(stream);
        try {
            final int size = dis.available();
            byte[] data = new byte[size];
            dis.readFully(data);
            dis.close();
            final int format = getFormat(data);
            initSoundParams();
            switch (format) {
                case 0: {
                    if (MainApp.verbose) {
                        System.out.println("Unknown format!");
                        break;
                    }
                    break;
                }
                case 1: {
                    if (MainApp.verbose) {
                        System.out.println("PCM format found");
                        break;
                    }
                    break;
                }
                case 17: {
                    if (MainApp.verbose) {
                        System.out.println("IMA_ADPCM format found");
                    }
                    final WaveConvertor conv = ImaConvertor.getInstance();
                    data = conv.convert(data, SoundConvertor.soundParams);
                    break;
                }
            }
            if (SoundConvertor.soundParams[0] > 0) {
                data = createRiffData(data, SoundConvertor.soundParams);
            }
            bs = new ByteArrayInputStream(data);
            return bs;
        }
        catch (Exception e) {
            System.out.println("SoundConvertor:" + e);
            e.printStackTrace();
            return null;
        }
    }
    
    private static int getFormat(final byte[] data) {
        int result = 0;
        if (data[0] == 82 && data[1] == 73 && data[2] == 70 && data[3] == 70 && data[8] == 87 && data[9] == 65 && data[10] == 86 && data[11] == 69 && data[12] == 102 && data[13] == 109 && data[14] == 116 && data[15] == 32) {
            if (data[20] == 1 && data[21] == 0) {
                result = 1;
            }
            else if (data[20] == 17 && data[21] == 0) {
                result = 17;
            }
            else {
                System.out.println("Sound convertor unknown WAV format: " + Integer.toHexString(data[20]) + "/" + Integer.toHexString(data[21]));
            }
        }
        return result;
    }
    
    private static void initSoundParams() {
        for (int i = 0; i < 3; ++i) {
            SoundConvertor.soundParams[i] = -1;
        }
    }
    
    private static byte[] createRiffData(final byte[] srcData, final int[] params) {
        final int channels = params[0];
        final int freq = params[1];
        final int bytesPerChannel = params[2];
        final int bytesPerSec = channels * bytesPerChannel * freq;
        final int size = SoundConvertor.RIFF_HEADER.length + srcData.length;
        final byte[] dstBuffer = new byte[size];
        System.arraycopy(SoundConvertor.RIFF_HEADER, 0, dstBuffer, 0, SoundConvertor.RIFF_HEADER.length);
        System.arraycopy(srcData, 0, dstBuffer, SoundConvertor.RIFF_HEADER.length, srcData.length);
        write2b(dstBuffer, 22, channels);
        write4b(dstBuffer, 24, freq);
        write4b(dstBuffer, 28, bytesPerSec);
        write2b(dstBuffer, 32, bytesPerChannel * channels);
        write2b(dstBuffer, 34, bytesPerChannel * 8);
        write4b(dstBuffer, 40, srcData.length);
        write4b(dstBuffer, 4, dstBuffer.length - 8);
        return dstBuffer;
    }
    
    private static void write4b(final byte[] data, final int offset, final int value) {
        data[offset] = (byte)(value & 0xFF);
        data[offset + 1] = (byte)(value >>> 8 & 0xFF);
        data[offset + 2] = (byte)(value >>> 16 & 0xFF);
        data[offset + 3] = (byte)(value >>> 24 & 0xFF);
    }
    
    private static void write2b(final byte[] data, final int offset, final int value) {
        data[offset] = (byte)(value & 0xFF);
        data[offset + 1] = (byte)(value >>> 8 & 0xFF);
    }
}
