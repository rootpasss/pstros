// 
// Decompiled by Procyon v0.5.36
// 

package com.gcjsp.v10.sound;

public class SoundTrack
{
    public static final int NO_DATA = 0;
    public static final int READY = 1;
    public static final int PLAYING = 2;
    public static final int PAUSED = 3;
    private int panpot;
    private Sound snd;
    private int state;
    private SoundTrackListener listener;
    private SoundTrack masterTrack;
    int volume;
    boolean mute;
    int loopCount;
    
    public SoundTrack() {
        this.panpot = 64;
        this.volume = 100;
    }
    
    public int getID() {
        return 0;
    }
    
    public int getPanpot() {
        return this.panpot;
    }
    
    public void setPanpot(final int value) {
        this.panpot = value;
    }
    
    public Sound getSound() {
        return this.snd;
    }
    
    public void setSound(final Sound p) {
        this.snd = p;
    }
    
    public int getState() {
        return this.state;
    }
    
    public SoundTrack getSyncMaster() {
        return this.masterTrack;
    }
    
    public void setSubjectTo(final SoundTrack master) {
        this.masterTrack = master;
    }
    
    public int getVolume() {
        return this.volume;
    }
    
    public void setVolume(final int value) {
        this.volume = value;
        if (this.volume < 0) {
            this.volume = 0;
        }
        if (this.volume > 127) {
            this.volume = 127;
        }
    }
    
    public boolean isMute() {
        return this.mute;
    }
    
    public void mute(final boolean mute) {
        this.mute = mute;
    }
    
    public void pause() {
        this.state = 3;
    }
    
    public void stop() {
        this.state = 1;
    }
    
    public void play() {
        if (this.state == 2) {
            return;
        }
        this.state = 2;
    }
    
    public void play(final int loop) {
        if (this.state == 2) {
            return;
        }
        this.state = 2;
        this.loopCount = loop;
    }
    
    public void removeSound() {
        this.snd = null;
    }
    
    public void resume() {
        this.state = 2;
    }
    
    public void setEventListener(final SoundTrackListener l) {
        this.listener = l;
    }
}
