// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import ole.pstros.EmuCanvas;
import java.awt.Image;
import java.awt.Graphics;
import ole.pstros.IEmuBridge;

class EmuLcduiBridge implements IEmuBridge
{
    private Displayable displayable;
    private int intResult;
    
    public Object handleEvent(final int type, final Object data) {
        switch (type) {
            case 0: {
                this.displayable = (Displayable)data;
                break;
            }
            case 2: {
                ((Canvas)data).emuHideNotify();
                break;
            }
            case 1: {
                ((Canvas)data).emuShowNotify();
                break;
            }
            case 8: {
                this.displayable.emuPaint((Graphics)data);
                break;
            }
            case 10: {
                if (((Displayable)data).emuIsFullScreen()) {
                    return this;
                }
                break;
            }
            case 11: {
                return ((Displayable)data).emuGetLeftCommand();
            }
            case 12: {
                return ((Displayable)data).emuGetRightCommand();
            }
            case 17: {
                return ((Canvas)data).emuGetGraphics();
            }
            case 18: {
                this.intResult = ((Canvas)data).emuGetKeyStates();
                break;
            }
            case 19: {
                ((Canvas)data).emuFlushGraphics();
                break;
            }
            case 22: {
                ((Canvas)data).emuCleanKeys();
                break;
            }
        }
        return null;
    }
    
    public Object handleEvent(final int type, final Object data1, final Object data2) {
        switch (type) {
            case 19: {
                ((Canvas)data1).emuFlushGraphics((Image)data2);
                break;
            }
            case 7: {
                ((Displayable)data1).emuSetEmuCanvas((EmuCanvas)data2);
                break;
            }
            case 9: {
                ((Displayable)data1).emuSetShown(data2 != null);
                break;
            }
            case 8: {
                ((Canvas)this.displayable).emuPaint((javax.microedition.lcdui.Graphics)data1, data2);
                break;
            }
            case 13: {
                final int[] k = (int[])data2;
                ((Displayable)data1).emuKeyAction(k[0], k[1], k[2], k[3]);
                break;
            }
            case 14: {
                final int[] p = (int[])data2;
                ((Canvas)data1).emuPointerPressed(p[0], p[1]);
                break;
            }
            case 15: {
                final int[] p = (int[])data2;
                ((Canvas)data1).emuPointerReleased(p[0], p[1]);
                break;
            }
            case 16: {
                final int[] p = (int[])data2;
                ((Canvas)data1).emuPointerDragged(p[0], p[1]);
                break;
            }
            case 23: {
                final int[] p = (int[])data2;
                final Canvas canvas = (Canvas)data1;
                final boolean fullScreen = canvas.emuIsFullScreen();
                canvas.emuSetScreenMode(fullScreen);
                canvas.emuSizeChanged(p[0], canvas.displaybableAreaHeight);
                break;
            }
            case 20: {
                final int[] p = (int[])data2;
                return ((javax.microedition.lcdui.Image)data1).emuGetImage(p[0]);
            }
            case 21: {
                final int[] p = (int[])data2;
                return ((javax.microedition.lcdui.Image)data1).emuGetCollisionData(p[0]);
            }
        }
        return null;
    }
    
    public int getIntResult() {
        return this.intResult;
    }
}
