// 
// Decompiled by Procyon v0.5.36
// 

package com.nokia.mid.ui;

import ole.pstros.ConfigData;
import ole.pstros.MainApp;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Canvas;

public abstract class FullCanvas extends Canvas
{
    public static final int KEY_UP_ARROW = -1;
    public static final int KEY_DOWN_ARROW = -2;
    public static final int KEY_LEFT_ARROW = -3;
    public static final int KEY_RIGHT_ARROW = -4;
    public static final int KEY_SOFTKEY3 = -5;
    public static final int KEY_SOFTKEY1 = -6;
    public static final int KEY_SOFTKEY2 = -7;
    public static final int KEY_SEND = -10;
    public static final int KEY_END = -11;
    
    protected FullCanvas() {
        this.setFullScreenMode(true);
    }
    
    public void addCommand(final Command cmd) {
        throw new IllegalStateException("Commands are not possible in FullCanvas.");
    }
    
    public void setCommandListener(final CommandListener l) {
        throw new IllegalStateException("Commands are not possible in FullCanvas.");
    }
    
    public int getGameAction(final int keyCode) {
        if (MainApp.verbose) {
            System.out.println("FullCanvas.getGameAction keyCode=" + keyCode);
        }
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
}
