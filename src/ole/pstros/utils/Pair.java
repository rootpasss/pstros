// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

public class Pair
{
    private Object o1;
    private Object o2;
    
    public Pair() {
    }
    
    public Pair(final Object first, final Object second) {
        this.set(first, second);
    }
    
    public void set(final Object first, final Object second) {
        this.o1 = first;
        this.o2 = second;
    }
    
    public void setFirst(final Object first) {
        this.o1 = first;
    }
    
    public void setSecond(final Object second) {
        this.o2 = second;
    }
    
    public Object getFirst() {
        return this.o1;
    }
    
    public Object getSecond() {
        return this.o2;
    }
}
