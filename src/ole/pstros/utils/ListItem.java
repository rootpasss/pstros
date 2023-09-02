// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

public class ListItem
{
    private String[] values;
    private boolean changed;
    
    public ListItem(final int columnCount) {
        this.values = new String[columnCount];
    }
    
    public void setColumnValue(final int column, final String value) {
        this.values[column] = value;
    }
    
    public String getColumnValue(final int column) {
        return this.values[column];
    }
    
    public void setChanged(final boolean state) {
        this.changed = state;
    }
    
    public boolean isChanged() {
        return this.changed;
    }
    
    public String toString() {
        String result = "";
        for (int size = this.values.length, i = 0; i < size; ++i) {
            result = String.valueOf(result) + this.values[i];
            if (i < size - 1) {
                result = String.valueOf(result) + ", ";
            }
        }
        return result;
    }
}
