// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros;

import java.awt.event.KeyEvent;
import java.awt.Component;

public interface EmuExecutor
{
    public static final int KEY_ACTION_PRESS = 0;
    public static final int KEY_ACTION_RELEASE = 1;
    
    void setEmuListener(final EmuListener p0);
    
    void setParentComponent(final Component p0);
    
    void setParameter(final String p0);
    
    Object getParameter(final String p0);
    
    boolean execute();
    
    void terminate();
    
    void pauseApp();
    
    void startApp();
    
    void showNotify();
    
    void hideNotify();
    
    void keyPressed(final KeyEvent p0);
    
    void keyReleased(final KeyEvent p0);
    
    void keyAction(final int p0, final int p1);
    
    void setClassLoader(final ClassLoader p0);
    
    void setEvent(final String p0, final Object p1);
}
