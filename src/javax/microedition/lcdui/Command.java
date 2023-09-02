// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

import ole.pstros.MainApp;

public class Command
{
    public static final int SCREEN = 1;
    public static final int BACK = 2;
    public static final int CANCEL = 3;
    public static final int OK = 4;
    public static final int HELP = 5;
    public static final int STOP = 6;
    public static final int EXIT = 7;
    public static final int ITEM = 8;
    private String shortLabel;
    private String longLabel;
    private int commandType;
    private int priority;
    Object owner;
    
    public Command(final String label, final int commandType, final int priority) {
        this(label, label, commandType, priority);
    }
    
    public Command(final String shortLabel, final String longLabel, final int commandType, final int priority) {
        if (MainApp.verbose) {
            System.out.println("Command:<init> sl=" + shortLabel + " ll=" + longLabel + " ct=" + commandType + " pri=" + priority);
        }
        this.shortLabel = shortLabel;
        this.longLabel = longLabel;
        this.commandType = commandType;
        this.priority = priority;
    }
    
    public String getLabel() {
        return this.shortLabel;
    }
    
    public String getLongLabel() {
        return this.longLabel;
    }
    
    public int getCommandType() {
        return this.commandType;
    }
    
    public int getPriority() {
        return this.priority;
    }
    
    public String toString() {
        return "Command@" + this.hashCode() + " sl=" + this.shortLabel + " ll=" + this.longLabel + " ct=" + this.commandType;
    }
}
