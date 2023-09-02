// 
// Decompiled by Procyon v0.5.36
// 

package com.vodafone.v10.sound;

import java.io.IOException;

public class Sound
{
    private byte[] data;
    
    public Sound(final String url) throws IOException {
    }
    
    public Sound(final byte[] data) {
        if (data == null) {
            throw new NullPointerException("sound data is null!");
        }
        this.data = data;
    }
    
    public int getSize() {
        if (this.data == null) {
            return 0;
        }
        return this.data.length;
    }
    
    public int getUseTracks() {
        return 0;
    }
}
