// 
// Decompiled by Procyon v0.5.36
// 

package com.nokia.mid.sound;

public class Sound
{
    public static final int FORMAT_TONE = 1;
    public static final int FORMAT_WAV = 5;
    public static final int SOUND_PLAYING = 0;
    public static final int SOUND_STOPPED = 1;
    public static final int SOUND_UNINITIALIZED = 3;
    private static final int[] SUPPORTED_FORMATS;
    private byte[] data;
    private int type;
    private int state;
    private int freq;
    private long duration;
    private int gain;
    private SoundListener listener;
    
    static {
        SUPPORTED_FORMATS = new int[] { 1, 5 };
    }
    
    public Sound(final byte[] data, final int type) {
        this.init(data, type);
    }
    
    public Sound(final int freq, final long duration) {
        this.init(freq, duration);
    }
    
    public static int getConcurrentSoundCount(final int type) {
        return 1;
    }
    
    public static int[] getSupportedFormats() {
        return Sound.SUPPORTED_FORMATS;
    }
    
    public void init(final byte[] data, int type) {
        type = 5;
        this.data = data;
        this.type = type;
        this.state = 1;
    }
    
    public void init(final int freq, final long duration) {
        this.type = 1;
        this.freq = freq;
        this.duration = duration;
        this.state = 1;
    }
    
    public void play(final int loop) {
        this.state = 0;
        this.emuNotifyListener();
    }
    
    public void stop() {
        this.state = 1;
        this.emuNotifyListener();
    }
    
    public void resume() {
        this.state = 0;
        this.emuNotifyListener();
    }
    
    public void setGain(final int gain) {
        this.gain = gain;
    }
    
    public int getGain() {
        return this.gain;
    }
    
    public int getState() {
        return this.state;
    }
    
    public void release() {
        this.data = null;
    }
    
    public void setSoundListener(final SoundListener listener) {
        this.listener = listener;
    }
    
    private void emuNotifyListener() {
        if (this.listener == null) {
            return;
        }
        this.listener.soundStateChanged(this, this.state);
    }
}
