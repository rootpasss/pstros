// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.monitor;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

class Variable
{
    protected Class parent;
    protected Field field;
    protected ClassMonitor monitor;
    private boolean change;
    private String value;
    private boolean leaf;
    private String fullName;
    private int arrayIndex;
    
    public Variable(final Class parent, final Field field, final ClassMonitor cm) {
        if (parent == null || field == null) {
            throw new NullPointerException();
        }
        this.parent = parent;
        this.field = field;
        this.monitor = cm;
        field.setAccessible(true);
        this.update(null);
    }
    
    public Variable(final Variable v) {
        this(v, -1);
    }
    
    public Variable(final Variable v, final int arrayIndex) {
        this(v.parent, v.field, v.monitor);
        this.arrayIndex = arrayIndex;
    }
    
    public void update(final Object object) {
        String newValue = "";
        if (object != null) {
            try {
                final Object result = this.field.get(object);
                if (result != null) {
                    if (result.getClass().isArray()) {
                        try {
                            newValue = Array.get(result, this.arrayIndex).toString();
                        }
                        catch (ArrayIndexOutOfBoundsException e2) {
                            newValue = "array index out of bounds!";
                        }
                        catch (Exception e3) {
                            newValue = "error";
                        }
                    }
                    else {
                        newValue = result.toString();
                    }
                }
                else {
                    newValue = "null";
                }
            }
            catch (Exception e) {
                System.out.println("Error: " + e + " object=" + object);
                e.printStackTrace();
            }
        }
        this.change = false;
        if (!newValue.equals(this.value)) {
            this.change = true;
        }
        this.value = newValue;
    }
    
    public Object getValue(final Object object, final int arIndex) {
        try {
            final Object result = this.field.get(object);
            if (result.getClass().isArray()) {
                try {
                    return Array.get(result, arIndex);
                }
                catch (Exception e2) {
                    return null;
                }
            }
            return result;
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
            return null;
        }
    }
    
    public int getArrayIndex() {
        return this.arrayIndex;
    }
    
    public boolean hasChanged() {
        return this.change;
    }
    
    public String getName() {
        return this.field.getName();
    }
    
    public String getStringValue() {
        return this.value;
    }
    
    public Class getType() {
        return this.field.getType();
    }
    
    public void setLeaf(final boolean state) {
        this.leaf = state;
    }
    
    public boolean isLeaf() {
        return this.leaf;
    }
    
    public void setClassMonitor(final ClassMonitor cm) {
        this.monitor = cm;
    }
    
    public ClassMonitor getClassMonitor() {
        return this.monitor;
    }
    
    public void setFullName(final String name) {
        this.fullName = name;
    }
    
    public String getFulName() {
        return this.fullName;
    }
}
