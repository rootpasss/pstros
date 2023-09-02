// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui.game;

import javax.microedition.lcdui.Graphics;
import ole.pstros.IEmuBridge;
import ole.pstros.EmuCanvas;
import ole.pstros.MainApp;
import javax.microedition.lcdui.Image;

public class Sprite extends Layer
{
    private static final int ANCHOR = 20;
    public static final int TRANS_NONE = 0;
    public static final int TRANS_MIRROR_ROT180 = 1;
    public static final int TRANS_MIRROR = 2;
    public static final int TRANS_ROT180 = 3;
    public static final int TRANS_MIRROR_ROT270 = 4;
    public static final int TRANS_ROT90 = 5;
    public static final int TRANS_ROT270 = 6;
    public static final int TRANS_MIRROR_ROT90 = 7;
    private static int[] tmpPoint;
    Image image;
    int frameW;
    int frameH;
    int[] frameSeq;
    int imgW;
    int imgH;
    int transform;
    int transformDX;
    int transformDY;
    int spotX;
    int spotY;
    private int posX;
    private int posY;
    private int curFrame;
    int collisionX;
    int collisionY;
    int collisionW;
    int collisionH;
    int cX;
    int cY;
    int cW;
    int cH;
    
    static {
        Sprite.tmpPoint = new int[4];
    }
    
    public Sprite(final Image image) {
        this(image, image.getWidth(), image.getHeight());
    }
    
    public Sprite(final Image image, final int frameWidth, final int frameHeight) {
        this.setImage(image, frameWidth, frameHeight);
        this.setVisible(true);
    }
    
    public Sprite(final Sprite s) {
        this(s.image, s.frameW, s.frameH);
        this.setFrameSequence(s.frameSeq);
        this.spotX = s.spotX;
        this.spotY = s.spotY;
        this.transform = s.transform;
        this.transformDX = s.transformDX;
        this.transformDY = s.transformDY;
        this.cX = s.cX;
        this.cY = s.cY;
        this.cW = s.cW;
        this.cH = s.cH;
    }
    
    public void setFrameSequence(final int[] sequence) {
        this.frameSeq = sequence;
    }
    
    public void defineReferencePixel(final int rX, final int rY) {
        this.posX += rX - this.spotX;
        this.posY += rY - this.spotY;
        this.spotX = rX;
        this.spotY = rY;
    }
    
    public void setRefPixelPosition(final int rX, final int rY) {
        this.posX = rX;
        this.posY = rY;
        this.emuUpdateTransformedDistances();
    }
    
    public int getRefPixelX() {
        return this.posX;
    }
    
    public int getRefPixelY() {
        return this.posY;
    }
    
    public void setPosition(final int pX, final int pY) {
        this.posX = pX + this.spotX - this.transformDX;
        this.posY = pY + this.spotY - this.transformDY;
        super.setPosition(pX, pY);
    }
    
    public void move(final int dx, final int dy) {
        this.posX += dx;
        this.posY += dy;
        super.move(dx, dy);
    }
    
    public void setFrame(final int sequenceIndex) {
        this.curFrame = sequenceIndex;
    }
    
    public final int getFrame() {
        return this.curFrame;
    }
    
    public int getRawFrameCount() {
        return this.imgW * this.imgH;
    }
    
    public int getFrameSequenceLength() {
        if (this.frameSeq == null) {
            return this.imgW * this.imgH;
        }
        return this.frameSeq.length;
    }
    
    public void nextFrame() {
        int max;
        if (this.frameSeq == null) {
            max = this.imgW * this.imgH;
        }
        else {
            max = this.frameSeq.length;
        }
        ++this.curFrame;
        if (this.curFrame >= max) {
            this.curFrame = 0;
        }
    }
    
    public void prevFrame() {
        int max;
        if (this.frameSeq == null) {
            max = this.imgW * this.imgH;
        }
        else {
            max = this.frameSeq.length;
        }
        --this.curFrame;
        if (this.curFrame < 0) {
            this.curFrame = max - 1;
        }
    }
    
    public void setImage(final Image img, final int frameWidth, final int frameHeight) {
        this.image = img;
        if (this.frameW != frameWidth || this.frameH != frameHeight) {
            this.collisionX = 0;
            this.collisionY = 0;
            this.cW = frameWidth;
            this.collisionW = frameWidth;
            this.cH = frameHeight;
            this.collisionH = frameHeight;
        }
        this.frameW = frameWidth;
        this.frameH = frameHeight;
        this.imgW = this.image.getWidth() / this.frameW;
        this.imgH = this.image.getHeight() / this.frameH;
        this.layerWidth = frameWidth;
        this.layerHeight = frameHeight;
        this.frameSeq = null;
        this.curFrame = 0;
    }
    
    public void setTransform(final int transform) {
        switch (transform) {
            case 0:
            case 1:
            case 2:
            case 3: {
                this.layerWidth = this.frameW;
                this.layerHeight = this.frameH;
                this.cW = this.collisionW;
                this.cH = this.collisionH;
                this.transform = transform;
                break;
            }
            case 4:
            case 5:
            case 6:
            case 7: {
                this.layerWidth = this.frameH;
                this.layerHeight = this.frameW;
                this.cW = this.collisionH;
                this.cH = this.collisionW;
                this.transform = transform;
                break;
            }
            default: {
                throw new IllegalArgumentException("invalid transform=" + transform);
            }
        }
        this.emuUpdateTransformedDistances();
    }
    
    private void emuUpdateTransformedDistances() {
        switch (this.transform) {
            case 0: {
                this.transformDX = 0;
                this.transformDY = 0;
                this.cX = this.collisionX;
                this.cY = this.collisionY;
                break;
            }
            case 1: {
                this.transformDX = 0;
                this.transformDY = -(this.frameH - 1 - 2 * this.spotY);
                this.cX = this.collisionX;
                this.cY = this.frameH - this.collisionY - this.collisionH;
                break;
            }
            case 2: {
                this.transformDX = -(this.frameW - 1 - 2 * this.spotX);
                this.transformDY = 0;
                this.cX = this.frameW - this.collisionX - this.collisionW;
                this.cY = this.collisionY;
                break;
            }
            case 3: {
                this.transformDX = -(this.frameW - 1 - 2 * this.spotX);
                this.transformDY = -(this.frameH - 1 - 2 * this.spotY);
                this.cX = this.frameW - this.collisionX - this.collisionW;
                this.cY = this.frameH - this.collisionY - this.collisionH;
                break;
            }
            case 4: {
                this.transformDX = this.spotX - this.spotY;
                this.transformDY = -(this.spotX - this.spotY);
                this.cX = this.collisionY;
                this.cY = this.collisionX;
                break;
            }
            case 5: {
                this.transformDX = -(this.frameH - this.spotX - this.spotY - 1);
                this.transformDY = -(this.spotX - this.spotY);
                this.cX = this.frameH - this.collisionY - this.collisionH;
                this.cY = this.collisionX;
                break;
            }
            case 6: {
                this.transformDX = this.spotX - this.spotY;
                this.transformDY = -(this.frameW - this.spotX - this.spotY - 1);
                this.cX = this.collisionY;
                this.cY = this.frameW - this.collisionX - this.collisionW;
                break;
            }
            case 7: {
                this.transformDX = -(this.frameH - this.spotX - this.spotY - 1);
                this.transformDY = -(this.frameW - this.spotX - this.spotY - 1);
                this.cX = this.frameH - this.collisionY - this.collisionH;
                this.cY = this.frameW - this.collisionX - this.collisionW;
                break;
            }
        }
        this.x = this.posX - this.spotX + this.transformDX;
        this.y = this.posY - this.spotY + this.transformDY;
    }
    
    public void defineCollisionRectangle(final int x, final int y, final int width, final int height) {
        this.collisionX = x;
        this.collisionY = y;
        this.cW = width;
        this.collisionW = width;
        this.cH = height;
        this.collisionH = height;
        this.emuUpdateTransformedDistances();
    }
    
    private boolean emuRectCollision(final int sx, final int sw, final int sy, final int sh, final int dx, final int dw, final int dy, final int dh) {
        final int sx2 = sx;
        final int sx3 = sx + sw;
        final int sy2 = sy;
        final int sy3 = sy + sh;
        final int dx2 = dx;
        final int dx3 = dx + dw;
        final int dy2 = dy;
        final int dy3 = dy + dh;
        return sx3 >= dx2 && sx2 <= dx3 && sy3 >= dy2 && sy2 <= dy3;
    }
    
    private boolean emuPixelCollision(final int sx, final int sw, final int sy, final int sh, final int dx, final int dw, final int dy, final int dh, final int sOffset, final int sScanLength, final boolean[] sSolid, final int dOffset, final int dScanLength, final boolean[] dSolid) {
        final int sx2 = sx;
        final int sx3 = sx + sw;
        final int sy2 = sy;
        final int sy3 = sy + sh;
        final int dx2 = dx;
        final int dx3 = dx + dw;
        final int dy2 = dy;
        final int dy3 = dy + dh;
        final int rectX = (sx > dx) ? sx : dx;
        final int rectY = (sy > dy) ? sy : dy;
        final int rectW = (sx3 > dx3) ? (dx3 - rectX) : (sx3 - rectX);
        final int rectH = (sy3 > dy3) ? (dy3 - rectY) : (sy3 - rectY);
        final int sOffsX = rectX - sx;
        final int dOffsX = rectX - dx;
        final int sOffsY = rectY - sy;
        final int dOffsY = rectY - dy;
        if (dSolid == null) {
            System.out.println("!!! dsolid is null");
            return false;
        }
        if (sSolid == null) {
            System.out.println("!!! ssolid is null");
            return false;
        }
        for (int y = 0; y < rectH; ++y) {
            int sIndex = sOffset + (sOffsY + y) * sScanLength + sOffsX;
            int dIndex = dOffset + (dOffsY + y) * dScanLength + dOffsX;
            for (int x = 0; x < rectW; ++x) {
                if (sIndex > sSolid.length) {
                    System.out.println("!! Source: max=" + sSolid.length);
                }
                if (dIndex > dSolid.length) {
                    System.out.println("!! Destin: max=" + dSolid.length + " current=" + dIndex + " rW=" + rectW + " rH=" + rectH + " dOY=" + dOffsY + " y=" + y + " x=" + x + " dScan=" + dScanLength + " dOffs=" + dOffset + " imgW=" + this.image.getWidth() + " imgH=" + this.image.getHeight());
                }
                if (sSolid[sIndex++] && dSolid[dIndex++]) {
                    return true;
                }
            }
        }
        return false;
    }
    
    int emuGetFrameIndex() {
        if (this.frameSeq == null) {
            return this.curFrame;
        }
        if (this.curFrame >= this.frameSeq.length) {
            this.curFrame = 0;
        }
        return this.frameSeq[this.curFrame];
    }
    
    void emuTranformCoordinate(int x_src, int y_src, int width, int height, final int[] result) {
        final Image img = this.image;
        int imgWidth = 0;
        switch (this.transform) {
            case 0: {
                imgWidth = img.getWidth();
                break;
            }
            case 1: {
                y_src = img.getHeight() - y_src - height;
                imgWidth = img.getWidth();
                break;
            }
            case 2: {
                x_src = img.getWidth() - x_src - width;
                imgWidth = img.getWidth();
                break;
            }
            case 3: {
                y_src = img.getHeight() - y_src - height;
                x_src = img.getWidth() - x_src - width;
                imgWidth = img.getWidth();
                break;
            }
            case 4: {
                int tmp = width;
                width = height;
                height = tmp;
                tmp = x_src;
                x_src = y_src;
                y_src = tmp;
                imgWidth = img.getHeight();
                break;
            }
            case 5: {
                imgWidth = img.getHeight();
                int tmp = width;
                width = height;
                height = tmp;
                tmp = x_src;
                x_src = y_src;
                y_src = tmp;
                x_src = img.getHeight() - x_src - width;
                break;
            }
            case 6: {
                imgWidth = img.getHeight();
                int tmp = width;
                width = height;
                height = tmp;
                tmp = x_src;
                x_src = y_src;
                y_src = tmp;
                y_src = img.getWidth() - y_src - height;
                break;
            }
            case 7: {
                imgWidth = img.getHeight();
                int tmp = width;
                width = height;
                height = tmp;
                tmp = x_src;
                x_src = y_src;
                y_src = tmp;
                y_src = img.getWidth() - y_src - height;
                x_src = img.getHeight() - x_src - width;
                break;
            }
            default: {
                if (this.transform != 0) {
                    System.out.println(" unsupported transform ! " + this.transform);
                    return;
                }
                break;
            }
        }
        result[0] = x_src;
        result[1] = y_src;
        result[2] = imgWidth;
    }
    
    public final boolean collidesWith(final Sprite s, final boolean pixelLevel) {
        if (MainApp.verbose) {
            System.out.println("Sprite@" + this.hashCode() + " collidesWith(Sprite) pixelLevel=" + pixelLevel);
        }
        if (!this.isVisible() || !s.isVisible()) {
            return false;
        }
        final int srcX = this.getX() + this.cX;
        final int srcY = this.getY() + this.cY;
        final int dstX = s.getX() + s.cX;
        final int dstY = s.getY() + s.cY;
        boolean result = this.emuRectCollision(srcX, this.cW, srcY, this.cH, dstX, s.cW, dstY, s.cH);
        if (result && pixelLevel) {
            final int sImgIndex = this.emuGetFrameIndex();
            final int dImgIndex = s.emuGetFrameIndex();
            int sImgPosX = sImgIndex % this.imgW * this.frameW;
            int sImgPosY = sImgIndex / this.imgW * this.frameH;
            this.emuTranformCoordinate(sImgPosX, sImgPosY, this.frameW, this.frameH, Sprite.tmpPoint);
            sImgPosX = Sprite.tmpPoint[0];
            sImgPosY = Sprite.tmpPoint[1];
            final int sScanLength = Sprite.tmpPoint[2];
            final int sOffset = (sImgPosY + this.cY) * sScanLength + (sImgPosX + this.cX);
            int dImgPosX = dImgIndex % s.imgW * s.frameW;
            int dImgPosY = dImgIndex / s.imgW * s.frameH;
            s.emuTranformCoordinate(dImgPosX, dImgPosY, s.frameW, s.frameH, Sprite.tmpPoint);
            dImgPosX = Sprite.tmpPoint[0];
            dImgPosY = Sprite.tmpPoint[1];
            final int dScanLength = Sprite.tmpPoint[2];
            final int dOffset = (dImgPosY + s.cY) * dScanLength + (dImgPosX + s.cX);
            final IEmuBridge bridge = EmuCanvas.instance.lcduiBridge;
            if (bridge == null) {
                return false;
            }
            Sprite.tmpPoint[0] = this.transform;
            final boolean[] srcCollision = (boolean[])bridge.handleEvent(21, this.image, Sprite.tmpPoint);
            Sprite.tmpPoint[0] = s.transform;
            final boolean[] dstCollision = (boolean[])bridge.handleEvent(21, s.image, Sprite.tmpPoint);
            result = this.emuPixelCollision(srcX, this.cW, srcY, this.cH, dstX, s.cW, dstY, s.cH, sOffset, sScanLength, srcCollision, dOffset, dScanLength, dstCollision);
        }
        return result;
    }
    
    public final boolean collidesWith(final TiledLayer t, final boolean pixelLevel) {
        if (MainApp.verbose) {
            System.out.println("Sprite@" + this.hashCode() + " collidesWith(TiledLayer)  pixelLevel=" + pixelLevel);
        }
        if (t == null || !t.isVisible() || !this.isVisible()) {
            return false;
        }
        final int srcX = this.getX() + this.cX;
        final int srcY = this.getY() + this.cY;
        if (!pixelLevel) {
            return t.emuCollideWithPoint(srcX, srcY) || t.emuCollideWithPoint(srcX + this.cW, srcY) || t.emuCollideWithPoint(srcX, srcY + this.cH) || t.emuCollideWithPoint(srcX + this.cW, srcY + this.cH);
        }
        final boolean c1 = t.emuCollideWithPoint(srcX, srcY);
        final boolean c2 = t.emuCollideWithPoint(srcX + this.cW, srcY);
        final boolean c3 = t.emuCollideWithPoint(srcX, srcY + this.cH);
        final boolean c4 = t.emuCollideWithPoint(srcX + this.cW, srcY + this.cH);
        if (!c1 && !c2 && !c3 && !c4) {
            return false;
        }
        final int sImgIndex = this.emuGetFrameIndex();
        int sImgPosX = sImgIndex % this.imgW * this.frameW;
        int sImgPosY = sImgIndex / this.imgW * this.frameH;
        this.emuTranformCoordinate(sImgPosX, sImgPosY, this.frameW, this.frameH, Sprite.tmpPoint);
        sImgPosX = Sprite.tmpPoint[0];
        sImgPosY = Sprite.tmpPoint[1];
        final int sScanLength = Sprite.tmpPoint[2];
        final int sOffset = (sImgPosY + this.cY) * sScanLength + (sImgPosX + this.cX);
        final IEmuBridge bridge = EmuCanvas.instance.lcduiBridge;
        if (bridge == null) {
            return false;
        }
        Sprite.tmpPoint[0] = this.transform;
        final boolean[] srcCollision = (boolean[])bridge.handleEvent(21, this.image, Sprite.tmpPoint);
        return t.emuPixelCollision(srcX, this.cW, srcY, this.cH, sOffset, sScanLength, srcCollision);
    }
    
    public final boolean collidesWith(final Image image, final int x, final int y, final boolean pixelLevel) {
        if (MainApp.verbose) {
            System.out.println("Sprite@" + this.hashCode() + " collidesWith(Image)");
        }
        return this.emuRectCollision(this.getX() + this.cX, this.cW, this.getY() + this.cY, this.cH, x, image.getWidth(), y, image.getHeight());
    }
    
    public void paint(final Graphics g) {
        if (!this.isVisible()) {
            return;
        }
        final int imgIndex = this.emuGetFrameIndex();
        final int paintX = this.getX();
        final int paintY = this.getY();
        if (MainApp.verbose) {
            System.out.println("Sprite@" + this.hashCode() + " paint x=" + paintX + " y=" + paintY + " spotX=" + this.spotX + " spotY=" + this.spotY + " frW=" + this.frameW + " frameH=" + this.frameH + " transform=" + this.transform);
        }
        final int imgPosX = imgIndex % this.imgW * this.frameW;
        final int imgPosY = imgIndex / this.imgW * this.frameH;
        g.drawRegion(this.image, imgPosX, imgPosY, this.frameW, this.frameH, this.transform, paintX, paintY, 20);
    }
}
