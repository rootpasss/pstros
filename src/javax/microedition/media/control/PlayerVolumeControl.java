// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.media.control;

import javax.microedition.media.Player;

public class PlayerVolumeControl implements VolumeControl
{
    private Player player;
    private boolean muted;
    
    public PlayerVolumeControl(final Player p) {
        this.player = p;
    }
    
    public int getLevel() {
        if (this.player != null) {
            return this.player.emuGetVolumeLevel();
        }
        return 0;
    }
    
    public boolean isMuted() {
        return this.muted;
    }
    
    public int setLevel(final int level) {
        if (this.player != null) {
            return this.player.emuSetVolumeLevel(level);
        }
        return 0;
    }
    
    public void setMute(final boolean mute) {
        this.muted = mute;
    }
}
