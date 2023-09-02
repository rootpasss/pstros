// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.media;

import javax.microedition.media.control.PlayerVolumeControl;
import ole.pstros.MainApp;
import java.util.Vector;

class DummyPlayer implements Player
{
    protected static final String EMU_CONTROL_VOLUME = "VolumeControl";
    protected static final String EMU_CONTROL_VOLUME_FULL = "javax.microedition.media.control.VolumeControl";
    protected int oldState;
    protected int state;
    private int level;
    private Vector listeners;
    protected int xHash;
    
    public DummyPlayer() {
        this.level = 100;
        this.oldState = 100;
        this.state = 100;
        this.listeners = new Vector(2);
    }
    
    public void addPlayerListener(final PlayerListener playerListener) throws IllegalStateException {
        if (playerListener == null) {
            return;
        }
        if (MainApp.verbose) {
            System.out.println("Player=" + this + " addPlayerListener listener=" + playerListener);
        }
        this.listeners.add(playerListener);
    }
    
    public void close() {
        if (MainApp.soundVerbose) {
            System.out.println("Player@" + this.hashCode() + " close()");
        }
        this.state = 0;
        this.emuReportEvent("closed", null);
    }
    
    public void deallocate() throws IllegalStateException {
        if (MainApp.soundVerbose) {
            System.out.println("Player@" + this.hashCode() + " deallocate()");
        }
        this.state = 200;
    }
    
    public String getContentType() throws IllegalStateException {
        return null;
    }
    
    public long getDuration() throws IllegalStateException {
        if (MainApp.soundVerbose) {
            System.out.println("Player@" + this.hashCode() + " getDuration()");
        }
        return 0L;
    }
    
    public long getMediaTime() {
        if (MainApp.soundVerbose) {
            System.out.println("Player@" + this.hashCode() + " getMediaTime()");
        }
        if (this.state == 0) {
            throw new IllegalStateException();
        }
        return 0L;
    }
    
    public int getState() {
        if (MainApp.soundVerbose) {
            System.out.println("Player@" + this.hashCode() + " getState()");
        }
        return this.state;
    }
    
    public void prefetch() throws MediaException {
        if (MainApp.soundVerbose) {
            System.out.println("Player@" + this.hashCode() + " prefetch()");
        }
        this.state = 300;
    }
    
    public void realize() throws MediaException {
        if (MainApp.soundVerbose) {
            System.out.println("Player@" + this.hashCode() + " realize()");
        }
        this.state = 200;
    }
    
    public void removePlayerListener(final PlayerListener playerListener) throws IllegalStateException {
        if (playerListener == null) {
            return;
        }
        this.listeners.remove(playerListener);
    }
    
    public void setLoopCount(final int count) {
        if (count == 0) {
            throw new IllegalArgumentException("count is invalid");
        }
        if (this.state == 400 || this.state == 0) {
            throw new IllegalStateException();
        }
    }
    
    public long setMediaTime(final long now) throws MediaException {
        if (this.state == 100 || this.state == 0) {
            throw new IllegalStateException();
        }
        return 0L;
    }
    
    public void start() throws MediaException {
        if (MainApp.soundVerbose) {
            System.out.println("Player@" + this.hashCode() + " start()");
        }
        if (this.state == 0) {
            throw new IllegalStateException("Player is in the CLOSED state.");
        }
        this.startImpl();
        this.state = 400;
        this.emuReportEvent("started", null);
    }
    
    protected void startImpl() throws MediaException {
    }
    
    public void stop() throws MediaException {
        if (MainApp.soundVerbose) {
            System.out.println("Player@" + this.hashCode() + " stop()");
        }
        if (this.state == 0) {
            throw new IllegalStateException("Player is in the CLOSED state.");
        }
        this.stopImpl();
        this.state = 300;
        this.oldState = 400;
        this.xHash = this.hashCode();
    }
    
    public void stopImpl() throws MediaException {
    }
    
    public Control getControl(final String controlType) {
        if (MainApp.soundVerbose) {
            System.out.println("Player@" + this.hashCode() + " getControl() " + controlType);
        }
        if (controlType.equals("VolumeControl") || controlType.equals("javax.microedition.media.control.VolumeControl")) {
            return new PlayerVolumeControl(this);
        }
        return null;
    }
    
    public Control[] getControls() {
        return null;
    }
    
    public void emuUpdatePlayer() {
    }
    
    public int emuGetVolumeLevel() {
        return this.level;
    }
    
    public int emuSetVolumeLevel(final int level) {
        return this.level = level;
    }
    
    protected void emuReportEvent(final String event, final Object data) {
        final int size = this.listeners.size();
        if (size < 1) {
            return;
        }
        if (MainApp.verbose) {
            System.out.println("Player=" + this + " reporting the event=" + event);
        }
        for (int i = 0; i < size; ++i) {
            final PlayerListener listener = (PlayerListener)this.listeners.get(i);
            listener.playerUpdate(this, event, data);
        }
    }
}
