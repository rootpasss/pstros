// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import java.util.StringTokenizer;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.Reader;
import java.io.LineNumberReader;
import java.io.FileReader;
import java.io.File;
import java.util.HashMap;

public class JadFileParser
{
    private static String TAG_JAR;
    private static String TAG_NAME;
    private static String TAG_CLASS;
    private static HashMap tags;
    private String className;
    private String packageName;
    private String packagePath;
    private String appName;
    
    static {
        JadFileParser.TAG_JAR = "MIDlet-Jar-URL";
        JadFileParser.TAG_NAME = "MIDlet-Name";
        JadFileParser.TAG_CLASS = "MIDlet-1";
    }
    
    public static String getValue(final String key) {
        if (JadFileParser.tags == null) {
            return null;
        }
        final Object result = JadFileParser.tags.get(key);
        if (result == null) {
            return null;
        }
        return (String)result;
    }
    
    public JadFileParser(final String fileName) {
        if (JadFileParser.tags == null) {
            JadFileParser.tags = new HashMap();
        }
        else {
            JadFileParser.tags.clear();
        }
        try {
            final File file = new File(fileName);
            if (!file.exists()) {
                System.out.println("Error:jadFileParser: file not exist, file=" + fileName);
                return;
            }
            this.packagePath = file.getParent();
            final FileReader fileReader = new FileReader(file);
            final LineNumberReader reader = new LineNumberReader(fileReader);
            this.parseData(reader);
            reader.close();
        }
        catch (Exception e) {
            System.out.println("Error:JadFileParser:" + e);
            e.printStackTrace();
        }
    }
    
    public JadFileParser(final InputStream ins) {
        if (JadFileParser.tags == null) {
            JadFileParser.tags = new HashMap();
        }
        else {
            JadFileParser.tags.clear();
        }
        try {
            final InputStreamReader inputReader = new InputStreamReader(ins);
            final LineNumberReader reader = new LineNumberReader(inputReader);
            this.parseData(reader);
        }
        catch (Exception e) {
            System.out.println("Error:JadFileParser:" + e);
            e.printStackTrace();
        }
    }
    
    private void parseData(final LineNumberReader reader) throws Exception {
        String line = ".";
        while (line != null) {
            line = reader.readLine();
            if (line != null && line.length() > 0) {
                this.parseLine(line);
            }
        }
    }
    
    private void parseLine(final String line) {
        final String tagName = getJadLineValue(line, 0);
        String tagValue = getJadLineValue(line, 1);
        final String tagValueFull = getJadLineValue(line, -1);
        tagValue = cleanValue(tagValue);
        if (line.startsWith(JadFileParser.TAG_JAR)) {
            this.packageName = getJadLineValue(line, 1);
            this.packageName = cleanValue(this.packageName);
        }
        else if (line.startsWith(JadFileParser.TAG_CLASS)) {
            this.className = getJadLineValue(line, 3);
            this.className = cleanValue(this.className);
        }
        else if (line.startsWith(JadFileParser.TAG_NAME)) {
            this.appName = getJadLineValue(line, 1);
            this.appName = cleanValue(this.appName);
        }
        JadFileParser.tags.put(tagName, tagValueFull);
    }
    
    public static String getLineValue(final String line, final int index) {
        final StringTokenizer tokenizer = new StringTokenizer(line, ":,=");
        for (int size = tokenizer.countTokens(), i = 0; i < size; ++i) {
            final String result = tokenizer.nextToken();
            if (i == index) {
                return result;
            }
        }
        return null;
    }
    
    public static String getJadLineValue(final String line, final int index) {
        int begin = 0;
        int end = 0;
        if (index < 0) {
            end = line.length();
            begin = line.indexOf(58);
            if (begin < 0) {
                begin = line.indexOf(61);
            }
            if (begin < 0) {
                return line;
            }
            final String result = cleanValue(line.substring(begin + 1, end));
            return result;
        }
        else {
            if (index != 0) {
                for (int i = 0; i < index; ++i) {
                    if (i == 0) {
                        final int pos = line.indexOf(58, begin);
                        if (pos < 0) {
                            begin = line.indexOf(61);
                        }
                        begin = pos;
                    }
                    else {
                        begin = line.indexOf(44, begin);
                    }
                    if (begin < 0) {
                        return null;
                    }
                    ++begin;
                    end = line.indexOf(44, begin);
                    if (end < 0) {
                        end = line.length();
                    }
                }
                final String result = cleanValue(line.substring(begin, end));
                return result;
            }
            end = line.indexOf(58);
            if (end < 0) {
                end = line.indexOf(61);
            }
            if (end < 0) {
                return line;
            }
            final String result = cleanValue(line.substring(begin, end));
            return result;
        }
    }
    
    public static String cleanValue(final String line) {
        if (line == null) {
            return null;
        }
        int size;
        int i;
        for (size = line.length(), i = 0; i < size && line.charAt(i) == ' '; ++i) {}
        if (i == 0) {
            return line;
        }
        return line.substring(i, size - i + 1);
    }
    
    public String getClassName() {
        if (this.className == null) {
            System.out.println("Error: incomplete JAD file. " + JadFileParser.TAG_CLASS + " is missing or currupted!");
        }
        return this.className;
    }
    
    public String getPackageName() {
        return this.packageName;
    }
    
    public String getPackageName(final String name) {
        if (name == null) {
            return null;
        }
        int index = name.lastIndexOf(47);
        if (index < 0) {
            index = name.lastIndexOf(92);
        }
        if (index < 0) {
            return name;
        }
        return name.substring(index + 1);
    }
    
    public String getPackagePath() {
        return this.packagePath;
    }
    
    public String getApplicationName() {
        return this.appName;
    }
}
