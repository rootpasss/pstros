// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;

public class Gauge extends Item
{
    public static final int INDEFINITE = -1;
    public static final int CONTINUOUS_IDLE = 0;
    public static final int INCREMENTAL_IDLE = 1;
    public static final int CONTINUOUS_RUNNING = 2;
    public static final int INCREMENTAL_UPDATING = 3;
    private boolean interactive;
    private int maxVal;
    private int curVal;
    private int w;
    private int h;
    
    public Gauge(final String label, final boolean interactive, final int maxValue, final int initialValue) {
        this.w = 100;
        this.h = 6;
        this.setLabel(label);
        this.interactive = interactive;
        this.maxVal = maxValue;
        this.curVal = initialValue;
    }
    
    public int getMaxValue() {
        return this.maxVal;
    }
    
    public boolean isInteractive() {
        return this.interactive;
    }
    
    public void setMaxValue(final int maxValue) {
        this.maxVal = maxValue;
    }
    
    public int getValue() {
        if (this.curVal == -1) {
            return 0;
        }
        return this.curVal;
    }
    
    public void setValue(int value) {
        if (value < 0) {
            value = 0;
        }
        else if (value > this.maxVal && this.maxVal > 0) {
            value = this.maxVal;
        }
        this.curVal = value;
        this.emuUpdateScreen();
    }
    
    public void setPreferredSize(final int width, final int height) {
        this.w = width;
        this.h = height;
    }
    
    int emuPaint(final Graphics g, final int x, int y) {
        int totalH = 0;
        g.setColor(Color.BLACK);
        if (this.label != null) {
            g.drawString(this.label, x, y);
            final Font font = g.getFont();
            totalH = font.getSize() + 2;
            y += totalH;
        }
        if (this.w > Display.WIDTH - x - 4) {
            this.w = Display.WIDTH - x - 4;
        }
        if (this.h > 16) {
            this.h = 16;
        }
        g.drawRect(x, y, this.w, this.h);
        final int gauge = this.curVal * this.w / this.maxVal;
        g.fillRect(x, y, gauge, this.h);
        return totalH + this.h + 2;
    }
    
    int emuGetHeight(final Graphics g) {
        int totalH = 0;
        if (this.label != null) {
            final Font font = g.getFont();
            totalH = font.getSize() + 2;
        }
        if (this.h > 16) {
            this.h = 16;
        }
        return totalH + this.h + 2;
    }
}
