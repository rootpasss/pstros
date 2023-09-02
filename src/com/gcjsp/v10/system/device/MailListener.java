// 
// Decompiled by Procyon v0.5.36
// 

package com.gcjsp.v10.system.device;

public interface MailListener
{
    public static final int SMS = 1;
    public static final int MMS = 2;
    public static final int CBS = 3;
    public static final int WEB = 4;
    
    void received(final int p0);
}
