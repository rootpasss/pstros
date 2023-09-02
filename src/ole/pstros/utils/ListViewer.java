// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import java.awt.event.AdjustmentEvent;
import java.awt.event.MouseEvent;
import ole.pstros.ConfigData;
import java.awt.event.WindowEvent;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.ScrollPaneAdjustable;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Color;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowListener;
import java.awt.Canvas;

public class ListViewer extends Canvas implements WindowListener, MouseListener, MouseMotionListener, AdjustmentListener
{
    private static final String[] COLUMN_TITLE;
    private static final int[] COLUMN_WIDTH;
    private static Color COLOR_WHITE;
    private static Color COLOR_BLACK;
    private static Color COLOR_RED;
    public static final Color COLOR_GRAY;
    public static final Color COLOR_LIGHT_GRAY;
    private ListItem[] items;
    private int itemCount;
    private Frame parent;
    private Container container;
    private ScrollPaneAdjustable vScroll;
    private int mouseX;
    private int mouseY;
    private int infoX;
    private int infoY;
    private int infoH;
    private int offsetY;
    private int fontAscent;
    private int fontDescent;
    private ListItem selectedItem;
    private String[] columnTitle;
    private int[] columnWidth;
    private Dimension itemsChange;
    private int columnMove;
    private int columnMoveX;
    
    static {
        COLUMN_TITLE = new String[] { "" };
        COLUMN_WIDTH = new int[] { 220 };
        ListViewer.COLOR_WHITE = new Color(16777215);
        ListViewer.COLOR_BLACK = new Color(0);
        ListViewer.COLOR_RED = new Color(16711680);
        COLOR_GRAY = new Color(10461087);
        COLOR_LIGHT_GRAY = new Color(13684944);
    }
    
    public ListViewer(final Frame parent, final Container container) {
        this.columnMove = -1;
        this.setBackground(ListViewer.COLOR_GRAY);
        this.parent = parent;
        this.container = container;
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.infoY = -1;
        this.columnTitle = ListViewer.COLUMN_TITLE;
        this.columnWidth = ListViewer.COLUMN_WIDTH;
    }
    
    public void setItems(final ListItem[] newItems, final int size) {
        this.items = newItems;
        this.itemCount = size;
    }
    
    public void notifyProfileChange() {
        this.itemsChange = this.getSize();
    }
    
    public void setColumnInfo(final String[] title, final int[] width) {
        this.columnTitle = title;
        this.columnWidth = width;
    }
    
    public void paint(final Graphics g) {
        final int size = this.itemCount;
        final int h = this.getFontHeight();
        final int w = this.getWidth() + 16;
        int posY = 2;
        int columnCount = 1;
        if (this.columnWidth != null) {
            columnCount = this.columnWidth.length;
        }
        if (this.infoH < 1) {
            final FontMetrics fm = g.getFontMetrics();
            this.infoH = fm.getHeight() + 5;
            this.fontAscent = fm.getAscent() + 2;
            this.fontDescent = fm.getDescent();
        }
        g.setColor(ListViewer.COLOR_LIGHT_GRAY);
        g.fillRect(0, posY - 1, w, h + 1);
        g.setColor(ListViewer.COLOR_BLACK);
        int posX = 5;
        for (int j = 0; j < columnCount; ++j) {
            final String value = this.columnTitle[j];
            if (value != null) {
                g.drawString(value, posX, posY + h - this.fontDescent);
                posX += this.columnWidth[j] - 4;
                g.drawLine(posX, posY - 1, posX, posY + h);
                posX += 4;
            }
        }
        posY += h;
        g.setColor(ListViewer.COLOR_BLACK);
        g.drawLine(2, posY, w - 20, posY);
        ++posY;
        for (int i = 0; i < size; ++i) {
            final ListItem item = this.items[i];
            g.setColor(ListViewer.COLOR_WHITE);
            g.setClip(0, posY, w, h);
            g.fillRect(0, posY, w, h);
            g.setColor(ListViewer.COLOR_BLACK);
            posX = 5;
            for (int k = 0; k < columnCount; ++k) {
                final int columnW = this.columnWidth[k];
                final String value = item.getColumnValue(k);
                if (value != null) {
                    g.setClip(posX, posY, columnW - 5, posY + h);
                    g.drawString(value, posX, posY + h - this.fontDescent);
                    posX += columnW - 4;
                    g.setClip(posX, posY - 1, 2, posY + h);
                    g.drawLine(posX, posY - 1, posX, posY + h);
                    posX += 4;
                }
            }
            posY += h;
            g.setColor(ListViewer.COLOR_BLACK);
            g.setClip(0, posY, w - 20, 2);
            g.drawLine(2, posY, w - 20, posY);
            ++posY;
        }
        if (this.itemsChange != null) {
            if (this.itemsChange.height > posY) {
                g.setColor(ListViewer.COLOR_GRAY);
                g.setClip(0, posY, this.getWidth(), this.itemsChange.height - posY);
                g.fillRect(0, posY, this.getWidth(), this.itemsChange.height - posY);
            }
            this.itemsChange = null;
        }
        if (this.columnMove > -1) {
            g.setColor(ListViewer.COLOR_RED);
            g.setClip(this.columnMoveX - 1, 0, 3, h);
            g.drawLine(this.columnMoveX, 0, this.columnMoveX, h);
        }
    }
    
    public void repaintItems() {
        if (this.items == null) {
            return;
        }
        if (this.itemsChange != null) {
            this.repaint();
            return;
        }
        final int h = this.getFontHeight();
        final int size = this.items.length;
        final int shiftY = (this.columnTitle != null) ? h : 0;
        final int w = this.getWidth();
        for (int i = 0; i < size; ++i) {
            final ListItem item = this.items[i];
            if (item.isChanged()) {
                final int posY = i * h + shiftY;
                this.repaint(0, posY, w, h + 2);
            }
        }
    }
    
    public void setVerticalScroll(final ScrollPaneAdjustable spa) {
        (this.vScroll = spa).addAdjustmentListener(this);
    }
    
    public Dimension getSize() {
        if (this.items == null) {
            return super.getSize();
        }
        int count = this.itemCount;
        int width = 260;
        if (this.columnTitle != null) {
            ++count;
        }
        if (this.columnWidth != null) {
            width = 0;
            for (int i = 0; i < this.columnWidth.length; ++i) {
                width += this.columnWidth[i];
            }
        }
        return new Dimension(width, (this.getFontHeight() + 2) * count);
    }
    
    private ListItem getItemAt(int y) {
        if (this.items == null) {
            return null;
        }
        final int fontHeight = this.getFontHeight() + 2;
        if (this.columnTitle != null) {
            y -= fontHeight;
        }
        final int index = y / fontHeight;
        if (index >= this.itemCount || y < 0) {
            return null;
        }
        return this.items[index];
    }
    
    private int getFontHeight() {
        final Font font = this.getFont();
        return font.getSize() + 4;
    }
    
    public void windowActivated(final WindowEvent e) {
        this.requestFocus();
    }
    
    public void windowClosed(final WindowEvent e) {
    }
    
    public void windowClosing(final WindowEvent e) {
        this.storeBounds();
        this.items = null;
        this.itemCount = 0;
        this.selectedItem = null;
        this.parent.dispose();
    }
    
    public void storeBounds() {
        ConfigData.classMonitorBounds = this.parent.getBounds();
    }
    
    public void windowDeactivated(final WindowEvent e) {
    }
    
    public void windowDeiconified(final WindowEvent e) {
    }
    
    public void windowIconified(final WindowEvent e) {
    }
    
    public void windowOpened(final WindowEvent e) {
    }
    
    public void mouseClicked(final MouseEvent e) {
    }
    
    public void mouseEntered(final MouseEvent e) {
    }
    
    public void mouseExited(final MouseEvent e) {
    }
    
    public void mousePressed(final MouseEvent e) {
        this.mouseY = e.getY();
        this.mouseX = e.getX();
        final int h = this.getFontHeight();
        if (this.mouseY < h) {
            int found = -1;
            int posX = 0;
            for (int i = 0; i < this.columnWidth.length; ++i) {
                posX += this.columnWidth[i];
                if (this.mouseX > posX - 2 && this.mouseX < posX + 4) {
                    found = i;
                    break;
                }
            }
            if (found > -1) {
                this.columnMove = found;
                this.columnMoveX = posX;
                this.repaint(0, 0, this.getWidth(), h);
            }
            return;
        }
        this.selectedItem = this.getItemAt(this.mouseY);
        if (this.selectedItem == null) {
            return;
        }
        this.infoX = this.mouseX;
        this.infoY = this.mouseY - this.infoH;
        if (this.infoY < 0) {
            this.infoY = 0;
        }
        this.repaint(this.infoX, this.infoY, 128, this.infoH);
    }
    
    public void mouseReleased(final MouseEvent e) {
        this.infoY = -1;
        if (this.columnMove > -1) {
            final int h = this.getFontHeight();
            if (e.getY() < h) {
                int posX = 0;
                for (int i = 0; i < this.columnMove + 1; ++i) {
                    posX += this.columnWidth[i];
                }
                final int diff = this.columnMoveX - posX;
                final int[] columnWidth = this.columnWidth;
                final int columnMove = this.columnMove;
                columnWidth[columnMove] += diff;
            }
            this.columnMove = -1;
        }
        this.repaint();
    }
    
    public void mouseDragged(final MouseEvent e) {
        if (this.columnMove > -1) {
            this.columnMoveX = e.getX();
            this.repaint(0, 0, this.getWidth(), 20);
        }
    }
    
    public void mouseMoved(final MouseEvent e) {
    }
    
    public void adjustmentValueChanged(final AdjustmentEvent e) {
        this.offsetY = e.getValue();
    }
}
