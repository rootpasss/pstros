// 
// Decompiled by Procyon v0.5.36
// 

package com.nokia.mid.ui;

public class DeviceControl
{
    public static void setLights(final int num, final int level) {
        if (num > 0 || num < 0) {
            throw new IllegalArgumentException("light num is not supported. num=" + num);
        }
        if (level < 0 || level > 100) {
            throw new IllegalArgumentException("level is not between 0-100 :" + level);
        }
    }
    
    public static void flashLights(final long duration) {
        if (duration < 0L) {
            throw new IllegalArgumentException("duration < 0 :" + duration);
        }
    }
    
    public static void startVibra(final int freq, final long duration) {
    }
    
    public static void stopVibra() {
    }
}
