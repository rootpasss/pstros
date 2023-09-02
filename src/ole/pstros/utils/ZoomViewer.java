// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import ole.pstros.ConfigData;
import java.awt.image.BufferedImage;
import javax.microedition.lcdui.Display;
import java.awt.image.ImageObserver;
import java.awt.Graphics;
import java.awt.ScrollPaneAdjustable;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import java.awt.Canvas;

public class ZoomViewer extends Canvas implements WindowListener, KeyListener
{
    private static final int[] DIMENSIONS;
    private static final boolean SHOW_TRANSFORM = false;
    private static Color COLOR_GRAY;
    private Image image;
    private Frame parent;
    private Container container;
    private ScrollPaneAdjustable vScroll;
    private int mouseX;
    private int mouseY;
    private int spotX;
    private int spotY;
    private int spotColor;
    private int zoomFactor;
    private int cWidth;
    private int cHeight;
    
    static {
        DIMENSIONS = new int[] { 11, 15, 19 };
        ZoomViewer.COLOR_GRAY = new Color(10461087);
    }
    
    public ZoomViewer(final Frame parent, final Image image, final int zoom) {
        this.setBackground(ZoomViewer.COLOR_GRAY);
        this.parent = parent;
        this.addKeyListener(this);
        this.image = image;
        this.zoomFactor = zoom;
        this.spotX = -1;
        this.spotY = -1;
        this.setSize();
    }
    
    private void setSize() {
        this.cWidth = ZoomViewer.DIMENSIONS[this.zoomFactor - 1] * 20;
        this.cHeight = this.cWidth + 20;
    }
    
    public int getCurrentWidth() {
        return this.cWidth;
    }
    
    public int getCurrentHeight() {
        return this.cHeight;
    }
    
    public void paint(final Graphics g) {
        if (this.image == null) {
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            return;
        }
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, this.getWidth(), 20);
        g.setColor(Color.BLACK);
        g.drawString("x:" + this.mouseX + " y:" + this.mouseY, 0, 15);
        if (this.spotX > -1 && this.spotY > -1) {
            g.drawString(" dx:" + (this.mouseX - this.spotX) + " dy:" + (this.mouseY - this.spotY), 70, 15);
        }
        g.drawString("0x" + Integer.toHexString(this.spotColor), 150, 15);
        final int pixWidth = ZoomViewer.DIMENSIONS[this.zoomFactor - 1];
        final int mouseShift = pixWidth / 2;
        final int srcX1 = this.mouseX - mouseShift;
        final int srcX2 = this.mouseX - mouseShift + pixWidth;
        final int srcY1 = this.mouseY - mouseShift;
        final int srcY2 = this.mouseY - mouseShift + pixWidth;
        g.drawImage(this.image, 0, 20, this.cWidth, this.cHeight, srcX1, srcY1, srcX2, srcY2, this);
        if (srcX1 < 0) {
            g.setColor(Color.GRAY);
            g.fillRect(0, 20, -20 * srcX1, this.cHeight);
        }
        if (srcX2 > Display.WIDTH) {
            final int blockWidth = (srcX2 - Display.WIDTH) * 20;
            g.setColor(Color.GRAY);
            g.fillRect(this.cWidth - blockWidth, 20, blockWidth, this.cHeight);
        }
        if (srcY1 < 0) {
            g.setColor(Color.GRAY);
            g.fillRect(0, 20, this.cWidth, -20 * srcY1);
        }
        if (srcY2 > Display.HEIGHT) {
            final int blockHeight = (srcY2 - Display.HEIGHT) * 20;
            g.setColor(Color.GRAY);
            g.fillRect(0, this.cHeight - blockHeight, this.cWidth, blockHeight);
        }
        g.setColor(Color.RED);
        final int posX = mouseShift * 20;
        final int posY = posX + 20;
        g.drawRect(posX, posY, 19, 19);
        g.setColor(Color.BLACK);
        g.drawRect(posX - 1, posY - 1, 21, 21);
    }
    
    public void updateView(final int mx, final int my, final boolean force) {
        if (!force && mx == this.mouseX && my == this.mouseY) {
            return;
        }
        this.mouseX = mx;
        this.mouseY = my;
        this.repaint();
    }
    
    public void setSpot(final int mx, final int my) {
        this.spotX = mx;
        this.spotY = my;
        if (this.image instanceof BufferedImage) {
            this.spotColor = ((BufferedImage)this.image).getRGB(this.spotX, this.spotY);
            this.spotColor &= 0xFFFFFF;
        }
        this.repaint();
    }
    
    public void update(final Graphics g) {
        this.paint(g);
    }
    
    public void storeBounds() {
        ConfigData.zoomViewerBounds = this.parent.getBounds();
    }
    
    public void windowActivated(final WindowEvent e) {
        this.requestFocus();
    }
    
    public void windowClosed(final WindowEvent e) {
    }
    
    public void windowClosing(final WindowEvent e) {
        ConfigData.zoomViewerBounds = this.parent.getBounds();
        this.parent.dispose();
    }
    
    public void windowDeactivated(final WindowEvent e) {
    }
    
    public void windowDeiconified(final WindowEvent e) {
    }
    
    public void windowIconified(final WindowEvent e) {
    }
    
    public void windowOpened(final WindowEvent e) {
    }
    
    public void mouseClicked(final MouseEvent e) {
    }
    
    public void keyPressed(final KeyEvent e) {
        final int keyCode = e.getKeyCode();
        final char keyChar = e.getKeyChar();
        final int modifiers = e.getModifiers();
        if (keyCode >= 49 && keyCode <= 51 && (modifiers & 0x8) != 0x0) {
            ConfigData.zoomSize = keyCode - 48;
            if (ConfigData.zoomSize == this.zoomFactor) {
                return;
            }
            this.zoomFactor = ConfigData.zoomSize;
            this.setSize();
            if (this.parent != null) {
                int w = this.getCurrentWidth();
                int h = this.getCurrentHeight();
                final Insets insets = this.parent.getInsets();
                w += insets.right + insets.left;
                h += insets.bottom + insets.top;
                this.parent.setSize(w, h);
                this.parent.doLayout();
            }
        }
    }
    
    public void keyReleased(final KeyEvent e) {
    }
    
    public void keyTyped(final KeyEvent e) {
    }
}
