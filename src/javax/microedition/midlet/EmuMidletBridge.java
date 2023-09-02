// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.midlet;

import ole.pstros.MainApp;
import ole.pstros.IEmuBridge;

class EmuMidletBridge implements IEmuBridge
{
    private MIDlet parent;
    
    public Object handleEvent(final int type, final Object data) {
        switch (type) {
            case 0: {
                this.parent = (MIDlet)data;
                MainApp.midletBridge = this;
                break;
            }
            case 3: {
                if (data == this.parent) {
                    try {
                        this.parent.emuRun();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                break;
            }
            case 4: {
                if (data == this.parent) {
                    this.parent.emuDestroy();
                    break;
                }
                break;
            }
            case 5: {
                if (data == this.parent) {
                    this.parent.emuPauseApp();
                    break;
                }
                break;
            }
            case 6: {
                if (data == this.parent) {
                    try {
                        this.parent.emuStartApp();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                break;
            }
        }
        return null;
    }
    
    public Object handleEvent(final int type, final Object data1, final Object data2) {
        return null;
    }
    
    public int getIntResult() {
        return 0;
    }
}
