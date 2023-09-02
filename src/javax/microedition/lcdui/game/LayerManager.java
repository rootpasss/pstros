// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui.game;

import javax.microedition.lcdui.Graphics;
import ole.pstros.MainApp;
import java.util.Vector;

public class LayerManager
{
    private Vector layers;
    private int winX;
    private int winY;
    private int winW;
    private int winH;
    
    public LayerManager() {
        this.layers = new Vector();
        this.setViewWindow(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    
    public void append(final Layer l) {
        if (l == null) {
            throw new NullPointerException();
        }
        this.layers.add(l);
    }
    
    public void insert(final Layer l, final int index) {
        if (l == null) {
            throw new NullPointerException();
        }
        final int currentIndex = this.layers.indexOf(l);
        if (currentIndex > -1) {
            this.layers.remove(currentIndex);
        }
        this.layers.insertElementAt(l, index);
    }
    
    public Layer getLayerAt(final int index) {
        return (Layer)this.layers.get(index);
    }
    
    public int getSize() {
        return this.layers.size();
    }
    
    public void remove(final Layer l) {
        if (l == null) {
            throw new NullPointerException();
        }
        this.layers.remove(l);
    }
    
    public void setViewWindow(final int x, final int y, final int width, final int height) {
        this.winX = x;
        this.winY = y;
        this.winW = width;
        this.winH = height;
        if (MainApp.verbose) {
            System.out.println("LayerManager.setViewWindow() x=" + x + " y=" + y + " w=" + width + " h=" + height);
        }
    }
    
    public void paint(final Graphics g, final int x, final int y) {
        final int trX = g.getTranslateX();
        final int trY = g.getTranslateY();
        final int size = this.layers.size();
        if (MainApp.verbose) {
            System.out.println("Pstros:LayerManager.paint() x=" + x + " y=" + y + " wX=" + this.winX + " wY=" + this.winY + " wW=" + this.winW + " wH=" + this.winH + " tr X=" + trX + " Y=" + trY + " layer count=" + size);
        }
        if (size < 1) {
            return;
        }
        final int origClipX = g.getClipX();
        final int origClipY = g.getClipY();
        final int origClipW = g.getClipWidth();
        final int origClipH = g.getClipHeight();
        final int boundX = this.winX + this.winW;
        final int boundY = this.winY + this.winH;
        g.setClip(x, y, this.winW, this.winH);
        g.translate(-this.winX + x, -this.winY + y);
        for (int i = size - 1; i > -1; --i) {
            final Layer l = (Layer)this.layers.get(i);
            l.paint(g);
        }
        g.translate(this.winX - x, this.winY - y);
        g.setClip(origClipX, origClipY, origClipW, origClipH);
    }
}
