// 
// Decompiled by Procyon v0.5.36
// 

package com.nokia.mid.ui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class DirectUtils
{
    public static Image createImage(final byte[] imageData, final int imageOffset, final int imageLength) {
        if (imageData == null) {
            throw new NullPointerException("imageData is null.");
        }
        if (imageOffset >= imageData.length || imageOffset + imageLength > imageData.length) {
            throw new ArrayIndexOutOfBoundsException("imageOffset and imageLength specify an invalid range.");
        }
        return Image.createImage(imageData, imageOffset, imageLength);
    }
    
    public static Image createImage(final int width, final int height, final int ARGBcolor) {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("either width or height is zero or less.");
        }
        return Image.createImage(width, height, ARGBcolor);
    }
    
    public static DirectGraphics getDirectGraphics(final Graphics g) {
        return g;
    }
}
