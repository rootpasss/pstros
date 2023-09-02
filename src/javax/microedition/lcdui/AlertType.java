// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.lcdui;

public class AlertType
{
    private static final int TYPE_ALARM = 1;
    private static final int TYPE_CONFIRMATION = 2;
    private static final int TYPE_ERROR = 3;
    private static final int TYPE_INFO = 4;
    private static final int TYPE_WARNING = 5;
    public static final AlertType ALARM;
    public static final AlertType CONFIRMATION;
    public static final AlertType ERROR;
    public static final AlertType INFO;
    public static final AlertType WARNING;
    int type;
    
    static {
        ALARM = new AlertType(1);
        CONFIRMATION = new AlertType(2);
        ERROR = new AlertType(3);
        INFO = new AlertType(4);
        WARNING = new AlertType(5);
    }
    
    protected AlertType() {
    }
    
    protected AlertType(final int t) {
        this.type = t;
    }
    
    public boolean playSound(final Display display) {
        return true;
    }
}
