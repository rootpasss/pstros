// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui.game;

import ole.pstros.EmuCanvas;
import ole.pstros.MainApp;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Canvas;

public abstract class GameCanvas extends Canvas
{
    public static final int UP_PRESSED = 2;
    public static final int DOWN_PRESSED = 64;
    public static final int LEFT_PRESSED = 4;
    public static final int RIGHT_PRESSED = 32;
    public static final int FIRE_PRESSED = 256;
    public static final int GAME_A_PRESSED = 512;
    public static final int GAME_B_PRESSED = 1024;
    public static final int GAME_C_PRESSED = 2048;
    public static final int GAME_D_PRESSED = 4096;
    public static final int SOFT_L_PRESSED = 16777216;
    public static final int SOFT_R_PRESSED = 67108864;
    public static final int SOFT_C_PRESSED = 33554432;
    private boolean suppresKeyEvents;
    private Graphics g;
    
    protected GameCanvas(final boolean suppressKeyEvents) {
        this.suppresKeyEvents = suppressKeyEvents;
    }
    
    protected Graphics getGraphics() {
        if (!MainApp.verbose) {
            System.out.println("Pstros: GameCanvas.getGraphics() ");
        }
        if (this.g == null) {
            final EmuCanvas ec = EmuCanvas.getInstance();
            if (ec != null) {
                this.g = ec.createGraphics();
            }
        }
        return this.g;
    }
    
    public int getKeyStates() {
        final EmuCanvas ec = EmuCanvas.getInstance();
        if (ec.lcduiBridge != null) {
            ec.lcduiBridge.handleEvent(18, this);
            return ec.lcduiBridge.getIntResult();
        }
        return 0;
    }
    
    public void flushGraphics(final int x, final int y, final int width, final int height) {
        if (MainApp.verbose) {
            System.out.println("GameCanvas: flushGraphics x=" + x + " y=" + y + " w=" + width + " h=" + height);
        }
        this.flushGraphics();
    }
    
    public void flushGraphics() {
        final EmuCanvas ec = EmuCanvas.getInstance();
        if (ec.lcduiBridge != null && this.g != null) {
            ec.lcduiBridge.handleEvent(19, this, this.g.emuGetGraphicsImage());
        }
    }
    
    public void paint(final Graphics g) {
    }
}
