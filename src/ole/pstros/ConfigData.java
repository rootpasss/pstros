// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros;

import java.lang.reflect.Field;
import java.awt.Rectangle;
import java.awt.Image;

public class ConfigData
{
    public static boolean configActive;
    public static boolean slaveMode;
    public static int keyLeftSoft;
    public static int keyRightSoft;
    public static int keyCenterSoft;
    public static int keyUpArrow;
    public static int keyDownArrow;
    public static int keyLeftArrow;
    public static int keyRightArrow;
    public static int keySelect;
    public static int keyBack;
    public static int keyClear;
    public static int topConsoleHeight;
    public static int bottomConsoleHeight;
    public static boolean fullScreenSupported;
    public static Image skinImage;
    public static int skinScreenX;
    public static int skinScreenY;
    public static int skinWidth;
    public static int skinHeight;
    public static int windowPositionX;
    public static int windowPositionY;
    public static int scale;
    public static boolean externalScaler;
    public static String deviceName;
    public static int captureHeight;
    public static int captureOffsetY;
    public static int captureMotionPrecision;
    public static final int VIDEO_MEMORY_DEFAULT = 1048576;
    public static int videoMemoryLimit;
    public static String captureFile;
    public static boolean storeImages;
    public static String storeImagePath;
    public static int drawWait;
    public static boolean updateSerialRunner;
    public static boolean saveParams;
    public static boolean controlConfig;
    public static Rectangle imageViewerBounds;
    public static Rectangle classMonitorBounds;
    public static Rectangle zoomViewerBounds;
    public static boolean classMonitor;
    public static boolean forceMute;
    public static boolean readOnly;
    public static int rotatedScreenWidth;
    public static int rotatedScreenHeight;
    public static int originalScreenWidth;
    public static int originalScreenHeight;
    public static int displayGamma;
    public static int zoomSize;
    public static String fileConnectionMapping;
    public static String callEvent;
    public static boolean numKeySwap;
    public static int readSpeed;
    public static int fontSizeSmall;
    public static int fontSizeLarge;
    public static int fontSizeMedium;
    public static int fontHeightSmall;
    public static int fontHeightLarge;
    public static int fontHeightMedium;
    
    static {
        ConfigData.configActive = true;
        ConfigData.slaveMode = false;
        ConfigData.keyLeftSoft = -6;
        ConfigData.keyRightSoft = -7;
        ConfigData.keyCenterSoft = -5;
        ConfigData.keyUpArrow = -1;
        ConfigData.keyDownArrow = -2;
        ConfigData.keyLeftArrow = -3;
        ConfigData.keyRightArrow = -4;
        ConfigData.keySelect = -6;
        ConfigData.keyBack = -7;
        ConfigData.keyClear = -8;
        ConfigData.topConsoleHeight = 0;
        ConfigData.bottomConsoleHeight = 0;
        ConfigData.fullScreenSupported = true;
        ConfigData.skinImage = null;
        ConfigData.skinScreenX = 10;
        ConfigData.skinScreenY = 20;
        ConfigData.skinWidth = 0;
        ConfigData.skinHeight = 0;
        ConfigData.windowPositionX = 0;
        ConfigData.windowPositionY = 0;
        ConfigData.scale = 2;
        ConfigData.externalScaler = false;
        ConfigData.captureHeight = 0;
        ConfigData.captureOffsetY = 0;
        ConfigData.captureMotionPrecision = 6;
        ConfigData.videoMemoryLimit = 1048576;
        ConfigData.captureFile = "./capt";
        ConfigData.storeImages = false;
        ConfigData.storeImagePath = null;
        ConfigData.drawWait = 5;
        ConfigData.updateSerialRunner = true;
        ConfigData.saveParams = true;
        ConfigData.controlConfig = false;
        ConfigData.classMonitor = false;
        ConfigData.forceMute = false;
        ConfigData.readOnly = false;
        ConfigData.rotatedScreenWidth = -1;
        ConfigData.rotatedScreenHeight = -1;
        ConfigData.originalScreenWidth = -1;
        ConfigData.originalScreenHeight = -1;
        ConfigData.displayGamma = 0;
        ConfigData.zoomSize = 1;
        ConfigData.numKeySwap = false;
        ConfigData.readSpeed = -1;
        ConfigData.fontSizeSmall = 10;
        ConfigData.fontSizeLarge = 14;
        ConfigData.fontSizeMedium = 12;
        ConfigData.fontHeightSmall = -1;
        ConfigData.fontHeightLarge = -1;
        ConfigData.fontHeightMedium = -1;
    }
    
    public static int getConsoleSize() {
        int consoleSize = 0;
        if (ConfigData.configActive) {
            if (ConfigData.topConsoleHeight > 0) {
                consoleSize += ConfigData.topConsoleHeight;
            }
            if (ConfigData.bottomConsoleHeight > 0) {
                consoleSize += ConfigData.bottomConsoleHeight;
            }
        }
        return consoleSize;
    }
    
    public static Object getParameter(final String name) {
        try {
            final Class cdc = Class.forName("javax.microedition.lcdui.Display");
            final Field fld = cdc.getDeclaredField(name);
            return fld.get(null);
        }
        catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
