// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import java.awt.Color;
import java.awt.Graphics;
import ole.pstros.utils.Pair;
import ole.pstros.MainApp;
import java.util.Vector;

public class ChoiceGroup extends Item implements Choice
{
    private static final String NONE = "";
    private int type;
    protected Vector items;
    protected int selected;
    private int fitPolicy;
    private Font font;
    private boolean[] flags;
    private static final int[] triangleX;
    private static final int[] triangleY;
    
    static {
        triangleX = new int[] { 0, 8, 4 };
        triangleY = new int[] { -4, -4, 0 };
    }
    
    public ChoiceGroup(final String label, final int choiceType) {
        this.label = label;
        this.type = choiceType;
        this.items = new Vector();
        this.selected = -1;
        this.font = Font.getDefaultFont();
        this.emuInteractive = true;
        if (choiceType != 4) {
            this.emuMultiElement = true;
        }
    }
    
    public ChoiceGroup(final String label, final int choiceType, final String[] stringElements, final Image[] imageElements) {
        this(label, choiceType);
        for (int size = stringElements.length, i = 0; i < size; ++i) {
            Image img = null;
            if (imageElements != null) {
                img = imageElements[i];
            }
            this.append(stringElements[i], img);
        }
        if (choiceType != 4) {
            this.emuMultiElement = true;
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
        this.updateFlags(-1);
        return this.items.indexOf(p);
    }
    
    public void delete(final int elementNum) {
        this.items.remove(elementNum);
        this.selected = 0;
        if (this.items.size() < 1) {
            this.selected = -1;
        }
    }
    
    public void deleteAll() {
        this.items.removeAllElements();
        this.selected = -1;
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
    
    public void insert(final int elementNum, final String stringPart, final Image imagePart) {
        if (this.selected == -1) {
            this.selected = 0;
        }
        final Pair p = new Pair(stringPart, imagePart);
        this.items.insertElementAt(p, elementNum);
        this.updateFlags(elementNum);
    }
    
    public void set(final int elementNum, final String stringPart, final Image imagePart) {
        final Pair p = (Pair)this.items.get(elementNum);
        p.set(stringPart, imagePart);
    }
    
    public boolean isSelected(final int elementNum) {
        return this.selected == elementNum;
    }
    
    public int getSelectedIndex() {
        return this.selected;
    }
    
    public int getSelectedFlags(final boolean[] selectedArray_return) {
        int result = 0;
        for (int i = 0; i < this.flags.length; ++i) {
            selectedArray_return[i] = this.flags[i];
            if (this.flags[i]) {
                ++result;
            }
        }
        for (int i = this.flags.length; i < selectedArray_return.length; ++i) {
            selectedArray_return[i] = false;
        }
        if (this.type == 4 || (this.type == 1 && this.selected >= 0)) {
            selectedArray_return[this.selected] = true;
            return 1;
        }
        return result;
    }
    
    public void setSelectedIndex(final int elementNum, final boolean selected) {
        boolean update = false;
        if (this.type == 2 || this.type == 1) {
            this.flags[elementNum] = selected;
            update = true;
        }
        else if (selected && elementNum >= 0 && elementNum < this.items.size()) {
            this.selected = elementNum;
            update = true;
        }
        if (update) {
            this.emuUpdateScreen();
        }
    }
    
    public void setSelectedFlags(final boolean[] selectedArray) {
        if (this.flags == null) {
            this.flags = new boolean[this.items.size()];
        }
        if (this.type == 2 || this.type == 1) {
            for (int i = 0; i < this.flags.length; ++i) {
                this.flags[i] = selectedArray[i];
            }
        }
        else {
            for (int i = 0; i < this.flags.length; ++i) {
                if (selectedArray[i]) {
                    this.selected = i;
                    break;
                }
            }
        }
        this.emuUpdateScreen();
    }
    
    public void setFitPolicy(final int fitPolicy) {
        this.fitPolicy = fitPolicy;
    }
    
    public int getFitPolicy() {
        return this.fitPolicy;
    }
    
    public void setFont(final int elementNum, Font font) {
        if (font == null) {
            font = Font.getDefaultFont();
        }
        else {
            this.font = font;
        }
    }
    
    public Font getFont(final int elementNum) {
        return this.font;
    }
    
    private void updateFlags(final int startIndex) {
        final int size = this.items.size();
        if (size == 1) {
            this.flags = new boolean[1];
            return;
        }
        final boolean[] tmpFlags = new boolean[size];
        if (startIndex < 0) {
            System.arraycopy(this.flags, 0, tmpFlags, 0, this.flags.length);
        }
        else {
            if (startIndex > 0) {
                System.arraycopy(this.flags, 0, tmpFlags, 0, startIndex);
            }
            System.arraycopy(this.flags, startIndex, tmpFlags, startIndex + 1, this.flags.length - startIndex);
        }
        this.flags = tmpFlags;
    }
    
    int emuPaint(final Graphics g, final int x, int y) {
        final java.awt.Font font = g.getFont();
        final int height = font.getSize() + 2;
        final int width = Display.WIDTH - x - 2;
        final int origY = y;
        Color paintColor = Color.BLACK;
        if (this.emuActive) {
            paintColor = Color.RED;
        }
        g.setColor(paintColor);
        if (this.label != null) {
            g.drawString(this.label, x + 2, y + 1);
            y += height + 2;
        }
        Pair p = (Pair)this.items.get(this.selected);
        if (this.type == 4) {
            if (this.emuActive) {
                g.setColor(ChoiceGroup.COLOR_HIGHLIGT);
                g.fillRect(x, y - height + 2, Display.WIDTH - 4, height + 2);
                g.setColor(paintColor);
            }
            g.drawString((String)p.getFirst(), x + 2, y + 1);
            g.drawRect(x, y - height + 2, Display.WIDTH - 4, height + 2);
            g.translate(Display.WIDTH - 16, y);
            g.fillPolygon(ChoiceGroup.triangleX, ChoiceGroup.triangleY, 3);
            g.translate(-(Display.WIDTH - 16), -y);
            y += height + 2;
            y += 4;
        }
        else if (this.type == 2) {
            for (int size = this.items.size(), i = 0; i < size; ++i) {
                p = (Pair)this.items.get(i);
                if (i == this.selected && this.emuActive) {
                    g.setColor(ChoiceGroup.COLOR_HIGHLIGT);
                    g.fillRect(x, y - height + 2, Display.WIDTH - 4, height + 2);
                    g.setColor(paintColor);
                }
                g.drawRect(x + 2, y - height + 4, 8, height - 2);
                if (this.flags[i]) {
                    g.drawLine(x + 4, y - height + 6, x + 8, y);
                    g.drawLine(x + 4, y, x + 8, y - height + 6);
                }
                g.drawString((String)p.getFirst(), x + 12, y + 1);
                y += height + 2;
            }
            y += 2;
        }
        else if (this.type == 1) {
            for (int size = this.items.size(), i = 0; i < size; ++i) {
                p = (Pair)this.items.get(i);
                if (i == this.selected && this.emuActive) {
                    g.setColor(ChoiceGroup.COLOR_HIGHLIGT);
                    g.fillRect(x, y - height + 2, Display.WIDTH - 4, height + 2);
                    g.setColor(paintColor);
                }
                g.drawRect(x + 2, y - height + 4, 8, height - 2);
                if (this.flags[i]) {
                    g.fillRect(x + 4, y - height + 7, 5, height - 7);
                }
                g.drawString((String)p.getFirst(), x + 12, y + 1);
                y += height + 2;
            }
            y += 2;
        }
        return y - origY;
    }
    
    int emuGetHeight(final Graphics g) {
        final java.awt.Font font = g.getFont();
        int height = font.getSize();
        final int h = height + 4;
        if (this.type == 4) {
            height = h;
            if (this.label != null && this.label.length() > 0) {
                height += h;
            }
            height += 4;
        }
        else if (this.type == 2 || this.type == 1) {
            height = 0;
            if (this.label != null && this.label.length() > 0) {
                height = h;
            }
            final int size = this.items.size();
            height += h * size;
            height += 2;
        }
        return height;
    }
    
    int emuGetElementHeight(final Graphics g) {
        if (!this.emuMultiElement) {
            return this.emuGetHeight(g);
        }
        final java.awt.Font font = g.getFont();
        int height = font.getSize();
        final int h = height + 4;
        height = 0;
        if (this.label != null && this.label.length() > 0) {
            height = h;
        }
        height += h * (this.selected + 1);
        height += 2;
        return height;
    }
    
    int emuGetYSpace(final Graphics g) {
        final java.awt.Font font = g.getFont();
        return font.getSize() + 3;
    }
    
    void emuActionPressed() {
        boolean update = false;
        if (this.type == 4) {
            final int max = this.items.size();
            ++this.selected;
            if (this.selected >= max) {
                this.selected = 0;
            }
            update = true;
        }
        else if (this.type == 2) {
            this.flags[this.selected] = !this.flags[this.selected];
        }
        else if (this.type == 1) {
            for (int i = 0; i < this.flags.length; ++i) {
                this.flags[i] = false;
            }
            this.flags[this.selected] = true;
        }
        if (update) {
            this.emuUpdateScreen();
        }
    }
    
    boolean emuMoveSelectionDown() {
        if (this.type != 2 && this.type != 1) {
            return true;
        }
        if (this.selected == this.items.size() - 1) {
            return true;
        }
        ++this.selected;
        this.emuUpdateScreen();
        return false;
    }
    
    boolean emuMoveSelectionUp() {
        if (this.type != 2 && this.type != 1) {
            return true;
        }
        if (this.selected == 0) {
            return true;
        }
        --this.selected;
        this.emuUpdateScreen();
        return false;
    }
}
