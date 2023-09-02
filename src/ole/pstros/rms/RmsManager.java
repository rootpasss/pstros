// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.rms;

import ole.pstros.utils.XmlTag;
import ole.pstros.utils.XmlWriter;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import ole.pstros.ConfigData;
import java.util.Vector;

public class RmsManager
{
    public static final String PUBLIC_STORAGE = "--==PUBLIC_STORAGE==--";
    private static RmsManager instance;
    private Vector groups;
    
    public static RmsManager getInstance() {
        if (RmsManager.instance == null) {
            RmsManager.instance = new RmsManager();
        }
        return RmsManager.instance;
    }
    
    private RmsManager() {
        this.groups = new Vector();
    }
    
    public RmsGroup getRmsGroup(final String appName, final String groupName, final boolean authPublic, final boolean create) {
        RmsGroup group = this.findGroup(appName, groupName);
        if (group == null && create) {
            group = new RmsGroup(appName, groupName);
            group.setAuthMode(authPublic);
            this.groups.add(group);
        }
        else if (group != null && group.isPrivate() && authPublic) {
            group = null;
        }
        return group;
    }
    
    public boolean removeGroup(final String appName, final String groupName) {
        final RmsGroup group = this.findGroup(appName, groupName);
        return group != null && this.groups.remove(group);
    }
    
    public String[] getGroupNames(final String appName) {
        final int size = this.groups.size();
        int count = 0;
        for (int i = 0; i < size; ++i) {
            final RmsGroup group = (RmsGroup)this.groups.get(i);
            if (group.getApplicationName().equals(appName)) {
                ++count;
            }
        }
        if (count == 0) {
            return null;
        }
        final String[] result = new String[count];
        count = 0;
        for (int j = 0; j < size; ++j) {
            final RmsGroup group = (RmsGroup)this.groups.get(j);
            if (group.getApplicationName().equals(appName)) {
                result[count] = group.getName();
                ++count;
            }
        }
        return result;
    }
    
    private RmsGroup findGroup(final String appName, final String groupName) {
        for (int size = this.groups.size(), i = 0; i < size; ++i) {
            final RmsGroup group = (RmsGroup)this.groups.get(i);
            if (group.getApplicationName().equals(appName) && group.getName().equals(groupName)) {
                return group;
            }
        }
        return null;
    }
    
    public void saveData() {
        if (ConfigData.readOnly) {
            return;
        }
        final File file = new File(this.getDataFilename());
        final XmlWriter writer = new XmlWriter(file);
        final XmlTag tag = new XmlTag();
        writer.startTag("rms");
        for (int size = this.groups.size(), i = 0; i < size; ++i) {
            final RmsGroup group = (RmsGroup)this.groups.get(i);
            final int groupSize = group.getSize();
            if (groupSize > 0) {
                tag.setName("record");
                tag.setEndTagType(1);
                tag.setProperty("application", group.getApplicationName());
                tag.setProperty("name", group.getName());
                if (group.isPublic()) {
                    tag.setProperty("auth", 1);
                }
                for (int j = 0; j < groupSize; ++j) {
                    final byte[] data = group.getRecordData(j + 1);
                    System.out.println("GAME BYTE DATA: "+new String(data,0,data.length));
                    if (data != null && data.length > 0) {
                      /*StringBuilder sb=new StringBuilder();
                      for(byte b:data)
                        sb.append(String.format("%02X ",b));
                      tag.setProperty("data"+j,sb.toString());*/
                        tag.setProperty("data" + j, this.toHex(data));
                    }
                }
                writer.startTag(tag);
            }
        }
        writer.endTag("rms");
        try {
            writer.close();
        }
        catch (Exception e) {
            System.out.println("Pstros: RmsManager: can't write rms file! " + file.getAbsolutePath());
            e.printStackTrace();
        }
    }

    private String toHex(final byte[] data) {
        final int size = data.length;
        final StringBuffer result = new StringBuffer();
        for (int i = 0; i < size; ++i) {
            result.append(Integer.toString(data[i])).append(' ');
        }
        return result.toString();
    }
    
    public String getDataFilename() {
        String dir = System.getProperty("user.home");
        dir = String.valueOf(dir) + "/.pstros";
        final File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        if (ConfigData.deviceName == null) {
            return String.valueOf(dir) + "/rms.xml";
        }
        return String.valueOf(dir) + "/rms_" + ConfigData.deviceName + ".xml";
    }
}
