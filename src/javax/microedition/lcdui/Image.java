// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import java.io.ByteArrayOutputStream;
import ole.pstros.ConfigData;
import java.io.InputStream;
import ole.pstros.utils.BaseClassLoader;
import java.io.IOException;
import java.io.DataInputStream;
import ole.pstros.MainApp;
import ole.pstros.utils.ImageCreator;
import ole.pstros.reference.ImageReferenceManager;
import java.awt.image.ImageObserver;
import java.awt.image.BufferedImage;
import ole.pstros.exterior.ObjectProvider;

public class Image implements ObjectProvider
{
    private static int[] tmpPixels;
    private java.awt.Image[] emuImages;
    private int width;
    private int height;
    private boolean[] emuValid;
    private boolean[][] collisionData;
    Graphics graphics;
    private static BufferedImage cacheImage;
    private static int cacheImageW;
    private static int cacheImageH;
    private static java.awt.Graphics cacheGraphics;
    
    private Image(final java.awt.Image image) {
        if (image == null) {
            throw new IllegalArgumentException();
        }
        this.emuImages = new java.awt.Image[8];
        this.collisionData = new boolean[8][];
        this.emuValid = new boolean[8];
        this.emuImages[0] = image;
        if (this.emuImages[0] == null) {
            System.out.println("Image constructor: null emuImage!");
        }
        this.width = this.emuImages[0].getWidth(null);
        this.height = this.emuImages[0].getHeight(null);
        try {
            ImageReferenceManager.addImage(this);
        }
        catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
    
    java.awt.Image emuGetImage(final int transform) {
        java.awt.Image result = this.emuImages[transform];
        if (transform != 0 && !this.emuValid[transform]) {
            this.emuCreateTransformedImage(transform);
            result = this.emuImages[transform];
            this.emuValid[transform] = true;
        }
        return result;
    }
    
    boolean[] emuGetCollisionData(final int transform) {
        boolean[] result = this.collisionData[transform];
        if (result == null) {
            this.emuCreateCollisionData(transform);
            result = this.collisionData[transform];
        }
        return result;
    }
    
    public Object getObject() {
        return this.emuImages[0];
    }
    
    private void emuCreateCollisionData(final int transform) {
        int imgW = this.getWidth();
        int imgH = this.getHeight();
        final boolean[] data = new boolean[imgW * imgH];
        if (transform > 3) {
            final int tmp = imgW;
            imgW = imgH;
            imgH = tmp;
        }
        if (this.emuGetImage(transform) instanceof BufferedImage) {
            final BufferedImage img = (BufferedImage)this.emuGetImage(transform);
            final int[] rgb = new int[imgW * imgH];
            img.getRGB(0, 0, imgW, imgH, rgb, 0, imgW);
            for (int i = 0; i < data.length; ++i) {
                data[i] = ((rgb[i] & 0xFF000000) == 0xFF000000);
            }
        }
        else {
            for (int j = 0; j < data.length; ++j) {
                data[j] = true;
            }
        }
        this.collisionData[transform] = data;
    }
    
    private void emuCreateTransformedImage(final int transform) {
        final java.awt.Image src = this.emuImages[0];
        final int w = src.getWidth(null);
        final int h = src.getHeight(null);
        this.emuImages[transform] = emuCreateTransformedImage(src, 0, 0, w, h, transform);
    }
    
    private static BufferedImage emuCreateTransformedImage(final java.awt.Image src, final int x, final int y, final int w, final int h, final int transform) {
        BufferedImage dst = null;
        if (src == null) {
            return null;
        }
        final BufferedImage tmpImage = new BufferedImage(w, h, 2);
        final java.awt.Graphics g = tmpImage.createGraphics();
        g.drawImage(src, -x, -y, null);
        if (transform == 0) {
            return tmpImage;
        }
        final int[] dataSrc = new int[w * h];
        final int[] dataDst = new int[w * h];
        tmpImage.getRGB(0, 0, w, h, dataSrc, 0, w);
        switch (transform) {
            case 1: {
                emuHorizontalSwap(dataDst, dataSrc, w, h);
                dst = new BufferedImage(w, h, 2);
                dst.setRGB(0, 0, w, h, dataDst, 0, w);
                break;
            }
            case 7: {
                emuHorizontalSwap(dataDst, dataSrc, w, h);
                System.arraycopy(dataDst, 0, dataSrc, 0, w * h);
                emuRotate270(dataDst, dataSrc, w, h);
                dst = new BufferedImage(h, w, 2);
                dst.setRGB(0, 0, h, w, dataDst, 0, h);
                break;
            }
            case 4: {
                emuHorizontalSwap(dataDst, dataSrc, w, h);
                System.arraycopy(dataDst, 0, dataSrc, 0, w * h);
                emuRotate90(dataDst, dataSrc, w, h);
                dst = new BufferedImage(h, w, 2);
                dst.setRGB(0, 0, h, w, dataDst, 0, h);
                break;
            }
            case 2: {
                emuVerticalSwap(dataDst, dataSrc, w, h);
                dst = new BufferedImage(w, h, 2);
                dst.setRGB(0, 0, w, h, dataDst, 0, w);
                break;
            }
            case 3: {
                emuHorizontalSwap(dataDst, dataSrc, w, h);
                System.arraycopy(dataDst, 0, dataSrc, 0, w * h);
                emuVerticalSwap(dataDst, dataSrc, w, h);
                dst = new BufferedImage(w, h, 2);
                dst.setRGB(0, 0, w, h, dataDst, 0, w);
                break;
            }
            case 5: {
                emuRotate90(dataDst, dataSrc, w, h);
                dst = new BufferedImage(h, w, 2);
                dst.setRGB(0, 0, h, w, dataDst, 0, h);
                break;
            }
            case 6: {
                emuRotate270(dataDst, dataSrc, w, h);
                dst = new BufferedImage(h, w, 2);
                dst.setRGB(0, 0, h, w, dataDst, 0, h);
                break;
            }
            default: {
                dst = tmpImage;
                System.out.println("Pstros: Image.emuCreateTransformedImage() - Unsupported transform! " + transform);
                break;
            }
        }
        return dst;
    }
    
    private static void emuHorizontalSwap(final int[] dataDst, final int[] dataSrc, final int w, final int h) {
        for (int y = 0; y < h; ++y) {
            final int baseSrc = y * w;
            final int baseDst = (h - y - 1) * w;
            for (int x = 0; x < w; ++x) {
                dataDst[baseDst + x] = dataSrc[baseSrc + x];
            }
        }
    }
    
    private static void emuVerticalSwap(final int[] dataDst, final int[] dataSrc, final int w, final int h) {
        for (int x = 0; x < w; ++x) {
            int baseSrc = x;
            int baseDst = w - x - 1;
            for (int y = 0; y < h; ++y) {
                dataDst[baseDst] = dataSrc[baseSrc];
                baseDst += w;
                baseSrc += w;
            }
        }
    }
    
    private static void emuRotate90(final int[] dataDst, final int[] dataSrc, final int w, final int h) {
        for (int y = 0; y < h; ++y) {
            final int baseSrc = y * w;
            final int baseDst = h - y - 1;
            for (int x = 0; x < w; ++x) {
                dataDst[baseDst + x * h] = dataSrc[baseSrc + x];
            }
        }
    }
    
    private static void emuRotate270(final int[] dataDst, final int[] dataSrc, final int w, final int h) {
        for (int y = 0; y < h; ++y) {
            final int baseSrc = y * w;
            final int baseDst = h * (w - 1) + y;
            for (int x = 0; x < w; ++x) {
                dataDst[baseDst - x * h] = dataSrc[baseSrc + x];
            }
        }
    }
    
    public static Image createImage(final int w, final int h) {
        if (w < 1 || h < 1) {
            throw new IllegalArgumentException("wrong image size");
        }
        final Image image = new Image(ImageCreator.createImage(w, h, -16711936));
        if (MainApp.verbose) {
            System.out.println("Image.createImage(w, h) w=" + w + " h=" + h + " image=" + image.hashCode());
        }
        return image;
    }
    
    public static Image createImage(final Image source) {
        if (MainApp.verbose) {
            System.out.println("Image.createImage(Image source).. ");
        }
        if (source == null) {
            throw new NullPointerException("source image is null");
        }
        final BufferedImage bi = emuCreateTransformedImage(source.emuGetImage(0), 0, 0, source.getWidth(), source.getHeight(), 0);
        return new Image(bi);
    }
    
    public static Image createImage(final String name) throws IOException {
        if (MainApp.verbose) {
            System.out.println("Image.createImage(String name) =" + name);
        }
        final BaseClassLoader loader = MainApp.getClassLoader();
        if (name == null) {
            if (MainApp.verbose) {
                System.out.println("resource name is null");
            }
            throw new NullPointerException("resource name is null");
        }
        byte[] data;
        if (loader != null) {
            data = loader.getResourceAsByteArray(name);
        }
        else {
            final InputStream is = MainApp.midlet.getClass().getResourceAsStream(name);
            final DataInputStream stream = new DataInputStream(is);
            data = null;
            int dataOffset = 0;
            try {
                for (int size = stream.available(); size > 0; size = stream.available()) {
                    if (data == null) {
                        data = new byte[size];
                    }
                    else {
                        final byte[] newData = new byte[dataOffset + size];
                        System.arraycopy(data, 0, newData, 0, dataOffset);
                        data = newData;
                    }
                    stream.readFully(data, dataOffset, size);
                    dataOffset += size;
                }
            }
            catch (Exception ex) {}
            stream.close();
            is.close();
        }
        if (data == null) {
            if (MainApp.verbose) {
                System.out.println("resource not found");
            }
            //throw new IOException("resource not found");
        }
        final ImageCreator ie = new ImageCreator();
        final Image result = new Image(ie.createImage(data));
        if (MainApp.verbose) {
            System.out.println(" image created=" + result + " w=" + result.getWidth() + " h=" + result.getHeight());
        }
        return result;
    }
    
    public static Image createImage(final byte[] imageData, final int imageOffset, final int imageLength) {
        if (MainApp.verbose) {
            System.out.println("Image.createImage(byte[] imageData, int imageOffset, int imageLength)  data.length=" + imageData.length + " offset=" + imageOffset + " length=" + imageLength);
        }
        if (imageData == null) {
            throw new NullPointerException("imageData is null");
        }
        byte[] data;
        if (imageOffset == 0 && imageData.length == imageLength) {
            data = imageData;
        }
        else {
            data = new byte[imageLength];
            System.arraycopy(imageData, imageOffset, data, 0, imageLength);
        }
        final ImageCreator ie = new ImageCreator();
        final Image result = new Image(ie.createImage(data));
        if (MainApp.verbose) {
            System.out.println(" image created=" + result + " w=" + result.getWidth() + " h=" + result.getHeight());
        }
        return result;
    }
    
    public static Image createImage(final Image image, final int x, final int y, final int width, final int height, final int transform) {
        if (MainApp.verbose) {
            System.out.println("Image.createImage(Image image, int x, int y, int width, int height, int transform) x=" + x + " y=" + y + " transform=" + transform);
        }
        final BufferedImage bi = emuCreateTransformedImage(image.emuGetImage(0), x, y, width, height, transform);
        if (bi == null) {
            return null;
        }
        if (ConfigData.storeImages) {
            ImageCreator.saveImage(bi);
        }
        return new Image(bi);
    }
    
    public static Image createImage(final int width, final int height, final int colorARGB) {
        final Image result = new Image(ImageCreator.createImage(width, height, colorARGB));
        if (MainApp.verbose) {
            System.out.println("Image.createImage(w, h, colorARGB) w=" + width + " h=" + height + " image=" + result);
        }
        return result;
    }
    
    public static Image createImage(final InputStream stream) throws IOException {
        if (stream == null) {
            throw new NullPointerException("input stream is null");
        }
        final ImageCreator ie = new ImageCreator();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int offset = 0;
        while (true) {
            final int red = stream.read(buffer);
            if (red == -1) {
                break;
            }
            out.write(buffer, 0, red);
            offset += red;
        }
        final byte[] data = out.toByteArray();
        final Image result = new Image(ie.createImage(data));
        return result;
    }
    
    public static Image createRGBImage(final int[] rgb, final int width, final int height, final boolean processAlpha) {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("wrong image size");
        }
        final BufferedImage bi = ImageCreator.createBufferedImage(width, height, -16711936);
        if (!processAlpha) {
            final int size = rgb.length;
            if (Image.tmpPixels == null || Image.tmpPixels.length < size) {
                Image.tmpPixels = new int[size];
            }
            System.arraycopy(rgb, 0, Image.tmpPixels, 0, size);
            for (int i = 0; i < size; ++i) {
                final int[] tmpPixels = Image.tmpPixels;
                final int n = i;
                tmpPixels[n] |= 0xFF000000;
            }
            bi.setRGB(0, 0, width, height, Image.tmpPixels, 0, width);
        }
        else {
            bi.setRGB(0, 0, width, height, rgb, 0, width);
        }
        final Image image = new Image(bi);
        if (MainApp.verbose) {
            System.out.println("Image.createRGBImage(w, h) w=" + width + " h=" + height + " image=" + image.hashCode());
        }
        return image;
    }
    
    public Graphics getGraphics() throws IllegalStateException {
        if (this.emuImages[0] == null) {
            System.out.println("Error: getGraphics: emuImage  is null!");
            return null;
        }
        if (this.graphics != null) {
            return this.graphics;
        }
        final Graphics result = new Graphics();
        result.emuSetGraphics(this.emuImages[0].getGraphics());
        result.emuSetGraphicsImage((BufferedImage)this.emuImages[0]);
        result.emuSetImage(this);
        result.setClip(0, 0, this.width + 1, this.height + 1);
        result.setColor(0);
        result.setFont(Font.getDefaultFont());
        result.setStrokeStyle(0);
        if (MainApp.verbose) {
            System.out.println("Image: get graphics =" + result.hashCode() + " srcImage=@" + this.hashCode());
        }
        return this.graphics = result;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public boolean isMutable() {
        return false;
    }
    
    void emuInvalidate() {
        for (int i = 1; i < 7; ++i) {
            this.emuValid[i] = false;
            this.collisionData[i] = null;
        }
    }
    
    public void getRGB(final int[] rgbData, final int offset, final int scanlength, final int x, final int y, final int width, final int height) {
        final java.awt.Image src = this.emuImages[0];
        final BufferedImage dst = null;
        if (src == null) {
            return;
        }
        final int w = src.getWidth(null);
        final int h = src.getHeight(null);
        if (Image.cacheImageW == 0 || Image.cacheImageW != width || Image.cacheImageH != height) {
            Image.cacheImage = new BufferedImage(width, height, 2);
            Image.cacheGraphics = Image.cacheImage.createGraphics();
            Image.cacheImageW = width;
            Image.cacheImageH = height;
        }
        Image.cacheGraphics.drawImage(src, -x, -y, null);
        Image.cacheImage.getRGB(0, 0, width, height, rgbData, offset, scanlength);
    }
}
