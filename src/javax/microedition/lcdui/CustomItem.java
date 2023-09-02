// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import ole.pstros.ConfigData;

public abstract class CustomItem extends Item
{
    protected static final int TRAVERSE_HORIZONTAL = 1;
    protected static final int TRAVERSE_VERTICAL = 2;
    protected static final int KEY_PRESS = 4;
    protected static final int KEY_RELEASE = 8;
    protected static final int KEY_REPEAT = 16;
    protected static final int POINTER_PRESS = 32;
    protected static final int POINTER_RELEASE = 64;
    protected static final int POINTER_DRAG = 128;
    protected static final int NONE = 0;
    
    protected CustomItem(final String label) {
        this.label = label;
    }
    
    public int getGameAction(final int keyCode) {
        if (keyCode == ConfigData.keyUpArrow) {
            return 1;
        }
        if (keyCode == ConfigData.keyDownArrow) {
            return 6;
        }
        if (keyCode == ConfigData.keyLeftArrow) {
            return 2;
        }
        if (keyCode == ConfigData.keyRightArrow) {
            return 5;
        }
        if (keyCode == ConfigData.keyCenterSoft) {
            return 8;
        }
        return 0;
    }
    
    protected final int getInteractionModes() {
        return 0;
    }
    
    protected abstract int getMinContentWidth();
    
    protected abstract int getMinContentHeight();
    
    protected abstract int getPrefContentWidth(final int p0);
    
    protected abstract int getPrefContentHeight(final int p0);
    
    protected void sizeChanged(final int w, final int h) {
    }
    
    protected final void invalidate() {
    }
    
    protected abstract void paint(final Graphics p0, final int p1, final int p2);
    
    protected final void repaint() {
    }
    
    protected final void repaint(final int x, final int y, final int w, final int h) {
        this.repaint();
    }
    
    protected boolean traverse(final int dir, final int viewportWidth, final int viewportHeight, final int[] visRect_inout) {
        return false;
    }
    
    protected void traverseOut() {
    }
    
    protected void keyPressed(final int keyCode) {
    }
    
    protected void keyReleased(final int keyCode) {
    }
    
    protected void keyRepeated(final int keyCode) {
    }
    
    protected void pointerPressed(final int x, final int y) {
    }
    
    protected void pointerReleased(final int x, final int y) {
    }
    
    protected void pointerDragged(final int x, final int y) {
    }
    
    protected void showNotify() {
    }
    
    protected void hideNotify() {
    }
    
    int emuGetHeight(final java.awt.Graphics g) {
        return this.getMinContentHeight();
    }
}
