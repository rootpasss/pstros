// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import javax.swing.ImageIcon;
import ole.pstros.MainApp;
import java.awt.Image;
import java.awt.image.ImageObserver;
import javax.microedition.lcdui.Display;
import ole.pstros.ConfigData;
import java.io.Reader;
import java.io.LineNumberReader;
import java.io.FileReader;
import java.io.File;

public class ConfigLoader
{
    private static final String KEY_LEFT_SOFT = "device.keyLeftSoft";
    private static final String KEY_RIGHT_SOFT = "device.keyRightSoft";
    private static final String KEY_MENU_SOFT = "device.keyMenuSoft";
    private static final String KEY_CENTER_SOFT = "device.keyCenterSoft";
    private static final String KEY_SEND = "device.keySend";
    private static final String KEY_END = "device.keyEnd";
    private static final String KEY_SHIFT = "device.keyShift";
    private static final String KEY_UP_ARROW = "device.keyUpArrow";
    private static final String KEY_DOWN_ARROW = "device.keyDownArrow";
    private static final String KEY_LEFT_ARROW = "device.keyLeftArrow";
    private static final String KEY_RIGHT_ARROW = "device.keyRightArrow";
    private static final String KEY_SELECT = "device.keySelect";
    private static final String KEY_BACK = "device.keyBack";
    private static final String KEY_CLEAR = "device.keyClear";
    private static final String KEY_PLUS = "device.keyPlus";
    private static final String CALL_EVENT = "device.callEvent";
    private static final String READ_SPEED = "device.readSpeed";
    private static final String FONT_SIZE_SMALL = "device.fontSizeSmall";
    private static final String FONT_SIZE_MEDIUM = "device.fontSizeMedium";
    private static final String FONT_SIZE_LARGE = "device.fontSizeLarge";
    private static final String FONT_HEIGHT_SMALL = "device.fontHeightSmall";
    private static final String FONT_HEIGHT_MEDIUM = "device.fontHeightMedium";
    private static final String FONT_HEIGHT_LARGE = "device.fontHeightLarge";
    private static final String SCREEN_HEIGHT = "device.height";
    private static final String SCREEN_WIDTH = "device.width";
    private static final String VIDEO_MEMORY = "device.videoMemory";
    private static final String SCREEN_ROT_HEIGHT = "device.rotatedHeight";
    private static final String SCREEN_ROT_WIDTH = "device.rotatedWidth";
    private static final String TOP_CONSOLE_HEIGHT = "device.topConsoleHeight";
    private static final String BOTTOM_CONSOLE_HEIGHT = "device.bottomConsoleHeight";
    private static final String SKIN_SCREEN_X = "skin.screenX";
    private static final String SKIN_SCREEN_Y = "skin.screenY";
    private static final String SKIN_IMAGE_NEUTRAL = "skin.imageNeutral";
    private static int changeCounter;
    private static String configPath;
    private static int screenHeight;
    
    static {
        ConfigLoader.screenHeight = -1;
    }
    
    public static void readConfig(final String fileName) {
        String line = ".";
        ConfigLoader.changeCounter = 0;
        System.out.println("Config loader: readConfig name=" + fileName);
        try {
            final File file = new File(fileName);
            if (!file.exists()) {
                System.out.println("Error:ConfigLoader: file not exist, file=" + fileName);
                return;
            }
            ConfigLoader.configPath = file.getParent();
            final FileReader fileReader = new FileReader(file);
            final LineNumberReader reader = new LineNumberReader(fileReader);
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    parseLine(line);
                }
            }
            if (ConfigLoader.changeCounter > 0) {
                ConfigData.configActive = true;
            }
            setScreenHeight();
        }
        catch (Exception e) {
            System.out.println("Error:ConfigLoader:" + e);
            e.printStackTrace();
        }
    }
    
    public static void parseLine(final String line) {
        if (line.startsWith("device.keyLeftSoft")) {
            ConfigData.keyLeftSoft = getIntValue(line);
        }
        else if (line.startsWith("device.keyRightSoft")) {
            ConfigData.keyRightSoft = getIntValue(line);
        }
        else if (line.startsWith("device.keyCenterSoft")) {
            ConfigData.keyCenterSoft = getIntValue(line);
        }
        else if (line.startsWith("device.keyUpArrow")) {
            ConfigData.keyUpArrow = getIntValue(line);
        }
        else if (line.startsWith("device.keyDownArrow")) {
            ConfigData.keyDownArrow = getIntValue(line);
        }
        else if (line.startsWith("device.keyLeftArrow")) {
            ConfigData.keyLeftArrow = getIntValue(line);
        }
        else if (line.startsWith("device.keyRightArrow")) {
            ConfigData.keyRightArrow = getIntValue(line);
        }
        else if (line.startsWith("device.keySelect")) {
            ConfigData.keySelect = getIntValue(line);
        }
        else if (line.startsWith("device.keyBack")) {
            ConfigData.keyBack = getIntValue(line);
        }
        else if (line.startsWith("device.keyClear")) {
            ConfigData.keyClear = getIntValue(line);
        }
        else if (line.startsWith("device.fontSizeSmall")) {
            ConfigData.fontSizeSmall = getIntValue(line);
        }
        else if (line.startsWith("device.fontSizeMedium")) {
            ConfigData.fontSizeMedium = getIntValue(line);
        }
        else if (line.startsWith("device.fontSizeLarge")) {
            ConfigData.fontSizeLarge = getIntValue(line);
        }
        else if (line.startsWith("device.fontHeightSmall")) {
            ConfigData.fontHeightSmall = getIntValue(line);
        }
        else if (line.startsWith("device.fontHeightMedium")) {
            ConfigData.fontHeightMedium = getIntValue(line);
        }
        else if (line.startsWith("device.fontHeightLarge")) {
            ConfigData.fontHeightLarge = getIntValue(line);
        }
        else if (line.startsWith("device.topConsoleHeight")) {
            ConfigData.topConsoleHeight = getIntValue(line);
        }
        else if (line.startsWith("device.bottomConsoleHeight")) {
            ConfigData.bottomConsoleHeight = getIntValue(line);
            ConfigData.fullScreenSupported = false;
        }
        else if (line.startsWith("device.videoMemory")) {
            ConfigData.videoMemoryLimit = getIntValue(line);
            if (ConfigData.videoMemoryLimit < 0) {
                ConfigData.videoMemoryLimit = 1048576;
            }
            else {
                ConfigData.videoMemoryLimit <<= 10;
            }
        }
        else if (line.startsWith("device.callEvent")) {
            int index = line.indexOf(61);
            if (index < 0) {
                index = line.indexOf(58);
            }
            ConfigData.callEvent = JadFileParser.cleanValue(line.substring(index + 1));
        }
        else if (line.startsWith("device.readSpeed")) {
            ConfigData.readSpeed = getIntValue(line);
        }
        else if (line.startsWith("skin.screenX")) {
            ConfigData.skinScreenX = getIntValue(line);
        }
        else if (line.startsWith("skin.screenY")) {
            ConfigData.skinScreenY = getIntValue(line);
        }
        else if (line.startsWith("device.width")) {
            Display.WIDTH = getIntValue(line);
            if (ConfigData.originalScreenWidth > 0) {
                ConfigData.originalScreenWidth = Display.WIDTH;
            }
        }
        else if (line.startsWith("device.height")) {
            ConfigLoader.screenHeight = getIntValue(line);
        }
        else if (line.startsWith("device.rotatedWidth")) {
            ConfigData.rotatedScreenWidth = getIntValue(line);
            if (ConfigData.rotatedScreenHeight < 0) {
                ConfigData.rotatedScreenHeight = Display.WIDTH;
            }
            ConfigData.originalScreenWidth = Display.WIDTH;
            ConfigData.originalScreenHeight = Display.HEIGHT;
        }
        else if (line.startsWith("device.rotatedHeight")) {
            ConfigData.rotatedScreenHeight = getIntValue(line);
            if (ConfigData.rotatedScreenWidth < 0) {
                ConfigData.rotatedScreenWidth = Display.HEIGHT;
            }
            ConfigData.originalScreenWidth = Display.WIDTH;
            ConfigData.originalScreenHeight = Display.HEIGHT;
        }
        else if (line.startsWith("skin.imageNeutral")) {
            String value = JadFileParser.getLineValue(line, 1);
            value = JadFileParser.cleanValue(value);
            final Image image = readImage(String.valueOf(ConfigLoader.configPath) + "/" + value);
            if (image != null) {
                ++ConfigLoader.changeCounter;
                ConfigData.skinImage = image;
                ConfigData.skinWidth = image.getWidth(null);
                ConfigData.skinHeight = image.getHeight(null);
            }
        }
    }
    
    public static void setScreenHeight() {
        if (ConfigLoader.screenHeight < 0) {
            ConfigLoader.screenHeight = Display.HEIGHT;
        }
        Display.HEIGHT = ConfigLoader.screenHeight + ConfigData.bottomConsoleHeight + ConfigData.topConsoleHeight;
        if (ConfigData.originalScreenHeight > 0) {
            ConfigData.originalScreenHeight = Display.HEIGHT;
            ConfigData.rotatedScreenHeight += ConfigData.bottomConsoleHeight;
        }
        if (MainApp.verbose) {
            System.out.println("Setting Display.HEIGHT=" + Display.HEIGHT);
        }
    }
    
    public static int getIntValue(final String line) {
        ++ConfigLoader.changeCounter;
        String value = JadFileParser.getLineValue(line, 1);
        value = JadFileParser.cleanValue(value);
        if (value == null || value.length() < 1) {
            return -1;
        }
        return Integer.parseInt(value);
    }
    
    public static String getStringValue(final String line) {
        ++ConfigLoader.changeCounter;
        String value = JadFileParser.getLineValue(line, 1);
        value = JadFileParser.cleanValue(value);
        if (value == null || value.length() < 1) {
            return null;
        }
        return value;
    }
    
    private static Image readImage(final String fileName) {
        final ImageIcon ii = new ImageIcon(fileName);
        if (ii == null) {
            return null;
        }
        return ii.getImage();
    }
}
