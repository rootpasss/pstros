// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import java.awt.Graphics;

public class TextBox extends Screen
{
    private static int MAX_SIZE;
    private String text;
    private int maxSize;
    private int constraints;
    private int caretPosition;
    private String charSubset;
    
    static {
        TextBox.MAX_SIZE = 8192;
    }
    
    public TextBox(final String title, final String text, final int maxSize, final int constraints) {
        this.setTitle(title);
        this.setMaxSize(maxSize);
        this.text = text;
        this.constraints = constraints;
    }
    
    public String getString() {
        return this.text;
    }
    
    public void setString(final String text) {
        this.text = text;
    }
    
    public int getChars(final char[] data) {
        if (this.text == null) {
            return 0;
        }
        final int length = this.text.length();
        this.text.getChars(0, length, data, 0);
        return length;
    }
    
    public void setChars(final char[] data, final int offset, final int length) {
        final String newText = new String(data, offset, length);
        this.text = newText;
    }
    
    public void insert(final String src, final int position) {
        final StringBuffer sb = new StringBuffer(this.text);
        sb.insert(position, src);
        this.text = sb.toString();
    }
    
    public void insert(final char[] data, final int offset, final int length, final int position) {
        final String newString = new String(data, offset, length);
        this.insert(newString, position);
    }
    
    public void delete(final int offset, final int length) {
        final StringBuffer sb = new StringBuffer(this.text);
        sb.delete(offset, offset + length);
        this.text = sb.toString();
    }
    
    public int getMaxSize() {
        return this.maxSize;
    }
    
    public int setMaxSize(int maxSize) {
        if (maxSize > TextBox.MAX_SIZE) {
            maxSize = TextBox.MAX_SIZE;
        }
        this.maxSize = maxSize;
        if (this.text != null && this.text.length() > maxSize) {
            this.text = this.text.substring(0, maxSize);
        }
        return maxSize;
    }
    
    public int size() {
        return (this.text == null) ? 0 : this.text.length();
    }
    
    public int getCaretPosition() {
        return this.caretPosition;
    }
    
    public void setConstraints(final int constraints) {
        this.constraints = constraints;
    }
    
    public int getConstraints() {
        return this.constraints;
    }
    
    public void setInitialInputMode(final String characterSubset) {
        this.charSubset = characterSubset;
    }
    
    void emuPaintScreenContent(final Graphics g) {
        final int y = 30;
        if (this.text != null) {
            g.drawString(this.text, 0, y);
        }
    }
    
    boolean emuKeyAction(final int key, final int keyChar, final int modifiers, final int action) {
        super.emuKeyAction(key, keyChar, modifiers, action);
        if (action == 0) {
            if (key == Display.keyLeft) {
                if (this.text != null) {
                    final int length = this.text.length();
                    ++this.caretPosition;
                    if (this.caretPosition > length) {
                        this.caretPosition = length;
                    }
                }
            }
            else if (key == Display.keyRight && this.text != null) {
                --this.caretPosition;
                if (this.caretPosition < 0) {
                    this.caretPosition = 0;
                }
            }
        }
        return true;
    }
}
