// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.io;

import java.io.File;
import java.io.FilenameFilter;

public class StarFilenameFilter implements FilenameFilter
{
    private boolean starts;
    private boolean ends;
    private boolean contains;
    private String startString;
    private String endString;
    private String containString;
    
    public StarFilenameFilter(final String pattern) {
        if (pattern.equals("*")) {
            return;
        }
        if (pattern.startsWith("*") && pattern.endsWith("*") && pattern.length() > 2) {
            this.contains = true;
            this.containString = pattern.substring(1, pattern.length() - 1);
            return;
        }
        if (pattern.startsWith("*")) {
            this.ends = true;
            this.endString = pattern.substring(1);
        }
        if (pattern.endsWith("*")) {
            this.starts = true;
            this.startString = pattern.substring(0, pattern.length() - 1);
        }
    }
    
    public boolean accept(final File dir, final String name) {
        return (!this.starts || name.startsWith(this.startString)) && (!this.ends || name.endsWith(this.endString)) && (!this.contains || name.indexOf(this.containString) >= 0);
    }
}
