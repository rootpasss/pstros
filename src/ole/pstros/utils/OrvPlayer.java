// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import java.awt.event.WindowEvent;
import java.awt.Insets;
import java.awt.Component;
import java.awt.Color;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.Graphics;
import java.io.FileInputStream;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.awt.Frame;
import java.awt.event.WindowListener;
import java.awt.Canvas;

public class OrvPlayer extends Canvas implements DecoderListener, WindowListener
{
    private Frame playerFrame;
    private InputStream is;
    private BufferedImage bi;
    private int imageWidth;
    private int imageHeight;
    private int[] RGBdata;
    private int frameWidth;
    private int frameHeight;
    private int frameMillis;
    
    public OrvPlayer(final String name, final int w, final int h) {
        this.frameMillis = 40;
        this.frameWidth = w;
        this.frameHeight = h;
        try {
            if (name != null) {
                this.is = new FileInputStream(name);
            }
            else {
                this.is = "".getClass().getResourceAsStream("/capt.orv");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void open() {
        this.createFrame();
        Orv2Tga.process(this.is, null, this);
    }
    
    public void update(final Graphics g) {
        if (this.bi != null) {
            g.drawImage(this.bi, 0, 0, null);
        }
        else {
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
    }
    
    public void setFrameDelay(final int millis) {
    }
    
    public int decodeFrame(final byte[] rgb, final int w, final int h) {
        if (w != this.imageWidth || h != this.imageHeight) {
            this.bi = new BufferedImage(w, h, 1);
            this.RGBdata = new int[w * h];
            this.imageWidth = w;
            this.imageHeight = h;
        }
        this.convertData(rgb);
        this.bi.setRGB(0, 0, w, h, this.RGBdata, 0, w);
        this.repaint();
        return this.frameMillis;
    }
    
    private void convertData(final byte[] rgb) {
        int max = rgb.length / 3;
        if (max > this.RGBdata.length) {
            max = this.RGBdata.length;
        }
        int offset = 0;
        for (int i = 0; i < max; ++i) {
            int pixel = rgb[offset++] & 0xFF;
            pixel |= (rgb[offset++] & 0xFF) << 8;
            pixel |= (rgb[offset++] & 0xFF) << 16;
            this.RGBdata[i] = pixel;
        }
    }
    
    private void createFrame() {
        (this.playerFrame = new Frame()).setTitle("Pstros ORV player");
        this.playerFrame.addWindowListener(this);
        this.playerFrame.add(this);
        this.playerFrame.setVisible(true);
        final Insets insets = this.playerFrame.getInsets();
        this.frameWidth += insets.right + insets.left;
        this.frameHeight += insets.bottom + insets.top;
        this.playerFrame.setSize(this.frameWidth, this.frameHeight);
        this.playerFrame.doLayout();
    }
    
    public void windowActivated(final WindowEvent e) {
    }
    
    public void windowClosed(final WindowEvent e) {
    }
    
    public void windowClosing(final WindowEvent e) {
        Orv2Tga.decode = false;
        this.playerFrame.setVisible(false);
        this.playerFrame.dispose();
        this.playerFrame = null;
    }
    
    public void windowDeactivated(final WindowEvent e) {
    }
    
    public void windowDeiconified(final WindowEvent e) {
    }
    
    public void windowIconified(final WindowEvent e) {
    }
    
    public void windowOpened(final WindowEvent e) {
    }
}
