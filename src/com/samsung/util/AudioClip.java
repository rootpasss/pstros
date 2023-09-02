// 
// Decompiled by Procyon v0.5.36
// 

package com.samsung.util;

public class AudioClip
{
    public AudioClip() {
    }
    
    public AudioClip(final int type, final String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("passed empty name!");
        }
    }
    
    public AudioClip(final int type, final byte[] data, final int start, final int length) {
    }
    
    public static boolean isSupported() {
        return false;
    }
    
    public void pause() {
    }
    
    public void play(final int loopCount, final int volume) {
    }
    
    public void resume() {
    }
    
    public void stop() {
    }
}
