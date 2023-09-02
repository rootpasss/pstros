// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.rms;

import java.util.Vector;

public class RmsGroup
{
    private String name;
    private String app;
    private Vector records;
    private boolean publicGroup;
    private int recordCounter;
    
    public RmsGroup(final String appName, final String name) {
        this.app = appName;
        this.name = name;
        this.records = new Vector();
        this.recordCounter = 1;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getApplicationName() {
        return this.app;
    }
    
    public int addRecord(final byte[] data) {
        final RmsRecord record = new RmsRecord(this.recordCounter, data);
        ++this.recordCounter;
        this.records.add(record);
        return record.getId();
    }
    
    public int addRecord(final byte[] data, final int pos, final int length) {
        final RmsRecord record = new RmsRecord(this.recordCounter, data, pos, length);
        ++this.recordCounter;
        this.records.add(record);
        return record.getId();
    }
    
    public boolean removeRecord(final int recordId) {
        final RmsRecord record = this.findRecord(recordId);
        return record != null && this.records.remove(record);
    }
    
    public RmsRecord findRecord(final int recordId) {
        for (int size = this.records.size(), i = 0; i < size; ++i) {
            final RmsRecord item = (RmsRecord)this.records.get(i);
            if (item.getId() == recordId) {
                return item;
            }
        }
        return null;
    }
    
    public int getNextRecordId() {
        return this.recordCounter;
    }
    
    public int getSize() {
        return this.records.size();
    }
    
    public byte[] getRecordData(final int recordId) {
        final RmsRecord result = this.findRecord(recordId);
        if (result == null) {
            return null;
        }
        return result.getData();
    }
    
    public RmsRecord getRecord(final int recordId) {
        final RmsRecord result = this.findRecord(recordId);
        if (result == null) {
            return null;
        }
        return result;
    }
    
    public int getDataSize() {
        int total = 0;
        for (int size = this.records.size(), i = 0; i < size; ++i) {
            final RmsRecord item = (RmsRecord)this.records.get(i);
            total += item.getData().length;
        }
        return total;
    }
    
    public void setAuthMode(final boolean publicMode) {
        this.publicGroup = publicMode;
    }
    
    public boolean isPublic() {
        return this.publicGroup;
    }
    
    public boolean isPrivate() {
        return !this.publicGroup;
    }
}
