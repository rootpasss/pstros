// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import java.io.EOFException;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class Orv2Tga
{
    private static byte[] buf;
    private static int width;
    private static int height;
    private static int frameSize;
    public static boolean decode;
    
    static {
        Orv2Tga.buf = new byte[8];
    }
    
    public static void process(final InputStream is, final String destPath, final DecoderListener listener) {
        int index = 0;
        DataInputStream stream = null;
        try {
            stream = new DataInputStream(new BufferedInputStream(is));
            stream.readFully(Orv2Tga.buf, 0, 8);
            if (!checkHeader()) {
                throw new Exception("unsupported file format");
            }
            Orv2Tga.frameSize = getFrameSize();
            final byte[] data = new byte[Orv2Tga.frameSize];
            Orv2Tga.decode = true;
            while (Orv2Tga.decode) {
                int timeDiff;
                try {
                    timeDiff = stream.readInt();
                }
                catch (Exception e3) {
                    timeDiff = 0;
                }
                final int scrollX = stream.readByte();
                final int scrollY = stream.readByte();
                final int scroll = scrollY * Orv2Tga.width + scrollX;
                doScroll(data, scroll * 3);
                for (int blockCounter = stream.readShort(), i = 0; i < blockCounter; ++i) {
                    final int pixelIndex = stream.readInt();
                    final int dataSize = stream.readInt();
                    stream.readFully(data, pixelIndex * 3, dataSize * 3);
                }
                if (listener != null) {
                    listener.decodeFrame(data, Orv2Tga.width, Orv2Tga.height);
                    if (timeDiff <= 0) {
                        continue;
                    }
                    try {
                        Thread.sleep(timeDiff);
                    }
                    catch (Exception ex) {}
                }
                else {
                    TgaWriter.saveImageData(destPath, data, 0, Orv2Tga.frameSize, Orv2Tga.width, Orv2Tga.height, index);
                    ++index;
                }
            }
        }
        catch (EOFException eofe) {
            System.out.println("eof detected");
        }
        catch (Exception e) {
            System.out.println("!Error:Orv2Tga: " + e);
        }
        finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            }
            catch (Exception e2) {
                System.out.println("!Error:Orv2Tga: " + e2);
            }
        }
        try {
            if (stream != null) {
                stream.close();
            }
        }
        catch (Exception e2) {
            System.out.println("!Error:Orv2Tga: " + e2);
        }
    }
    
    private static boolean checkHeader() {
        return Orv2Tga.buf[0] == 79 && Orv2Tga.buf[1] == 82 && Orv2Tga.buf[2] == 86 && Orv2Tga.buf[3] <= 1;
    }
    
    private static int getFrameSize() {
        Orv2Tga.width = (Orv2Tga.buf[4] & 0xFF) + (Orv2Tga.buf[5] & 0xFF) * 256;
        Orv2Tga.height = (Orv2Tga.buf[6] & 0xFF) + (Orv2Tga.buf[7] & 0xFF) * 256;
        return Orv2Tga.width * Orv2Tga.height * 3;
    }
    
    private static void doScroll(final byte[] data, final int scroll) {
        if (scroll == 0) {
            return;
        }
        if (scroll > 0) {
            int index = 0;
            for (int i = scroll; i < Orv2Tga.frameSize; ++i) {
                data[index++] = data[i];
            }
            for (int i = Orv2Tga.frameSize - scroll; i < Orv2Tga.frameSize; ++i) {
                data[i] = 0;
            }
        }
        else {
            int index = Orv2Tga.frameSize - 1;
            for (int i = Orv2Tga.frameSize - 1 + scroll; i >= 0; --i) {
                data[index--] = data[i];
            }
            for (int maxScroll = -scroll, j = 0; j < maxScroll; ++j) {
                data[j] = 0;
            }
        }
    }
}
