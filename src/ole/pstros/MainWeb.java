// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros;

import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.awt.Color;
import java.awt.Component;
import java.net.URLClassLoader;
import java.net.URL;
import java.util.StringTokenizer;
import ole.pstros.utils.ImageCreator;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.event.KeyListener;
import java.applet.Applet;

public class MainWeb extends Applet implements Runnable, EmuListener, KeyListener
{
    private static final boolean DEMO = false;
    private static MainWeb instance;
    private String jarName;
    private String midlet;
    private String message;
    private EmuExecutor executor;
    private Image image;
    private boolean emuStart;
    private int appletWidth;
    private int appletHeight;
    private String params;
    private boolean stopCalled;
    private int[] paintLock;
    private BufferedImage canvas;
    private Graphics cg;
    private static int[] bitmark;
    private Image watermark;
    
    static {
        MainWeb.bitmark = new int[] { -1, -1, 0, 0, 0, 0, 1070068735, 1070068220, 969814131, 969831308, 969801840, 969831296, 969801840, 965636992, 1065873520, 1057911036, 939552880, 969830428, 939552880, 969830428, 939552880, 969830428, 940339312, 969831196, 940564592, 969925624, 0, 0, 0, 0, -1, -1 };
    }
    
    public MainWeb() {
        this.message = null;
    }
    
    public String getAppletInfo() {
        return "Pstros: the J2ME execution environment. \n(c)2005-2007 Marek Olejnik";
    }
    
    public String[][] getParameterInfo() {
        final String[][] pinfo = { { "midlet", "string", "name of the MIDlet class" }, { "jarName", "string", "optional name of the jar package" } };
        return pinfo;
    }
    
    public void init() {
        int safetyCounter = 30;
        while (MainWeb.instance != null && safetyCounter-- >= 0) {
            try {
                Thread.sleep(100L);
            }
            catch (Exception ex) {}
        }
        if (safetyCounter < 0) {
            return;
        }
        this.paintLock = new int[1];
        MainWeb.instance = this;
        this.jarName = this.getParameter("jarName");
        this.midlet = this.getParameter("midlet");
        this.params = this.getParameter("parameters");
        System.out.println("Pstros: jarName=" + this.jarName);
        this.addKeyListener(this);
        this.appletWidth = this.getWidth();
        this.appletHeight = this.getHeight();
        this.canvas = ImageCreator.createBufferedImage(this.appletWidth, this.appletHeight, -1);
        this.cg = this.canvas.getGraphics();
    }
    
    public void start() {
        if (MainWeb.instance != this) {
            return;
        }
        this.stopCalled = false;
        new Thread(this).start();
    }
    
    public void stop() {
        if (MainWeb.instance != this) {
            return;
        }
        this.stopCalled = true;
        MainWeb.instance = null;
        if (this.executor != null) {
            this.cleanEnvironment(true);
        }
    }
    
    public void run() {
        try {
            Thread.currentThread().setPriority(1);
            this.emuStart = this.runEmu();
            this.requestFocus();
            while (MainWeb.instance != null) {
                try {
                    Thread.sleep(100L);
                }
                catch (Exception ex) {}
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private boolean runEmu() {
        (this.executor = MainApp.getEmuExecutorRo()).setEmuListener(this);
        this.executor.setParameter("-ro");
        this.executor.setParameter("-w" + this.appletWidth);
        this.executor.setParameter("-h" + this.appletHeight);
        this.executor.setParameter("-C" + this.midlet);
        if (this.params != null && this.params.length() > 1) {
            final StringTokenizer st = new StringTokenizer(this.params, " ");
            for (int size = st.countTokens(), i = 0; i < size; ++i) {
                this.executor.setParameter(st.nextToken());
            }
        }
        try {
            if (this.jarName != null) {
                final URL url = new URL(this.jarName);
                final URL[] urls = { url };
                final URLClassLoader cl = new URLClassLoader(urls);
                this.executor.setClassLoader(cl);
            }
            else {
                this.executor.setClassLoader(null);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.executor.setParentComponent(this);
        return this.executor.execute();
    }
    
    public void paint(final Graphics g) {
        if (MainWeb.instance != this) {
            g.setColor(Color.white);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(Color.black);
            if (this.message == null) {
                this.message = " Game is already runninng in other window.";
            }
            g.drawString(this.message, 0, 20);
            return;
        }
        if (this.executor != null) {
            g.drawImage(this.canvas, 0, 0, null);
        }
        else {
            g.setColor(Color.white);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(Color.black);
            if (this.message == null) {
                this.message = " Loading...";
            }
            g.drawString(this.message, 0, 20);
        }
    }
    
    private void cleanEnvironment(final boolean terminate) {
        if (this.executor != null) {
            final EmuExecutor old = this.executor;
            this.executor = null;
            old.setEmuListener(null);
            if (terminate) {
                old.terminate();
            }
            this.repaint();
        }
    }
    
    public void emuUpadate(final String event, final Object data) {
        if (event == "emuRefreshDisplay") {
            this.cg.drawImage((Image)data, 0, 0, null);
            this.repaint();
            Thread.yield();
        }
        else if (event == "emuClosed" && !this.stopCalled) {
            this.cleanEnvironment(true);
        }
    }
    
    public void keyPressed(final KeyEvent e) {
        if (this.executor != null) {
            this.executor.keyPressed(e);
        }
    }
    
    public void keyReleased(final KeyEvent e) {
        if (this.executor != null) {
            this.executor.keyReleased(e);
        }
    }
    
    public void keyTyped(final KeyEvent e) {
    }
    
    public void update(final Graphics g) {
        this.paint(g);
    }
    
    private void createWatermark() {
        if (this.watermark != null) {
            return;
        }
        final int w = 64;
        final int h = 16;
        final int[] water = new int[w * h];
        int offset = 0;
        for (int j = 0; j < h; ++j) {
            for (int i = 0; i < w; ++i) {
                final int pixel = MainWeb.bitmark[offset / 32];
                int bitmask = 31 - offset % 32;
                bitmask = 1 << bitmask;
                water[offset++] = (((pixel & bitmask) == 0x0) ? 2134917184 : 2146365166);
            }
        }
        final BufferedImage bi = new BufferedImage(w, h, 2);
        bi.setRGB(0, 0, w, h, water, 0, w);
        this.watermark = bi;
    }
}
