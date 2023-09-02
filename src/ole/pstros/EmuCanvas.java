// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros;

import java.awt.event.MouseEvent;
import ole.pstros.monitor.ClassManager;
import java.awt.Insets;
import java.util.Vector;
import java.awt.ScrollPaneAdjustable;
import java.awt.Container;
import ole.pstros.utils.ImageViewer;
import java.awt.ScrollPane;
import ole.pstros.reference.ImageReferenceManager;
import java.awt.Point;
import ole.pstros.rms.RmsManager;
import ole.pstros.utils.ParamWriter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.util.StringTokenizer;
import java.awt.geom.Rectangle2D;
import ole.pstros.utils.TgaWriter;
import ole.pstros.utils.StreamSaver;
import java.awt.Shape;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Screen;
import java.awt.image.ImageObserver;
import java.awt.geom.AffineTransform;
import javax.microedition.lcdui.Display;
import ole.pstros.utils.MonitorFrame;
import ole.pstros.utils.ZoomViewer;
import java.awt.Frame;
import java.awt.font.FontRenderContext;
import java.awt.Font;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Image;
import javax.microedition.lcdui.Displayable;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowListener;
import java.awt.event.KeyListener;
import java.awt.Canvas;

public class EmuCanvas extends Canvas implements KeyListener, WindowListener, MouseListener, MouseMotionListener
{
    private static Color COLOR_BLACK;
    private static Color COLOR_WHITE;
    private static Color COLOR_BLUE;
    private static Color COLOR_LIGHT_GRAY;
    private static Color COLOR_GRAY;
    public static final int MASK_ALT = 8;
    public static EmuCanvas instance;
    private static Rectangle tmpClipRect;
    private Displayable displayable;
    private Image emuImage;
    private BufferedImage scaledImage;
    private Graphics emuGraph;
    private Graphics scaledGraph;
    private javax.microedition.lcdui.Graphics deviceGraph;
    private static int emuFrameCounter;
    private int[] scaledSrc;
    private int[] scaledDst;
    private int lastScale;
    private boolean capture;
    private boolean screenShot;
    private int scrShotIndex;
    private int captureWidth;
    private int captureHeight;
    private int captureOffsetY;
    private boolean painting;
    private boolean emuPaintRequest;
    private boolean hideNotifyTest;
    private boolean debugKeys;
    private boolean distanceMode;
    private int mouseX;
    private int mouseY;
    private int repaintX;
    private int repaintY;
    private int repaintW;
    private int repaintH;
    private Component parentComponent;
    private Font consoleFont;
    private FontRenderContext frc;
    public int[] paintLock;
    private int[] pauseLock;
    private int[] tmpInt;
    private boolean showImageView;
    private boolean showZoom;
    private Frame imageViewerFrame;
    private Frame zoomFrame;
    private ZoomViewer zoomViewer;
    private MonitorFrame classMonitorFrame;
    private long lastTime;
    private int fps;
    private boolean paused;
    public IEmuBridge lcduiBridge;
    private int[] frameBackupRGB;
    private boolean restoreFrame;
    
    static {
        EmuCanvas.COLOR_BLACK = new Color(0);
        EmuCanvas.COLOR_WHITE = new Color(16777215);
        EmuCanvas.COLOR_BLUE = new Color(255);
        EmuCanvas.COLOR_LIGHT_GRAY = new Color(12303291);
        EmuCanvas.COLOR_GRAY = new Color(9474192);
        EmuCanvas.tmpClipRect = new Rectangle();
    }
    
    public static EmuCanvas getInstance() {
        if (EmuCanvas.instance == null) {
            EmuCanvas.instance = new EmuCanvas();
        }
        return EmuCanvas.instance;
    }
    
    private EmuCanvas() {
        this.debugKeys = false;
        this.distanceMode = false;
        this.paintLock = new int[1];
        this.pauseLock = new int[1];
        this.tmpInt = new int[4];
        this.showImageView = false;
        this.showZoom = false;
        this.setBackground(EmuCanvas.COLOR_WHITE);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }
    
    public void setParentComponent(final Component c) {
        this.parentComponent = c;
    }
    
    public void backupImage() {
        if (!(this.emuImage instanceof BufferedImage)) {
            return;
        }
        final BufferedImage img = (BufferedImage)this.emuImage;
        final int w = img.getWidth();
        final int h = img.getHeight();
        img.getRGB(0, 0, w, h, this.frameBackupRGB = new int[w * h], 0, w);
    }
    
    public void restoreImage() {
        this.restoreFrame = true;
    }
    
    public javax.microedition.lcdui.Graphics createGraphics() {
        Image img = this.createImage(Display.WIDTH, Display.HEIGHT);
        if (img == null && this.parentComponent != null) {
            img = this.parentComponent.createImage(Display.WIDTH, Display.HEIGHT);
        }
        if (img == null) {
            img = new BufferedImage(Display.WIDTH, Display.HEIGHT, 2);
        }
        final Graphics eg = img.getGraphics();
        eg.setColor(EmuCanvas.COLOR_BLACK);
        eg.fillRect(0, 0, Display.WIDTH, Display.HEIGHT);
        final javax.microedition.lcdui.Graphics dg = new javax.microedition.lcdui.Graphics();
        dg.emuSetGraphics(eg);
        dg.emuSetGraphicsImage((BufferedImage)img);
        dg.setClip(0, 0, Display.WIDTH, Display.HEIGHT);
        return dg;
    }
    
    public void init() {
        this.emuImage = null;
        this.emuImage = this.createImage(Display.WIDTH, Display.HEIGHT);
        if (this.emuImage == null && this.parentComponent != null) {
            this.emuImage = this.parentComponent.createImage(Display.WIDTH, Display.HEIGHT);
        }
        if (this.emuImage == null) {
            this.emuImage = new BufferedImage(Display.WIDTH, Display.HEIGHT, 2);
        }
        this.initScaledGraphics();
        (this.emuGraph = this.emuImage.getGraphics()).setColor(EmuCanvas.COLOR_BLACK);
        this.emuGraph.fillRect(0, 0, Display.WIDTH, Display.HEIGHT);
        (this.deviceGraph = new javax.microedition.lcdui.Graphics()).emuSetGraphics(this.emuGraph);
        this.deviceGraph.emuSetGraphicsImage((BufferedImage)this.emuImage);
        this.deviceGraph.setClip(0, 0, Display.WIDTH, Display.HEIGHT);
        if (ConfigData.bottomConsoleHeight > 0) {
            this.consoleFont = new Font(null, 0, ConfigData.bottomConsoleHeight - 4);
        }
        else {
            this.consoleFont = new Font(null, 0, 12);
        }
        this.frc = new FontRenderContext(null, false, false);
        this.mouseX = -1;
        this.mouseY = -1;
    }
    
    public javax.microedition.lcdui.Graphics getDeviceGraphics() {
        return this.deviceGraph;
    }
    
    public BufferedImage getEmuImage() {
        return (BufferedImage)this.emuImage;
    }
    
    public void setEmuPaintRequest(final int x, final int y, final int w, final int h) {
        if (!this.emuPaintRequest) {
            this.repaintX = x;
            this.repaintY = y;
            this.repaintW = w;
            this.repaintH = h;
        }
        else {
            int repaintX2 = this.repaintX + this.repaintW;
            int repaintY2 = this.repaintY + this.repaintH;
            final int x2 = x + w;
            final int y2 = y + w;
            if (x < this.repaintX) {
                this.repaintX = x;
            }
            if (y < this.repaintY) {
                this.repaintY = y;
            }
            if (repaintX2 < x2) {
                repaintX2 = x2;
            }
            if (repaintY2 < y2) {
                repaintY2 = y2;
            }
            this.repaintW = repaintX2 - this.repaintX;
            this.repaintH = repaintY2 - this.repaintY;
        }
        this.emuPaintRequest = true;
    }
    
    public void setBridge(final IEmuBridge bridge) {
        this.lcduiBridge = bridge;
    }
    
    public void setContent(final Displayable d, final IEmuBridge bridge) {
        if (this.displayable instanceof javax.microedition.lcdui.Canvas) {
            this.lcduiBridge.handleEvent(2, this.displayable);
        }
        this.displayable = d;
        this.lcduiBridge = bridge;
        if (this.displayable != null) {
            this.lcduiBridge.handleEvent(7, this.displayable, this);
        }
        this.deviceGraph.translate(-this.deviceGraph.getTranslateX(), -this.deviceGraph.getTranslateY());
        this.deviceGraph.setClip(0, 0, 1000, 1000);
        if (this.displayable instanceof javax.microedition.lcdui.Canvas) {
            synchronized (this.displayable) {
                this.lcduiBridge.handleEvent(1, this.displayable);
            }
            // monitorexit(this.displayable)
        }
        if (MainApp.verbose) {
            System.out.println("EmuCanvas.setContent:" + d);
        }
        if (ConfigData.slaveMode) {
            if (d != null) {
                this.setEmuPaintRequest(0, 0, d.getWidth(), d.getHeight());
                this.update();
            }
        }
        else if (d != null) {
            this.setEmuPaintRequest(0, 0, d.getWidth(), d.getHeight());
            this.repaint();
        }
    }
    
    public void update(Graphics g) {
        if (MainApp.verbose) {
            System.out.println("EmuCanvas: update:" + g);
        }
        if (g == null) {
            g = this.getGraphics();
        }
        if (this.hideNotifyTest) {
            g.setColor(Color.CYAN);
            g.setClip(0, 0, 1000, 1000);
            g.fillRect(0, 0, 1000, 1000);
            g.setColor(Color.RED);
            for (int i = 0; i < 100; ++i) {
                g.fillRect(0, i * 20, 1000, 10);
            }
        }
        else {
            this.paintContent(g);
        }
    }
    
    public void update() {
        this.paintContent(null);
    }
    
    public int getFps() {
        return this.fps;
    }
    
    public void paint() {
        if (MainApp.verbose) {
            System.out.println("EmuCanvas: paint()...");
        }
        final Graphics g = this.getGraphics();
        this.paintContent(g);
        g.dispose();
    }
    
    public boolean paintRequestValid() {
        return this.emuPaintRequest;
    }
    
    public void paint(final Graphics g) {
        if (MainApp.verbose) {
            System.out.println("EmuCanvas: paint(g)...");
        }
        if (ConfigData.skinImage != null) {
            g.drawImage(ConfigData.skinImage, 0, 0, null);
        }
        this.paintContent(g);
    }
    
    private void paintContent(final Graphics g) {
        synchronized (this.paintLock) {
            this.painting = true;
            if (this.emuGraph == null) {
                if (MainApp.verbose) {
                    System.out.println("EmuCanvas: paintContent() emuGraph is null.");
                }
                // monitorexit(this.paintLock)
                return;
            }
            if (this.restoreFrame) {
                if (this.frameBackupRGB != null) {
                    final int w = this.emuImage.getWidth(null);
                    final int h = this.emuImage.getHeight(null);
                    this.deviceGraph.drawRGB(this.frameBackupRGB, 0, w, 0, 0, w, h, true);
                    this.frameBackupRGB = null;
                }
                this.restoreFrame = false;
            }
            if (this.displayable instanceof Screen) {
                this.paintScreen(g, (Screen)this.displayable);
            }
            else if (this.displayable instanceof javax.microedition.lcdui.Canvas) {
                this.paintCanvas(g, (javax.microedition.lcdui.Canvas)this.displayable);
            }
            else if (MainApp.verbose) {
                System.out.println("Pstros : paint unknown displayable! " + this.displayable);
            }
            if (this.distanceMode && this.zoomViewer != null && this.zoomViewer.isShowing()) {
                this.zoomViewer.repaint();
            }
            this.painting = false;
        }
        // monitorexit(this.paintLock)
    }
    
    public boolean callRunner() {
        return !this.painting && Display.emuRunSerialRunner();
    }
    
    public boolean isPainting() {
        return this.painting;
    }
    
    private void togglePause() {
        synchronized (this.pauseLock) {
            this.paused = !this.paused;
            final boolean stop = this.paused;
        }
        // monitorexit(this.pauseLock)
    }
    
    public void flushGraphics(Image img) {
        if (img == null) {
            img = this.emuImage;
        }
        if (ConfigData.slaveMode) {
            this.paintCanvasContent(null, this.displayable, img, true);
        }
        else {
            final Graphics g = this.getGraphics();
            this.paintCanvasContent(g, this.displayable, img, true);
            if (g != null) {
                g.dispose();
                this.updateFps();
            }
        }
        if (ConfigData.drawWait > 0) {
            try {
                Thread.sleep(ConfigData.drawWait);
            }
            catch (Exception ex) {}
        }
    }
    
    public void checkPause() {
        if (ConfigData.slaveMode) {
            return;
        }
        while (true) {
            boolean stop = false;
            Label_0064: {
                synchronized (this.pauseLock) {
                    stop = this.paused;
                    // monitorexit(this.pauseLock)
                    //break Label_0064;
                }
                synchronized (this.pauseLock) {
                    stop = this.paused;
                }
                // monitorexit(this.pauseLock)
                try {
                    Thread.sleep(5L);
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
            if (!stop) {
                return;
            }
            continue;
        }
    }
    
    private void paintScreen(final Graphics g, final Screen screen) {
        this.lcduiBridge.handleEvent(8, this.emuGraph);
        if (g != null) {
            final int x = ConfigData.skinScreenX;
            final int y = ConfigData.skinScreenY;
            final int w = Display.WIDTH * ConfigData.scale;
            final int h = Display.HEIGHT * ConfigData.scale;
            g.drawImage(this.emuImage, x, y, x + w, y + h, 0, 0, Display.WIDTH, Display.HEIGHT, this);
        }
        else {
            MainApp.getInstance().setEvent("emuRefreshDisplay", this.emuImage);
        }
    }
    
    private void updateFps() {
        ++EmuCanvas.emuFrameCounter;
        if (EmuCanvas.emuFrameCounter % 40 == 0) {
            final long now = System.currentTimeMillis();
            if (this.lastTime > 0L) {
                int divisor = (int)(now - this.lastTime);
                if (divisor == 0) {
                    divisor = 5;
                    try {
                        Thread.sleep(5L);
                    }
                    catch (Exception ex) {}
                }
                this.fps = 40000 / divisor;
            }
            this.lastTime = now;
        }
    }
    
    private void paintCanvas(final Graphics g, final javax.microedition.lcdui.Canvas canvas) {
        final boolean wasEmuRequested = this.emuPaintRequest;
        if (this.emuPaintRequest) {
            this.emuPaintRequest = false;
            try {
                if (MainApp.verbose) {
                    System.out.println("-------------Pstros:EmuCanvas.paint ------ frame=" + EmuCanvas.emuFrameCounter);
                }
                this.updateFps();
                this.deviceGraph.setColor(0);
                this.deviceGraph.translate(-this.deviceGraph.getTranslateX(), -this.deviceGraph.getTranslateY());
                this.deviceGraph.setClip(this.repaintX, this.repaintY, this.repaintW, this.repaintH);
                this.deviceGraph.setFont(javax.microedition.lcdui.Font.getDefaultFont());
                this.deviceGraph.setStrokeStyle(0);
                this.lcduiBridge.handleEvent(9, canvas, this);
                this.lcduiBridge.handleEvent(8, this.deviceGraph, this);
                if (ConfigData.drawWait > 0) {
                    try {
                        Thread.sleep(ConfigData.drawWait);
                    }
                    catch (Exception e) {
                        System.out.println("thread.sleep interrupted! " + e);
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Pstros: unhandled exception in J2ME code! Error:" + e);
                e.printStackTrace();
            }
        }
        this.paintCanvasContent(g, canvas, this.emuImage, wasEmuRequested);
    }
    
    private void paintCanvasContent(final Graphics g, final Displayable disp, final Image img, final boolean wasEmuRequest) {
        int topH = ConfigData.topConsoleHeight;
        int botH = ConfigData.bottomConsoleHeight;
        final int x = ConfigData.skinScreenX;
        final int y = ConfigData.skinScreenY;
        final int w = Display.WIDTH * ConfigData.scale;
        int h = (Display.HEIGHT - topH) * ConfigData.scale;
        if (disp == null) {
            return;
        }
        if (this.lcduiBridge.handleEvent(10, disp) != null) {
            topH = 0;
            botH = 0;
            h = Display.HEIGHT * ConfigData.scale;
        }
        Color currColor = null;
        final int ths = topH * ConfigData.scale;
        if (topH > 0 && g != null) {
            currColor = this.emuGraph.getColor();
            this.emuGraph.getClipBounds(EmuCanvas.tmpClipRect);
            g.setColor(EmuCanvas.COLOR_LIGHT_GRAY);
            g.setClip(x, y, w, ths);
            g.fillRect(x, y, w, ths);
        }
        if (botH > 0) {
            if (currColor == null) {
                currColor = this.emuGraph.getColor();
                this.emuGraph.getClipBounds(EmuCanvas.tmpClipRect);
            }
            this.emuGraph.setClip(0, 0, Display.WIDTH, Display.HEIGHT);
            this.emuGraph.setColor(EmuCanvas.COLOR_LIGHT_GRAY);
            this.emuGraph.fillRect(0, Display.HEIGHT - botH - topH, Display.WIDTH, botH);
            final Object lc = this.lcduiBridge.handleEvent(11, disp);
            final Object rc = this.lcduiBridge.handleEvent(12, disp);
            if (lc != null) {
                final Font currentFont = this.emuGraph.getFont();
                this.emuGraph.setColor(Color.BLACK);
                this.emuGraph.setFont(this.consoleFont);
                this.emuGraph.drawString(((Command)lc).getLabel(), 0, Display.HEIGHT - 4 - topH);
                this.emuGraph.setFont(currentFont);
            }
            if (rc != null) {
                final String label = ((Command)rc).getLabel();
                final Rectangle2D rect = this.consoleFont.getStringBounds(label, this.frc);
                final Font currentFont2 = this.emuGraph.getFont();
                this.emuGraph.setColor(Color.BLACK);
                this.emuGraph.setFont(this.consoleFont);
                this.emuGraph.drawString(label, Display.WIDTH - (int)rect.getWidth() - 1, Display.HEIGHT - 4 - topH);
                this.emuGraph.setFont(currentFont2);
            }
        }
        if (currColor != null) {
            this.emuGraph.setClip(EmuCanvas.tmpClipRect);
            this.emuGraph.setColor(currColor);
        }
        if (g != null) {
            if (wasEmuRequest) {
                g.setClip(x + this.repaintX * ConfigData.scale, y + this.repaintY * ConfigData.scale, this.repaintW * ConfigData.scale, this.repaintH * ConfigData.scale);
            }
            else {
                g.setClip(x, y, 1024, 1024);
            }
            if ((!ConfigData.externalScaler && ConfigData.displayGamma % 100 == 0) || (ConfigData.scale == 1 && ConfigData.displayGamma % 100 == 0)) {
                g.drawImage(img, x, y + ths, x + w, y + ths + h, 0, 0, Display.WIDTH, Display.HEIGHT - topH, this);
            }
            else {
                this.rescale(false, img);
                g.drawImage(this.scaledImage, x, y + ths, this);
            }
        }
        else if (ConfigData.slaveMode) {
            MainApp.getInstance().setEvent("emuRefreshDisplay", img);
        }
        if (this.capture) {
            StreamSaver.saveImageData((BufferedImage)img, this.captureWidth, this.captureHeight, this.captureOffsetY);
        }
        if (this.screenShot) {
            this.screenShot = false;
            TgaWriter.saveImage("scrShot", (BufferedImage)img, this.captureWidth, this.captureHeight, this.captureOffsetY, this.scrShotIndex++);
        }
    }
    
    private void setCaptureSize() {
        this.captureWidth = Display.WIDTH;
        this.captureHeight = Display.HEIGHT;
        if (ConfigData.captureHeight > 0 && this.captureHeight >= ConfigData.captureHeight) {
            this.captureOffsetY = (this.captureHeight - ConfigData.captureHeight) / 2;
            if (ConfigData.captureOffsetY > 0 && this.captureHeight >= ConfigData.captureOffsetY + ConfigData.captureHeight) {
                this.captureOffsetY = ConfigData.captureOffsetY;
            }
            this.captureHeight = ConfigData.captureHeight;
        }
    }
    
    public void keyAction(final int actionType, final int key) {
        if (this.displayable != null) {
            this.tmpInt[0] = key;
            this.tmpInt[1] = 32;
            this.tmpInt[2] = 0;
            this.tmpInt[3] = actionType;
            this.lcduiBridge.handleEvent(13, this.displayable, this.tmpInt);
        }
    }
    
    private void initScaledGraphics() {
        if (!ConfigData.slaveMode && (ConfigData.externalScaler || ConfigData.displayGamma % 100 != 0)) {
            this.scaledImage = new BufferedImage(Display.WIDTH * ConfigData.scale, Display.HEIGHT * ConfigData.scale, 2);
            this.scaledGraph = this.scaledImage.getGraphics();
        }
    }
    
    private void runCallEvent(final javax.microedition.lcdui.Canvas gCanvas) {
        if (ConfigData.callEvent == null) {
            this.lcduiBridge.handleEvent(2, gCanvas);
            this.hideNotifyTest = true;
            final Graphics g = this.getGraphics();
            this.update(g);
            g.dispose();
            try {
                Thread.sleep(2000L);
            }
            catch (Exception ex) {}
            Thread.yield();
            this.hideNotifyTest = false;
            this.lcduiBridge.handleEvent(1, gCanvas);
        }
        else {
            if (MainApp.verbose) {
                System.out.println("Pstros: device.callEvent=" + ConfigData.callEvent);
            }
            final StringTokenizer st = new StringTokenizer(ConfigData.callEvent, ",; ");
            for (int size = st.countTokens(), i = 0; i < size; ++i) {
                final String command = st.nextToken();
                if (MainApp.verbose) {
                    System.out.println("pstros: event command=" + command);
                }
                if (command.equals("pauseApp") || command.equals("pa")) {
                    if (MainApp.midlet != null) {
                        MainApp.midletBridge.handleEvent(5, MainApp.midlet);
                    }
                }
                else if (command.equals("startApp") || command.equals("sa")) {
                    if (MainApp.midlet != null) {
                        MainApp.midletBridge.handleEvent(6, MainApp.midlet);
                    }
                }
                else if (command.equals("hideNotify") || command.equals("hn")) {
                    this.lcduiBridge.handleEvent(2, gCanvas);
                }
                else if (command.equals("showNotify") || command.equals("sn")) {
                    this.lcduiBridge.handleEvent(1, gCanvas);
                }
                else if (command.equals("screenOff") || command.equals("s0")) {
                    this.hideNotifyTest = true;
                    final Graphics g2 = this.getGraphics();
                    this.update(g2);
                    g2.dispose();
                }
                else if (command.equals("screenOn") || command.equals("s1")) {
                    this.hideNotifyTest = false;
                }
                else if (command.startsWith("wait") || command.startsWith("w")) {
                    String number;
                    if (command.startsWith("wait")) {
                        number = command.substring(4);
                    }
                    else {
                        number = command.substring(1);
                    }
                    if (number == null || number.length() < 1) {
                        number = "1000";
                    }
                    final int value = Integer.parseInt(number);
                    try {
                        Thread.sleep(value);
                    }
                    catch (Exception ex2) {}
                }
            }
        }
    }
    
    public void keyPressed(final KeyEvent e) {
        final int keyCode = e.getKeyCode();
        final char keyChar = e.getKeyChar();
        final int modifiers = e.getModifiers();
        if (this.debugKeys) {
            System.out.println("EmuCanvas.keyPressed key=" + keyCode + " modifiers=" + Integer.toHexString(modifiers) + " diplayable=" + this.displayable);
        }
        if (this.displayable != null) {
            this.tmpInt[0] = keyCode;
            this.tmpInt[1] = keyChar;
            this.tmpInt[2] = modifiers;
            this.tmpInt[3] = 0;
            this.lcduiBridge.handleEvent(13, this.displayable, this.tmpInt);
        }
        if (keyCode >= 49 && keyCode <= 51 && (modifiers & 0x8) != 0x0) {
            ConfigData.scale = keyCode - 48;
            Display.emuResizeEmuFrame();
        }
        else if (keyCode == 20) {
            this.debugKeys = !this.debugKeys;
        }
        else if (keyCode == 77 && (modifiers & 0x8) != 0x0) {
            this.showClassMonitor();
        }
        else if (keyCode == 75 && (modifiers & 0x8) != 0x0) {
            ConfigData.numKeySwap = !ConfigData.numKeySwap;
            if (this.displayable instanceof javax.microedition.lcdui.Canvas) {
                this.lcduiBridge.handleEvent(22, this.displayable, this.tmpInt);
            }
        }
        else if (keyCode != 86 || (modifiers & 0x8) == 0x0) {
            if (keyCode == 73 && (modifiers & 0x8) != 0x0) {
                this.showImageInfo();
            }
            else if (keyCode == 71 && (modifiers & 0x8) != 0x0) {
                MainApp.forceGC();
            }
            else if (keyCode == 68 && (modifiers & 0x8) != 0x0) {
                this.distanceMode = true;
                this.showZoomFrame();
            }
            else if (keyCode == Display.keyRotate) {
                this.rotateDisplay();
            }
            else if (keyCode == Display.keyPause) {
                this.togglePause();
            }
            else if (keyCode == Display.keyScreenShot) {
                this.screenShot = true;
                this.setCaptureSize();
                System.out.println("Screen shot taken!  offsetY=" + this.captureOffsetY + " height=" + this.captureHeight);
            }
            else if (keyCode == Display.keyCaptureVideo) {
                if (this.capture) {
                    this.capture = false;
                    StreamSaver.close();
                    System.out.println("Capture stop!");
                }
                else {
                    this.setCaptureSize();
                    StreamSaver.setFileName(String.valueOf(ConfigData.captureFile) + ".orv");
                    try {
                        if ((modifiers & 0x8) == 0x0) {
                            StreamSaver.init(this.captureWidth, this.captureHeight, 1);
                        }
                        else {
                            StreamSaver.init(this.captureWidth, this.captureHeight, 0);
                        }
                        System.out.println("Capture start!  offsetY=" + this.captureOffsetY + " height=" + this.captureHeight);
                        this.capture = true;
                    }
                    catch (Exception exc) {
                        System.out.println("Capture init error:" + exc);
                    }
                }
            }
            else if (keyCode == Display.keyShowHideNotify && this.displayable instanceof javax.microedition.lcdui.Canvas) {
                final javax.microedition.lcdui.Canvas gCanvas = (javax.microedition.lcdui.Canvas)this.displayable;
                this.runCallEvent(gCanvas);
            }
        }
        if (keyCode == 8) {
            this.setEmuPaintRequest(this.repaintX, this.repaintY, this.repaintW, this.repaintH);
            this.repaint();
        }
        else if (keyCode == 9) {
            this.callRunner();
        }
        else if (keyCode == 27) {
            Display.emuDestroyFrame();
        }
    }
    
    public void keyReleased(final KeyEvent e) {
        if (this.displayable != null) {
            this.tmpInt[0] = e.getKeyCode();
            this.tmpInt[1] = e.getKeyChar();
            this.tmpInt[2] = e.getModifiers();
            this.tmpInt[3] = 1;
            this.lcduiBridge.handleEvent(13, this.displayable, this.tmpInt);
        }
    }
    
    public void keyTyped(final KeyEvent e) {
    }
    
    public void windowActivated(final WindowEvent e) {
        this.requestFocus();
    }
    
    public void windowClosed(final WindowEvent e) {
    }
    
    public void windowClosing(final WindowEvent e) {
        if (ConfigData.saveParams) {
            if (this.classMonitorFrame != null && this.classMonitorFrame.isVisible()) {
                this.classMonitorFrame.setVisible(false);
                this.classMonitorFrame.dispose();
                this.classMonitorFrame = null;
            }
            if (this.imageViewerFrame != null && this.imageViewerFrame.isVisible()) {
                ConfigData.imageViewerBounds = this.imageViewerFrame.getBounds();
                this.imageViewerFrame.setVisible(false);
                this.imageViewerFrame.dispose();
                this.imageViewerFrame = null;
            }
            final Point point = e.getWindow().getLocation();
            ConfigData.windowPositionX = point.x;
            ConfigData.windowPositionY = point.y;
            ParamWriter.saveParams();
        }
        if (MainApp.midlet != null) {
            MainApp.midletBridge.handleEvent(4, MainApp.midlet);
        }
        //RmsManager.getInstance().saveData();
        this.setContent(null, null);
        this.emuGraph.dispose();
        try {
            System.exit(0);
        }
        catch (Exception ex) {}
    }
    
    public void windowDeactivated(final WindowEvent e) {
    }
    
    public void windowDeiconified(final WindowEvent e) {
    }
    
    public void windowIconified(final WindowEvent e) {
    }
    
    public void windowOpened(final WindowEvent e) {
        if (ConfigData.slaveMode) {
            return;
        }
        if (this.showImageView) {
            this.showImageView = false;
            this.showImageInfo();
        }
        if (this.showZoom) {
            this.showZoom = false;
            this.showZoomFrame();
        }
    }
    
    private void rotateDisplay() {
        if (ConfigData.slaveMode) {
            return;
        }
        if (this.imageViewerFrame != null && this.imageViewerFrame.isShowing()) {
            this.showImageView = true;
            this.imageViewerFrame.setVisible(false);
            this.imageViewerFrame.dispose();
            this.imageViewerFrame = null;
        }
        if (this.zoomFrame != null && this.zoomFrame.isShowing()) {
            this.showZoom = true;
            this.zoomFrame.setVisible(false);
            this.zoomFrame.dispose();
            this.zoomFrame = null;
        }
        if (this.classMonitorFrame != null && this.classMonitorFrame.isShowing()) {
            this.classMonitorFrame.setVisible(false);
            this.classMonitorFrame.dispose();
            this.classMonitorFrame = null;
        }
        Display.emuRotateDisplay();
        if (this.displayable instanceof javax.microedition.lcdui.Canvas) {
            this.tmpInt[0] = Display.WIDTH;
            this.tmpInt[1] = Display.HEIGHT;
            this.lcduiBridge.handleEvent(23, this.displayable, this.tmpInt);
        }
    }
    
    private void showImageInfo() {
        if (ConfigData.slaveMode) {
            return;
        }
        if (this.imageViewerFrame != null && this.imageViewerFrame.isShowing()) {
            System.out.println("Pstros: image viewer already shown! close it first!");
            return;
        }
        MainApp.forceGC();
        final Vector images = ImageReferenceManager.getImages();
        this.imageViewerFrame = new Frame();
        final ScrollPane scrollPane = new ScrollPane(0);
        final ImageViewer viewer = new ImageViewer(images, this.imageViewerFrame, scrollPane);
        viewer.setVerticalScroll((ScrollPaneAdjustable)scrollPane.getVAdjustable());
        scrollPane.add(viewer);
        this.imageViewerFrame.setTitle("Image viewer");
        this.imageViewerFrame.addWindowListener(viewer);
        this.imageViewerFrame.add(scrollPane);
        if (ConfigData.imageViewerBounds != null) {
            this.imageViewerFrame.setBounds(ConfigData.imageViewerBounds);
        }
        else {
            this.imageViewerFrame.setSize(260, 515);
        }
        this.imageViewerFrame.setVisible(true);
    }
    
    private void showZoomFrame() {
        if (ConfigData.slaveMode) {
            return;
        }
        if (this.imageViewerFrame != null && this.imageViewerFrame.isShowing()) {
            return;
        }
        this.zoomFrame = new Frame();
        this.zoomViewer = new ZoomViewer(this.zoomFrame, this.emuImage, ConfigData.zoomSize);
        this.zoomFrame.setTitle("Zoom");
        this.zoomFrame.setResizable(false);
        this.zoomFrame.addWindowListener(this.zoomViewer);
        this.zoomFrame.add(this.zoomViewer);
        int w = this.zoomViewer.getCurrentWidth();
        int h = this.zoomViewer.getCurrentHeight();
        this.zoomFrame.setSize(w, h);
        if (ConfigData.zoomViewerBounds != null) {
            this.zoomFrame.setBounds(ConfigData.zoomViewerBounds);
        }
        this.zoomFrame.setVisible(true);
        final Insets insets = this.zoomFrame.getInsets();
        w += insets.right + insets.left;
        h += insets.bottom + insets.top;
        this.zoomFrame.setSize(w, h);
        this.zoomFrame.doLayout();
    }
    
    private void showClassMonitor() {
        if (ConfigData.slaveMode || !ConfigData.classMonitor) {
            return;
        }
        if (this.classMonitorFrame != null && this.classMonitorFrame.isShowing()) {
            return;
        }
        this.classMonitorFrame = new MonitorFrame();
        final ClassManager cm = ClassManager.getInstance();
        cm.invalidateCurrentProfile();
        cm.setProfile(MainApp.getApplicationName(), MainApp.midlet);
        if (ConfigData.classMonitorBounds != null) {
            this.classMonitorFrame.setBounds(ConfigData.classMonitorBounds);
        }
        else {
            this.classMonitorFrame.setSize(260, 515);
        }
        this.classMonitorFrame.setVisible(true);
        this.classMonitorFrame.selectProfileChoice(cm.getCurrentProfileName());
    }
    
    public void updateClassMonitor() {
        if (ConfigData.slaveMode || !ConfigData.classMonitor) {
            return;
        }
        if (this.classMonitorFrame == null || !this.classMonitorFrame.isShowing()) {
            return;
        }
        this.classMonitorFrame.updateItems();
    }
    
    private void rescale(final boolean setGammaOnly, final Image img) {
        if (this.lastScale != ConfigData.scale) {
            this.lastScale = ConfigData.scale;
            if (this.scaledSrc == null) {
                this.scaledSrc = new int[Display.WIDTH * Display.HEIGHT];
            }
            final int newSize = Display.WIDTH * Display.HEIGHT * this.lastScale * this.lastScale;
            if (this.scaledDst == null || this.scaledDst.length < newSize) {
                this.scaledDst = new int[newSize];
            }
            this.initScaledGraphics();
        }
        if (img instanceof BufferedImage) {
            ((BufferedImage)this.emuImage).getRGB(0, 0, Display.WIDTH, Display.HEIGHT, this.scaledSrc, 0, Display.WIDTH);
        }
        else {
            this.scaledGraph.drawImage(img, 0, 0, null);
            this.scaledImage.getRGB(0, 0, Display.WIDTH, Display.HEIGHT, this.scaledSrc, 0, Display.WIDTH);
        }
        if (ConfigData.displayGamma % 100 != 0) {
            final int dif = (ConfigData.displayGamma - 100) * 255 / 100;
            final int max = Display.WIDTH * Display.HEIGHT;
            if (dif > 0) {
                for (int i = 0; i < max; ++i) {
                    final int pixel = this.scaledSrc[i];
                    int cR = (pixel & 0xFF0000) >> 16;
                    int cG = (pixel & 0xFF00) >> 8;
                    int cB = pixel & 0xFF;
                    cR += (255 - cR) * dif >> 8;
                    cG += (255 - cG) * dif >> 8;
                    cB += (255 - cB) * dif >> 8;
                    if (cR > 255) {
                        cR = 255;
                    }
                    if (cG > 255) {
                        cG = 255;
                    }
                    if (cB > 255) {
                        cB = 255;
                    }
                    this.scaledSrc[i] = (0xFF000000 | cR << 16 | cG << 8 | cB);
                }
            }
            else {
                for (int i = 0; i < max; ++i) {
                    final int pixel = this.scaledSrc[i];
                    int cR = (pixel & 0xFF0000) >> 16;
                    int cG = (pixel & 0xFF00) >> 8;
                    int cB = pixel & 0xFF;
                    cR += cR * dif >> 8;
                    cG += cG * dif >> 8;
                    cB += cB * dif >> 8;
                    if (cR < 0) {
                        cR = 0;
                    }
                    if (cG < 0) {
                        cG = 0;
                    }
                    if (cB < 0) {
                        cB = 0;
                    }
                    this.scaledSrc[i] = (0xFF000000 | cR << 16 | cG << 8 | cB);
                }
            }
        }
        if (this.lastScale != 1) {
            final int dstWidth = Display.WIDTH * this.lastScale;
            int dstI = 0;
            for (int y = 0; y < Display.HEIGHT; ++y) {
                final int srcBaseY = y * Display.WIDTH;
                final int dstBaseY = y * dstWidth * this.lastScale;
                for (int x = 0; x < Display.WIDTH; ++x) {
                    final int pixel = this.scaledSrc[srcBaseY + x];
                    for (int j = 0; j < this.lastScale; ++j) {
                        this.scaledDst[dstI++] = pixel;
                    }
                }
                for (int j = 1; j < this.lastScale; ++j) {
                    System.arraycopy(this.scaledDst, dstBaseY, this.scaledDst, dstBaseY + j * dstWidth, dstWidth);
                    dstI += dstWidth;
                }
            }
            this.scaledImage.setRGB(0, 0, dstWidth, Display.HEIGHT * this.lastScale, this.scaledDst, 0, dstWidth);
        }
        else {
            this.scaledImage.setRGB(0, 0, Display.WIDTH, Display.HEIGHT, this.scaledSrc, 0, Display.WIDTH);
        }
    }
    
    public void hideNotify() {
        if (this.displayable instanceof javax.microedition.lcdui.Canvas) {
            this.lcduiBridge.handleEvent(2, this.displayable);
        }
    }
    
    public void showNotify() {
        if (this.displayable instanceof javax.microedition.lcdui.Canvas) {
            this.lcduiBridge.handleEvent(1, this.displayable);
        }
    }
    
    public void mouseClicked(final MouseEvent e) {
        if (ConfigData.slaveMode) {
            return;
        }
        final int x = ConfigData.skinScreenX;
        final int y = ConfigData.skinScreenY;
        final int button = e.getButton();
        this.mouseX = e.getX();
        this.mouseY = e.getY();
        if (button != 3) {
            if (this.zoomViewer != null && this.zoomViewer.isShowing()) {
                this.zoomViewer.setSpot((this.mouseX - x) / ConfigData.scale, (this.mouseY - y) / ConfigData.scale);
            }
        }
        else {
            this.distanceMode = !this.distanceMode;
            if (this.distanceMode) {
                this.showZoomFrame();
                if (this.zoomViewer != null && this.zoomViewer.isShowing()) {
                    this.zoomViewer.updateView((this.mouseX - x) / ConfigData.scale, (this.mouseY - y) / ConfigData.scale, true);
                }
            }
            else if (this.zoomFrame != null && this.zoomFrame.isShowing()) {
                this.zoomViewer.storeBounds();
                this.zoomFrame.setVisible(false);
                this.zoomFrame.dispose();
            }
        }
    }
    
    public void mouseEntered(final MouseEvent e) {
    }
    
    public void mouseExited(final MouseEvent e) {
    }
    
    public void mousePressed(final MouseEvent e) {
        final int x = ConfigData.skinScreenX;
        final int y = ConfigData.skinScreenY;
        final int button = e.getButton();
        if (button == 1 && (this.zoomFrame == null || ConfigData.slaveMode) && this.displayable instanceof javax.microedition.lcdui.Canvas) {
            this.tmpInt[0] = (e.getX() - x) / ConfigData.scale;
            this.tmpInt[1] = (e.getY() - y) / ConfigData.scale;
            this.lcduiBridge.handleEvent(14, this.displayable, this.tmpInt);
        }
    }
    
    public void mouseReleased(final MouseEvent e) {
        final int x = ConfigData.skinScreenX;
        final int y = ConfigData.skinScreenY;
        final int button = e.getButton();
        if (button == 1 && this.zoomFrame == null && this.displayable instanceof javax.microedition.lcdui.Canvas) {
            this.tmpInt[0] = (e.getX() - x) / ConfigData.scale;
            this.tmpInt[1] = (e.getY() - y) / ConfigData.scale;
            this.lcduiBridge.handleEvent(15, this.displayable, this.tmpInt);
        }
    }
    
    public void mouseDragged(final MouseEvent e) {
        final int x = ConfigData.skinScreenX;
        final int y = ConfigData.skinScreenY;
        final int button = e.getButton();
        if (button != 3 && this.zoomFrame == null && this.displayable instanceof javax.microedition.lcdui.Canvas) {
            this.tmpInt[0] = (e.getX() - x) / ConfigData.scale;
            this.tmpInt[1] = (e.getY() - y) / ConfigData.scale;
            this.lcduiBridge.handleEvent(16, this.displayable, this.tmpInt);
        }
    }
    
    public void mouseMoved(final MouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
        final int x = ConfigData.skinScreenX;
        final int y = ConfigData.skinScreenY + ConfigData.topConsoleHeight * ConfigData.scale;
        if (this.zoomViewer != null && this.zoomViewer.isShowing()) {
            this.zoomViewer.updateView((this.mouseX - x) / ConfigData.scale, (this.mouseY - y) / ConfigData.scale, false);
        }
    }
}
