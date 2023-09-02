// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.rms;

public interface RecordEnumeration
{
    void destroy();
    
    boolean isKeptUpdated();
    
    boolean hasNextElement();
    
    boolean hasPreviousElement();
    
    void keepUpdated(final boolean p0);
    
    byte[] nextRecord() throws InvalidRecordIDException, RecordStoreNotOpenException, RecordStoreException;
    
    int nextRecordId() throws InvalidRecordIDException;
    
    int numRecords();
    
    byte[] previousRecord() throws InvalidRecordIDException, RecordStoreNotOpenException, RecordStoreException;
    
    int previousRecordId() throws InvalidRecordIDException;
    
    void rebuild();
    
    void reset();
}
