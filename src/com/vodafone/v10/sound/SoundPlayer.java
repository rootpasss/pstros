// 
// Decompiled by Procyon v0.5.36
// 

package com.vodafone.v10.sound;

public class SoundPlayer
{
    private static final int MAX_TRACKS = 16;
    private SoundTrack[] tracks;
    
    private SoundPlayer() {
        this.tracks = new SoundTrack[16];
    }
    
    public void disposePlayer() {
    }
    
    public void disposeTrack(final SoundTrack t) {
        if (t == null) {
            throw new NullPointerException("soundTrack is null!");
        }
        for (int i = 0; i < 16; ++i) {
            if (this.tracks[i] == t) {
                this.tracks[i] = null;
            }
        }
    }
    
    public static SoundPlayer getPlayer() {
        return new SoundPlayer();
    }
    
    public SoundTrack getTrack() {
        for (int i = 0; i < 16; ++i) {
            if (this.tracks[i] == null) {
                return this.tracks[i] = new SoundTrack();
            }
        }
        throw new IllegalStateException("no more tracks available!");
    }
    
    public SoundTrack getTrack(final int track) {
        if (track >= 16 || track < 0) {
            throw new IllegalArgumentException();
        }
        if (this.tracks[track] != null) {
            throw new IllegalStateException("track is in use");
        }
        return this.tracks[track] = new SoundTrack();
    }
    
    public int getTrackCount() {
        int count = 0;
        for (int i = 0; i < 16; ++i) {
            if (this.tracks[i] == null) {
                ++count;
            }
        }
        return count;
    }
    
    public void kill() {
        for (int i = 0; i < 16; ++i) {
            final SoundTrack st = this.tracks[i];
            if (st != null) {
                st.stop();
                st.setSound(null);
            }
        }
    }
    
    public void pause() {
        for (int i = 0; i < 16; ++i) {
            final SoundTrack st = this.tracks[i];
            if (st != null) {
                st.pause();
            }
        }
    }
    
    public void resume() {
        for (int i = 0; i < 16; ++i) {
            final SoundTrack st = this.tracks[i];
            if (st != null) {
                st.resume();
            }
        }
    }
}
