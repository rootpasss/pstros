// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import java.awt.image.ImageObserver;
import java.awt.Graphics;
import ole.pstros.MainApp;

public class Alert extends Screen
{
    public static final Command DISMISS_COMMAND;
    public static final int FOREVER = -2;
    private String text;
    private Image image;
    private AlertType alertType;
    private int timeout;
    private Displayable emuNextDisplayable;
    
    static {
        DISMISS_COMMAND = new Command("Dismiss", 4, 0);
    }
    
    public Alert(final String title) {
        this(title, null, null, null);
    }
    
    public Alert(final String title, final String alertText, final Image alertImage, final AlertType alertType) {
        this.setTitle(title);
        this.text = alertText;
        this.image = alertImage;
        this.alertType = alertType;
        this.timeout = 3000;
        if (MainApp.verbose) {
            System.out.println("Alert created: type=" + alertType.type + " title=" + title);
        }
        this.leftCommand = Alert.DISMISS_COMMAND;
    }
    
    public int getDefaultTimeout() {
        return 3000;
    }
    
    public int getTimeout() {
        return this.timeout;
    }
    
    public void setTimeout(final int time) {
        if (MainApp.verbose) {
            System.out.println("Alert: setTimeout=" + time);
        }
        this.timeout = time;
    }
    
    public AlertType getType() {
        return this.alertType;
    }
    
    public void setType(final AlertType type) {
        if (MainApp.verbose) {
            System.out.println("Alert: setType=" + this.alertType);
        }
        this.alertType = type;
    }
    
    public String getString() {
        return this.text;
    }
    
    public void setString(final String str) {
        this.text = str;
    }
    
    public Image getImage() {
        return this.image;
    }
    
    public void setImage(final Image img) {
        this.image = img;
    }
    
    public void addCommand(final Command cmd) {
        if (this.leftCommand == Alert.DISMISS_COMMAND) {
            this.leftCommand = null;
        }
        super.addCommand(cmd);
    }
    
    public void removeCommand(final Command cmd) {
        this.leftCommand = Alert.DISMISS_COMMAND;
    }
    
    public void setCommandListener(final CommandListener l) {
        super.setCommandListener(l);
    }
    
    void emuPaintScreenContent(final Graphics g) {
        int y = 28;
        if (this.image != null) {
            g.drawImage(this.image.emuGetImage(0), (Display.WIDTH - this.image.getWidth()) / 2, y, null);
            y += this.image.getHeight() + 4;
        }
        if (this.text != null) {
            Item.emuDrawMultiLine(g, this.text, 0, y, true);
        }
    }
    
    void emuSetNextDisplayable(final Displayable d) {
        this.emuNextDisplayable = d;
        final EmuCommandListener ecl = new EmuCommandListener(this.emuNextDisplayable, 0);
        this.setCommandListener(ecl);
    }
    
    Displayable emuGetNextDisplayable() {
        return this.emuNextDisplayable;
    }
}
