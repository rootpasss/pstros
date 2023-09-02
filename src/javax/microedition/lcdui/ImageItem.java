// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import java.awt.image.ImageObserver;
import java.awt.Graphics;

public class ImageItem extends Item
{
    public static final int LAYOUT_DEFAULT = 0;
    public static final int LAYOUT_LEFT = 1;
    public static final int LAYOUT_RIGHT = 2;
    public static final int LAYOUT_CENTER = 3;
    public static final int LAYOUT_NEWLINE_BEFORE = 256;
    public static final int LAYOUT_NEWLINE_AFTER = 512;
    private int mode;
    private Image image;
    private int layout;
    private String text;
    
    public ImageItem(final String label, final Image img, final int layout, final String altText) {
        this(label, img, layout, altText, 0);
    }
    
    public ImageItem(final String label, final Image image, final int layout, final String altText, final int appearanceMode) {
        this.setLabel(label);
        this.image = image;
        this.layout = layout;
        this.text = altText;
        this.mode = appearanceMode;
    }
    
    public Image getImage() {
        return this.image;
    }
    
    public void setImage(final Image img) {
        this.image = img;
        this.emuUpdateScreen();
    }
    
    public String getAltText() {
        return this.text;
    }
    
    public void setAltText(final String text) {
        this.text = text;
    }
    
    public int getLayout() {
        return this.layout;
    }
    
    public void setLayout(final int layout) {
        this.layout = layout;
    }
    
    public int getAppearanceMode() {
        return this.mode;
    }
    
    int emuPaint(final Graphics g, int x, int y) {
        final int hLayout = this.layout & 0xF;
        final int vLayout = this.layout & 0xF0;
        switch (hLayout) {
            case 2: {
                x = Display.WIDTH - this.image.getWidth();
                break;
            }
            case 3: {
                x = (Display.WIDTH - this.image.getWidth()) / 2;
                break;
            }
        }
        final int imageHeight = this.image.getHeight();
        switch (vLayout) {
            case 32: {
                y -= imageHeight;
                break;
            }
            case 48: {
                y -= imageHeight / 2;
                break;
            }
        }
        g.drawImage(this.image.emuGetImage(0), x, y, null);
        return imageHeight + 2;
    }
    
    int emuGetHeight(final Graphics g) {
        return this.image.getHeight() + 2;
    }
    
    int emuGetYSpace(final Graphics g) {
        final int imageHeight = this.image.getHeight();
        final int vLayout = this.layout & 0xF0;
        int result = 1;
        switch (vLayout) {
            case 32: {
                result += imageHeight;
                break;
            }
            case 48: {
                result += imageHeight / 2;
                break;
            }
        }
        return result;
    }
}
