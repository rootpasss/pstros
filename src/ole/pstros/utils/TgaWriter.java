// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import java.io.FileOutputStream;
import java.awt.image.BufferedImage;

public class TgaWriter
{
    public static final byte FORMAT_RGBA8888 = 32;
    public static final byte FORMAT_RGB888 = 24;
    private static byte format;
    private static byte[] tgaHeader;
    
    static {
        TgaWriter.format = 24;
        TgaWriter.tgaHeader = new byte[] { 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 24, 32 };
    }
    
    public static void setFormat(final byte f) {
        TgaWriter.format = f;
    }
    
    public static String saveImageTransparent(final String name, final BufferedImage image, final int w, final int h, final int y, final int index) {
        if (image == null) {
            return null;
        }
        String fileName = null;
        try {
            setFormat((byte)32);
            image.getRGB(0, y, w, h, StreamSaver.data, 0, w);
            final int size = w * h;
            int offset = 0;
            for (int i = 0; i < size; ++i) {
                final int pixel = StreamSaver.data[i];
                StreamSaver.buf[offset++] = (byte)(pixel & 0xFF);
                StreamSaver.buf[offset++] = (byte)(pixel >> 8 & 0xFF);
                StreamSaver.buf[offset++] = (byte)(pixel >> 16 & 0xFF);
                StreamSaver.buf[offset++] = (byte)(pixel >> 24 & 0xFF);
            }
            fileName = saveImageData(name, StreamSaver.buf, 0, offset, w, h, index);
        }
        catch (Exception e) {
            System.out.println("Error: StreamSaver: " + e);
        }
        return fileName;
    }
    
    public static String saveImage(final String name, final BufferedImage image, final int w, final int h, final int y, final int index) {
        if (image == null) {
            return null;
        }
        String fileName = null;
        try {
            setFormat((byte)24);
            image.getRGB(0, y, w, h, StreamSaver.data, 0, w);
            final int size = w * h;
            int offset = 0;
            for (int i = 0; i < size; ++i) {
                final int pixel = StreamSaver.data[i];
                StreamSaver.buf[offset++] = (byte)(pixel & 0xFF);
                StreamSaver.buf[offset++] = (byte)(pixel >> 8 & 0xFF);
                StreamSaver.buf[offset++] = (byte)(pixel >> 16 & 0xFF);
            }
            fileName = saveImageData(name, StreamSaver.buf, 0, offset, w, h, index);
        }
        catch (Exception e) {
            System.out.println("Error: StreamSaver: " + e);
        }
        return fileName;
    }
    
    public static String saveImageData(final String name, final byte[] data, final int dataOffset, final int dataSize, final int width, final int height, final int index) {
        setHeader(width, height);
        final String fileName = createFileName(name, index);
        try {
            final FileOutputStream stream = new FileOutputStream(fileName);
            stream.write(TgaWriter.tgaHeader);
            stream.write(data, dataOffset, dataSize);
            stream.close();
        }
        catch (Exception e) {
            System.out.println("Error! TgaSaver:saveImageData :" + e);
        }
        return fileName;
    }
    
    private static String createFileName(final String name, final int index) {
        final String number = Integer.toString(index);
        final int len = number.length();
        return String.valueOf(name) + "000000".substring(len) + number + ".tga";
    }
    
    private static void setHeader(final int width, final int height) {
        TgaWriter.tgaHeader[12] = (byte)(width & 0xFF);
        TgaWriter.tgaHeader[13] = (byte)((width & 0xFF00) >> 8);
        TgaWriter.tgaHeader[14] = (byte)(height & 0xFF);
        TgaWriter.tgaHeader[15] = (byte)((height & 0xFF00) >> 8);
        TgaWriter.tgaHeader[16] = TgaWriter.format;
        if (TgaWriter.format == 32) {
            TgaWriter.tgaHeader[17] = 40;
        }
        else {
            TgaWriter.tgaHeader[17] = 32;
        }
    }
    
    private static void swapRGB(final byte[] data) {
        for (int size = data.length, i = 0; i < size; i += 3) {
            final byte t = data[i];
            data[i] = data[i + 2];
            data[i + 2] = t;
        }
    }
}
