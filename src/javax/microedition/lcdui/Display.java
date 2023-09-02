// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import java.awt.Insets;
import java.awt.Component;
import java.awt.event.WindowListener;
import java.util.TimerTask;
import java.util.Timer;
import ole.pstros.utils.AlertTimerTask;
import javax.microedition.midlet.MIDlet;
import ole.pstros.MainApp;
import ole.pstros.ConfigData;
import java.awt.AWTEvent;
import java.awt.Window;
import java.awt.event.WindowEvent;
import ole.pstros.EmuCanvas;
import java.awt.Frame;
import java.util.HashMap;

public class Display
{
    public static final int COLOR_BACKGROUND = 0;
    public static final int COLOR_FOREGROUND = 1;
    public static final int COLOR_HIGHLIGHTED_BACKGROUND = 2;
    public static final int COLOR_HIGHLIGHTED_FOREGROUND = 3;
    public static final int COLOR_BORDER = 4;
    public static final int COLOR_HIGHLIGHTED_BORDER = 5;
    public static int keySoftLeft;
    public static int keySoftLeft2;
    public static int keySoftRight;
    public static int keySoftRight2;
    public static int keySoftCenter;
    public static int keyLeft;
    public static int keyUp;
    public static int keyRight;
    public static int keyDown;
    public static int keyFire;
    public static int keyFire2;
    public static int keyNum0;
    public static int keyNum7;
    public static int keyNum8;
    public static int keyNum9;
    public static int keyNum4;
    public static int keyNum5;
    public static int keyNum6;
    public static int keyNum1;
    public static int keyNum2;
    public static int keyNum3;
    public static int keyStar;
    public static int keyCross;
    public static int keyRotate;
    public static int keyPause;
    public static int keyVirtual1;
    public static int keyVirtual2;
    public static int keyVirtual3;
    public static int keyScreenShot;
    public static int keyCaptureVideo;
    public static int keyShowHideNotify;
    public static int WIDTH;
    public static int HEIGHT;
    private static final String titleName = "Pstros";
    private static HashMap displays;
    private static Displayable displayable;
    private static Frame emuFrame;
    private static EmuCanvas emuCanvas;
    private static Runnable emuSerialRunner;
    private static boolean emulationStarted;
    
    static {
        Display.keySoftLeft = 90;
        Display.keySoftLeft2 = 112;
        Display.keySoftRight = 67;
        Display.keySoftRight2 = 113;
        Display.keySoftCenter = 88;
        Display.keyLeft = 37;
        Display.keyUp = 38;
        Display.keyRight = 39;
        Display.keyDown = 40;
        Display.keyFire = 17;
        Display.keyFire2 = 10;
        Display.keyNum0 = 96;
        Display.keyNum7 = 97;
        Display.keyNum8 = 98;
        Display.keyNum9 = 99;
        Display.keyNum4 = 100;
        Display.keyNum5 = 101;
        Display.keyNum6 = 102;
        Display.keyNum1 = 103;
        Display.keyNum2 = 104;
        Display.keyNum3 = 105;
        Display.keyStar = 106;
        Display.keyCross = 111;
        Display.keyRotate = 115;
        Display.keyPause = 116;
        Display.keyVirtual1 = 117;
        Display.keyVirtual2 = 118;
        Display.keyVirtual3 = 119;
        Display.keyScreenShot = 122;
        Display.keyCaptureVideo = 123;
        Display.keyShowHideNotify = 120;
        /*Display.WIDTH = 176;
        Display.HEIGHT = 220;*/
        Display.WIDTH = 240;
        Display.HEIGHT = 320;
        Display.displays = new HashMap();
        Display.emulationStarted = false;
    }
    
    public static void emuDestroyFrame() {
        if (Display.emuFrame != null) {
            final WindowEvent ev = new WindowEvent(Display.emuFrame, 201, null, 200, 202);
            Display.emuFrame.dispatchEvent(ev);
            Display.emuFrame.dispose();
            Display.emuCanvas = null;
            Display.emuFrame = null;
        }
        else if (!ConfigData.slaveMode) {
            System.out.println("Emu frame is null!");
        }
        if (!ConfigData.slaveMode) {
            System.exit(0);
        }
        else {
            MainApp.midletBridge.handleEvent(4, MainApp.midlet);
            MainApp.getInstance().setEvent("emuClosed", null);
        }
    }
    
    void emuBackupFrame() {
        Display.emuCanvas.backupImage();
    }
    
    void emuRestoreFrame() {
        Display.emuCanvas.restoreImage();
    }
    
    public static synchronized boolean emuRunSerialRunner() {
        if (Display.emuSerialRunner != null) {
            if (MainApp.verbose) {
                System.out.println("EmuCanvas: calling the runner=" + Display.emuSerialRunner);
            }
            final Runnable tmp = Display.emuSerialRunner;
            Display.emuSerialRunner = null;
            tmp.run();
            return true;
        }
        return false;
    }
    
    public static Display getDisplay(final MIDlet m) {
        Object d = Display.displays.get(m);
        if (d == null) {
            d = new Display();
            Display.displays.put(m, d);
        }
        if (MainApp.verbose) {
            System.out.println("EmuCanvas: getDisplay=" + d);
        }
        return (Display)d;
    }
    
    public void emuRepaintDisplay() {
        if (Display.emuCanvas != null) {
            Display.emuCanvas.setEmuPaintRequest(0, 0, Display.WIDTH, Display.HEIGHT);
            Display.emuCanvas.repaint();
        }
    }
    
    private Display() {
    }
    
    public boolean isColor() {
        return true;
    }
    
    public int numColors() {
        return 415030;
    }
    
    public int numAlphaLevels() {
        return 255;
    }
    
    public void setCurrent(final Alert alert, final Displayable nextDisplayable) {
        if (alert == null || nextDisplayable == null) {
            throw new NullPointerException();
        }
        if (alert == nextDisplayable) {
            throw new IllegalArgumentException();
        }
        if (MainApp.verbose) {
            System.out.println("Display: setCurrent Alert! nextDisplayable=" + nextDisplayable);
        }
        alert.emuSetNextDisplayable(nextDisplayable);
        this.setCurrent(alert);
    }
    
    public void setCurrent(final Displayable nextDisplayable) {
        if (MainApp.verbose) {
            if (nextDisplayable != null) {
                System.out.println("Display: setCurrent displayable=" + nextDisplayable.toString() + " old displayable=" + Display.displayable);
            }
            else {
                System.out.println("Display: setCurrent displayable=null");
            }
        }
        if (Display.displayable != null) {
            Display.displayable.shown = false;
        }
        if (nextDisplayable == null) {
            return;
        }
        if (nextDisplayable instanceof Alert) {
            final Alert alert = (Alert)nextDisplayable;
            if (alert.getTimeout() > 0) {
                Displayable displayableAfterAlert = Display.displayable;
                if (alert.emuGetNextDisplayable() != null) {
                    displayableAfterAlert = alert.emuGetNextDisplayable();
                }
                final TimerTask tt = new AlertTimerTask(this, displayableAfterAlert);
                final Timer alertTimer = new Timer();
                alertTimer.schedule(tt, alert.getTimeout());
            }
            else if (alert.emuGetNextDisplayable() == null && Display.displayable != null) {
                alert.emuSetNextDisplayable(Display.displayable);
            }
        }
        Display.displayable = nextDisplayable;
        if (Display.displayable != null) {
            Display.displayable.shown = true;
        }
        emuRunEmulation();
        Display.emuCanvas.setContent(Display.displayable, Display.displayable.emuLcduiBridge);
    }
    
    public Displayable getCurrent() {
        return Display.displayable;
    }
    
    public void setCurrentItem(final Item item) {
        final Displayable d = item.emuGetDisplayable();
        if (d instanceof Alert) {
            throw new IllegalStateException("item owned by Alert");
        }
        if (d == null) {
            throw new IllegalStateException("item not owned by a container");
        }
        if (d != this.getCurrent()) {
            this.setCurrent(d);
        }
    }
    
    public boolean vibrate(final int duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("Duration is negative : value=" + duration);
        }
        return true;
    }
    
    public synchronized void callSerially(final Runnable r) {
        if (MainApp.verbose) {
            System.out.println("Pstros: Display.callSerially r=" + r);
        }
        Display.emuSerialRunner = r;
    }
    
    public boolean flashBacklight(final int duration) {
        if (MainApp.verbose) {
            System.out.println("Pstros: flashBacklight duration=" + duration);
        }
        return Display.emulationStarted;
    }
    
    public int getBestImageWidth(final int imageType) {
        return 0;
    }
    
    public int getBestImageHeight(final int imageType) {
        return 0;
    }
    
    public int getBorderStyle(final boolean highlighted) {
        return 0;
    }
    
    public int getColor(final int colorSpecifier) {
        switch (colorSpecifier) {
            case 0: {
                return 8421504;
            }
            case 1: {
                return 0;
            }
            case 2: {
                return 10000536;
            }
            case 3: {
                return 16711680;
            }
            case 4: {
                return 6316128;
            }
            case 5: {
                return 16711680;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public static void emuRotateDisplay() {
        if (!Display.emulationStarted) {
            return;
        }
        if (ConfigData.slaveMode) {
            return;
        }
        if (MainApp.verbose) {
            System.out.println("emuRotateDisplay");
        }
        final int w = Display.WIDTH;
        final int h = Display.HEIGHT;
        if (ConfigData.originalScreenWidth > 0) {
            if (w == ConfigData.originalScreenWidth) {
                Display.WIDTH = ConfigData.rotatedScreenWidth;
                Display.HEIGHT = ConfigData.rotatedScreenHeight;
            }
            else {
                Display.WIDTH = ConfigData.originalScreenWidth;
                Display.HEIGHT = ConfigData.originalScreenHeight;
            }
        }
        else {
            Display.WIDTH = h;
            Display.HEIGHT = w;
        }
        emuInitFrame();
        Display.emuCanvas.init();
    }
    
    private static void emuInitFrame() {
        if (Display.emuFrame != null) {
            Display.emuFrame.setVisible(false);
            Display.emuFrame.dispose();
        }
        Display.emuFrame = new Frame();
        int w = Display.WIDTH * ConfigData.scale;
        int h = Display.HEIGHT * ConfigData.scale;
        if (ConfigData.skinWidth > 0 && ConfigData.skinHeight > 0) {
            w = ConfigData.skinWidth;
            h = ConfigData.skinHeight;
        }
        else {
            ConfigData.skinScreenX = 0;
            ConfigData.skinScreenY = 0;
        }
        Display.emuFrame.setSize(10, 10);
        Display.emuFrame.setResizable(false);
        Display.emuFrame.setLocation(ConfigData.windowPositionX, ConfigData.windowPositionY);
        Display.emuFrame.addWindowListener(Display.emuCanvas);
        Display.emuFrame.add(Display.emuCanvas);
        emuSetTitle(0, 0, ConfigData.videoMemoryLimit >> 10, 0);
        Display.emuFrame.show();
        final Insets insets = Display.emuFrame.getInsets();
        w += insets.right + insets.left;
        h += insets.bottom + insets.top;
        Display.emuFrame.setSize(w, h);
        Display.emuFrame.doLayout();
    }
    
    public static void emuRunEmulation() {
        if (Display.emulationStarted) {
            return;
        }
        Display.emulationStarted = true;
        if (MainApp.verbose) {
            System.out.println("runEmuFrame!");
        }
        (Display.emuCanvas = EmuCanvas.getInstance()).setParentComponent(MainApp.parentComponent);
        if (!ConfigData.slaveMode) {
            emuInitFrame();
        }
        Display.emuCanvas.init();
    }
    
    public static void emuSetTitle(final int imageSize, final int peakSize, final int limit, final int fps) {
        if (Display.emuFrame != null) {
            Display.emuFrame.setTitle("Pstros [" + imageSize + "/" + peakSize + "/" + limit + "] " + fps + " fps");
        }
    }
    
    public static void emuResizeEmuFrame() {
        if (Display.emuFrame == null) {
            return;
        }
        if (ConfigData.skinWidth > 0 || ConfigData.skinHeight > 0) {
            return;
        }
        int w = Display.WIDTH * ConfigData.scale;
        int h = Display.HEIGHT * ConfigData.scale;
        final Insets insets = Display.emuFrame.getInsets();
        w += insets.right + insets.left;
        h += insets.bottom + insets.top;
        Display.emuFrame.setSize(w, h);
        Display.emuFrame.doLayout();
    }
}
