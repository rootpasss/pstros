// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.media.control;

import javax.microedition.media.Control;

public interface VolumeControl extends Control
{
    int getLevel();
    
    int setLevel(final int p0);
    
    boolean isMuted();
    
    void setMute(final boolean p0);
}
