// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import java.awt.Color;
import java.awt.Graphics;

public class Form extends Screen
{
    private ItemStateListener itemListener;
    private int emuOffsY;
    
    public Form(final String title) {
        this.setTitle(title);
    }
    
    public Form(final String title, final Item[] it) {
        this(title);
        this.setItems(it);
    }
    
    boolean emuHasStringItem(final String text) {
        for (int max = this.items.size(), i = 0; i < max; ++i) {
            final Object item = this.items.get(i);
            if (item instanceof StringItem && ((StringItem)item).getText().equals(text)) {
                return true;
            }
        }
        return false;
    }
    
    public int append(final Item item) {
        this.items.add(item);
        item.emuSetDisplayable(this);
        final int position = this.items.indexOf(item);
        if (position == 0) {
            item.emuSetActive(true);
            final Command itemCommand = item.emuGetDefaultCommand();
            if (itemCommand != null) {
                this.addCommand(itemCommand);
            }
        }
        return position;
    }
    
    public int append(final String str) {
        final Item i = new StringItem(null, str);
        i.emuSetDisplayable(this);
        return this.append(i);
    }
    
    public int append(final Image img) {
        final Item i = new ImageItem(null, img, 0, null);
        i.emuSetDisplayable(this);
        return this.append(i);
    }
    
    public void insert(final int itemNum, final Item item) {
        item.emuSetDisplayable(this);
        this.items.add(itemNum, item);
    }
    
    public void delete(final int itemNum) {
        this.items.remove(itemNum);
    }
    
    public void deleteAll() {
        this.items.clear();
    }
    
    public void set(final int itemNum, final Item item) {
        item.emuSetDisplayable(this);
        this.items.set(itemNum, item);
    }
    
    public Item get(final int itemNum) {
        return (Item)this.items.get(itemNum);
    }
    
    public void setItemStateListener(final ItemStateListener iListener) {
        this.itemListener = iListener;
    }
    
    public int size() {
        return this.items.size();
    }
    
    private void setItems(final Item[] it) {
        if (it == null || it.length < 1) {
            return;
        }
        this.items.clear();
        for (int i = 0; i < it.length; ++i) {
            it[i].emuSetActive(i == 0);
            this.items.add(it[i]);
        }
    }
    
    void emuPaintScreenContent(final Graphics g) {
        final int size = this.items.size();
        int sliderY = 0;
        int sliderH = 0;
        if (size < 1) {
            return;
        }
        g.getClipBounds(Form.tmpRect);
        int y = 0;
        int i;
        for (i = 0; i < size && i < this.selected; ++i) {
            final Item item = (Item)this.items.get(i);
            final int itemHeight = item.emuGetHeight(g);
            y += itemHeight;
        }
        sliderY = y;
        Item item = (Item)this.items.get(i);
        int itemHeight;
        if (item.emuIsMultiElement()) {
            itemHeight = item.emuGetElementHeight(g);
            sliderH = item.emuGetHeight(g);
        }
        else {
            itemHeight = (sliderH = item.emuGetHeight(g));
        }
        if (this.emuOffsY + y + itemHeight > Form.tmpRect.height) {
            this.emuOffsY = Form.tmpRect.height - y - itemHeight;
        }
        else if (this.emuOffsY + y < 0) {
            this.emuOffsY = -y;
        }
        y = 0;
        for (i = 0; i < size; ++i) {
            item = (Item)this.items.get(i);
            final int shiftY = item.emuGetYSpace(g);
            final int height = item.emuPaint(g, 0, this.emuOffsY + y + shiftY + Form.tmpRect.y);
            y += height;
        }
        if (y > Form.tmpRect.height) {
            g.setColor(Item.COLOR_HIGHLIGT);
            g.fillRect(Display.WIDTH - 2, Form.tmpRect.y, 2, Form.tmpRect.height);
            final int posY = sliderY * Form.tmpRect.height / y;
            final int sliderSize = sliderH * Form.tmpRect.height / y + 1;
            g.setColor(Color.RED);
            g.fillRect(Display.WIDTH - 2, Form.tmpRect.y + posY, 2, sliderSize);
        }
    }
    
    boolean emuKeyAction(final int key, final int keyChar, final int modifiers, final int action) {
        if (action == 1) {
            super.emuKeyAction(key, keyChar, modifiers, action);
            return true;
        }
        Item item = null;
        if (this.selected >= 0) {
            try {
                item = (Item)this.items.get(this.selected);
            }
            catch (Exception ex) {}
            if (item != null) {
                item.emuSetActive(false);
            }
        }
        final int oldSelected = this.selected;
        super.emuKeyAction(key, keyChar, modifiers, action);
        if (oldSelected >= 0 && this.selected != oldSelected) {
            item = (Item)this.items.get(oldSelected);
            if (item != null) {
                final Command itemCommand = item.emuGetDefaultCommand();
                if (itemCommand != null) {
                    this.removeCommand(itemCommand);
                }
            }
        }
        if (this.selected >= 0) {
            item = (Item)this.items.get(this.selected);
            if (item != null) {
                final Command itemCommand = item.emuGetDefaultCommand();
                if (itemCommand != null) {
                    this.addCommand(itemCommand);
                }
                item.emuSetActive(true);
                if (item.emuIsInteractive()) {
                    item.emuKeyAction(key, keyChar, action);
                }
            }
        }
        this.emuUpdateScreen();
        return true;
    }
    
    void emuFirePressed() {
        final int max = this.items.size();
        if (max < 1 || this.selected < 0 || this.selected > max - 1) {
            return;
        }
        final Item item = (Item)this.items.get(this.selected);
        if (item.emuIsInteractive()) {
            item.emuActionPressed();
            if (this.itemListener != null) {
                this.itemListener.itemStateChanged(item);
            }
        }
    }
}
