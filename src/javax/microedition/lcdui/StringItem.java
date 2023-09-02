// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import java.awt.Color;
import java.awt.Graphics;

public class StringItem extends Item
{
    private static final int[] DEFAULT_POINT;
    private String text;
    private int mode;
    private Font emuFont;
    
    static {
        DEFAULT_POINT = new int[3];
    }
    
    public StringItem(final String label, final String text) {
        this.setLabel(label);
        this.text = text;
    }
    
    public StringItem(final String label, final String text, final int appearanceMode) {
        this(label, text);
        this.mode = appearanceMode;
    }
    
    public String getText() {
        return this.text;
    }
    
    public void setText(final String text) {
        this.text = text;
        this.emuUpdateScreen();
    }
    
    public int getAppearanceMode() {
        return this.mode;
    }
    
    public void setFont(final Font font) {
        this.emuFont = font;
    }
    
    public Font getFont() {
        return this.emuFont;
    }
    
    int emuPaint(final Graphics g, int x, int y) {
        if (this.text == null && this.label == null) {
            return 0;
        }
        final String l = this.getLabel();
        int[] point = StringItem.DEFAULT_POINT;
        final int origX = x;
        final int origY = y;
        ++x;
        if (!this.emuActive) {
            g.setColor(Color.BLACK);
        }
        else {
            final int areaH = this.emuGetHeight(g);
            final int fontH = g.getFont().getSize();
            g.setColor(Item.COLOR_HIGHLIGT);
            g.fillRect(x, y - fontH, Display.WIDTH - 4, areaH);
            g.setColor(Color.RED);
        }
        if (l != null) {
            final java.awt.Font origFont = g.getFont();
            final java.awt.Font font = new java.awt.Font(origFont.getFontName(), 1, origFont.getSize());
            g.setFont(font);
            point = Item.emuDrawMultiLine(g, l, x, y + 2, true);
            x += point[0];
            y += point[1];
            g.setFont(origFont);
        }
        if (this.text != null) {
            point = Item.emuDrawMultiLine(g, this.text, x, y + 2, true);
            y += point[1];
            x += point[0];
        }
        if (this.getAppearanceMode() == 2) {
            g.drawRect(origX, origY - point[2] / 2 - 3, x - origX, y - origY + point[2]);
        }
        return y - origY + point[2];
    }
    
    int emuGetHeight(final Graphics g) {
        if (this.text == null && this.label == null) {
            return 0;
        }
        int[] point = StringItem.DEFAULT_POINT;
        final String l = this.getLabel();
        int x = 0;
        int y = 0;
        if (l != null) {
            final java.awt.Font origFont = g.getFont();
            final java.awt.Font font = new java.awt.Font(origFont.getFontName(), 1, origFont.getSize());
            g.setFont(font);
            point = Item.emuDrawMultiLine(g, l, x, y + 2, false);
            x += point[0];
            y += point[1];
            g.setFont(origFont);
        }
        if (this.text != null) {
            point = Item.emuDrawMultiLine(g, this.text, x, y + 2, false);
            y += point[1];
            x += point[0];
        }
        return y + point[2];
    }
    
    int emuGetYSpace(final Graphics g) {
        if (this.text == null && this.label == null) {
            return 0;
        }
        final java.awt.Font font = g.getFont();
        return font.getSize() + 2;
    }
}
