// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import ole.pstros.utils.ImageCreator;
import ole.pstros.ConfigData;
import java.awt.image.ImageObserver;
import java.awt.Color;
import ole.pstros.MainApp;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import ole.pstros.exterior.ObjectProvider;
import com.nokia.mid.ui.DirectGraphics;

public class Graphics implements DirectGraphics, ObjectProvider
{
    private static final int EMU_BUFF_WIDTH = 480;
    private static final int EMU_BUFF_HEIGHT = 640;
    private static BufferedImage bi;
    private static int biWidth;
    private static int biHeight;
    private static java.awt.Graphics biGraphics;
    private static int[] pixelData;
    private static int[] polygonX;
    private static int[] polygonY;
    private static int[] polygonPointsX;
    private static int[] polygonPointsY;
    private static int[] tmpRgbData;
    private static final String lock = "Lock";
    public static final int HCENTER = 1;
    public static final int VCENTER = 2;
    public static final int LEFT = 4;
    public static final int RIGHT = 8;
    public static final int TOP = 16;
    public static final int BOTTOM = 32;
    public static final int BASELINE = 64;
    public static final int SOLID = 0;
    public static final int DOTTED = 1;
    private static Rectangle emuTmpRect;
    protected int trX;
    protected int trY;
    private int color;
    private int strokeStyle;
    private int clX;
    private int clY;
    private int clW;
    private int clH;
    private Font font;
    protected java.awt.Graphics emuGraphics;
    protected BufferedImage emuGraphicsImage;
    protected Image emuImage;
    int emuActionCounter;
    int emuDrawRegionCounter;
    
    static {
        Graphics.polygonX = new int[32];
        Graphics.polygonY = new int[32];
        Graphics.emuTmpRect = new Rectangle();
    }
    
    public Graphics() {
        this.font = Font.getDefaultFont();
        if (Graphics.bi == null) {
            this.initBufferedImage(480, 640);
        }
    }
    
    private void initBufferedImage(int w, int h) {
        final int maxPixels = 307200;
        if (w * h > maxPixels) {
            if (w > 480) {
                h = maxPixels / 480;
            }
            else {
                w = maxPixels / 640;
            }
        }
        Graphics.biWidth = w;
        Graphics.biHeight = h;
        Graphics.bi = new BufferedImage(w, h, 2);
        Graphics.biGraphics = Graphics.bi.getGraphics();
        Graphics.pixelData = new int[307200];
    }
    
    public void emuSetImage(final Image img) {
        this.emuImage = img;
    }
    
    public void emuSetGraphics(final java.awt.Graphics eg) {
        this.emuGraphics = eg;
    }
    
    public java.awt.Graphics emuGetGraphics() {
        return this.emuGraphics;
    }
    
    public void emuSetGraphicsImage(final BufferedImage bi) {
        this.emuGraphicsImage = bi;
    }
    
    public BufferedImage emuGetGraphicsImage() {
        return this.emuGraphicsImage;
    }
    
    public void translate(final int x, final int y) {
        if (MainApp.verbose) {
            System.out.println("Graphics: translate x=" + x + " y=" + y);
        }
        this.trX += x;
        this.trY += y;
    }
    
    public int getTranslateX() {
        return this.trX;
    }
    
    public int getTranslateY() {
        return this.trY;
    }
    
    public int getColor() {
        return this.color;
    }
    
    public int getDisplayColor(final int col) {
        return col;
    }
    
    public int getRedComponent() {
        return this.color >> 16 & 0xFF;
    }
    
    public int getGreenComponent() {
        return this.color >> 8 & 0xFF;
    }
    
    public int getBlueComponent() {
        return this.color & 0xFF;
    }
    
    public int getGrayScale() {
        return (this.getRedComponent() + this.getGreenComponent() + this.getBlueComponent()) / 3;
    }
    
    public void setColor(final int red, final int green, final int blue) {
        this.setColor(((red & 0xFF) << 16) + ((green & 0xFF) << 8) + (blue & 0xFF));
    }
    
    public void setColor(final int RGB) {
        if (MainApp.verbose) {
            System.out.println("Graphics: setColor: color=" + RGB);
        }
        this.color = (0xFF000000 | RGB);
        this.emuGraphics.setColor(new Color(this.color));
    }
    
    public void setGrayScale(final int value) {
        this.setColor(value, value, value);
    }
    
    public Font getFont() {
        if (MainApp.verbose) {
            System.out.println("Graphics:" + this.hashCode() + "  getFont() " + this.font);
        }
        return this.font;
    }
    
    public void setStrokeStyle(final int style) {
        this.strokeStyle = style;
    }
    
    public int getStrokeStyle() {
        return this.strokeStyle;
    }
    
    public void setFont(final Font f) {
        if (MainApp.verbose) {
            System.out.println("Graphics:" + this.hashCode() + "  setFont=" + this.font);
        }
        if (f == null) {
            this.font = Font.getDefaultFont();
        }
        else {
            this.font = f;
        }
    }
    
    public int getClipX() {
        if (MainApp.verbose) {
            System.out.println("Graphics:" + this.hashCode() + "  getClipX=" + this.clX + " trX=" + this.trX);
        }
        return this.clX - this.trX;
    }
    
    public int getClipY() {
        if (MainApp.verbose) {
            System.out.println("Graphics:" + this.hashCode() + "  getClipY=" + this.clY + " trY=" + this.trY);
        }
        return this.clY - this.trY;
    }
    
    public int getClipWidth() {
        if (MainApp.verbose) {
            System.out.println("Graphics:" + this.hashCode() + "  getClipW=" + this.clW);
        }
        return this.clW;
    }
    
    public int getClipHeight() {
        if (MainApp.verbose) {
            System.out.println("Graphics:" + this.hashCode() + "  getClipH=" + this.clH);
        }
        return this.clH;
    }
    
    public void setClip(final int x, final int y, final int width, final int height) {
        if (MainApp.verbose) {
            System.out.println("Graphics:" + this.hashCode() + " setClip" + " x=" + x + " y=" + y + " w=" + width + " h=" + height + " trX=" + this.trX + " trY=" + this.trY);
        }
        this.clX = this.trX + x;
        this.clY = this.trY + y;
        this.clW = width;
        this.clH = height;
        this.emuGraphics.setClip(this.clX, this.clY, width, height);
    }
    
    public void clipRect(final int x, final int y, final int width, final int height) {
        if (MainApp.verbose) {
            System.out.println("Graphics:" + this.hashCode() + "  clipRct" + " x=" + x + " y=" + y + " w=" + width + " h=" + height + " trX=" + this.trX + " trY=" + this.trY);
        }
        int clX1 = this.clX + this.clW;
        int clY1 = this.clY + this.clH;
        final int clX2 = this.trX + x + width;
        final int clY2 = this.trY + y + height;
        if (this.trX + x > this.clX) {
            this.clX = this.trX + x;
        }
        if (clX2 < clX1) {
            clX1 = clX2;
        }
        this.clW = clX1 - this.clX;
        if (this.trY + y > this.clY) {
            this.clY = this.trY + y;
        }
        if (clY2 < clY1) {
            clY1 = clY2;
        }
        this.clH = clY1 - this.clY;
        this.emuGraphics.clipRect(this.trX + x, this.trY + y, width, height);
    }
    
    public void copyArea(final int x_src, final int y_src, final int width, final int height, final int x_dest, final int y_dest, final int anchor) {
        final int srcX = x_src + this.trX;
        final int srcY = y_src + this.trY;
        if (this.emuImage == null) {
            throw new IllegalStateException();
        }
        if (srcX < 0 || srcY < 0 || srcX + width > this.emuImage.getWidth() || srcY + height > this.emuImage.getHeight()) {
            throw new IllegalArgumentException();
        }
        if (MainApp.verbose) {
            System.out.println("Graphics:" + this.hashCode() + "  copyArea x_src=" + x_src + " y_src=" + y_src + " w=" + width + " h=" + height + " x_dest=" + x_dest + " y_dest=" + y_dest + " anchor=" + anchor + " trX=" + this.trX + " trY=" + this.trY);
        }
        synchronized ("Lock") {
            int anX = 0;
            int anY = 0;
            final int size = width * height;
            if (Graphics.tmpRgbData == null || Graphics.tmpRgbData.length < size) {
                Graphics.tmpRgbData = new int[size];
            }
            this.emuImage.getRGB(Graphics.tmpRgbData, 0, width, srcX, srcY, width, height);
            if ((anchor & 0x1) != 0x0) {
                anX = width >> 1;
            }
            else if ((anchor & 0x8) != 0x0) {
                anX = width;
            }
            if ((anchor & 0x2) != 0x0) {
                anY = height >> 1;
            }
            else if ((anchor & 0x20) != 0x0) {
                anY = height;
            }
            this.drawRGB(Graphics.tmpRgbData, 0, width, x_dest - anX, y_dest - anY, width, height, false);
        }
        // monitorexit("Lock")
        ++this.emuActionCounter;
    }
    
    public void drawLine(final int x1, final int y1, final int x2, final int y2) {
        if (MainApp.verbose) {
            System.out.println("Graphics:" + this.hashCode() + "  drawLine x1=" + x1 + " y1=" + y1 + " x2=" + x2 + " y2=" + y2);
        }
        this.emuGraphics.drawLine(x1 + this.trX, y1 + this.trY, x2 + this.trX, y2 + this.trY);
        ++this.emuActionCounter;
    }
    
    public void fillTriangle(final int x1, final int y1, final int x2, final int y2, final int x3, final int y3) {
        Graphics.polygonX[0] = x1 + this.trX;
        Graphics.polygonX[1] = x2 + this.trX;
        Graphics.polygonX[2] = x3 + this.trX;
        Graphics.polygonY[0] = y1 + this.trY;
        Graphics.polygonY[1] = y2 + this.trY;
        Graphics.polygonY[2] = y3 + this.trY;
        this.emuGraphics.fillPolygon(Graphics.polygonX, Graphics.polygonY, 3);
        ++this.emuActionCounter;
    }
    
    public void fillRect(final int x, final int y, final int width, final int height) {
        if (MainApp.verbose) {
            System.out.println("fill rect x=" + x + " y=" + y + " w=" + width + " h=" + height);
        }
        this.emuGraphics.fillRect(x + this.trX, y + this.trY, width, height);
        ++this.emuActionCounter;
    }
    
    public void fillRoundRect(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight) {
        if (MainApp.verbose) {
            System.out.println("fill round rect x=" + x + " y=" + y + " w=" + width + " h=" + height);
        }
        this.emuGraphics.fillRoundRect(x + this.trX, y + this.trY, width, height, arcWidth, arcHeight);
        ++this.emuActionCounter;
    }
    
    public void fillArc(final int x, final int y, final int width, final int height, final int startAngle, final int arcAngle) {
        this.emuGraphics.fillArc(x + this.trX, y + this.trY, width, height, startAngle, arcAngle);
        ++this.emuActionCounter;
    }
    
    public void drawRect(final int x, final int y, final int width, final int height) {
        if (MainApp.verbose) {
            System.out.println("draw rect clip=" + this.emuGraphics.getClip());
        }
        this.emuGraphics.drawRect(x + this.trX, y + this.trY, width, height);
        ++this.emuActionCounter;
    }
    
    public void drawRoundRect(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight) {
        if (MainApp.verbose) {
            System.out.println("draw round rect x=" + x + " y=" + y + " w=" + width + " h=" + height);
        }
        this.emuGraphics.drawRoundRect(x + this.trX, y + this.trY, width, height, arcWidth, arcHeight);
        ++this.emuActionCounter;
    }
    
    public void drawArc(final int x, final int y, final int width, final int height, final int startAngle, final int arcAngle) {
        this.emuGraphics.drawArc(x + this.trX, y + this.trY, width, height, startAngle, arcAngle);
        ++this.emuActionCounter;
    }
    
    public void drawChars(final char[] data, final int offset, final int length, final int x, final int y, final int anchor) {
        final String line = new String(data, offset, length);
        this.drawString(line, x, y, anchor);
        ++this.emuActionCounter;
    }
    
    public void drawChar(final char character, final int x, final int y, final int anchor) {
        final String line = String.valueOf(character);
        this.drawString(line, x, y, anchor);
        ++this.emuActionCounter;
    }
    
    public void drawSubstring(final String str, final int offset, final int len, final int x, final int y, final int anchor) {
        this.drawString(str.substring(offset, offset + len), x, y, anchor);
        ++this.emuActionCounter;
    }
    
    public void drawString(final String str, final int x, final int y, final int anchor) {
        if (MainApp.verbose) {
            System.out.println("Graphics:" + this.hashCode() + " draw string=" + str + " x=" + x + " y=" + y + " anchor=" + anchor + " trX=" + this.trX + " trY=" + this.trY + " clX=" + this.clX + " clY=" + this.clY + " clW=" + this.clW + " clH=" + this.clH);
        }
        int anX = 0;
        int anY = 0;
        if ((anchor & 0x1) != 0x0) {
            anX = this.font.stringWidth(str) >> 1;
        }
        else if ((anchor & 0x8) != 0x0) {
            anX = this.font.stringWidth(str);
        }
        if ((anchor & 0x2) != 0x0) {
            anY = this.font.getHeight() >> 1;
        }
        else if ((anchor & 0x20) != 0x0) {
            anY = this.font.getHeight();
        }
        else if ((anchor & 0x40) != 0x0) {
            anY = this.font.getBaselinePosition();
        }
        this.emuGraphics.setFont(this.font.emuGetFont());
        this.emuGraphics.drawString(str, x + this.trX - anX, y + this.trY - anY + this.font.getHeight() - this.font.emuGetExtraHeight());
        ++this.emuActionCounter;
    }
    
    public void drawImage(final Image img, final int x, final int y, final int anchor) {
        if (img == null) {
            return;
        }
        this.emuDrawImage(img.emuGetImage(0), x, y, anchor, img.getWidth(), img.getHeight());
        ++this.emuActionCounter;
    }
    
    void emuDrawImage(final java.awt.Image image, final int x, final int y, final int anchor, final int width, final int height) {
        int anX = 0;
        int anY = 0;
        if (MainApp.verbose) {
            System.out.println("Graphics:" + this.hashCode() + "  drawImage: img=" + image.hashCode() + " x=" + x + " y=" + y + " anchor=" + anchor + " trX=" + this.trX + " trY=" + this.trY + " w=" + image.getWidth(null) + " h=" + image.getHeight(null));
        }
        if ((anchor & 0x1) != 0x0) {
            anX = width >> 1;
        }
        else if ((anchor & 0x8) != 0x0) {
            anX = width;
        }
        if ((anchor & 0x2) != 0x0) {
            anY = height >> 1;
        }
        else if ((anchor & 0x20) != 0x0) {
            anY = height;
        }
        this.emuGraphics.drawImage(image, x + this.trX - anX, y + this.trY - anY, null);
        if (this.emuImage != null) {
            this.emuImage.emuInvalidate();
        }
    }
    
    public void drawRGB(int[] rgbData, int offset, int scanlength, final int x, final int y, final int width, final int height, final boolean processAlpha) {
        if (MainApp.verbose) {
            System.out.println("Graphics: drawRGB() x=" + x + " y=" + y + " w=" + width + " h=" + height + " offset=" + offset + " scanl=" + scanlength + " alpha=" + processAlpha);
        }
        if (!processAlpha) {
            rgbData = this.emuGetOpaqueRGB(rgbData, offset, width, height, scanlength);
            offset = 0;
            scanlength = width;
        }
        if (width > Graphics.biWidth) {
            this.initBufferedImage(width, 640);
        }
        else if (height > Graphics.biHeight) {
            this.initBufferedImage(480, height);
        }
        Graphics.bi.setRGB(0, 0, width, height, rgbData, offset, scanlength);
        this.emuPushClip();
        this.emuGraphics.clipRect(x + this.trX, y + this.trY, width, height);
        this.emuGraphics.drawImage(Graphics.bi, x + this.trX, y + this.trY, null);
        this.emuPopClip();
        ++this.emuActionCounter;
    }
    
    private int[] emuGetOpaqueRGB(final int[] srcData, final int offset, final int width, final int height, final int scanlength) {
        int srcOffset = offset;
        int dstOffset = 0;
        final int size = height * width;
        final int skip = scanlength - width;
        if (Graphics.pixelData.length < size) {
            Graphics.pixelData = new int[size];
        }
        while (dstOffset < size) {
            for (int i = 0; i < width; ++i) {
                Graphics.pixelData[dstOffset] = (srcData[srcOffset] | 0xFF000000);
                ++dstOffset;
                ++srcOffset;
            }
            srcOffset += skip;
        }
        return Graphics.pixelData;
    }
    
    protected void emuPushClip() {
        this.emuGraphics.getClipBounds(Graphics.emuTmpRect);
    }
    
    protected void emuPopClip() {
        this.emuGraphics.setClip(Graphics.emuTmpRect.x, Graphics.emuTmpRect.y, Graphics.emuTmpRect.width, Graphics.emuTmpRect.height);
    }
    
    public void drawRegion(final Image img, int x_src, int y_src, int width, int height, final int transform, final int x_dest, final int y_dest, final int anchor) {
        if (x_src < 0 || y_src < 0 || x_src + width > img.getWidth() || y_src + height > img.getHeight()) {
            throw new IllegalArgumentException("region to be copied exceeds the bounds of the source image");
        }
        this.emuGraphics.getClipBounds(Graphics.emuTmpRect);
        if (MainApp.verbose) {
            System.out.println("Graphics:" + this.hashCode() + " draw region! image=" + img.hashCode() + " x_src=" + x_src + " y_src=" + y_src + " w=" + width + " h=" + height + " transform=" + transform + " x_dst=" + x_dest + " y_dst=" + y_dest + " anchor=" + anchor + " trX=" + this.trX + " trY=" + this.trY);
            System.out.println(" current clip x=" + Graphics.emuTmpRect.x + " y=" + Graphics.emuTmpRect.y + " w=" + Graphics.emuTmpRect.width + " h=" + Graphics.emuTmpRect.height);
        }
        int anX = 0;
        int anY = 0;
        switch (transform) {
            case 1: {
                y_src = img.getHeight() - y_src - height;
                break;
            }
            case 2: {
                x_src = img.getWidth() - x_src - width;
                break;
            }
            case 3: {
                y_src = img.getHeight() - y_src - height;
                x_src = img.getWidth() - x_src - width;
                break;
            }
            case 4: {
                int tmp = width;
                width = height;
                height = tmp;
                tmp = x_src;
                x_src = y_src;
                y_src = tmp;
                break;
            }
            case 5: {
                int tmp = width;
                width = height;
                height = tmp;
                tmp = x_src;
                x_src = y_src;
                y_src = tmp;
                x_src = img.getHeight() - x_src - width;
                break;
            }
            case 6: {
                int tmp = width;
                width = height;
                height = tmp;
                tmp = x_src;
                x_src = y_src;
                y_src = tmp;
                y_src = img.getWidth() - y_src - height;
                break;
            }
            case 7: {
                int tmp = width;
                width = height;
                height = tmp;
                tmp = x_src;
                x_src = y_src;
                y_src = tmp;
                y_src = img.getWidth() - y_src - height;
                x_src = img.getHeight() - x_src - width;
                break;
            }
            default: {
                if (transform != 0) {
                    throw new IllegalArgumentException("unsupported transform ! " + transform);
                }
                break;
            }
        }
        if ((anchor & 0x1) != 0x0) {
            anX = width >> 1;
        }
        else if ((anchor & 0x8) != 0x0) {
            anX = width;
        }
        if ((anchor & 0x2) != 0x0) {
            anY = height >> 1;
        }
        else if ((anchor & 0x20) != 0x0) {
            anY = height;
        }
        this.emuGraphics.clipRect(x_dest + this.trX - anX, y_dest + this.trY - anY, width, height);
        if (transform != 0 && img.graphics != null && img.graphics.emuDrawRegionCounter != img.graphics.emuActionCounter) {
            img.graphics.emuDrawRegionCounter = img.graphics.emuActionCounter;
            img.emuInvalidate();
        }
        this.emuGraphics.drawImage(img.emuGetImage(transform), x_dest + this.trX - anX - x_src, y_dest + this.trY - anY - y_src, null);
        this.emuGraphics.setClip(Graphics.emuTmpRect.x, Graphics.emuTmpRect.y, Graphics.emuTmpRect.width, Graphics.emuTmpRect.height);
        ++this.emuActionCounter;
    }
    
    public void emuDrawImage(final java.awt.Image img, final int x, final int y) {
        this.emuGraphics.drawImage(img, x + this.trX, y + this.trY, null);
    }
    
    public void emuDrawRegionSimple(final java.awt.Image img, final int x_src, final int y_src, final int width, final int height, final int x_dest, final int y_dest) {
        this.emuGraphics.getClipBounds(Graphics.emuTmpRect);
        final boolean verbose = MainApp.verbose;
        this.emuGraphics.setClip(x_dest + this.trX, y_dest + this.trY, width, height);
        this.emuGraphics.drawImage(img, x_dest + this.trX - x_src, y_dest + this.trY - y_src, null);
        if (Graphics.emuTmpRect.width == 0) {
            Graphics.emuTmpRect.width = Display.WIDTH;
            Graphics.emuTmpRect.height = Display.HEIGHT;
        }
        this.emuGraphics.setClip(Graphics.emuTmpRect.x, Graphics.emuTmpRect.y, Graphics.emuTmpRect.width, Graphics.emuTmpRect.height);
    }
    
    public void drawImage(final Image img, final int x, final int y, final int anchor, final int manipulation) {
        int transform = 0;
        Color color = null;
        switch (manipulation) {
            case 8192: {
                transform = 2;
                color = Color.BLUE;
                break;
            }
            case 16384: {
                transform = 1;
                color = Color.YELLOW;
                break;
            }
            case 180: {
                transform = 3;
                color = Color.MAGENTA;
                break;
            }
            case 90: {
                transform = 6;
                color = Color.CYAN;
                break;
            }
            case 270: {
                transform = 5;
                break;
            }
            case 8282: {
                transform = 7;
                break;
            }
            case 8372: {
                transform = 1;
                break;
            }
            case 8462: {
                transform = 4;
                color = Color.GREEN;
                break;
            }
            case 16474: {
                transform = 4;
                break;
            }
            case 16564: {
                transform = 2;
                break;
            }
            case 16654: {
                transform = 7;
                color = Color.RED;
                break;
            }
            case 24576: {
                transform = 3;
                break;
            }
            case 24666: {
                transform = 5;
                break;
            }
            case 24756: {
                transform = 0;
                break;
            }
            case 24846: {
                transform = 6;
                break;
            }
            default: {
                if (manipulation != 0) {
                    System.out.println("Pstros: ExtendedGraphics.drawImage: unknow transform! " + manipulation);
                    break;
                }
                break;
            }
        }
        if (MainApp.verbose) {
            System.out.println("ExtendedGraphics: " + this.hashCode() + " drawImage...) " + img.hashCode() + " x=" + x + " y=" + y + " manipulation=" + manipulation + " trX=" + this.trX + " trY=" + this.trY + " anchor=" + anchor + " w=" + img.getWidth() + " h=" + img.getHeight() + " transform=" + transform);
        }
        this.drawRegion(img, 0, 0, img.getWidth(), img.getHeight(), transform, x, y, anchor);
        ++this.emuActionCounter;
    }
    
    public void drawPixels(final byte[] pixels, final byte[] transparencyMask, final int offset, final int scanlength, final int x, final int y, final int width, final int height, final int manipulation, final int format) {
        System.out.println("ExtendedGraphics: drawPixels(byte[],...) format=" + format + " NOT IMPLEMENTED!");
    }
    
    public void drawPixels(final int[] pixels, final boolean transparency, final int offset, final int scanlength, final int x, final int y, final int width, final int height, final int manipulation, final int format) {
        if (MainApp.verbose) {
            System.out.println("ExtendedGraphics: drawPixels(int[],...) format=" + format);
        }
        int manipX = 1;
        int manipY = 1;
        switch (manipulation) {
            case 8192: {
                manipX = -1;
                manipY = 1;
                break;
            }
            case 16384: {
                manipX = 1;
                manipY = -1;
                break;
            }
            case 180:
            case 24576: {
                manipX = -1;
                manipY = -1;
                break;
            }
        }
        switch (format) {
            case 888: {
                emuConvertPixelData888(pixels, width, height);
                break;
            }
            case 8888: {
                emuConvertPixelData8888(pixels, width, height, offset, scanlength, manipX, manipY);
                break;
            }
        }
        if (ConfigData.storeImages) {
            ImageCreator.savePixels(Graphics.pixelData, width, height);
        }
        Graphics.bi.setRGB(0, 0, width, height, Graphics.pixelData, 0, width);
        this.emuPushClip();
        this.emuGraphics.clipRect(x + this.trX, y + this.trY, width, height);
        this.emuDrawImage(Graphics.bi, x, y, 0, width, height);
        this.emuPopClip();
        ++this.emuActionCounter;
    }
    
    public void drawPixels(final short[] pixels, final boolean transparency, final int offset, final int scanlength, final int x, final int y, final int width, final int height, final int manipulation, final int format) {
        if (MainApp.verbose) {
            System.out.println("ExtendedGraphics: drawPixels(short[],...) format=" + format + " tr=" + transparency + " offset=" + offset + " scan=" + scanlength + " x=" + x + " y=" + y + " w=" + width + " h=" + height + " man=" + manipulation);
        }
        int manipX = 1;
        int manipY = 1;
        switch (manipulation) {
            case 8192: {
                manipX = -1;
                manipY = 1;
                break;
            }
            case 16384: {
                manipX = 1;
                manipY = -1;
                break;
            }
            case 180:
            case 24576: {
                manipX = -1;
                manipY = -1;
                break;
            }
        }
        switch (format) {
            case 444: {
                emuConvertPixelData444(pixels, width, height);
                break;
            }
            case 4444: {
                emuConvertPixelData4444(pixels, width, height, offset, scanlength, manipX, manipY);
                break;
            }
        }
        if (ConfigData.storeImages) {
            final int num = ImageCreator.savePixels(Graphics.pixelData, width, height);
            if (MainApp.verbose) {
                System.out.println("ExtendedGraphics: drawPixels imgIndex=" + num);
            }
        }
        if (width > Graphics.biWidth) {
            this.initBufferedImage(width, 640);
        }
        else if (height > Graphics.biHeight) {
            this.initBufferedImage(480, height);
        }
        Graphics.bi.setRGB(0, 0, width, height, Graphics.pixelData, 0, width);
        this.emuPushClip();
        this.emuGraphics.clipRect(x + this.trX, y + this.trY, width, height);
        this.emuDrawImage(Graphics.bi, x, y, 0, width, height);
        this.emuPopClip();
        ++this.emuActionCounter;
    }
    
    public void drawPolygon(final int[] xPoints, final int xOffset, final int[] yPoints, final int yOffset, final int nPoints, final int argbColor) {
        if (MainApp.verbose) {
            System.out.println("ExtendedGraphics: drawPolygon()");
        }
        if (xPoints == null || yPoints == null || nPoints == 0) {
            return;
        }
        if (Graphics.polygonX.length < nPoints) {
            Graphics.polygonX = new int[nPoints];
            Graphics.polygonY = new int[nPoints];
        }
        final int[] polygonX = Graphics.polygonX;
        final int n = 0;
        final int n2 = xPoints[xOffset] + this.trX;
        polygonX[n] = n2;
        int maxX = n2;
        int minX = n2;
        final int[] polygonY = Graphics.polygonY;
        final int n3 = 0;
        final int n4 = yPoints[yOffset] + this.trY;
        polygonY[n3] = n4;
        int maxY = n4;
        int minY = n4;
        for (int i = 1; i < nPoints; ++i) {
            final int[] polygonX2 = Graphics.polygonX;
            final int n5 = i;
            final int n6 = xPoints[i + xOffset] + this.trX;
            polygonX2[n5] = n6;
            final int valX = n6;
            final int[] polygonY2 = Graphics.polygonY;
            final int n7 = i;
            final int n8 = yPoints[i + yOffset] + this.trY;
            polygonY2[n7] = n8;
            final int valY = n8;
            if (valX < minX) {
                minX = valX;
            }
            else if (valX > maxX) {
                maxX = valX;
            }
            if (valY < minY) {
                minY = valY;
            }
            else if (valY > maxY) {
                maxY = valY;
            }
        }
        ++maxX;
        ++maxY;
        if (minX < this.clX) {
            minX = this.clX;
        }
        if (minY < this.clY) {
            minY = this.clY;
        }
        if (maxX > this.clX + this.clW) {
            maxX = this.clX + this.clW;
        }
        if (maxY > this.clY + this.clH) {
            maxY = this.clY + this.clH;
        }
        if (minX < 0) {
            minX = 0;
        }
        if (maxX > Display.WIDTH) {
            maxX = Display.WIDTH;
        }
        if (minY < 0) {
            minY = 0;
        }
        if (maxY > Display.HEIGHT) {
            maxY = Display.HEIGHT;
        }
        this.emuCleanBi(minX, minY, maxX, maxY);
        Graphics.biGraphics.setColor(new Color(-1));
        Graphics.biGraphics.drawPolygon(Graphics.polygonX, Graphics.polygonY, nPoints);
        this.emuApplyAlpha(minX, minY, maxX, maxY, argbColor);
        this.emuPushClip();
        this.emuGraphics.clipRect(minX + this.trX, minY + this.trY, maxX - minX, maxY - minY);
        this.emuGraphics.drawImage(Graphics.bi, this.trX, this.trY, null);
        this.emuPopClip();
        ++this.emuActionCounter;
    }
    
    public void drawTriangle(final int x1, final int y1, final int x2, final int y2, final int x3, final int y3, final int argbColor) {
        if (MainApp.verbose) {
            System.out.println("ExtendedGraphics: drawTriangle()");
        }
        Graphics.polygonX[0] = x1 + this.trX;
        Graphics.polygonX[1] = x2 + this.trX;
        Graphics.polygonX[2] = x3 + this.trX;
        Graphics.polygonY[0] = y1 + this.trY;
        Graphics.polygonY[1] = y2 + this.trY;
        Graphics.polygonY[2] = y3 + this.trY;
        int minX;
        int maxX = minX = Graphics.polygonX[0];
        int minY;
        int maxY = minY = Graphics.polygonY[0];
        for (int i = 1; i < 3; ++i) {
            final int valX = Graphics.polygonX[i];
            final int valY = Graphics.polygonY[i];
            if (valX < minX) {
                minX = valX;
            }
            else if (valX > maxX) {
                maxX = valX;
            }
            if (valY < minY) {
                minY = valY;
            }
            else if (valY > maxY) {
                maxY = valY;
            }
        }
        ++maxX;
        ++maxY;
        if (minX < this.clX) {
            minX = this.clX;
        }
        if (minY < this.clY) {
            minY = this.clY;
        }
        if (maxX > this.clX + this.clW) {
            maxX = this.clX + this.clW;
        }
        if (maxY > this.clY + this.clH) {
            maxY = this.clY + this.clH;
        }
        if (minX < 0) {
            minX = 0;
        }
        if (maxX > Display.WIDTH) {
            maxX = Display.WIDTH;
        }
        if (minY < 0) {
            minY = 0;
        }
        if (maxY > Display.HEIGHT) {
            maxY = Display.HEIGHT;
        }
        this.emuCleanBi(minX, minY, maxX, maxY);
        Graphics.biGraphics.setColor(new Color(-1));
        Graphics.biGraphics.drawPolygon(Graphics.polygonX, Graphics.polygonY, 3);
        this.emuApplyAlpha(minX, minY, maxX, maxY, argbColor);
        this.emuPushClip();
        this.emuGraphics.clipRect(minX + this.trX, minY + this.trY, maxX - minX, maxY - minY);
        this.emuGraphics.drawImage(Graphics.bi, this.trX, this.trY, null);
        this.emuPopClip();
        ++this.emuActionCounter;
    }
    
    public void fillPolygon(final int[] xPoints, final int xOffset, final int[] yPoints, final int yOffset, final int nPoints, final int argbColor) {
        if (MainApp.verbose) {
            System.out.println("ExtendedGraphics: fillPolygon()  coloralpha=" + (argbColor >> 24) + " color=" + Integer.toHexString(argbColor) + " xOffset=" + xOffset + " yOffset=" + yOffset + " nPoints=" + nPoints + " size=" + xPoints.length);
        }
        if (xPoints == null || yPoints == null || nPoints == 0) {
            return;
        }
        if (Graphics.polygonX.length < nPoints) {
            Graphics.polygonX = new int[nPoints];
            Graphics.polygonY = new int[nPoints];
        }
        final int[] polygonX = Graphics.polygonX;
        final int n = 0;
        final int n2 = xPoints[xOffset] + this.trX;
        polygonX[n] = n2;
        int maxX = n2;
        int minX = n2;
        final int[] polygonY = Graphics.polygonY;
        final int n3 = 0;
        final int n4 = yPoints[yOffset] + this.trY;
        polygonY[n3] = n4;
        int maxY = n4;
        int minY = n4;
        for (int i = 1; i < nPoints; ++i) {
            final int[] polygonX2 = Graphics.polygonX;
            final int n5 = i;
            final int n6 = xPoints[i + xOffset] + this.trX;
            polygonX2[n5] = n6;
            final int valX = n6;
            final int[] polygonY2 = Graphics.polygonY;
            final int n7 = i;
            final int n8 = yPoints[i + yOffset] + this.trY;
            polygonY2[n7] = n8;
            final int valY = n8;
            if (valX < minX) {
                minX = valX;
            }
            else if (valX > maxX) {
                maxX = valX;
            }
            if (valY < minY) {
                minY = valY;
            }
            else if (valY > maxY) {
                maxY = valY;
            }
        }
        if (minX < this.clX) {
            minX = this.clX;
        }
        if (minY < this.clY) {
            minY = this.clY;
        }
        if (maxX > this.clX + this.clW) {
            maxX = this.clX + this.clW;
        }
        if (maxY > this.clY + this.clH) {
            maxY = this.clY + this.clH;
        }
        if (minX < 0) {
            minX = 0;
        }
        if (maxX > Display.WIDTH) {
            maxX = Display.WIDTH;
        }
        if (minY < 0) {
            minY = 0;
        }
        if (maxY > Display.HEIGHT) {
            maxY = Display.HEIGHT;
        }
        this.emuCleanBi(minX, minY, maxX, maxY);
        Graphics.biGraphics.setColor(new Color(-1));
        Graphics.biGraphics.fillPolygon(Graphics.polygonX, Graphics.polygonY, nPoints);
        this.emuApplyAlpha(minX, minY, maxX, maxY, argbColor);
        this.emuPushClip();
        this.emuGraphics.clipRect(minX, minY, maxX - minX, maxY - minY);
        this.emuGraphics.drawImage(Graphics.bi, 0, 0, null);
        this.emuPopClip();
        ++this.emuActionCounter;
    }
    
    public void fillTriangle(final int x1, final int y1, final int x2, final int y2, final int x3, final int y3, final int argbColor) {
        if (MainApp.verbose) {
            System.out.println("ExtendedGraphics: fillTriangle() alpha=" + (argbColor >> 24));
        }
        Graphics.polygonX[0] = x1 + this.trX;
        Graphics.polygonX[1] = x2 + this.trX;
        Graphics.polygonX[2] = x3 + this.trX;
        Graphics.polygonY[0] = y1 + this.trY;
        Graphics.polygonY[1] = y2 + this.trY;
        Graphics.polygonY[2] = y3 + this.trY;
        int minX;
        int maxX = minX = Graphics.polygonX[0];
        int minY;
        int maxY = minY = Graphics.polygonY[0];
        for (int i = 1; i < 3; ++i) {
            final int valX = Graphics.polygonX[i];
            final int valY = Graphics.polygonY[i];
            if (valX < minX) {
                minX = valX;
            }
            else if (valX > maxX) {
                maxX = valX;
            }
            if (valY < minY) {
                minY = valY;
            }
            else if (valY > maxY) {
                maxY = valY;
            }
        }
        if (minX < this.clX) {
            minX = this.clX;
        }
        if (minY < this.clY) {
            minY = this.clY;
        }
        if (maxX > this.clX + this.clW) {
            maxX = this.clX + this.clW;
        }
        if (maxY > this.clY + this.clH) {
            maxY = this.clY + this.clH;
        }
        if (minX < 0) {
            minX = 0;
        }
        if (maxX > Display.WIDTH) {
            maxX = Display.WIDTH;
        }
        if (minY < 0) {
            minY = 0;
        }
        if (maxY > Display.HEIGHT) {
            maxY = Display.HEIGHT;
        }
        this.emuCleanBi(minX, minY, maxX, maxY);
        Graphics.biGraphics.setColor(new Color(-1));
        Graphics.biGraphics.fillPolygon(Graphics.polygonX, Graphics.polygonY, 3);
        this.emuApplyAlpha(minX, minY, maxX, maxY, argbColor);
        this.emuPushClip();
        this.emuGraphics.clipRect(minX, minY, maxX - minX, maxY - minY);
        this.emuGraphics.drawImage(Graphics.bi, 0, 0, null);
        this.emuPopClip();
        ++this.emuActionCounter;
    }
    
    private void emuCleanBi(final int x1, final int y1, final int x2, final int y2) {
        for (int j = y1; j < y2; ++j) {
            int base = j * 480 + x1;
            for (int i = x1; i < x2; ++i) {
                Graphics.pixelData[base++] = 0;
            }
        }
        Graphics.bi.setRGB(x1, y1, x2 - x1, y2 - y1, Graphics.pixelData, y1 * 480 + x1, 480);
        Graphics.biGraphics = Graphics.bi.getGraphics();
    }
    
    private void emuApplyAlpha(final int x1, final int y1, final int x2, final int y2, final int alpha) {
        final int height = y2 - y1;
        final int width = x2 - x1;
        Graphics.bi.getRGB(x1, y1, width, height, Graphics.pixelData, 0, 480);
        for (int j = 0; j < height; ++j) {
            int base = j * 480;
            for (int i = 0; i < width; ++i) {
                if (Graphics.pixelData[base] == -1) {
                    Graphics.pixelData[base] = alpha;
                }
                ++base;
            }
        }
        Graphics.bi.setRGB(x1, y1, width, height, Graphics.pixelData, 0, 480);
    }
    
    public int getAlphaComponent() {
        if (MainApp.verbose) {
            System.out.println("ExtendedGraphics: getAlphaComponent()");
        }
        return (this.color & 0xFF000000) >>> 24;
    }
    
    public int getNativePixelFormat() {
        return 4444;
    }
    
    public void getPixels(final byte[] pixels, final byte[] transparencyMask, final int offset, final int scanlength, final int x, final int y, final int width, final int height, final int format) {
        System.out.println("ExtendedGraphics: getPixels(byte[])  format" + format + " !NOT IMPLEMENTED!");
    }
    
    public void getPixels(final int[] pixels, final int offset, final int scanlength, final int x, final int y, final int width, final int height, final int format) {
        if (MainApp.verbose) {
            System.out.println("ExtendedGraphics: getPixels(int[])  format" + format);
        }
        final BufferedImage img = this.emuGetGraphicsImage();
        img.getRGB(x + this.trX, y + this.trY, width, height, Graphics.pixelData, 0, width);
        switch (format) {
            case 888:
            case 8888: {
                this.emuStorePixelData8888(pixels, offset, scanlength, width, height);
                break;
            }
        }
    }
    
    public void getPixels(final short[] pixels, final int offset, final int scanlength, final int x, final int y, final int width, final int height, final int format) {
        if (MainApp.verbose) {
            System.out.println("ExtendedGraphics: getPixels(short[])  format=" + format + " x=" + x + " y=" + y + " w=" + width + " h=" + height);
        }
        final BufferedImage img = this.emuGetGraphicsImage();
        img.getRGB(x + this.trX, y + this.trY, width, height, Graphics.pixelData, 0, width);
        switch (format) {
            case 4444: {
                this.emuStorePixelData4444(pixels, offset, scanlength, width, height);
                break;
            }
            case 444: {
                this.emuStorePixelData444(pixels, offset, scanlength, width, height);
                break;
            }
        }
    }
    
    public void setARGBColor(final int argbColor) {
        if (MainApp.verbose) {
            System.out.println("ExtendedGraphics: setARGBColor() " + Integer.toHexString(argbColor));
        }
        this.color = argbColor;
        this.emuGraphics.setColor(new Color(this.color >> 16 & 0xFF, this.color >> 8 & 0xFF, this.color & 0xFF, this.color >> 24 & 0xFF));
    }
    
    private static void emuConvertPixelData4444(final short[] pixels, final int width, final int height, final int offset, final int scan, final int manipX, final int manipY) {
        int srcOffset = 0;
        int dstOffset = 0;
        boolean debug = false;
        if (MainApp.verbose && width == 16 && height == 16) {
            debug = true;
            System.out.println("!pixel debug:");
        }
        for (int y = 0; y < height; ++y) {
            srcOffset = offset + y * scan;
            if (manipY > 0) {
                dstOffset = y * width;
            }
            else {
                dstOffset = (height - y - 1) * width;
            }
            if (manipX < 0) {
                dstOffset += width - 1;
            }
            for (int x = 0; x < width; ++x) {
                final int srcPix = pixels[srcOffset++];
                if (debug) {
                    System.out.print(Integer.toHexString(srcPix));
                }
                int dstPix = (srcPix & 0xF) << 4;
                dstPix |= (srcPix & 0xF0) << 8;
                dstPix |= (srcPix & 0xF00) << 12;
                dstPix |= (srcPix & 0xF000) << 16;
                dstPix |= dstPix >> 4;
                Graphics.pixelData[dstOffset] = dstPix;
                dstOffset += manipX;
            }
        }
        if (debug) {
            System.out.println();
        }
    }
    
    private static void emuConvertPixelData444(final short[] pixels, final int width, final int height) {
        int offset = 0;
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                final int srcPix = pixels[offset];
                int dstPix = (srcPix & 0xF) << 4;
                dstPix |= (srcPix & 0xF0) << 8;
                dstPix |= (srcPix & 0xF00) << 12;
                dstPix |= 0xF0000000;
                dstPix |= dstPix >> 4;
                Graphics.pixelData[offset++] = dstPix;
            }
        }
    }
    
    private void emuStorePixelData444(final short[] pixels, final int offset, final int scan, final int width, final int height) {
        int srcOffset = 0;
        int dstOffset = 0;
        for (int y = 0; y < height; ++y) {
            srcOffset = y * width;
            dstOffset = offset + y * scan;
            for (int x = 0; x < width; ++x) {
                final int srcPix = Graphics.pixelData[srcOffset++];
                short dstPix = -4096;
                dstPix |= (short)((srcPix & 0xF0) >> 4);
                dstPix |= (short)((srcPix & 0xF000) >> 8);
                dstPix |= (short)((srcPix & 0xF00000) >> 12);
                pixels[dstOffset++] = dstPix;
            }
        }
    }
    
    private void emuStorePixelData4444(final short[] pixels, final int offset, final int scan, final int width, final int height) {
        int srcOffset = 0;
        int dstOffset = 0;
        for (int y = 0; y < height; ++y) {
            srcOffset = y * width;
            dstOffset = offset + y * scan;
            for (int x = 0; x < width; ++x) {
                final int srcPix = Graphics.pixelData[srcOffset++];
                short dstPix = (short)((srcPix & 0xF0000000) >> 16);
                dstPix |= (short)((srcPix & 0xF0) >> 4);
                dstPix |= (short)((srcPix & 0xF000) >> 8);
                dstPix |= (short)((srcPix & 0xF00000) >> 12);
                pixels[dstOffset++] = dstPix;
            }
        }
    }
    
    private void emuStorePixelData8888(final int[] pixels, final int offset, final int scan, final int width, final int height) {
        int srcOffset = 0;
        int dstOffset = 0;
        for (int y = 0; y < height; ++y) {
            srcOffset = y * width;
            dstOffset = offset + y * scan;
            for (int x = 0; x < width; ++x) {
                final int srcPix = Graphics.pixelData[srcOffset++];
                pixels[dstOffset++] = srcPix;
            }
        }
    }
    
    private static void emuConvertPixelData8888(final int[] pixels, final int width, final int height, final int offset, final int scan, final int manipX, final int manipY) {
        int srcOffset = 0;
        int dstOffset = 0;
        for (int y = 0; y < height; ++y) {
            srcOffset = offset + y * scan;
            if (manipY > 0) {
                dstOffset = y * width;
            }
            else {
                dstOffset = (height - y - 1) * width;
            }
            if (manipX < 0) {
                dstOffset += width - 1;
            }
            for (int x = 0; x < width; ++x) {
                final int srcPix = pixels[srcOffset++];
                Graphics.pixelData[dstOffset] = srcPix;
                dstOffset += manipX;
            }
        }
    }
    
    private static void emuConvertPixelData888(final int[] pixels, final int width, final int height) {
        int offset = 0;
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                final int srcPix = pixels[offset];
                Graphics.pixelData[offset++] = (0xFF000000 | (srcPix & 0xFFFFFF));
            }
        }
    }
    
    public Object getObject() {
        return this.emuGraphics;
    }
}
