// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.monitor;

import java.lang.reflect.Field;
import ole.pstros.utils.ListItem;
import java.util.Vector;

class ClassMonitor
{
    private Class baseClass;
    private Vector variables;
    private Vector watchedVars;
    private static ListItem[] items;
    
    public ClassMonitor(final Class newClass) {
        if (newClass == null) {
            throw new NullPointerException();
        }
        this.baseClass = newClass;
        this.variables = new Vector();
        this.watchedVars = new Vector();
        this.init();
    }
    
    private void init() {
        this.variables.removeAllElements();
        final Field[] fields = this.baseClass.getDeclaredFields();
        for (int size = fields.length, i = 0; i < size; ++i) {
            final Variable v = new Variable(this.baseClass, fields[i], this);
            this.variables.add(v);
        }
        final SecurityManager sm = System.getSecurityManager();
    }
    
    public Class getBaseClass() {
        return this.baseClass;
    }
    
    private int getArrayIndex(final String name) {
        final int startIndex = name.indexOf(91);
        final int endIndex = name.indexOf(93, startIndex);
        if (startIndex < 0 || endIndex < 0) {
            return -1;
        }
        try {
            return Integer.parseInt(name.substring(startIndex + 1, endIndex));
        }
        catch (Exception e) {
            return -1;
        }
    }
    
    private String getArrayName(final String name) {
        final int startIndex = name.indexOf(91);
        if (startIndex < 0) {
            return name;
        }
        return name.substring(0, startIndex);
    }
    
    private String getVariableName(final String fullName, final int level) {
        if (level < 0) {
            return null;
        }
        int startOffs = 0;
        int endOffs = 0;
        if (level > 0) {
            startOffs = -1;
            for (int i = 0; i < level; ++i) {
                ++startOffs;
                startOffs = fullName.indexOf(46, startOffs);
            }
            if (startOffs < 0) {
                return null;
            }
            ++startOffs;
        }
        endOffs = fullName.indexOf(46, startOffs);
        if (endOffs < 0) {
            endOffs = fullName.length();
        }
        return fullName.substring(startOffs, endOffs);
    }
    
    public boolean isWatched(final String fullName) {
        for (int size = this.watchedVars.size(), i = 0; i < size; ++i) {
            final Variable v = (Variable)this.watchedVars.get(i);
            if (fullName.equals(v.getFulName())) {
                return true;
            }
        }
        return false;
    }
    
    public int addWatchedVariable(final String fullName, final int level) {
        final int size = this.variables.size();
        Variable v = null;
        if (this.isWatched(fullName)) {
            return level;
        }
        String varName = this.getVariableName(fullName, level);
        if (varName == null) {
            return level;
        }
        final int arrayIndex = this.getArrayIndex(varName);
        if (arrayIndex > -1) {
            varName = this.getArrayName(varName);
        }
        for (int i = 0; i < size; ++i) {
            v = (Variable)this.variables.get(i);
            if (varName.equals(v.getName())) {
                break;
            }
            v = null;
        }
        if (v != null) {
            final Variable watchVariable = new Variable(v, arrayIndex);
            final Class cl = watchVariable.getType();
            watchVariable.setFullName(fullName);
            this.watchedVars.add(watchVariable);
            if (cl.isPrimitive()) {
                watchVariable.setLeaf(true);
            }
            else {
                final ClassManager man = ClassManager.getInstance();
                final ClassMonitor mon = man.getClassMonitor(cl);
                if (mon != null) {
                    final int newLevel = mon.addWatchedVariable(fullName, level + 1);
                    if (newLevel > level + 1) {
                        watchVariable.setClassMonitor(mon);
                    }
                    else {
                        watchVariable.setLeaf(true);
                        watchVariable.setClassMonitor(this);
                    }
                }
            }
            return level + 1;
        }
        return level;
    }
    
    public void cleanWatchedVariables() {
        this.watchedVars.clear();
    }
    
    public int getVariableCount() {
        return this.watchedVars.size();
    }
    
    public ListItem[] getVariables(final Object object) {
        final int size = this.watchedVars.size();
        if (ClassMonitor.items == null || ClassMonitor.items.length < size) {
            ClassMonitor.items = new ListItem[size];
            for (int i = 0; i < size; ++i) {
                ClassMonitor.items[i] = new ListItem(2);
            }
        }
        for (int j = 0; j < size; ++j) {
            final Variable v = (Variable)this.watchedVars.get(j);
            this.dumpVar(v, object, v.getFulName(), ClassMonitor.items[j]);
        }
        return ClassMonitor.items;
    }
    
    public void dumpVariable(final Object object, final String fullName, final ListItem listItem) {
        for (int size = this.watchedVars.size(), i = 0; i < size; ++i) {
            final Variable v = (Variable)this.watchedVars.get(i);
            if (fullName.equals(v.getFulName())) {
                this.dumpVar(v, object, fullName, listItem);
            }
        }
    }
    
    private void dumpVar(final Variable v, final Object object, final String fullName, final ListItem listItem) {
        final ClassMonitor cm = v.getClassMonitor();
        if (cm == this) {
            v.update(object);
            listItem.setColumnValue(0, v.getFulName());
            listItem.setColumnValue(1, v.getStringValue());
            listItem.setChanged(v.hasChanged());
        }
        else if (cm != null) {
            cm.dumpVariable(v.getValue(object, v.getArrayIndex()), fullName, listItem);
        }
    }
}
