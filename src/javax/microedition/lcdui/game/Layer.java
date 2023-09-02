// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui.game;

import javax.microedition.lcdui.Graphics;

public abstract class Layer
{
    int layerWidth;
    int layerHeight;
    int x;
    int y;
    private boolean visible;
    
    public final int getHeight() {
        return this.layerHeight;
    }
    
    public final int getWidth() {
        return this.layerWidth;
    }
    
    public final int getX() {
        return this.x;
    }
    
    public final int getY() {
        return this.y;
    }
    
    public final boolean isVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
    
    public void setPosition(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    
    public void move(final int dx, final int dy) {
        this.x += dx;
        this.y += dy;
    }
    
    public abstract void paint(final Graphics p0);
}
