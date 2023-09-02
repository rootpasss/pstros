// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import java.io.FileOutputStream;
import ole.pstros.MainApp;
import java.io.File;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import ole.pstros.ConfigData;
import java.awt.Image;
import java.awt.image.ImageObserver;

public class ImageCreator implements ImageObserver
{
    private static boolean ICON;
    private static int imgNumber;
    private int width;
    private int height;
    private boolean loadDone;
    private static int[] pixelSignatures;
    
    static {
        ImageCreator.ICON = true;
        ImageCreator.pixelSignatures = new int[4096];
    }
    
    public Image createImage(final byte[] data) {
        if (ConfigData.storeImages) {
            saveImage(data);
        }
        //if (isPng(data) && SixlegsHelper.isAvailable()) {
        if (isPng(data) ) {
            final ByteArrayInputStream stream = new ByteArrayInputStream(data);
            final Image image = SixlegsHelper.getImage(stream);
            if (image == null) {
                throw new IllegalArgumentException();
            }
            return image;
            //return new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
        }
        else {
            if (!ImageCreator.ICON) {
                final Toolkit tk = Toolkit.getDefaultToolkit();
                final Image img = tk.createImage(data);
                img.getWidth(this);
                img.getHeight(this);
                while (!this.loadDone) {
                    try {
                        Thread.sleep(5L);
                    }
                    catch (Exception e) {
                        System.out.println("ImageCreator: " + e);
                    }
                }
                return img;
            }
            final ImageIcon ii = new ImageIcon(data);
            if (ii.getIconWidth() < 0 || ii.getIconHeight() < 0) {
                return null;
            }
            return ii.getImage();
        }
    }
    
    private static boolean isPng(final byte[] data) {
        return data[1] == 80 && data[2] == 78 && data[3] == 71;
    }
    
    public static Image createImage(final int width, final int height, final int colorARGB) {
        return createBufferedImage(width, height, colorARGB);
    }
    
    public static BufferedImage createBufferedImage(final Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }
        final int w = image.getWidth(null);
        final int h = image.getHeight(null);
        final BufferedImage result = createBufferedImage(w, h, 0);
        final Graphics g = result.getGraphics();
        g.drawImage(image, 0, 0, null);
        return result;
    }
    
    public static BufferedImage createBufferedImage(final int width, final int height, final int colorARGB) {
        final BufferedImage result = new BufferedImage(width, height, 2);
        final int size = width * height;
        final int[] data = new int[size];
        for (int i = 0; i < size; ++i) {
            data[i] = colorARGB;
        }
        result.setRGB(0, 0, width, height, data, 0, width);
        return result;
    }
    
    public boolean imageUpdate(final Image img, final int infoflags, final int x, final int y, final int width, final int height) {
        this.width = width;
        this.height = height;
        return infoflags != 32 || (this.loadDone = true);
    }
    
    private static void saveImage(final byte[] data) {
        final File file = new File(String.valueOf(ConfigData.storeImagePath) + "img" + Integer.toString(ImageCreator.imgNumber) + ".png");
        if (MainApp.verbose) {
            System.out.println("Saving image: " + file.getAbsolutePath());
        }
        try {
            final FileOutputStream stream = new FileOutputStream(file);
            stream.write(data);
            stream.flush();
            stream.close();
            ImageCreator.pixelSignatures[ImageCreator.imgNumber] = 0;
            ++ImageCreator.imgNumber;
        }
        catch (Exception e) {
            System.out.println("Image creator: Save image failed! " + e);
        }
    }
    
    public static void saveImage(final BufferedImage img) {
        final String fileName = TgaWriter.saveImageTransparent(String.valueOf(ConfigData.storeImagePath) + "img", img, img.getWidth(), img.getHeight(), 0, ImageCreator.imgNumber);
        if (fileName != null) {
            ++ImageCreator.imgNumber;
            if (MainApp.verbose) {
                System.out.println("Saving image: " + fileName);
            }
        }
    }
    
    public static int savePixels(final int[] pixels, final int w, final int h) {
        final int signature = computePixelSignature(pixels, w, h);
        int index = findSignature(signature);
        if (index == -1) {
            index = ImageCreator.imgNumber;
            ImageCreator.pixelSignatures[index] = signature;
            final int size = w * h;
            final byte[] data = new byte[size * 3];
            for (int i = 0; i < size; ++i) {
                final int pixel = pixels[i];
                data[i * 3] = (byte)(pixel & 0xFF);
                data[i * 3 + 1] = (byte)((pixel & 0xFF00) >> 8);
                data[i * 3 + 2] = (byte)((pixel & 0xFF0000) >> 16);
            }
            TgaWriter.setFormat((byte)24);
            TgaWriter.saveImageData(String.valueOf(ConfigData.storeImagePath) + "pixels", data, 0, data.length, w, h, index);
            ++ImageCreator.imgNumber;
        }
        return index;
    }
    
    private static int computePixelSignature(final int[] pixels, final int w, final int h) {
        int result = 0;
        final int size = w * h;
        for (int i = 0; i < size; ++i) {
            result += pixels[i] * i;
        }
        result |= size;
        return result;
    }
    
    private static int findSignature(final int sig) {
        for (int i = 0; i < ImageCreator.imgNumber; ++i) {
            if (ImageCreator.pixelSignatures[i] == sig) {
                return i;
            }
        }
        return -1;
    }
}
