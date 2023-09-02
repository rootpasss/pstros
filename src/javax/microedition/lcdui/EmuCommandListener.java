// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import ole.pstros.MainApp;
import java.util.Vector;

class EmuCommandListener implements CommandListener
{
    public static final int SET_SCREEN = 0;
    public static final int RUN_COMMAND = 1;
    CommandListener listener;
    Displayable displayable;
    int type;
    Vector commands;
    
    public EmuCommandListener(final Displayable d, final int type) {
        this.displayable = d;
        this.type = type;
    }
    
    public void commandAction(final Command c, final Displayable d) {
        if (this.type == 0) {
            final Display display = Display.getDisplay(MainApp.midlet);
            if (display != null) {
                display.emuBackupFrame();
                display.setCurrent(this.displayable);
            }
        }
        else if (this.type == 1) {
            if (c.getCommandType() == 2) {
                final Display display = Display.getDisplay(MainApp.midlet);
                if (display != null) {
                    display.setCurrent(this.displayable);
                    display.emuRestoreFrame();
                    display.emuRepaintDisplay();
                }
            }
            else {
                int index = 0;
                if (d instanceof Screen) {
                    index = ((Screen)d).selected;
                }
                final Command cmd = (Command)this.commands.get(index);
                this.listener.commandAction(cmd, this.displayable);
            }
        }
    }
    
    public void setCommandListener(final CommandListener l) {
        this.listener = l;
    }
    
    public boolean addCommand(final Command cmd) {
        if (this.commands == null) {
            this.commands = new Vector();
        }
        final int index = this.commands.indexOf(cmd);
        if (index >= 0) {
            return false;
        }
        this.commands.add(cmd);
        return true;
    }
}
