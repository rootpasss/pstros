// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.midlet;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Display;
import ole.pstros.MainApp;
import ole.pstros.utils.JadFileParser;
import ole.pstros.IEmuBridge;
import ole.pstros.ConfigData;

public abstract class MIDlet
{
    private boolean running;
    private boolean internalDestroy;
    
    public MIDlet() {
        final String platform = "microedition.platform";
        if (!ConfigData.readOnly) {
            final String val = System.getProperty("microedition.platform");
            if (val == null) {
                System.setProperty("microedition.platform", "pstros");
            }
        }
        final IEmuBridge eb = new EmuMidletBridge();
        eb.handleEvent(0, this);
    }
    
    public final boolean emuIsRunning() {
        return this.running;
    }
    
    public final int checkPermission(final String permission) {
        return 0;
    }
    
    protected abstract void destroyApp(final boolean p0) throws MIDletStateChangeException;
    
    public final String getAppProperty(final String key) {
        String result = JadFileParser.getValue(key);
        if (result == null && !ConfigData.readOnly) {
            result = System.getProperty(key);
        }
        if (MainApp.verbose) {
            System.out.println("Pstros: MIDlet.getAppProperty key=" + key + " result=" + result);
        }
        return result;
    }
    
    public final void notifyDestroyed() {
        this.internalDestroy = true;
        this.running = false;
        if (MainApp.verbose) {
            System.out.println("Pstros: notifyDestroyed called!");
        }
        Display.emuDestroyFrame();
    }
    
    public final void notifyPaused() {
        if (MainApp.verbose) {
            System.out.println("Pstros: notifyPaused called!");
        }
    }
    
    protected abstract void pauseApp();
    
    public final boolean platformRequest(String URL) throws ConnectionNotFoundException {
        if (ConfigData.slaveMode) {
            MainApp.getInstance().setEvent("emuPlatformRequest", URL);
            return false;
        }
        System.out.println("Pstros: platformRequest called.  URL=" + URL);
        if (System.getProperty("os.name").startsWith("Windows")) {
            try {
                int index = URL.indexOf(38);
                if (index > 0) {
                    index = URL.indexOf("://");
                    if (index > 0) {
                        index += 3;
                        URL = String.valueOf(URL.substring(0, index)) + '\"' + URL.substring(index) + '\"';
                    }
                }
                Runtime.getRuntime().exec(new String[] { "cmd.exe", "/c", "start", URL });
            }
            catch (Exception e) {
                if (MainApp.verbose) {
                    e.printStackTrace();
                }
            }
            return false;
        }
        throw new ConnectionNotFoundException("Feature not implemented in emulator");
    }
    
    public final void resumeRequest() {
        System.out.println("Pstros: resume request called!");
    }
    
    protected abstract void startApp() throws MIDletStateChangeException;
    
    final void emuRun() throws MIDletStateChangeException {
        this.running = true;
        Display.emuRunEmulation();
        this.startApp();
    }
    
    final void emuDestroy() {
        if (this.internalDestroy) {
            return;
        }
        try {
            this.destroyApp(true);
            this.running = false;
        }
        catch (Exception ex) {}
    }
    
    final void emuPauseApp() {
        this.pauseApp();
    }
    
    final void emuStartApp() throws MIDletStateChangeException {
        this.startApp();
    }
}
