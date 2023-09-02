// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.media;

public interface Player extends Controllable
{
    public static final int UNREALIZED = 100;
    public static final int REALIZED = 200;
    public static final int PREFETCHED = 300;
    public static final int STARTED = 400;
    public static final int CLOSED = 0;
    public static final long TIME_UNKNOWN = -1L;
    
    void realize() throws MediaException;
    
    void prefetch() throws MediaException;
    
    void start() throws MediaException;
    
    void stop() throws MediaException;
    
    void deallocate() throws IllegalStateException;
    
    void close();
    
    long setMediaTime(final long p0) throws MediaException;
    
    long getMediaTime();
    
    int getState();
    
    long getDuration() throws IllegalStateException;
    
    String getContentType() throws IllegalStateException;
    
    void setLoopCount(final int p0) throws Exception;
    
    void addPlayerListener(final PlayerListener p0) throws IllegalStateException;
    
    void removePlayerListener(final PlayerListener p0) throws IllegalStateException;
    
    void emuUpdatePlayer();
    
    int emuGetVolumeLevel();
    
    int emuSetVolumeLevel(final int p0);
}
