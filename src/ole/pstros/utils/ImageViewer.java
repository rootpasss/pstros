// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import java.awt.event.AdjustmentEvent;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import ole.pstros.ConfigData;
import java.awt.event.WindowEvent;
import java.awt.Dimension;
import ole.pstros.IEmuBridge;
import java.awt.FontMetrics;
import java.awt.image.ImageObserver;
import ole.pstros.EmuCanvas;
import java.awt.Graphics;
import javax.microedition.lcdui.Image;
import java.awt.ScrollPaneAdjustable;
import java.awt.Container;
import java.awt.Frame;
import java.util.Vector;
import java.awt.Color;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowListener;
import java.awt.Canvas;

public class ImageViewer extends Canvas implements WindowListener, MouseListener, AdjustmentListener
{
    private static final boolean SHOW_TRANSFORM = false;
    private static Color COLOR_WHITE;
    private static Color COLOR_BLACK;
    private static Color COLOR_GRAY;
    private static int imgCounter;
    private static final int[] tmpData;
    private Vector images;
    private Frame parent;
    private Container container;
    private ScrollPaneAdjustable vScroll;
    private int mouseX;
    private int mouseY;
    private int infoX;
    private int infoY;
    private int infoH;
    private int fontAscent;
    private int offsetY;
    private Image selectedImage;
    
    static {
        ImageViewer.COLOR_WHITE = new Color(16777215);
        ImageViewer.COLOR_BLACK = new Color(0);
        ImageViewer.COLOR_GRAY = new Color(10461087);
        tmpData = new int[1];
    }
    
    public ImageViewer(final Vector images, final Frame parent, final Container container) {
        this.setBackground(ImageViewer.COLOR_GRAY);
        this.images = images;
        this.parent = parent;
        this.container = container;
        this.addMouseListener(this);
        this.infoY = -1;
    }
    
    public void paint(final Graphics g) {
        final int size = this.images.size();
        final int w = this.getWidth() + 16;
        int posY = 2;
        final int imgCount = 1;
        if (this.infoH < 1) {
            final FontMetrics fm = g.getFontMetrics();
            this.infoH = fm.getHeight() + 5;
            this.fontAscent = fm.getAscent() + 2;
        }
        final IEmuBridge bridge = EmuCanvas.instance.lcduiBridge;
        for (int i = 0; i < size; ++i) {
            final Image image = (Image)this.images.get(i);
            final int h = image.getHeight();
            g.setColor(ImageViewer.COLOR_GRAY);
            g.fillRect(0, posY - 2, w, h + 5);
            g.setColor(ImageViewer.COLOR_BLACK);
            final int posX = 5;
            for (int j = 0; j < imgCount; ++j) {
                ImageViewer.tmpData[0] = j;
                final java.awt.Image img = (java.awt.Image)bridge.handleEvent(20, image, ImageViewer.tmpData);
                if (img != null) {
                    g.drawImage(img, posX, posY, null);
                }
            }
            posY += h + 2;
            g.setColor(ImageViewer.COLOR_BLACK);
            g.drawLine(2, posY, w - 20, posY);
            posY += 3;
        }
        if (this.infoY > -1 && this.selectedImage != null) {
            final int imW = this.selectedImage.getWidth();
            final int imH = this.selectedImage.getHeight();
            g.setColor(ImageViewer.COLOR_WHITE);
            g.fillRect(this.infoX, this.infoY, 128, this.infoH);
            g.setColor(ImageViewer.COLOR_BLACK);
            g.drawString(String.valueOf(Integer.toString(imW)) + "x" + Integer.toString(imH) + " [" + Integer.toString(imW * imH) + "]", this.infoX + 4, this.infoY + this.fontAscent);
            g.drawRect(this.infoX, this.infoY, 127, this.infoH - 1);
        }
    }
    
    public void setVerticalScroll(final ScrollPaneAdjustable spa) {
        (this.vScroll = spa).addAdjustmentListener(this);
    }
    
    public Dimension getSize() {
        if (this.images == null) {
            return super.getSize();
        }
        final int size = this.images.size();
        int posY = 0;
        int posX = 0;
        posY += 2;
        for (int i = 0; i < size; ++i) {
            final Image image = (Image)this.images.get(i);
            final int h = image.getHeight();
            final int w = image.getWidth() + 10;
            posY += h + 5;
            if (w > posX) {
                posX = w;
            }
        }
        return new Dimension(posX, posY);
    }
    
    private Image getImageAt(final int y) {
        if (this.images == null) {
            return null;
        }
        Image image = null;
        final int size = this.images.size();
        int posY = 0;
        posY += 2;
        int h;
        for (int i = 0; i < size && posY < y; posY += h + 5, ++i) {
            image = (Image)this.images.get(i);
            h = image.getHeight();
        }
        return image;
    }
    
    public void windowActivated(final WindowEvent e) {
        this.requestFocus();
    }
    
    public void windowClosed(final WindowEvent e) {
        System.out.println("closed");
        this.parent.removeWindowListener(this);
        this.images.removeAllElements();
        this.selectedImage = null;
    }
    
    public void windowClosing(final WindowEvent e) {
        System.out.println("Closing!");
        ConfigData.imageViewerBounds = this.parent.getBounds();
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
    
    public void mouseEntered(final MouseEvent e) {
    }
    
    public void mouseExited(final MouseEvent e) {
    }
    
    public void mousePressed(final MouseEvent e) {
        final int button = e.getButton();
        final int modifiers = e.getModifiers();
        this.mouseY = e.getY();
        this.mouseX = e.getX();
        this.selectedImage = this.getImageAt(this.mouseY);
        if (this.selectedImage == null) {
            return;
        }
        if (button == 1) {
            this.infoX = this.mouseX;
            this.infoY = this.mouseY - this.infoH;
            if (this.infoY < 0) {
                this.infoY = 0;
            }
            this.repaint(this.infoX, this.infoY, 128, this.infoH);
        }
        else if (button == 3 && (modifiers & 0x2) != 0x0) {
            System.out.print("Save image...");
            ImageViewer.tmpData[0] = 0;
            final IEmuBridge bridge = EmuCanvas.instance.lcduiBridge;
            final java.awt.Image img = (java.awt.Image)bridge.handleEvent(20, this.selectedImage, ImageViewer.tmpData);
            BufferedImage bi = null;
            bi = ImageCreator.createBufferedImage(img);
            final String name = TgaWriter.saveImageTransparent("iw", bi, this.selectedImage.getWidth(), this.selectedImage.getHeight(), 0, ImageViewer.imgCounter);
            if (name != null) {
                ++ImageViewer.imgCounter;
                System.out.println("ok. " + name);
            }
            else {
                System.out.println("failed!");
            }
        }
    }
    
    public void mouseReleased(final MouseEvent e) {
        this.infoY = -1;
        this.repaint();
    }
    
    public void adjustmentValueChanged(final AdjustmentEvent e) {
        this.offsetY = e.getValue();
    }
}
