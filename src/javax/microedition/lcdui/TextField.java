// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;

public class TextField extends Item
{
    public static final int ANY = 0;
    public static final int EMAILADDR = 1;
    public static final int NUMERIC = 2;
    public static final int PHONENUMBER = 3;
    public static final int URL = 4;
    public static final int DECIMAL = 5;
    public static final int PASSWORD = 65536;
    public static final int UNEDITABLE = 131072;
    public static final int SENSITIVE = 262144;
    public static final int NON_PREDICTIVE = 524288;
    public static final int INITIAL_CAPS_WORD = 1048576;
    public static final int INITIAL_CAPS_SENTENCE = 2097152;
    public static final int CONSTRAINT_MASK = 65535;
    protected String text;
    protected int constraints;
    protected int maxSize;
    
    public TextField(final String label, final String text, final int maxSize, final int constraints) {
        this.setLabel(label);
        this.text = text;
        this.maxSize = maxSize;
        this.constraints = constraints;
        if (maxSize < 1) {
            throw new IllegalArgumentException(" maxSize < 1: maxSize=" + maxSize);
        }
        this.emuInteractive = true;
    }
    
    public String getString() {
        return this.text;
    }
    
    public void setString(final String text) {
        this.text = text;
    }
    
    public int getChars(final char[] data) {
        int size = data.length;
        final char[] src = this.text.toCharArray();
        if (src.length < size) {
            size = src.length;
        }
        System.arraycopy(src, 0, data, 0, size);
        return size;
    }
    
    public void setChars(final char[] data, final int offset, final int length) {
        this.text = new String(data, offset, length);
    }
    
    public void insert(final String src, final int position) {
    }
    
    public void insert(final char[] data, final int offset, final int length, final int position) {
    }
    
    public void delete(final int offset, final int length) {
    }
    
    public int getMaxSize() {
        return this.maxSize;
    }
    
    public int setMaxSize(final int maxSize) {
        return this.maxSize = maxSize;
    }
    
    public int size() {
        return this.text.length();
    }
    
    public int getCaretPosition() {
        return 0;
    }
    
    public void setConstraints(final int constraints) {
        this.constraints = constraints;
    }
    
    public int getConstraints() {
        return this.constraints;
    }
    
    public void setInitialInputMode(final String characterSubset) {
    }
    
    void emuKeyAction(final int key, final int keyChar, final int action) {
        if (Character.getNumericValue((char)keyChar) != -1) {
            if (this.text == null) {
                this.text = "";
            }
            this.text = String.valueOf(this.text) + (char)keyChar;
            this.emuUpdateScreen();
        }
        else if (key == 8 && this.text.length() > 0) {
            this.text = this.text.substring(0, this.text.length() - 1);
        }
        else {
            if (keyChar == 65535 || keyChar == 127) {
                return;
            }
            if (this.text == null) {
                this.text = "";
            }
            this.text = String.valueOf(this.text) + (char)keyChar;
            this.emuUpdateScreen();
        }
    }
    
    int emuPaint(final Graphics g, final int x, int y) {
        final Font font = g.getFont();
        final int height = font.getSize() + 2;
        final int width = Display.WIDTH - x - 2;
        final int origY = y;
        Color paintColor = Color.BLACK;
        if (this.emuActive) {
            paintColor = Color.RED;
        }
        g.setColor(paintColor);
        if (this.label != null) {
            g.drawString(this.label, x + 2, y + 1);
            y += height + 2;
        }
        if (this.emuActive) {
            g.setColor(TextField.COLOR_HIGHLIGT);
            g.fillRect(x, y - height + 2, Display.WIDTH - 4, height + 2);
            g.setColor(paintColor);
        }
        if (this.text != null) {
            g.drawString(this.text, x + 2, y + 1);
        }
        g.drawRect(x, y - height + 2, Display.WIDTH - 4, height + 2);
        y += height + 2;
        y += 4;
        return y - origY;
    }
    
    int emuGetHeight(final Graphics g) {
        final Font font = g.getFont();
        int height = font.getSize();
        final int h = height += 4;
        if (this.label != null && this.label.length() > 0) {
            height += h;
        }
        return height + 4;
    }
    
    int emuGetYSpace(final Graphics g) {
        final Font font = g.getFont();
        return font.getSize() + 3;
    }
}
