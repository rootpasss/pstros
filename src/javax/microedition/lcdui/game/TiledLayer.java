// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui.game;

import javax.microedition.lcdui.Display;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.awt.image.BufferedImage;
import ole.pstros.EmuCanvas;
import javax.microedition.lcdui.Image;

public class TiledLayer extends Layer
{
    private static final int MAX_ANIM_TILES = 64;
    private static final int ANCHOR = 20;
    int maxX;
    int maxY;
    int imageW;
    int imageH;
    int imageX;
    int imageY;
    int tileW;
    int tileH;
    Image image;
    java.awt.Image[] tiles;
    boolean[][] solid;
    short[] tMap;
    int mapSize;
    short[] aMap;
    int aMax;
    
    public TiledLayer(final int columns, final int rows, final Image image, final int tileWidth, final int tileHeight) {
        if (image == null) {
            throw new NullPointerException("image is null!");
        }
        if (columns < 1 || rows < 1) {
            throw new IllegalArgumentException("columns=" + columns + " rows=" + rows);
        }
        this.maxX = columns;
        this.maxY = rows;
        this.mapSize = this.maxX * this.maxY;
        this.tMap = new short[this.mapSize];
        this.aMax = 0;
        this.aMap = new short[64];
        this.setStaticTileSet(image, tileWidth, tileHeight);
        this.setVisible(true);
    }
    
    public void setStaticTileSet(final Image image, final int tileWidth, final int tileHeight) {
        if (image == null) {
            throw new NullPointerException("image is null!");
        }
        if (tileWidth < 1 || tileHeight < 1) {
            throw new IllegalArgumentException("tileWidth=" + tileWidth + " tileHeight=" + tileHeight);
        }
        final int oldTileCount = this.imageX * this.imageY;
        final int imageW = image.getWidth();
        final int imageH = image.getHeight();
        if (imageW % tileWidth != 0) {
            throw new IllegalArgumentException("not an integer multiple! tileWidth=" + tileWidth + " imageWidth=" + imageW);
        }
        if (imageH % tileHeight != 0) {
            throw new IllegalArgumentException("not an integer multiple! tileHeight=" + tileHeight + " imageHeight=" + imageH);
        }
        this.image = image;
        this.tileW = tileWidth;
        this.tileH = tileHeight;
        this.imageX = imageW / this.tileW;
        this.imageY = imageH / this.tileH;
        this.layerWidth = this.maxX * this.tileW;
        this.layerHeight = this.maxY * this.tileH;
        final int size = this.imageX * this.imageY;
        this.tiles = new java.awt.Image[size];
        this.solid = new boolean[size][];
        final java.awt.Image img = (java.awt.Image)EmuCanvas.instance.lcduiBridge.handleEvent(20, image, new int[1]);
        for (int i = 0; i < size; ++i) {
            this.tiles[i] = new BufferedImage(this.tileW, this.tileH, 2);
            final Graphics g = this.tiles[i].getGraphics();
            final int tileX = i % this.imageX;
            final int tileY = i / this.imageX;
            g.drawImage(img, -tileX * this.tileW, -tileY * this.tileH, null);
        }
        if (size < oldTileCount) {
            for (int i = 0; i < this.mapSize; ++i) {
                this.tMap[i] = 0;
            }
            for (int i = 0; i < this.aMax; ++i) {
                this.aMap[i] = 0;
            }
            this.aMax = 0;
        }
    }
    
    public int createAnimatedTile(final int staticTileIndex) {
        if (staticTileIndex < 1 || staticTileIndex > this.imageX * this.imageY) {
            throw new IndexOutOfBoundsException("staticTileIndex=" + staticTileIndex + " max allowed value=" + this.imageX * this.imageY);
        }
        this.aMap[this.aMax] = (short)staticTileIndex;
        ++this.aMax;
        return -this.aMax;
    }
    
    public void setAnimatedTile(final int animatedTileIndex, final int staticTileIndex) {
        if (staticTileIndex < 1 || staticTileIndex > this.imageX * this.imageY) {
            throw new IndexOutOfBoundsException("staticTileIndex=" + staticTileIndex + " max allowed value=" + this.imageX * this.imageY);
        }
        if (-animatedTileIndex > this.aMax) {
            throw new IndexOutOfBoundsException("animatedTileIndex=" + animatedTileIndex + " max allowed value=" + -this.aMax);
        }
        this.aMap[-animatedTileIndex - 1] = (short)staticTileIndex;
    }
    
    public int getAnimatedTile(final int animatedTileIndex) {
        if (-animatedTileIndex > this.aMax) {
            throw new IndexOutOfBoundsException("animatedTileIndex=" + animatedTileIndex + " max allowed value=" + -this.aMax);
        }
        return this.aMap[-animatedTileIndex - 1];
    }
    
    public void setCell(final int col, final int row, final int tileIndex) {
        if (col >= this.maxX || row >= this.maxY) {
            throw new IndexOutOfBoundsException("col=" + col + " max allowed column=" + this.maxX + " row=" + row + " max alowed row=" + this.maxY);
        }
        this.tMap[row * this.maxX + col] = (short)tileIndex;
    }
    
    public int getCell(final int col, final int row) {
        if (col >= this.maxX || row >= this.maxY) {
            throw new IndexOutOfBoundsException("col=" + col + " max allowed column=" + this.maxX + " row=" + row + " max alowed row=" + this.maxY);
        }
        return this.tMap[row * this.maxX + col];
    }
    
    public final int getCellWidth() {
        return this.tileW;
    }
    
    public final int getCellHeight() {
        return this.tileH;
    }
    
    public final int getColumns() {
        return this.maxX;
    }
    
    public final int getRows() {
        return this.maxY;
    }
    
    public void fillCells(final int col, final int row, final int numCols, final int numRows, final int tileIndex) {
        final int maxRow = row + numRows;
        final int maxCol = col + numCols;
        if (numCols < 1 || numRows < 0 || col >= this.maxX || maxCol > this.maxX || row >= this.maxY || maxRow > this.maxY) {
            throw new IllegalArgumentException("invalid region specified!");
        }
        for (int j = row; j < maxRow; ++j) {
            int idx = j * this.maxX + col;
            for (int i = col; i < maxCol; ++i) {
                this.tMap[idx] = (short)tileIndex;
                ++idx;
            }
        }
    }
    
    public void paint(final javax.microedition.lcdui.Graphics g) {
        if (!this.isVisible()) {
            return;
        }
        final int x = this.getX();
        final int y = this.getY();
        final int trX = g.getTranslateX();
        final int trY = g.getTranslateY();
        int x2 = (-x - trX) / this.tileW;
        int x3 = x2 + Display.WIDTH / this.tileW + 2;
        int y2 = (-y - trY) / this.tileH;
        int y3 = y2 + Display.HEIGHT / this.tileH + 2;
        if (x2 < 0) {
            x2 = 0;
        }
        if (x3 > this.maxX) {
            x3 = this.maxX;
        }
        if (y2 < 0) {
            y2 = 0;
        }
        if (y3 > this.maxY) {
            y3 = this.maxY;
        }
        for (int j = y2; j < y3; ++j) {
            int mapIndex = j * this.maxX + x2;
            final int posY = y + j * this.tileH;
            int posX = x + x2 * this.tileW;
            for (int i = x2; i < x3; ++i) {
                int tileIndex = this.tMap[mapIndex];
                if (tileIndex != 0) {
                    if (tileIndex < 0) {
                        tileIndex = this.aMap[-tileIndex - 1];
                    }
                    if (--tileIndex > -1) {
                        if (tileIndex < this.tiles.length) {
                            g.emuDrawImage(this.tiles[tileIndex], posX, posY);
                        }
                        else {
                            System.out.println("Pstros: internal error: TiledLayer: oti=" + this.tMap[mapIndex] + " max=" + this.tiles.length);
                        }
                    }
                }
                ++mapIndex;
                posX += this.tileW;
            }
        }
    }
    
    private boolean[] emuCreateSolid(final int tileIndex) {
        final BufferedImage bi = (BufferedImage)this.tiles[tileIndex];
        final int[] rgb = new int[this.tileW * this.tileH];
        final boolean[] data = new boolean[this.tileW * this.tileH];
        bi.getRGB(0, 0, this.tileW, this.tileH, rgb, 0, this.tileW);
        for (int i = 0; i < data.length; ++i) {
            data[i] = ((rgb[i] & 0xFF000000) == 0xFF000000);
        }
        return data;
    }
    
    boolean emuCollideWithPoint(final int pointX, final int pointY) {
        final int x = this.getX();
        final int y = this.getY();
        final int x2 = (-x + pointX) / this.tileW;
        final int y2 = (-y + pointY) / this.tileH;
        if (x2 < 0 || y2 < 0 || x2 >= this.maxX || y2 >= this.maxY) {
            return false;
        }
        final int mapIndex = y2 * this.maxX + x2;
        final int tileIndex = this.tMap[mapIndex];
        return tileIndex != 0;
    }
    
    boolean emuPixelCollision(final int sX, final int sW, final int sY, final int sH, final int sOffset, final int sScanLength, final boolean[] sSolid) {
        final int cX = this.getX();
        final int cY = this.getY();
        for (int pointY = 0; pointY < sH; ++pointY) {
            int sIndex = sOffset + sScanLength * pointY;
            final int pixelY;
            int y1 = pixelY = -cY + pointY + sY;
            final int y2 = y1 % this.tileH;
            y1 /= this.tileH;
            if (pixelY >= 0) {
                if (y1 < this.maxY) {
                    for (int pointX = 0; pointX < sW; ++pointX) {
                        if (sSolid[sIndex]) {
                            final int pixelX;
                            int x1 = pixelX = -cX + pointX + sX;
                            final int x2 = x1 % this.tileW;
                            x1 /= this.tileW;
                            if (pixelX >= 0 && x1 < this.maxX) {
                                final int mapIndex = y1 * this.maxX + x1;
                                int tileIndex = this.tMap[mapIndex];
                                if (tileIndex != 0) {
                                    if (tileIndex < 0) {
                                        tileIndex = this.aMap[-tileIndex - 1];
                                    }
                                    if (--tileIndex >= 0) {
                                        boolean[] dSolid = this.solid[tileIndex];
                                        if (dSolid == null) {
                                            dSolid = this.emuCreateSolid(tileIndex);
                                            this.solid[tileIndex] = dSolid;
                                        }
                                        if (dSolid[y2 * this.tileW + x2]) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                        ++sIndex;
                    }
                }
            }
        }
        return false;
    }
}
