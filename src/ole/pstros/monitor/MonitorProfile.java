// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.monitor;

import java.util.Vector;

public class MonitorProfile
{
    private String name;
    private Vector items;
    
    public MonitorProfile(final String profileName) {
        this.name = profileName;
        this.items = new Vector();
    }
    
    public String getName() {
        return this.name;
    }
    
    public void addItem(final String item) {
        if (item == null || item.length() < 1) {
            return;
        }
        this.items.add(item);
    }
    
    public void deleteItem(final String item) {
        this.items.remove(item);
    }
    
    public int getSize() {
        return this.items.size();
    }
    
    public String getItem(final int i) {
        return this.items.get(i).toString();
    }
}
