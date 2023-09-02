// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.rms;

public interface RecordComparator
{
    public static final int EQUIVALENT = 0;
    public static final int FOLLOWS = 1;
    public static final int PRECEDES = -1;
    
    int compare(final byte[] p0, final byte[] p1);
}
