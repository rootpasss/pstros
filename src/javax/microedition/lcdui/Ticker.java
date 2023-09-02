// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

public class Ticker
{
    private String line;
    
    public Ticker(final String s) {
        this.setString(s);
    }
    
    public void setString(final String str) {
        this.line = str;
    }
    
    public String getString() {
        return this.line;
    }
}
