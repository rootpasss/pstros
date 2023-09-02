// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import java.awt.Image;
import ole.pstros.MainApp;
import ole.pstros.ConfigData;
import ole.pstros.EmuCanvas;

public abstract class Canvas extends Displayable
{
    public static final int UP = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 5;
    public static final int DOWN = 6;
    public static final int FIRE = 8;
    public static final int GAME_A = 9;
    public static final int GAME_B = 10;
    public static final int GAME_C = 11;
    public static final int GAME_D = 12;
    public static final int SOFT_L = 24;
    public static final int SOFT_C = 25;
    public static final int SOFT_R = 26;
    public static final int KEY_NUM0 = 48;
    public static final int KEY_NUM1 = 49;
    public static final int KEY_NUM2 = 50;
    public static final int KEY_NUM3 = 51;
    public static final int KEY_NUM4 = 52;
    public static final int KEY_NUM5 = 53;
    public static final int KEY_NUM6 = 54;
    public static final int KEY_NUM7 = 55;
    public static final int KEY_NUM8 = 56;
    public static final int KEY_NUM9 = 57;
    public static final int KEY_STAR = 42;
    public static final int KEY_POUND = 35;
    private static final int MASK_KEY_NUM0 = 1;
    private static final int MASK_KEY_NUM1 = 2;
    private static final int MASK_KEY_NUM2 = 4;
    private static final int MASK_KEY_NUM3 = 8;
    private static final int MASK_KEY_NUM4 = 16;
    private static final int MASK_KEY_NUM5 = 32;
    private static final int MASK_KEY_NUM6 = 64;
    private static final int MASK_KEY_NUM7 = 128;
    private static final int MASK_KEY_NUM8 = 256;
    private static final int MASK_KEY_NUM9 = 512;
    private static final int MASK_KEY_STAR = 1024;
    private static final int MASK_KEY_CROSS = 2048;
    private boolean fullScreen;
    private static final int DEFAULT_CONSOLE_SIZE = 24;
    
    protected Canvas() {
        this.displaybableAreaWidth = Display.WIDTH;
        this.emuCreateBridge();
        this.emuSetScreenMode(false);
        final EmuCanvas ec = EmuCanvas.getInstance();
        if (ec != null) {
            ec.setBridge(this.emuLcduiBridge);
        }
    }
    
    boolean emuIsFullScreen() {
        return this.fullScreen;
    }
    
    public boolean isDoubleBuffered() {
        return true;
    }
    
    public boolean hasPointerEvents() {
        return true;
    }
    
    public boolean hasPointerMotionEvents() {
        return true;
    }
    
    public boolean hasRepeatEvents() {
        return true;
    }
    
    public int getKeyCode(final int gameAction) {
        switch (gameAction) {
            case 1: {
                return ConfigData.keyUpArrow;
            }
            case 6: {
                return ConfigData.keyDownArrow;
            }
            case 2: {
                return ConfigData.keyLeftArrow;
            }
            case 5: {
                return ConfigData.keyRightArrow;
            }
            case 8: {
                return ConfigData.keyCenterSoft;
            }
            case 9: {
                return 49;
            }
            case 10: {
                return 51;
            }
            case 11: {
                return 55;
            }
            case 12: {
                return 57;
            }
            default: {
                return 0;
            }
        }
    }
    
    public String getKeyName(final int keyCode) {
        if (MainApp.verbose) {
            System.out.println("Pstros: Canvas. getKeyName called. keyCode=" + keyCode);
        }
        return "key";
    }
    
    public int getGameAction(final int keyCode) {
        if (keyCode == ConfigData.keyUpArrow || keyCode == 50) {
            return 1;
        }
        if (keyCode == ConfigData.keyDownArrow || keyCode == 56) {
            return 6;
        }
        if (keyCode == ConfigData.keyLeftArrow || keyCode == 52) {
            return 2;
        }
        if (keyCode == ConfigData.keyRightArrow || keyCode == 54) {
            return 5;
        }
        if (keyCode == ConfigData.keyCenterSoft || keyCode == 53) {
            return 8;
        }
        if (keyCode == 49) {
            return 9;
        }
        if (keyCode == 51) {
            return 10;
        }
        if (keyCode == 55) {
            return 11;
        }
        if (keyCode == 57) {
            return 12;
        }
        return 0;
    }
    
    public void setFullScreenMode(final boolean mode) {
        final boolean newMode = mode && ConfigData.fullScreenSupported;
        if (this.fullScreen != newMode) {
            this.emuSetScreenMode(newMode);
        }
    }
    
    void emuSetScreenMode(final boolean mode) {
        this.fullScreen = mode;
        this.displaybableAreaWidth = Display.WIDTH;
        if (this.fullScreen) {
            this.displaybableAreaHeight = Display.HEIGHT;
        }
        else {
            int consoleSize = ConfigData.getConsoleSize();
            if (consoleSize < 1) {
                consoleSize = (ConfigData.bottomConsoleHeight = 24);
            }
            this.displaybableAreaHeight = Display.HEIGHT - consoleSize;
        }
    }
    
    protected void keyPressed(final int keyCode) {
    }
    
    protected void keyRepeated(final int keyCode) {
    }
    
    protected void keyReleased(final int keyCode) {
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
    
    protected abstract void paint(final Graphics p0);
    
    void emuPointerPressed(final int x, final int y) {
        this.pointerPressed(x, y);
    }
    
    void emuPointerReleased(final int x, final int y) {
        this.pointerReleased(x, y);
    }
    
    void emuPointerDragged(final int x, final int y) {
        this.pointerDragged(x, y);
    }
    
    void emuSizeChanged(final int w, final int h) {
        this.sizeChanged(w, h);
    }
    
    Graphics emuGetGraphics() {
        if (this.emuCanvas != null) {
            return this.emuCanvas.getDeviceGraphics();
        }
        this.emuSetEmuCanvas(EmuCanvas.getInstance());
        if (this.emuCanvas != null) {
            return this.emuCanvas.getDeviceGraphics();
        }
        System.out.println("Pstros: GameCanvas.getGraphics() - emuCanvas is null!");
        return null;
    }
    
    int emuGetKeyStates() {
        return this.emuKeyStates;
    }
    
    void emuFlushGraphics() {
        if (this.emuCanvas != null) {
            this.emuCanvas.flushGraphics(null);
            this.emuCanvas.checkPause();
        }
    }
    
    void emuFlushGraphics(final Image img) {
        if (this.emuCanvas != null) {
            this.emuCanvas.flushGraphics(img);
            this.emuCanvas.checkPause();
        }
    }
    
    void emuCleanKeys() {
        this.emuNumStates = 0;
    }
    
    boolean emuKeyAction(final int key, final int keyChar, final int modifiers, final int action) {
        final int origKeyStates = this.emuKeyStates;
        final boolean processed = super.emuKeyAction(key, keyChar, modifiers, action);
        if (action == 0) {
            if (key == Display.keyFire || key == Display.keyFire2) {
                if ((origKeyStates & 0x100) == 0x0) {
                    this.keyPressed(ConfigData.configActive ? ConfigData.keyCenterSoft : 8);
                }
                else {
                    this.keyRepeated(ConfigData.configActive ? ConfigData.keyCenterSoft : 8);
                }
            }
            else if (key == Display.keyLeft) {
                if ((origKeyStates & 0x4) == 0x0) {
                    this.keyPressed(ConfigData.configActive ? ConfigData.keyLeftArrow : 2);
                }
                else {
                    this.keyRepeated(ConfigData.configActive ? ConfigData.keyLeftArrow : 2);
                }
            }
            else if (key == Display.keyRight) {
                if ((origKeyStates & 0x20) == 0x0) {
                    this.keyPressed(ConfigData.configActive ? ConfigData.keyRightArrow : 5);
                }
                else {
                    this.keyRepeated(ConfigData.configActive ? ConfigData.keyRightArrow : 5);
                }
            }
            else if (key == Display.keyUp) {
                if ((origKeyStates & 0x2) == 0x0) {
                    this.keyPressed(ConfigData.configActive ? ConfigData.keyUpArrow : 1);
                }
                else {
                    this.keyRepeated(ConfigData.configActive ? ConfigData.keyUpArrow : 1);
                }
            }
            else if (key == Display.keyDown) {
                if ((origKeyStates & 0x40) == 0x0) {
                    this.keyPressed(ConfigData.configActive ? ConfigData.keyDownArrow : 6);
                }
                else {
                    this.keyRepeated(ConfigData.configActive ? ConfigData.keyDownArrow : 6);
                }
            }
            else if ((key == Display.keySoftLeft || key == Display.keySoftLeft2) && modifiers == 0) {
                if (!processed) {
                    if ((origKeyStates & 0x1000000) == 0x0) {
                        this.keyPressed(ConfigData.configActive ? ConfigData.keyLeftSoft : 24);
                    }
                    else {
                        this.keyRepeated(ConfigData.configActive ? ConfigData.keyLeftSoft : 24);
                    }
                }
            }
            else if ((key == Display.keySoftRight || key == Display.keySoftRight2) && modifiers == 0) {
                if (!processed) {
                    if ((origKeyStates & 0x4000000) == 0x0) {
                        this.keyPressed(ConfigData.configActive ? ConfigData.keyRightSoft : 26);
                    }
                    else {
                        this.keyRepeated(ConfigData.configActive ? ConfigData.keyRightSoft : 26);
                    }
                }
            }
            else if (key == Display.keyNum5) {
                if ((this.emuNumStates & 0x20) == 0x0) {
                    this.emuNumStates |= 0x20;
                    this.keyPressed(53);
                }
                else {
                    this.keyRepeated(53);
                }
            }
            else if (key == Display.keyNum1) {
                if (ConfigData.numKeySwap) {
                    if ((this.emuNumStates & 0x80) == 0x0) {
                        this.emuNumStates |= 0x80;
                        this.keyPressed(55);
                    }
                    else {
                        this.keyRepeated(55);
                    }
                }
                else if ((this.emuNumStates & 0x2) == 0x0) {
                    this.emuNumStates |= 0x2;
                    this.keyPressed(49);
                }
                else {
                    this.keyRepeated(49);
                }
            }
            else if (key == Display.keyNum2) {
                if (ConfigData.numKeySwap) {
                    if ((this.emuNumStates & 0x100) == 0x0) {
                        this.emuNumStates |= 0x100;
                        this.keyPressed(56);
                    }
                    else {
                        this.keyRepeated(56);
                    }
                }
                else if ((this.emuNumStates & 0x4) == 0x0) {
                    this.emuNumStates |= 0x4;
                    this.keyPressed(50);
                }
                else {
                    this.keyRepeated(50);
                }
            }
            else if (key == Display.keyNum3) {
                if (ConfigData.numKeySwap) {
                    if ((this.emuNumStates & 0x200) == 0x0) {
                        this.emuNumStates |= 0x200;
                        this.keyPressed(57);
                    }
                    else {
                        this.keyRepeated(57);
                    }
                }
                else if ((this.emuNumStates & 0x8) == 0x0) {
                    this.emuNumStates |= 0x8;
                    this.keyPressed(51);
                }
                else {
                    this.keyRepeated(51);
                }
            }
            else if (key == Display.keyNum4) {
                if ((this.emuNumStates & 0x10) == 0x0) {
                    this.emuNumStates |= 0x10;
                    this.keyPressed(52);
                }
                else {
                    this.keyRepeated(52);
                }
            }
            else if (key == Display.keyNum6) {
                if ((this.emuNumStates & 0x40) == 0x0) {
                    this.emuNumStates |= 0x40;
                    this.keyPressed(54);
                }
                else {
                    this.keyRepeated(54);
                }
            }
            else if (key == Display.keyNum7) {
                if (ConfigData.numKeySwap) {
                    if ((this.emuNumStates & 0x2) == 0x0) {
                        this.emuNumStates |= 0x2;
                        this.keyPressed(49);
                    }
                    else {
                        this.keyRepeated(49);
                    }
                }
                else if ((this.emuNumStates & 0x80) == 0x0) {
                    this.emuNumStates |= 0x80;
                    this.keyPressed(55);
                }
                else {
                    this.keyRepeated(55);
                }
            }
            else if (key == Display.keyNum8) {
                if (ConfigData.numKeySwap) {
                    if ((this.emuNumStates & 0x4) == 0x0) {
                        this.emuNumStates |= 0x4;
                        this.keyPressed(50);
                    }
                    else {
                        this.keyRepeated(50);
                    }
                }
                else if ((this.emuNumStates & 0x100) == 0x0) {
                    this.emuNumStates |= 0x100;
                    this.keyPressed(56);
                }
                else {
                    this.keyRepeated(56);
                }
            }
            else if (key == Display.keyNum9) {
                if (ConfigData.numKeySwap) {
                    if ((this.emuNumStates & 0x8) == 0x0) {
                        this.emuNumStates |= 0x8;
                        this.keyPressed(51);
                    }
                    else {
                        this.keyRepeated(51);
                    }
                }
                else if ((this.emuNumStates & 0x200) == 0x0) {
                    this.emuNumStates |= 0x200;
                    this.keyPressed(57);
                }
                else {
                    this.keyRepeated(57);
                }
            }
            else if (key == Display.keyNum0) {
                if ((this.emuNumStates & 0x1) == 0x0) {
                    this.emuNumStates |= 0x1;
                    this.keyPressed(48);
                }
                else {
                    this.keyRepeated(48);
                }
            }
            else if (key == Display.keyCross) {
                if ((this.emuNumStates & 0x800) == 0x0) {
                    this.emuNumStates |= 0x800;
                    this.keyPressed(35);
                }
                else {
                    this.keyRepeated(35);
                }
            }
            else if (key == Display.keyStar) {
                if ((this.emuNumStates & 0x400) == 0x0) {
                    this.emuNumStates |= 0x400;
                    this.keyPressed(42);
                }
                else {
                    this.keyRepeated(42);
                }
            }
            else if (key >= 48 && key <= 57) {
                final int keyIndex = key - 48;
                final int keyMask = 1 << keyIndex;
                if ((this.emuNumStates & keyMask) == 0x0) {
                    this.emuNumStates |= keyMask;
                    this.keyPressed(48 + keyIndex);
                }
                else {
                    this.keyRepeated(48 + keyIndex);
                }
            }
            else if (key == Display.keyVirtual1) {
                this.keyPressed(1000000);
            }
            if (key == Display.keyVirtual2) {
                this.keyPressed(1000001);
            }
            else if (key == Display.keyVirtual3) {
                this.keyPressed(1000002);
            }
        }
        else if (action == 1) {
            if (key == Display.keyFire || key == Display.keyFire2) {
                if ((origKeyStates & 0x100) != 0x0) {
                    this.keyReleased(ConfigData.configActive ? ConfigData.keyCenterSoft : 8);
                }
            }
            else if (key == Display.keyLeft) {
                if ((origKeyStates & 0x4) != 0x0) {
                    this.keyReleased(ConfigData.configActive ? ConfigData.keyLeftArrow : 2);
                }
            }
            else if (key == Display.keyRight) {
                if ((origKeyStates & 0x20) != 0x0) {
                    this.keyReleased(ConfigData.configActive ? ConfigData.keyRightArrow : 5);
                }
            }
            else if (key == Display.keyUp) {
                if ((origKeyStates & 0x2) != 0x0) {
                    this.keyReleased(ConfigData.configActive ? ConfigData.keyUpArrow : 1);
                }
            }
            else if (key == Display.keyDown) {
                if ((origKeyStates & 0x40) != 0x0) {
                    this.keyReleased(ConfigData.configActive ? ConfigData.keyDownArrow : 6);
                }
            }
            else if ((key == Display.keySoftLeft || key == Display.keySoftLeft2) && modifiers == 0) {
                if ((origKeyStates & 0x1000000) != 0x0) {
                    this.keyReleased(ConfigData.configActive ? ConfigData.keyLeftSoft : 24);
                }
            }
            else if ((key == Display.keySoftRight || key == Display.keySoftRight2) && modifiers == 0) {
                if ((origKeyStates & 0x4000000) != 0x0) {
                    this.keyReleased(ConfigData.configActive ? ConfigData.keyRightSoft : 26);
                }
            }
            else if (key == Display.keyNum5) {
                if ((this.emuNumStates & 0x20) != 0x0) {
                    this.emuNumStates &= 0xFFFFFFDF;
                    this.keyReleased(53);
                }
            }
            else if (key == Display.keyNum0) {
                if ((this.emuNumStates & 0x1) != 0x0) {
                    this.emuNumStates &= 0xFFFFFFFE;
                    this.keyReleased(48);
                }
            }
            else if (key == Display.keyNum1) {
                if (ConfigData.numKeySwap) {
                    if ((this.emuNumStates & 0x80) != 0x0) {
                        this.emuNumStates &= 0xFFFFFF7F;
                        this.keyReleased(55);
                    }
                }
                else if ((this.emuNumStates & 0x2) != 0x0) {
                    this.emuNumStates &= 0xFFFFFFFD;
                    this.keyReleased(49);
                }
            }
            else if (key == Display.keyNum2) {
                if (ConfigData.numKeySwap) {
                    if ((this.emuNumStates & 0x100) != 0x0) {
                        this.emuNumStates &= 0xFFFFFEFF;
                        this.keyReleased(56);
                    }
                }
                else if ((this.emuNumStates & 0x4) != 0x0) {
                    this.emuNumStates &= 0xFFFFFFFB;
                    this.keyReleased(50);
                }
            }
            else if (key == Display.keyNum3) {
                if (ConfigData.numKeySwap) {
                    if ((this.emuNumStates & 0x200) != 0x0) {
                        this.emuNumStates &= 0xFFFFFDFF;
                        this.keyReleased(57);
                    }
                }
                else if ((this.emuNumStates & 0x8) != 0x0) {
                    this.emuNumStates &= 0xFFFFFFF7;
                    this.keyReleased(51);
                }
            }
            else if (key == Display.keyNum4) {
                if ((this.emuNumStates & 0x10) != 0x0) {
                    this.emuNumStates &= 0xFFFFFFEF;
                    this.keyReleased(52);
                }
            }
            else if (key == Display.keyNum6) {
                if ((this.emuNumStates & 0x40) != 0x0) {
                    this.emuNumStates &= 0xFFFFFFBF;
                    this.keyReleased(54);
                }
            }
            else if (key == Display.keyNum7) {
                if (ConfigData.numKeySwap) {
                    if ((this.emuNumStates & 0x2) != 0x0) {
                        this.emuNumStates &= 0xFFFFFFFD;
                        this.keyReleased(49);
                    }
                }
                else if ((this.emuNumStates & 0x80) != 0x0) {
                    this.emuNumStates &= 0xFFFFFF7F;
                    this.keyReleased(55);
                }
            }
            else if (key == Display.keyNum8) {
                if (ConfigData.numKeySwap) {
                    if ((this.emuNumStates & 0x4) != 0x0) {
                        this.emuNumStates &= 0xFFFFFFFB;
                        this.keyReleased(50);
                    }
                }
                else if ((this.emuNumStates & 0x100) != 0x0) {
                    this.emuNumStates &= 0xFFFFFEFF;
                    this.keyReleased(56);
                }
            }
            else if (key == Display.keyNum9) {
                if (ConfigData.numKeySwap) {
                    if ((this.emuNumStates & 0x8) != 0x0) {
                        this.emuNumStates &= 0xFFFFFFF7;
                        this.keyReleased(51);
                    }
                }
                else if ((this.emuNumStates & 0x200) != 0x0) {
                    this.emuNumStates &= 0xFFFFFDFF;
                    this.keyReleased(57);
                }
            }
            else if (key == Display.keyCross) {
                if ((this.emuNumStates & 0x800) != 0x0) {
                    this.emuNumStates &= 0xFFFFF7FF;
                    this.keyReleased(35);
                }
            }
            else if (key == Display.keyStar) {
                if ((this.emuNumStates & 0x400) != 0x0) {
                    this.emuNumStates &= 0xFFFFFBFF;
                    this.keyReleased(42);
                }
            }
            else if (key >= 48 && key <= 57) {
                final int keyIndex = key - 48;
                final int keyMask = 1 << keyIndex;
                if ((this.emuNumStates & keyMask) != 0x0) {
                    this.emuNumStates &= ~keyMask;
                    this.keyReleased(48 + keyIndex);
                }
            }
            else if (key == Display.keyVirtual1) {
                this.keyReleased(1000000);
            }
            if (key == Display.keyVirtual2) {
                this.keyReleased(1000001);
            }
            else if (key == Display.keyVirtual3) {
                this.keyReleased(1000002);
            }
        }
        return true;
    }
    
    public final void repaint(final int x, final int y, final int width, final int height) {
        if (MainApp.verbose) {
            System.out.println("Pstros: repaint called 1 ec=" + this.emuCanvas + " x=" + x + " y=" + y + " w=" + width + " h=" + height);
        }
        if (this.emuCanvas != null) {
            synchronized (this.emuCanvas.paintLock) {
                final int[] paintLock = this.emuCanvas.paintLock;
                final int n = 0;
                ++paintLock[n];
                this.emuCanvas.setEmuPaintRequest(x, y, width, height);
                if (ConfigData.slaveMode) {
                    this.emuCanvas.update();
                }
                else {
                    this.emuCanvas.repaint(x, y, width, height);
                }
            }
            // monitorexit(this.emuCanvas.paintLock)
            this.emuCanvas.checkPause();
        }
        if (MainApp.verbose) {
            System.out.println("Pstros: Canvas.repaint called emuCanvas=" + this.emuCanvas);
        }
    }
    
    public final void repaint() {
        this.repaint(0, 0, Display.WIDTH, Display.HEIGHT);
    }
    
    public final void serviceRepaints() {
        if (MainApp.verbose) {
            System.out.println("Pstros: Canvas.serviceRepaints called emuCanvas=" + this.emuCanvas);
        }
        if (this.emuCanvas != null) {
            int safeUnlock = 10;
            while (this.emuCanvas.paintRequestValid() && safeUnlock > 0) {
                --safeUnlock;
                Thread.yield();
                try {
                    Thread.sleep(2L);
                }
                catch (Exception ex) {}
            }
            synchronized (this.emuCanvas.paintLock) {
                if (this.emuCanvas.paintLock[0] > 0) {
                    if (MainApp.verbose && this.emuCanvas.paintLock[0] > 1) {
                        System.out.println("Pstros: ?? paint lock=" + this.emuCanvas.paintLock[0]);
                    }
                    safeUnlock = 500;
                    while (this.emuCanvas.isPainting() && safeUnlock > 0) {
                        try {
                            Thread.sleep(2L);
                            --safeUnlock;
                        }
                        catch (Exception ex2) {}
                    }
                    this.emuCanvas.paintLock[0] = 0;
                }
                if (this.emuCanvas.paintRequestValid()) {
                    this.emuCanvas.flushGraphics(null);
                }
            }
            // monitorexit(this.emuCanvas.paintLock)
            this.emuCanvas.checkPause();
        }
    }
    
    protected void sizeChanged(final int w, final int h) {
    }
    
    void emuPaint(final Graphics g, final Object o) {
        if (MainApp.verbose) {
            System.out.println("**canvas paint! caller=" + o);
        }
        this.paint(g);
    }
    
    void emuPaint(final java.awt.Graphics g) {
    }
    
    void emuShowNotify() {
        try {
            this.showNotify();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.emuSetShown(true);
    }
    
    void emuHideNotify() {
        try {
            this.hideNotify();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.emuSetShown(false);
    }
}
