// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import ole.pstros.ConfigData;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

public class StreamSaver
{
    public static final byte ORV_VERSION = 1;
    private static String fileName;
    private static byte[] header;
    public static final int TYPE_ORV = 0;
    public static final int TYPE_TGA = 1;
    private static int type;
    private static int imageIndex;
    private static FileOutputStream stream;
    public static byte[] buf;
    public static int[] data;
    public static int[] data2;
    private static boolean dataSwitch;
    private static int[] scrollResult;
    private static long frameTime;
    private static byte[] frameTimeBuff;
    
    static {
        StreamSaver.fileName = "capture.orv";
        StreamSaver.header = new byte[8];
        StreamSaver.type = 1;
        StreamSaver.buf = new byte[307200];
        StreamSaver.data = new int[76832];
        StreamSaver.scrollResult = new int[2];
        StreamSaver.frameTime = -1L;
        StreamSaver.frameTimeBuff = new byte[4];
    }
    
    public static void setFileName(final String name) {
        StreamSaver.fileName = name;
    }
    
    public static void init(final int width, final int height, final int streamType) throws Exception {
        final int dataSize = width * height * 3;
        StreamSaver.type = streamType;
        if (StreamSaver.type == 0) {
            if (StreamSaver.data2 == null) {
                StreamSaver.data2 = new int[StreamSaver.data.length];
            }
            setHeader(width, height);
            (StreamSaver.stream = new FileOutputStream(StreamSaver.fileName)).write(StreamSaver.header);
        }
        else {
            final File file = new File("capt");
            if (!file.exists()) {
                file.mkdir();
            }
            else if (!file.isDirectory()) {
                throw new Exception("file 'capt' already exists, can't capture!");
            }
            StreamSaver.imageIndex = 0;
        }
    }
    
    public static void saveImageData(final BufferedImage image, final int w, final int h, final int y) {
        if (image == null) {
            return;
        }
        try {
            int[] RGBdata = StreamSaver.data;
            int[] OLDdata = StreamSaver.data2;
            if (StreamSaver.type == 0) {
                if (StreamSaver.dataSwitch) {
                    RGBdata = StreamSaver.data2;
                    OLDdata = StreamSaver.data;
                }
                StreamSaver.dataSwitch = !StreamSaver.dataSwitch;
            }
            image.getRGB(0, y, w, h, RGBdata, 0, w);
            if (StreamSaver.type == 0) {
                final int dataSize = fillDiffBuffer(RGBdata, OLDdata, w, h);
                final long now = System.currentTimeMillis();
                if (StreamSaver.frameTime < 0L) {
                    StreamSaver.frameTime = now;
                }
                final int diffTime = (int)(now - StreamSaver.frameTime);
                StreamSaver.frameTime = now;
                StreamSaver.frameTimeBuff[0] = (byte)((diffTime & 0xFF000000) >> 24);
                StreamSaver.frameTimeBuff[1] = (byte)((diffTime & 0xFF0000) >> 16);
                StreamSaver.frameTimeBuff[2] = (byte)((diffTime & 0xFF00) >> 8);
                StreamSaver.frameTimeBuff[3] = (byte)(diffTime & 0xFF);
                StreamSaver.stream.write(StreamSaver.frameTimeBuff, 0, 4);
                StreamSaver.stream.write(StreamSaver.buf, 0, dataSize);
            }
            else {
                final int dataSize = fillFullBuffer(RGBdata, w, h);
                TgaWriter.saveImageData("capt/image", StreamSaver.buf, 0, dataSize, w, h, StreamSaver.imageIndex++);
            }
        }
        catch (Exception e) {
            System.out.println("Error: StreamSaver: " + e);
        }
    }
    
    private static int fillFullBuffer(final int[] RGBdata, final int w, final int h) {
        final int size = w * h;
        final int dataSize = size * 3;
        int offset = 0;
        for (final int pixel : RGBdata) {
            StreamSaver.buf[offset++] = (byte)(pixel & 0xFF);
            StreamSaver.buf[offset++] = (byte)(pixel >> 8 & 0xFF);
            StreamSaver.buf[offset++] = (byte)(pixel >> 16 & 0xFF);
        }
        return dataSize;
    }
    
    private static int fillDiffBuffer(final int[] RGBdata, final int[] OLDdata, final int w, final int h) {
        int blockCounter = 0;
        final int size = w * h;
        int offset = 4;
        int sizeOffset = 8;
        boolean storeSize = false;
        int blockSize = 0;
        final int scroll = detectScroll(RGBdata, OLDdata, w, h);
        StreamSaver.buf[0] = (byte)StreamSaver.scrollResult[0];
        StreamSaver.buf[1] = (byte)StreamSaver.scrollResult[1];
        for (int i = 0; i < size; ++i) {
            final int pixelIndex = i + scroll;
            final int pixel1 = RGBdata[i];
            int pixel2;
            if (pixelIndex < 0 || pixelIndex >= size) {
                pixel2 = 0;
            }
            else {
                pixel2 = OLDdata[pixelIndex];
            }
            if (pixel1 == pixel2) {
                if (storeSize) {
                    writeInt(blockSize, sizeOffset);
                    storeSize = false;
                    blockSize = 0;
                }
            }
            else {
                if (!storeSize) {
                    offset = (sizeOffset = writeInt(i, offset));
                    offset += 4;
                    storeSize = true;
                    ++blockCounter;
                }
                StreamSaver.buf[offset++] = (byte)(pixel1 & 0xFF);
                StreamSaver.buf[offset++] = (byte)(pixel1 >> 8 & 0xFF);
                StreamSaver.buf[offset++] = (byte)(pixel1 >> 16 & 0xFF);
                ++blockSize;
            }
        }
        if (storeSize) {
            writeInt(blockSize, sizeOffset);
        }
        writeShort(blockCounter, 2);
        return offset;
    }
    
    private static int writeInt(final int number, int offset) {
        StreamSaver.buf[offset++] = (byte)(number >> 24 & 0xFF);
        StreamSaver.buf[offset++] = (byte)(number >> 16 & 0xFF);
        StreamSaver.buf[offset++] = (byte)(number >> 8 & 0xFF);
        StreamSaver.buf[offset++] = (byte)(number & 0xFF);
        return offset;
    }
    
    private static int writeShort(final int number, int offset) {
        StreamSaver.buf[offset++] = (byte)(number >> 8 & 0xFF);
        StreamSaver.buf[offset++] = (byte)(number & 0xFF);
        return offset;
    }
    
    private static int detectScroll(final int[] cData, final int[] oData, final int w, final int h) {
        final int yStep = (h - 16) / ConfigData.captureMotionPrecision;
        final int xStep = (w - 16) / ConfigData.captureMotionPrecision;
        final int yMax = 8 + yStep * ConfigData.captureMotionPrecision;
        final int xMax = 8 + xStep * ConfigData.captureMotionPrecision;
        int offset = 0;
        final int resultIndex = 0;
        int maxRes = -1;
        int maxIdx = -1;
        int scrollX = 0;
        int scrollY = 0;
        for (int i = 0; i < 81; ++i) {
            scrollX = i % 9;
            scrollY = i / 9;
            if (scrollX > 4) {
                scrollX = -scrollX + 4;
            }
            if (scrollY > 4) {
                scrollY = -scrollY + 4;
            }
            offset = scrollY * w + scrollX;
            int res = 0;
            for (int y = 8; y < yMax; y += yStep) {
                for (int x = 8; x < xMax; x += xStep) {
                    final int pixelOffset = y * w + x;
                    final boolean scrollTest = cData[pixelOffset] == oData[pixelOffset + offset];
                    res += (scrollTest ? 1 : 0);
                }
            }
            if (res > maxRes) {
                maxRes = res;
                maxIdx = i;
                StreamSaver.scrollResult[0] = scrollX;
                StreamSaver.scrollResult[1] = scrollY;
            }
        }
        return StreamSaver.scrollResult[1] * w + StreamSaver.scrollResult[0];
    }
    
    public static void close() {
        if (StreamSaver.stream != null) {
            try {
                StreamSaver.stream.close();
            }
            catch (Exception e) {
                System.out.println("Error: StreamSaver: " + e);
            }
            StreamSaver.stream = null;
        }
    }
    
    private static void setHeader(final int width, final int height) {
        StreamSaver.header[0] = 79;
        StreamSaver.header[1] = 82;
        StreamSaver.header[2] = 86;
        StreamSaver.header[3] = 1;
        StreamSaver.header[4] = (byte)(width & 0xFF);
        StreamSaver.header[5] = (byte)((width & 0xFF00) >> 8);
        StreamSaver.header[6] = (byte)(height & 0xFF);
        StreamSaver.header[7] = (byte)((height & 0xFF00) >> 8);
    }
}
