// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import ole.pstros.ConfigData;
import ole.pstros.MainApp;
import java.awt.Graphics;
import ole.pstros.IEmuBridge;
import ole.pstros.EmuCanvas;

public abstract class Displayable
{
    Command leftCommand;
    Command rightCommand;
    CommandListener listener;
    Form commandForm;
    EmuCommandListener commandFormListener;
    int displaybableAreaWidth;
    int displaybableAreaHeight;
    boolean shown;
    String title;
    Ticker ticker;
    EmuCanvas emuCanvas;
    int emuKeyStates;
    int emuNumStates;
    IEmuBridge emuLcduiBridge;
    
    abstract void emuPaint(final Graphics p0);
    
    void emuCreateBridge() {
        (this.emuLcduiBridge = new EmuLcduiBridge()).handleEvent(0, this);
    }
    
    private boolean isOkCommandtype(final Command c) {
        return c.getCommandType() == 4;
    }
    
    public void addCommand(final Command cmd) throws NullPointerException {
        if (MainApp.verbose) {
            System.out.println("Displayable.addCommand =" + cmd.getLabel() + " type=" + cmd.getCommandType() + " command=" + cmd);
        }
        if (cmd == null) {
            throw new NullPointerException("command is null!");
        }
        if (this.leftCommand == cmd || this.rightCommand == cmd) {
            return;
        }
        boolean update = false;
        final int commandType = cmd.getCommandType();
        if ((this.leftCommand != null && this.rightCommand != null) || this.commandForm != null) {
            if (this.commandForm == null) {
                (this.commandForm = new Form(this.title)).append(this.leftCommand.getLabel());
                this.commandForm.append(this.rightCommand.getLabel());
                this.commandForm.addCommand(new Command("Select", 4, 1));
                this.commandForm.addCommand(new Command("Back", 2, 1));
                (this.commandFormListener = new EmuCommandListener(this, 1)).addCommand(this.leftCommand);
                this.commandFormListener.addCommand(this.rightCommand);
                this.commandForm.listener = this.commandFormListener;
                this.leftCommand = new Command("Options", 1, 1);
                this.rightCommand = null;
                this.listener = new EmuCommandListener(this.commandForm, 0);
                update = true;
            }
            if (!this.commandForm.emuHasStringItem(cmd.getLabel())) {
                this.commandForm.append(cmd.getLabel());
                this.commandFormListener.addCommand(cmd);
            }
        }
        else if (commandType == 4) {
            update = this.addOkCommand(cmd);
        }
        else if (commandType == 2 || commandType == 3 || commandType == 7 || commandType == 8) {
            update = this.addBackCommand(cmd);
        }
        else if (this.leftCommand == null) {
            this.leftCommand = cmd;
            update = true;
        }
        else if (this.rightCommand == null) {
            this.rightCommand = cmd;
            update = true;
        }
        if (update) {
            this.emuUpdateScreen();
        }
    }
    
    private boolean addOkCommand(final Command cmd) {
        if (ConfigData.keyLeftSoft == ConfigData.keySelect) {
            if (this.leftCommand == null || !this.isOkCommandtype(this.leftCommand)) {
                this.leftCommand = cmd;
                return true;
            }
        }
        else if (this.rightCommand == null) {
            this.rightCommand = cmd;
            return true;
        }
        return false;
    }
    
    private boolean addBackCommand(final Command cmd) {
        if (ConfigData.keyLeftSoft != ConfigData.keySelect) {
            if (this.leftCommand == null) {
                this.leftCommand = cmd;
                return true;
            }
            return this.addOkCommand(cmd);
        }
        else {
            if (this.rightCommand == null) {
                this.rightCommand = cmd;
                return true;
            }
            return this.addOkCommand(cmd);
        }
    }
    
    public void removeCommand(final Command cmd) {
        if (cmd == null) {
            return;
        }
        boolean update = false;
        if (cmd == this.leftCommand) {
            this.leftCommand = null;
            update = true;
        }
        else if (cmd == this.rightCommand) {
            this.rightCommand = null;
            update = true;
        }
        if (update) {
            this.emuUpdateScreen();
        }
    }
    
    public void setCommandListener(final CommandListener l) {
        if (MainApp.verbose) {
            System.out.println("Displayable: setCommandListener to=" + this + " listener=" + l);
        }
        if (this.commandForm != null) {
            this.commandFormListener.setCommandListener(l);
        }
        else {
            this.listener = l;
        }
    }
    
    public int getWidth() {
        return this.displaybableAreaWidth;
    }
    
    public int getHeight() {
        return this.displaybableAreaHeight;
    }
    
    protected void sizeChanged(final int w, final int h) {
    }
    
    public boolean isShown() {
        return this.shown;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(final String s) {
        this.title = s;
        this.emuUpdateScreen();
    }
    
    public Ticker getTicker() {
        return this.ticker;
    }
    
    public void setTicker(final Ticker t) {
        this.ticker = t;
        this.emuUpdateScreen();
    }
    
    void emuSetEmuCanvas(final EmuCanvas c) {
        this.emuKeyStates = 0;
        this.emuCanvas = c;
    }
    
    void emuUpdateScreen() {
        if (this.emuCanvas != null) {
            if (ConfigData.slaveMode) {
                this.emuCanvas.update();
            }
            else {
                this.emuCanvas.repaint();
            }
        }
    }
    
    Command emuGetLeftCommand() {
        return this.leftCommand;
    }
    
    Command emuGetRightCommand() {
        return this.rightCommand;
    }
    
    boolean emuIsFullScreen() {
        return false;
    }
    
    boolean emuKeyAction(final int key, final int keyChar, final int modifiers, final int action) {
        if (MainApp.verbose) {
            System.out.println("Displayable.emuKeyAction key=" + key + " action=" + action + " ecs=" + Integer.toHexString(this.emuKeyStates));
        }
        if (action == 0) {
            if ((key == Display.keySoftLeft || key == Display.keySoftLeft2) && modifiers == 0) {
                if ((this.emuKeyStates & 0x1000000) == 0x0) {
                    this.emuKeyStates |= 0x1000000;
                    if (this.leftCommand != null) {
                        if (MainApp.verbose) {
                            System.out.println("pressed LEFT, command=" + this.leftCommand);
                        }
                        ItemCommandListener itemListener = null;
                        final Object owner = this.leftCommand.owner;
                        Item item = null;
                        if (owner instanceof Item) {
                            item = (Item)owner;
                            itemListener = item.emuGetItemCommandListener();
                        }
                        if (itemListener != null) {
                            itemListener.commandAction(this.leftCommand, item);
                        }
                        else if (this.listener != null) {
                            this.listener.commandAction(this.leftCommand, this);
                            return true;
                        }
                    }
                }
            }
            else if ((key == Display.keySoftRight || key == Display.keySoftRight2) && modifiers == 0) {
                if ((this.emuKeyStates & 0x4000000) == 0x0) {
                    this.emuKeyStates |= 0x4000000;
                    if (this.rightCommand != null) {
                        if (MainApp.verbose) {
                            System.out.println("pressed RIGHT, command=" + this.rightCommand);
                        }
                        ItemCommandListener itemListener = null;
                        final Object owner = this.rightCommand.owner;
                        Item item = null;
                        if (owner instanceof Item) {
                            item = (Item)owner;
                            itemListener = item.emuGetItemCommandListener();
                        }
                        if (itemListener != null) {
                            itemListener.commandAction(this.rightCommand, item);
                        }
                        else if (this.listener != null) {
                            this.listener.commandAction(this.rightCommand, this);
                        }
                        return true;
                    }
                }
            }
            else if (key == Display.keyFire2 || key == Display.keyFire) {
                if ((this.emuKeyStates & 0x100) == 0x0) {
                    this.emuKeyStates |= 0x100;
                }
            }
            else if (key == Display.keyLeft) {
                if ((this.emuKeyStates & 0x4) == 0x0) {
                    this.emuKeyStates |= 0x4;
                }
            }
            else if (key == Display.keyRight) {
                if ((this.emuKeyStates & 0x20) == 0x0) {
                    this.emuKeyStates |= 0x20;
                }
            }
            else if (key == Display.keyUp) {
                if ((this.emuKeyStates & 0x2) == 0x0) {
                    this.emuKeyStates |= 0x2;
                }
            }
            else if (key == Display.keyDown && (this.emuKeyStates & 0x40) == 0x0) {
                this.emuKeyStates |= 0x40;
            }
        }
        else if (action == 1) {
            if ((key == Display.keySoftLeft || key == Display.keySoftLeft2) && modifiers == 0) {
                if ((this.emuKeyStates & 0x1000000) != 0x0) {
                    this.emuKeyStates &= 0xFEFFFFFF;
                    if (this.listener != null && this.leftCommand != null) {
                        return true;
                    }
                }
            }
            else if ((key == Display.keySoftRight || key == Display.keySoftRight2) && modifiers == 0) {
                if ((this.emuKeyStates & 0x4000000) != 0x0) {
                    this.emuKeyStates &= 0xFBFFFFFF;
                    if (this.listener != null && this.leftCommand != null) {
                        return true;
                    }
                }
            }
            else if (key == Display.keyLeft) {
                if ((this.emuKeyStates & 0x4) != 0x0) {
                    this.emuKeyStates &= 0xFFFFFFFB;
                }
            }
            else if (key == Display.keyRight) {
                if ((this.emuKeyStates & 0x20) != 0x0) {
                    this.emuKeyStates &= 0xFFFFFFDF;
                }
            }
            else if (key == Display.keyUp) {
                if ((this.emuKeyStates & 0x2) != 0x0) {
                    this.emuKeyStates &= 0xFFFFFFFD;
                }
            }
            else if (key == Display.keyDown) {
                if ((this.emuKeyStates & 0x40) != 0x0) {
                    this.emuKeyStates &= 0xFFFFFFBF;
                }
            }
            else if ((key == Display.keyFire2 || key == Display.keyFire) && (this.emuKeyStates & 0x100) != 0x0) {
                this.emuKeyStates &= 0xFFFFFEFF;
            }
        }
        return false;
    }
    
    void emuSetShown(final boolean state) {
        this.shown = state;
    }
    
    public String toString() {
        return String.valueOf(this.getClass().getName()) + "@" + this.hashCode();
    }
}
