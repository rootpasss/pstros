// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import ole.pstros.ConfigData;
import java.awt.geom.AffineTransform;
import java.awt.font.FontRenderContext;
import java.util.Vector;

public class Font
{
    public static final int STYLE_PLAIN = 0;
    public static final int STYLE_BOLD = 1;
    public static final int STYLE_ITALIC = 2;
    public static final int STYLE_UNDERLINED = 4;
    public static final int SIZE_SMALL = 8;
    public static final int SIZE_MEDIUM = 0;
    public static final int SIZE_LARGE = 16;
    public static final int FACE_SYSTEM = 0;
    public static final int FACE_MONOSPACE = 32;
    public static final int FACE_PROPORTIONAL = 64;
    public static final int FONT_STATIC_TEXT = 0;
    public static final int FONT_INPUT_TEXT = 1;
    private static Vector fontCache;
    private int face;
    private int style;
    private int size;
    private java.awt.Font emuFont;
    private static Font defaultFont;
    static FontRenderContext fontContext;
    private static char[] charBuff;
    
    static {
        Font.fontCache = new Vector();
        Font.defaultFont = new Font(0, 0, 0);
        Font.fontContext = new FontRenderContext(null, false, false);
        Font.charBuff = new char[1];
    }
    
    private Font(final int face, final int style, final int size) {
        this.face = face;
        this.style = style;
        this.size = size;
        String fontName = null;
        switch (face) {
            case 32: {
                fontName = "Monospaced";
                break;
            }
            case 64: {
                fontName = "Serif";
                break;
            }
            default: {
                fontName = "Dialog";
                break;
            }
        }
        int fontSize = ConfigData.fontSizeMedium;
        switch (size) {
            case 8: {
                fontSize = ConfigData.fontSizeSmall;
                break;
            }
            case 16: {
                fontSize = ConfigData.fontSizeLarge;
                break;
            }
        }
        this.emuFont = new java.awt.Font(fontName, style, fontSize);
    }
    
    public static Font getDefaultFont() {
        return Font.defaultFont;
    }
    
    public static Font getFont(final int fontSpecifier) {
        int face = 0;
        int style = 0;
        int size = 0;
        switch (fontSpecifier) {
            case 0: {
                face = 64;
                style = 0;
                size = 8;
                break;
            }
            case 1: {
                face = 32;
                style = 0;
                size = 8;
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid font specifier: " + fontSpecifier);
            }
        }
        return getFont(face, style, size);
    }
    
    public static Font getFont(final int face, final int style, final int size) {
        for (int cacheSize = Font.fontCache.size(), i = 0; i < cacheSize; ++i) {
            final Font font = (Font)Font.fontCache.get(i);
            if (font.getFace() == face && font.getStyle() == style && font.getSize() == size) {
                return font;
            }
        }
        final Font font = new Font(face, style, size);
        Font.fontCache.add(font);
        return font;
    }
    
    public java.awt.Font emuGetFont() {
        return this.emuFont;
    }
    
    public int getStyle() {
        return this.style;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public int getFace() {
        return this.face;
    }
    
    public boolean isPlain() {
        return this.style == 0;
    }
    
    public boolean isBold() {
        return (this.style & 0x1) != 0x0;
    }
    
    public boolean isItalic() {
        return (this.style & 0x2) != 0x0;
    }
    
    public boolean isUnderlined() {
        return (this.style & 0x4) != 0x0;
    }
    
    public int charWidth(final char ch) {
        Font.charBuff[0] = ch;
        final Rectangle2D rect = this.emuFont.getStringBounds(Font.charBuff, 0, 1, Font.fontContext);
        return (int)rect.getWidth();
    }
    
    public int charsWidth(final char[] ch, final int offset, final int length) {
        return this.stringWidth(String.valueOf(ch, offset, length));
    }
    
    public int stringWidth(final String str) {
        final Rectangle2D rect = this.emuFont.getStringBounds(str, Font.fontContext);
        return (int)rect.getWidth();
    }
    
    public int substringWidth(final String str, final int offset, final int len) {
        return this.stringWidth(str.substring(offset, offset + len));
    }
    
    public int getHeight() {
        int configSize = ConfigData.fontHeightMedium;
        switch (this.size) {
            case 8: {
                configSize = ConfigData.fontHeightSmall;
                break;
            }
            case 16: {
                configSize = ConfigData.fontHeightLarge;
                break;
            }
        }
        if (configSize < 1) {
            return this.emuFont.getSize() + 4;
        }
        return configSize;
    }
    
    public int getBaselinePosition() {
        final LineMetrics lm = this.emuFont.getLineMetrics("pstros", Font.fontContext);
        int configSize = ConfigData.fontHeightMedium;
        switch (this.size) {
            case 8: {
                configSize = ConfigData.fontHeightSmall;
                break;
            }
            case 16: {
                configSize = ConfigData.fontHeightLarge;
                break;
            }
        }
        if (configSize < 1) {
            return (int)(lm.getAscent() + lm.getLeading());
        }
        return this.getHeight() - this.emuGetExtraHeight();
    }
    
    int emuGetExtraHeight() {
        int configSize = ConfigData.fontHeightMedium;
        switch (this.size) {
            case 8: {
                configSize = ConfigData.fontHeightSmall;
                break;
            }
            case 16: {
                configSize = ConfigData.fontHeightLarge;
                break;
            }
        }
        if (configSize < 1) {
            return 4;
        }
        final LineMetrics lm = this.emuFont.getLineMetrics("pstros", Font.fontContext);
        return (int)(this.getHeight() - (lm.getAscent() + lm.getLeading()));
    }
}
