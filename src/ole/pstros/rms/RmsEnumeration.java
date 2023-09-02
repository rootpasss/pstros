// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.rms;

import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;
import javax.microedition.rms.InvalidRecordIDException;
import ole.pstros.MainApp;
import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordEnumeration;

public class RmsEnumeration implements RecordEnumeration
{
    RmsGroup group;
    private int groupSize;
    private int currentIndex;
    private boolean keepUpdated;
    private RecordFilter filter;
    private RecordComparator comparator;
    
    public RmsEnumeration(final RecordFilter filter, final RecordComparator comparator, final RmsGroup group) {
        this.group = group;
        this.filter = filter;
        this.comparator = comparator;
        this.groupSize = group.getSize();
        this.currentIndex = 0;
        if (MainApp.verbose) {
            System.out.println("RmsEnumeration created groupSize=" + this.groupSize + " comparator=" + comparator);
        }
    }
    
    public void destroy() {
        this.group = null;
        this.filter = null;
        this.comparator = null;
    }
    
    public boolean hasNextElement() {
        if (MainApp.verbose) {
            System.out.println("RmsEnumeration: hasNextElement=" + (this.currentIndex < this.groupSize) + " groupSize=" + this.groupSize + " currentIndex=" + this.currentIndex);
        }
        return this.currentIndex < this.groupSize;
    }
    
    public boolean hasPreviousElement() {
        if (MainApp.verbose) {
            System.out.println("RmsEnumeration: hasPreviousElement");
        }
        return this.currentIndex > 0;
    }
    
    public boolean isKeptUpdated() {
        return this.keepUpdated;
    }
    
    public void keepUpdated(final boolean keepUpdated) {
        this.keepUpdated = keepUpdated;
        this.rebuild();
    }
    
    public byte[] nextRecord() throws InvalidRecordIDException, RecordStoreNotOpenException, RecordStoreException {
        if (MainApp.verbose) {
            System.out.println("RmsEnumeration: nextRecord");
        }
        if (!this.hasNextElement()) {
            throw new InvalidRecordIDException();
        }
        final byte[] result = this.group.getRecordData(this.currentIndex);
        ++this.currentIndex;
        return result;
    }
    
    public int nextRecordId() throws InvalidRecordIDException {
        if (!this.hasNextElement()) {
            throw new InvalidRecordIDException();
        }
        if (MainApp.verbose) {
            System.out.println("RmsEnumeration: nextRecordId=" + (this.currentIndex + 1));
        }
        return ++this.currentIndex;
    }
    
    public int numRecords() {
        if (MainApp.verbose) {
            System.out.println("RmsEnumeration: numRecord=" + this.groupSize);
        }
        return this.groupSize;
    }
    
    public byte[] previousRecord() throws InvalidRecordIDException, RecordStoreNotOpenException, RecordStoreException {
        if (MainApp.verbose) {
            System.out.println("RmsEnumeration: previousRecord");
        }
        if (!this.hasPreviousElement()) {
            throw new InvalidRecordIDException();
        }
        --this.currentIndex;
        return this.group.getRecordData(this.currentIndex);
    }
    
    public int previousRecordId() throws InvalidRecordIDException {
        if (MainApp.verbose) {
            System.out.println("RmsEnumeration: previousRecordId");
        }
        if (!this.hasPreviousElement()) {
            throw new InvalidRecordIDException();
        }
        return this.currentIndex - 1;
    }
    
    public void rebuild() {
        if (MainApp.verbose) {
            System.out.println("RmsEnumeration: rebuild");
        }
        this.groupSize = this.group.getSize();
    }
    
    public void reset() {
        if (MainApp.verbose) {
            System.out.println("RmsEnumeration: reset");
        }
        this.currentIndex = -1;
    }
}
