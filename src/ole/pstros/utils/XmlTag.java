// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import java.util.ArrayList;

public class XmlTag
{
    public static final int END_TAG_FULL = 0;
    public static final int END_TAG_SHORT = 1;
    private String name;
    private int endTagType;
    private ArrayList propertyList;
    
    public XmlTag() {
        this.propertyList = new ArrayList();
    }
    
    public XmlTag(final String name) {
        this.propertyList = new ArrayList();
        this.name = name;
    }
    
    public XmlTag(final String name, final String property, final String value) {
        this.propertyList = new ArrayList();
        this.name = name;
        this.setProperty(property, value);
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getCount() {
        return this.propertyList.size();
    }
    
    public String getPropertyName(final int index) {
        final XmlProperty p = (XmlProperty)this.propertyList.get(index);
        return p.getName();
    }
    
    public String getPropertyValue(final int index) {
        final XmlProperty p = (XmlProperty)this.propertyList.get(index);
        final String stringValue = p.getValue();
        return this.toUtfValue(stringValue);
    }
    
    public void setName(final String name) {
        this.name = name;
        this.propertyList.clear();
    }
    
    public void clear() {
        this.propertyList.clear();
    }
    
    public void setProperty(final String name, final String value) {
        final XmlProperty p = new XmlProperty(name, value);
        this.propertyList.add(p);
    }
    
    public void setProperty(final String name, final int value) {
        this.setProperty(name, Integer.toString(value));
    }
    
    public void setProperty(final String name, final boolean value) {
        this.setProperty(name, Integer.toString((int)(value ? 1 : 0)));
    }
    
    public void setProperty(final String name, final float value) {
        this.setProperty(name, Float.toString(value));
    }
    
    public void setEndTagType(final int endTagType) {
        this.endTagType = endTagType;
    }
    
    public int getEndTagType() {
        return this.endTagType;
    }
    
    private String toUtfValue(final String line) {
        final int max = line.length();
        boolean allCharsBasic = true;
        for (int i = 0; i < max; ++i) {
            if (line.charAt(i) >= '\u0080') {
                allCharsBasic = false;
                break;
            }
        }
        if (allCharsBasic) {
            return line;
        }
        String result = "";
        for (int j = 0; j < max; ++j) {
            result = String.valueOf(result) + this.getUtfChar(line.charAt(j));
        }
        return result;
    }
    
    private String getUtfChar(final char c) {
        if (c < '\u0080') {
            return String.valueOf(c);
        }
        return "&#x" + Integer.toHexString(c) + ";";
    }
    
    private class XmlProperty
    {
        private String name;
        private String value;
        
        public XmlProperty() {
        }
        
        public XmlProperty(final String name, final String value) {
            this.name = name;
            this.value = value;
        }
        
        public String getName() {
            return this.name;
        }
        
        public String getValue() {
            return this.value;
        }
    }
}
