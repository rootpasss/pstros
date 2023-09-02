// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros;

public interface EmuListener
{
    public static final String EMU_EVENT_REFRESH_DISPLAY = "emuRefreshDisplay";
    public static final String EMU_EVENT_STARTED = "emuStarted";
    public static final String EMU_EVENT_CLOSED = "emuClosed";
    public static final String EMU_EVENT_ERROR = "emuError";
    public static final String EMU_EVENT_PLATFORM_REQUEST = "emuPlatformRequest";
    
    void emuUpadate(final String p0, final Object p1);
}
