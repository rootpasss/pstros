// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import java.io.FileOutputStream;
import java.io.File;

public class XmlWriter
{
    protected int curLevel;
    protected StringBuffer dataBuffer;
    protected File file;
    
    public XmlWriter(final File file) {
        this.file = file;
        this.create(true);
    }
    
    public XmlWriter(final File file, final boolean header) {
        this.file = file;
        this.create(header);
    }
    
    private void create(final boolean header) {
        if (header) {
            this.dataBuffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        }
        else {
            this.dataBuffer = new StringBuffer();
        }
        this.curLevel = 0;
    }
    
    public void close() throws Exception {
        final byte[] buff = this.dataBuffer.toString().getBytes();
        final FileOutputStream dataOut = new FileOutputStream(this.file);
        dataOut.write(buff);
        dataOut.flush();
        dataOut.close();
        this.curLevel = 0;
        System.out.println("RMS stored (XmlWriter 43)");
    }
    
    public void newLine() {
        this.dataBuffer.append("\n");
    }
    
    public void comment(final String text) {
        if (text == null) {
            return;
        }
        this.dataBuffer.append(this.levelSpaces()).append("<!-- ").append(text).append(" -->\n");
    }
    
    public void startTag(final String tag) {
        final String string = new String(String.valueOf(this.levelSpaces()) + "<" + tag + ">\n");
        this.dataBuffer.append(string);
        ++this.curLevel;
    }
    
    public void startTag(final String tag, final String property, final String value) {
        this.dataBuffer.append(this.levelSpaces()).append('<').append(tag).append(' ').append(property).append("=\"").append(value).append("\" >\n");
        ++this.curLevel;
    }
    
    public void startTag(final XmlTag tag) {
        String string = new String(String.valueOf(this.levelSpaces()) + "<" + tag.getName() + " ");
        if (tag.getCount() <= 0) {
            this.startTag(tag.getName());
        }
        else if (tag.getCount() == 1) {
            string = String.valueOf(string) + tag.getPropertyName(0) + "=\"";
            string = String.valueOf(string) + tag.getPropertyValue(0) + "\" ";
        }
        else {
            for (int max = tag.getCount(), i = 0; i < max; ++i) {
                string = String.valueOf(string) + "\n" + this.levelSpaces() + "    ";
                string = String.valueOf(string) + tag.getPropertyName(i) + "=\"";
                string = String.valueOf(string) + tag.getPropertyValue(i) + "\" ";
            }
        }
        if (tag.getEndTagType() == 1) {
            string = String.valueOf(string) + "/>\n";
        }
        else {
            string = String.valueOf(string) + ">\n";
            ++this.curLevel;
        }
        this.dataBuffer.append(string);
    }
    
    public void endTag(final String tag) {
        --this.curLevel;
        final String string = new String(String.valueOf(this.levelSpaces()) + "</" + tag + ">\n");
        this.dataBuffer.append(string);
    }
    
    public void endTag(final XmlTag tag) {
        this.endTag(tag.getName());
    }
    
    public void simpleTag(final String tag, final String text) {
        if (text == null) {
            this.dataBuffer.append(this.levelSpaces()).append('<').append(tag).append("></").append(tag).append(">\n");
        }
        else {
            this.dataBuffer.append(this.levelSpaces()).append('<').append(tag).append('>').append(text).append("</").append(tag).append(">\n");
        }
    }
    
    public void simpleTag(final String tag, final float value) {
        this.simpleTag(tag, Float.toString(value));
    }
    
    public void simplePropertyTag(final String tag, final String property, final String value) {
        if (value == null) {
            this.dataBuffer.append(this.levelSpaces()).append('<').append(tag).append("></").append(tag).append(">\n");
        }
        else {
            this.dataBuffer.append(this.levelSpaces()).append('<').append(tag).append(' ').append(property).append("=\"").append(value).append("\" />\n");
        }
    }
    
    public void prepInstruction(final String text) {
        if (text == null) {
            return;
        }
        this.dataBuffer.append(this.levelSpaces()).append("<?").append(text).append("?>\n");
    }
    
    public void cppCommand(final String text) {
        if (text == null) {
            return;
        }
        this.dataBuffer.append('\n').append(text).append('\n');
    }
    
    public void simpleTag(final String tag, final int[] values) {
        if (values == null) {
            return;
        }
        final int size = values.length;
        final StringBuffer xmlBuffer = new StringBuffer(size * 4);
        for (int i = 0; i < size; ++i) {
            xmlBuffer.append(values[i]);
            xmlBuffer.append(' ');
        }
        this.simpleTag(tag, xmlBuffer.toString());
    }
    
    public void simpleTag(final String tag, final byte[] values) {
        if (values == null) {
            return;
        }
        final int size = values.length;
        final StringBuffer xmlBuffer = new StringBuffer(size * 2);
        for (int i = 0; i < size; ++i) {
            xmlBuffer.append(values[i]);
            xmlBuffer.append(' ');
        }
        this.simpleTag(tag, xmlBuffer.toString());
    }
    
    public void simpleTag(final String tag, final float[] values) {
        if (values == null) {
            return;
        }
        final int size = values.length;
        final StringBuffer xmlBuffer = new StringBuffer(size * 8);
        for (int i = 0; i < size; ++i) {
            xmlBuffer.append(values[i]);
            xmlBuffer.append(' ');
        }
        this.simpleTag(tag, xmlBuffer.toString());
    }
    
    public void simpleTag(final String tag, final float[] values, final int groupCount) {
        if (values == null) {
            return;
        }
        final int size = values.length;
        final StringBuffer xmlBuffer = new StringBuffer(size * 8);
        for (int i = 0; i < size; ++i) {
            if (i % groupCount == 0) {
                xmlBuffer.append("\n").append(this.levelSpaces()).append("    ");
            }
            xmlBuffer.append(values[i]);
            xmlBuffer.append(' ');
        }
        xmlBuffer.append("\n").append(this.levelSpaces());
        this.simpleTag(tag, xmlBuffer.toString());
    }
    
    public void simpleTag(final String tag, final int value) {
        this.simpleTag(tag, Integer.toString(value));
    }
    
    public int getLevel() {
        return this.curLevel;
    }
    
    public void setLevel(final int level) {
        this.curLevel = level;
    }
    
    private String levelSpaces() {
        String string = new String("");
        for (int i = 0; i < this.curLevel; ++i) {
            string = String.valueOf(string) + "\t";
        }
        return string;
    }
}
