// 
// Decompiled by Procyon v0.5.36
// 

package com.nokia.mid.ui;

import javax.microedition.lcdui.Image;

public interface DirectGraphics
{
    public static final int FLIP_HORIZONTAL = 8192;
    public static final int FLIP_VERTICAL = 16384;
    public static final int ROTATE_90 = 90;
    public static final int ROTATE_180 = 180;
    public static final int ROTATE_270 = 270;
    public static final int TYPE_BYTE_1_GRAY = 1;
    public static final int TYPE_BYTE_1_GRAY_VERTICAL = -1;
    public static final int TYPE_BYTE_2_GRAY = 2;
    public static final int TYPE_BYTE_4_GRAY = 4;
    public static final int TYPE_BYTE_8_GRAY = 8;
    public static final int TYPE_BYTE_332_RGB = 332;
    public static final int TYPE_USHORT_4444_ARGB = 4444;
    public static final int TYPE_USHORT_444_RGB = 444;
    public static final int TYPE_USHORT_555_RGB = 555;
    public static final int TYPE_USHORT_1555_ARGB = 1555;
    public static final int TYPE_USHORT_565_RGB = 565;
    public static final int TYPE_INT_888_RGB = 888;
    public static final int TYPE_INT_8888_ARGB = 8888;
    
    void drawImage(final Image p0, final int p1, final int p2, final int p3, final int p4);
    
    void drawPixels(final byte[] p0, final byte[] p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9);
    
    void drawPixels(final int[] p0, final boolean p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9);
    
    void drawPixels(final short[] p0, final boolean p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9);
    
    void drawPolygon(final int[] p0, final int p1, final int[] p2, final int p3, final int p4, final int p5);
    
    void drawTriangle(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6);
    
    void fillPolygon(final int[] p0, final int p1, final int[] p2, final int p3, final int p4, final int p5);
    
    void fillTriangle(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6);
    
    int getAlphaComponent();
    
    int getNativePixelFormat();
    
    void getPixels(final byte[] p0, final byte[] p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8);
    
    void getPixels(final int[] p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7);
    
    void getPixels(final short[] p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7);
    
    void setARGBColor(final int p0);
}
