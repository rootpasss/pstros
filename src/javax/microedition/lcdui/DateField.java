// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Date;
import java.util.TimeZone;

public class DateField extends Item
{
    public static final int DATE = 1;
    public static final int TIME = 2;
    public static final int DATE_TIME = 3;
    private TimeZone timeZone;
    private int inputMode;
    
    public DateField(final String label, final int mode) {
        this(label, mode, null);
    }
    
    public DateField(final String label, final int mode, final TimeZone timeZone) {
        this.setLabel(label);
        if (timeZone == null) {
            this.timeZone = TimeZone.getDefault();
        }
        else {
            this.timeZone = timeZone;
        }
        this.setInputMode(mode);
    }
    
    public Date getDate() {
        return null;
    }
    
    public void setDate(final Date date) {
    }
    
    public int getInputMode() {
        return this.inputMode;
    }
    
    public void setInputMode(final int mode) {
        this.inputMode = mode;
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
            g.setColor(DateField.COLOR_HIGHLIGT);
            g.fillRect(x, y - height + 2, Display.WIDTH - 4, height + 2);
            g.setColor(paintColor);
        }
        g.drawString("not implemented in Pstros", x + 2, y + 1);
        g.drawRect(x, y - height + 2, Display.WIDTH - 4, height + 2);
        y += height + 2;
        y += 4;
        return y - origY;
    }
    
    int emuGetYSpace(final Graphics g) {
        final Font font = g.getFont();
        return font.getSize() + 3;
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
}
