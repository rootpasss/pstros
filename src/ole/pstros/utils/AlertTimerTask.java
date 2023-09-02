// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Display;
import java.util.TimerTask;

public class AlertTimerTask extends TimerTask
{
    private Display display;
    private Displayable d;
    
    public AlertTimerTask(final Display display, final Displayable d) {
        this.display = display;
        this.d = d;
    }
    
    public void run() {
        if (this.display == null || this.d == null) {
            return;
        }
        this.display.setCurrent(this.d);
    }
}
