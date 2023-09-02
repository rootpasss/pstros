// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import java.awt.Graphics;

public class Spacer extends Item
{
    int minWidth;
    int minHeight;
    
    public Spacer(final int minWidth, final int minHeight) {
        this.setMinimumSize(minWidth, minHeight);
    }
    
    public void setMinimumSize(final int minWidth, final int minHeight) {
        if (minWidth < 0 || minHeight < 0) {
            throw new IllegalArgumentException();
        }
        this.minHeight = minHeight;
        this.minWidth = minWidth;
    }
    
    public void addCommand(final Command cmd) {
        throw new IllegalStateException();
    }
    
    public void setDefaultCommand(final Command cmd) {
        throw new IllegalStateException();
    }
    
    public void setLabel(final String label) {
        throw new IllegalStateException();
    }
    
    int emuGetHeight(final Graphics g) {
        return this.minHeight;
    }
    
    int emuPaint(final Graphics g, final int x, final int y) {
        return this.minHeight;
    }
}
