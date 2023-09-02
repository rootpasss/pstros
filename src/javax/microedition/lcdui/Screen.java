// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.awt.font.FontRenderContext;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.util.Vector;
import java.awt.Rectangle;

public class Screen extends Displayable
{
    static Rectangle tmpRect;
    protected Vector items;
    int selected;
    static Font screenFont;
    
    static {
        Screen.tmpRect = new Rectangle();
    }
    
    public Screen() {
        this.items = new Vector();
        this.selected = 0;
        this.emuCreateBridge();
        if (Screen.screenFont == null) {
            Screen.screenFont = new Font("Dialog", 0, 12);
        }
    }
    
    void emuPaint(final Graphics g) {
        final Font oldFont = g.getFont();
        g.setFont(Screen.screenFont);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, Display.WIDTH, Display.HEIGHT);
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, Display.WIDTH, 16);
        g.fillRect(0, Display.HEIGHT - 16, Display.WIDTH, 16);
        g.setColor(Color.BLACK);
        if (this.title != null) {
            g.drawString(this.title, 0, 12);
        }
        if (this.leftCommand != null) {
            g.drawString(this.leftCommand.getLabel(), 0, Display.HEIGHT - 4);
        }
        if (this.rightCommand != null) {
            final Rectangle2D rect = Screen.screenFont.getStringBounds(this.rightCommand.getLabel(), new FontRenderContext(null, false, false));
            g.drawString(this.rightCommand.getLabel(), Display.WIDTH - (int)rect.getWidth() - 1, Display.HEIGHT - 4);
        }
        g.setClip(0, 16, Display.WIDTH, Display.HEIGHT - 16 - 16);
        this.emuPaintScreenContent(g);
        g.setClip(0, 0, Display.WIDTH, Display.HEIGHT);
        g.setFont(oldFont);
    }
    
    void emuPaintScreenContent(final Graphics g) {
        System.out.println("Pstros: Screen: implement emuPaintContent for:" + this);
    }
    
    boolean emuKeyAction(final int key, final int keyChar, final int modifiers, final int action) {
        super.emuKeyAction(key, keyChar, modifiers, action);
        if (action == 0) {
            if ((this.emuKeyStates & 0x2) != 0x0) {
                this.moveSelectionUp();
            }
            else if ((this.emuKeyStates & 0x40) != 0x0) {
                this.moveSelectionDown();
            }
            else if ((this.emuKeyStates & 0x100) != 0x0) {
                this.emuFirePressed();
            }
        }
        return true;
    }
    
    private void moveSelectionDown() {
        if (this.items.size() < 1) {
            return;
        }
        Object item = this.items.get(this.selected);
        boolean nextItem = true;
        if (item instanceof Item && ((Item)item).emuIsInteractive()) {
            nextItem = ((Item)item).emuMoveSelectionDown();
        }
        if (nextItem) {
            final int max = this.items.size();
            if (this.selected < max - 1) {
                ++this.selected;
                item = this.items.get(this.selected);
                if (item instanceof Spacer) {
                    if (this.selected < max - 1) {
                        ++this.selected;
                    }
                    else {
                        --this.selected;
                    }
                }
                this.emuUpdateScreen();
            }
        }
    }
    
    private void moveSelectionUp() {
        if (this.items.size() < 1) {
            return;
        }
        Object item = this.items.get(this.selected);
        boolean previousItem = true;
        boolean nextIsSpacer = false;
        if (item instanceof Item) {
            if (((Item)item).emuIsInteractive()) {
                previousItem = ((Item)item).emuMoveSelectionUp();
            }
            nextIsSpacer = (item instanceof Spacer);
        }
        if (previousItem && this.selected > 0) {
            --this.selected;
            item = this.items.get(this.selected);
            if (item instanceof Spacer) {
                if (this.selected > 0) {
                    --this.selected;
                }
                else {
                    ++this.selected;
                }
            }
            this.emuUpdateScreen();
        }
    }
    
    void emuFirePressed() {
        final int max = this.items.size();
        if (max < 1 || this.selected < 0 || this.selected > max - 1) {
            return;
        }
        final Object item = this.items.get(this.selected);
        if (item instanceof Item && ((Item)item).emuIsInteractive()) {
            ((Item)item).emuActionPressed();
        }
    }
}
