// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import java.awt.image.ImageObserver;
import java.awt.Color;
import java.awt.Graphics;
import ole.pstros.utils.Pair;
import ole.pstros.MainApp;

public class List extends Screen implements Choice
{
    public static final Command SELECT_COMMAND;
    private int type;
    private Command selectCommand;
    private int emuOffsY;
    private int fitPolicy;
    
    static {
        SELECT_COMMAND = new Command("Select", 1, 0);
    }
    
    private List() {
        this.addCommand(List.SELECT_COMMAND);
        this.fitPolicy = 0;
    }
    
    public List(final String title, final int listType) {
        this();
        this.setTitle(title);
        this.type = listType;
    }
    
    public List(final String title, final int listType, final String[] stringElements, final Image[] imageElements) {
        this(title, listType);
        for (int size = stringElements.length, i = 0; i < size; ++i) {
            Image img = null;
            if (imageElements != null) {
                img = imageElements[i];
            }
            this.append(stringElements[i], img);
        }
    }
    
    public int append(final String stringPart, final Image imagePart) {
        if (this.selected == -1) {
            this.selected = 0;
        }
        if (MainApp.verbose) {
            System.out.println("append: " + stringPart + " image=" + imagePart);
        }
        final Pair p = new Pair(stringPart, imagePart);
        this.items.add(p);
        return this.items.indexOf(p);
    }
    
    public void delete(final int elementNum) {
        this.items.remove(elementNum);
        this.selected = 0;
        if (this.items.size() < 1) {
            this.selected = 0;
        }
    }
    
    public void deleteAll() {
        this.items.clear();
        this.selected = 0;
    }
    
    public int getSelectedFlags(final boolean[] selectedArray_return) {
        return 0;
    }
    
    public int getSelectedIndex() {
        return this.selected;
    }
    
    public void insert(final int elementNum, final String stringPart, final Image imagePart) {
        if (!MainApp.verbose) {
            System.out.println("append: " + stringPart + " image=" + imagePart);
        }
        final Pair p = new Pair(stringPart, imagePart);
        this.items.insertElementAt(p, elementNum);
    }
    
    public boolean isSelected(final int elementNum) {
        return this.selected == elementNum;
    }
    
    public void set(final int elementNum, final String stringPart, final Image imagePart) {
        final Pair p = (Pair)this.items.get(elementNum);
        p.set(stringPart, imagePart);
    }
    
    public void setSelectedFlags(final boolean[] selectedArray) {
    }
    
    public void setSelectedIndex(final int elementNum, final boolean selected) {
        if (this.selected == elementNum && !selected) {
            this.selected = 0;
        }
        else {
            this.selected = elementNum;
        }
    }
    
    public void setSelectCommand(final Command c) {
        this.selectCommand = c;
        if (c == null) {
            super.removeCommand(List.SELECT_COMMAND);
        }
        else {
            this.addCommand(c);
        }
    }
    
    public void setFont(final int elementNum, final Font font) {
    }
    
    public Font getFont(final int elementNum) {
        return Font.getDefaultFont();
    }
    
    public void removeCommand(final Command cmd) {
        if (this.selectCommand == cmd) {
            this.selectCommand = null;
        }
        super.removeCommand(cmd);
    }
    
    public void setFitPolicy(final int fitPolicy) {
        this.fitPolicy = fitPolicy;
    }
    
    public int getFitPolicy() {
        return this.fitPolicy;
    }
    
    public int size() {
        return this.items.size();
    }
    
    public String getString(final int elementNum) {
        final Pair pair = (Pair)this.items.get(elementNum);
        if (pair == null) {
            return null;
        }
        return (String)pair.getFirst();
    }
    
    public Image getImage(final int elementNum) {
        final Pair pair = (Pair)this.items.get(elementNum);
        if (pair == null) {
            return null;
        }
        return (Image)pair.getSecond();
    }
    
    void emuFirePressed() {
        final int max = this.items.size();
        if (max < 1 || this.selected < 0 || this.selected > max - 1) {
            return;
        }
        if (this.selectCommand != null && this.listener != null) {
            this.listener.commandAction(this.selectCommand, this);
        }
    }
    
    void emuPaintScreenContent(final Graphics g) {
        final int size = this.items.size();
        int sliderY = 0;
        int sliderH = 0;
        final java.awt.Font font = g.getFont();
        final int itemHeight = font.getSize() + 4;
        g.getClipBounds(List.tmpRect);
        int y = sliderY = this.selected * itemHeight;
        sliderH = itemHeight;
        if (this.emuOffsY + y + itemHeight > List.tmpRect.height) {
            this.emuOffsY = List.tmpRect.height - y - itemHeight;
        }
        else if (this.emuOffsY + y < 0) {
            this.emuOffsY = -y;
        }
        y = 0;
        for (int i = 0; i < size; ++i) {
            final Pair item = (Pair)this.items.get(i);
            y += this.emuPaintItem(g, 0, this.emuOffsY + y + List.tmpRect.y + itemHeight, (String)item.getFirst(), (Image)item.getSecond(), i == this.selected, itemHeight);
        }
        if (y > List.tmpRect.height) {
            g.setColor(Item.COLOR_HIGHLIGT);
            g.fillRect(Display.WIDTH - 2, List.tmpRect.y, 2, List.tmpRect.height);
            final int posY = sliderY * List.tmpRect.height / y;
            final int sliderSize = sliderH * List.tmpRect.height / y + 1;
            g.setColor(Color.RED);
            g.fillRect(Display.WIDTH - 2, List.tmpRect.y + posY, 2, sliderSize);
        }
    }
    
    private int emuPaintItem(final Graphics g, int x, final int y, final String text, final Image icon, final boolean selected, final int itemHeight) {
        if (text == null) {
            return 0;
        }
        if (selected) {
            g.setColor(Item.COLOR_HIGHLIGT);
            g.fillRect(x, y - itemHeight, Display.WIDTH - 4, itemHeight);
            g.setColor(Color.RED);
        }
        else {
            g.setColor(Color.BLACK);
        }
        if (icon != null) {
            g.drawImage(icon.emuGetImage(0), x + 2, y - itemHeight, null);
            x += icon.getWidth();
        }
        g.drawString(text, x + 2, y - 2);
        return itemHeight;
    }
}
