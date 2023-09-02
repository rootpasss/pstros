// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros;

public interface IEmuBridge
{
    public static final int EVENT_SET_PARENT = 0;
    public static final int EVENT_SHOW_NOTIFY = 1;
    public static final int EVENT_HIDE_NOTIFY = 2;
    public static final int EVENT_RUN_MIDLET = 3;
    public static final int EVENT_DESTROY_MIDLET = 4;
    public static final int EVENT_PAUSE_APP = 5;
    public static final int EVENT_START_APP = 6;
    public static final int EVENT_SET_EMU_CANVAS = 7;
    public static final int EVENT_PAINT = 8;
    public static final int EVENT_SET_SHOWN = 9;
    public static final int EVENT_IS_FULL_SCREEN = 10;
    public static final int EVENT_GET_LEFT_COMMAND = 11;
    public static final int EVENT_GET_RIGHT_COMMAND = 12;
    public static final int EVENT_KEY_ACTION = 13;
    public static final int EVENT_POINTER_PRESSED = 14;
    public static final int EVENT_POINTER_RELEASED = 15;
    public static final int EVENT_POINTER_DRAGGED = 16;
    public static final int EVENT_GET_GRAPHICS = 17;
    public static final int EVENT_GET_KEY_STATES = 18;
    public static final int EVENT_FLUSH_GRAPHICS = 19;
    public static final int EVENT_GET_IMAGE = 20;
    public static final int EVENT_GET_COLLISION = 21;
    public static final int EVENT_CLEAN_KEYS = 22;
    public static final int EVENT_SCREEN_SIZE_CHANGED = 23;
    
    Object handleEvent(final int p0, final Object p1);
    
    Object handleEvent(final int p0, final Object p1, final Object p2);
    
    int getIntResult();
}
