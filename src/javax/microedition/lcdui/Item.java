// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import java.awt.geom.Rectangle2D;
import java.awt.Font;
import java.util.StringTokenizer;
import java.awt.geom.AffineTransform;
import java.awt.font.FontRenderContext;
import ole.pstros.MainApp;
import java.awt.Graphics;
import java.awt.Color;

public class Item
{
    public static final int LAYOUT_DEFAULT = 0;
    public static final int LAYOUT_LEFT = 1;
    public static final int LAYOUT_RIGHT = 2;
    public static final int LAYOUT_CENTER = 3;
    public static final int LAYOUT_TOP = 16;
    public static final int LAYOUT_BOTTOM = 32;
    public static final int LAYOUT_VCENTER = 48;
    public static final int LAYOUT_NEWLINE_BEFORE = 256;
    public static final int LAYOUT_NEWLINE_AFTER = 512;
    public static final int LAYOUT_SHRINK = 1024;
    public static final int LAYOUT_EXPAND = 2048;
    public static final int LAYOUT_VSHRINK = 4096;
    public static final int LAYOUT_VEXPAND = 8192;
    public static final int LAYOUT_2 = 16384;
    public static final int PLAIN = 0;
    public static final int HYPERLINK = 1;
    public static final int BUTTON = 2;
    static final Color COLOR_HIGHLIGT;
    protected String label;
    protected int layout;
    protected Command command;
    protected ItemCommandListener listener;
    private Displayable emuDisplayable;
    boolean emuInteractive;
    boolean emuActive;
    boolean emuMultiElement;
    private static int[] emuResult;
    
    static {
        COLOR_HIGHLIGT = new Color(220, 220, 220);
        Item.emuResult = new int[3];
    }
    
    void emuSetDisplayable(final Displayable d) {
        this.emuDisplayable = d;
    }
    
    Displayable emuGetDisplayable() {
        return this.emuDisplayable;
    }
    
    protected void emuUpdateScreen() {
        if (this.emuDisplayable != null) {
            this.emuDisplayable.emuUpdateScreen();
        }
    }
    
    boolean emuIsInteractive() {
        return this.emuInteractive;
    }
    
    boolean emuIsMultiElement() {
        return this.emuMultiElement;
    }
    
    void emuSetActive(final boolean state) {
        this.emuActive = state;
        if (this.command != null && this.listener != null && state) {
            this.command.owner = this;
        }
    }
    
    void emuKeyAction(final int key, final int keyChar, final int action) {
    }
    
    public void setLabel(final String l) {
        this.label = l;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public int getLayout() {
        return this.layout;
    }
    
    public void setLayout(final int l) throws Exception {
        this.layout = l;
    }
    
    int emuPaint(final Graphics g, final int x, final int y) {
        if (MainApp.verbose) {
            System.out.println("Item.emuPaint()  not implemented for item=" + this);
        }
        return 0;
    }
    
    int emuGetHeight(final Graphics g) {
        if (MainApp.verbose) {
            System.out.println("Item.emuGetHeight()  not implemented for item=" + this);
        }
        return 0;
    }
    
    int emuGetElementHeight(final Graphics g) {
        if (!this.emuMultiElement) {
            return this.emuGetHeight(g);
        }
        if (MainApp.verbose) {
            System.out.println("Item.emuGetHeight()  not implemented for item=" + this);
        }
        return 0;
    }
    
    int emuGetYSpace(final Graphics g) {
        return 0;
    }
    
    void emuActionPressed() {
    }
    
    boolean emuMoveSelectionDown() {
        return true;
    }
    
    boolean emuMoveSelectionUp() {
        return true;
    }
    
    public void addCommand(final Command cmd) {
        this.command = cmd;
        if (this.command != null) {
            this.command.owner = this;
        }
    }
    
    public void removeCommand(final Command cmd) {
        if (this.command == cmd) {
            this.command = null;
        }
    }
    
    public void setDefaultCommand(final Command cmd) {
        this.command = cmd;
    }
    
    public void setItemCommandListener(final ItemCommandListener l) {
        this.listener = l;
    }
    
    ItemCommandListener emuGetItemCommandListener() {
        return this.listener;
    }
    
    static int[] emuDrawMultiLine(final Graphics g, final String text, int x, int y, final boolean draw) {
        Item.emuResult[0] = 0;
        Item.emuResult[1] = 0;
        Item.emuResult[2] = 0;
        if (text == null) {
            return Item.emuResult;
        }
        final int origX = x;
        final int origY = y;
        final Font font = g.getFont();
        final FontRenderContext frc = new FontRenderContext(null, false, false);
        final int spaceWidth = (int)font.getStringBounds(" ", frc).getWidth();
        final int fontHeight = (int)font.getStringBounds("I", frc).getHeight();
        final StringTokenizer tokenizer = new StringTokenizer(text, "\n");
        for (int size = tokenizer.countTokens(), j = 0; j < size; ++j) {
            final StringTokenizer lineTokenizer = new StringTokenizer(tokenizer.nextToken(), " ");
            for (int lineSize = lineTokenizer.countTokens(), i = 0; i < lineSize; ++i) {
                final String word = lineTokenizer.nextToken();
                final Rectangle2D rect = font.getStringBounds(word, frc);
                final int wordLength = (int)rect.getWidth();
                x += wordLength;
                if (x > Display.WIDTH) {
                    x = 0;
                    y += fontHeight + 2;
                }
                else {
                    x -= wordLength;
                }
                if (draw) {
                    g.drawString(word, x, y);
                }
                x += wordLength;
                x += spaceWidth;
            }
            if (j < size - 1) {
                x = 0;
                y += fontHeight + 2;
            }
        }
        Item.emuResult[0] = x - origX;
        Item.emuResult[1] = y - origY;
        Item.emuResult[2] = fontHeight + 2;
        return Item.emuResult;
    }
    
    Command emuGetDefaultCommand() {
        return this.command;
    }
}
