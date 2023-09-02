// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import java.io.File;
import ole.pstros.monitor.MonitorProfile;
import ole.pstros.monitor.ClassManager;
import javax.microedition.lcdui.Display;
import java.io.OutputStream;
import ole.pstros.ConfigData;
import java.io.FileOutputStream;

public class ParamWriter
{
    public static final String ASSIGN = " = ";
    public static final String WINDOW_POS_X = "window.positionX";
    public static final String WINDOW_POS_Y = "window.positionY";
    public static final String CONTROL_CONFIG = "control.config";
    public static final String CONTROL_KEY_SOFT_LEFT = "control.keySoftLeft";
    public static final String CONTROL_KEY_SOFT_RIGHT = "control.keySoftRight";
    public static final String CONTROL_KEY_SOFT_RIGHT_OBSOLETE = "control.keySoftRifgt";
    public static final String CONTROL_KEY_SOFT_CENTER = "control.keySoftCenter";
    public static final String CONTROL_KEY_LEFT = "control.keyLeft";
    public static final String CONTROL_KEY_UP = "control.keyUp";
    public static final String CONTROL_KEY_RIGHT = "control.keyRight";
    public static final String CONTROL_KEY_DOWN = "control.keyDown";
    public static final String CONTROL_KEY_FIRE = "control.keyFire";
    public static final String CONTROL_KEY_NUM_0 = "control.keyNum0";
    public static final String CONTROL_KEY_NUM_7 = "control.keyNum7";
    public static final String CONTROL_KEY_NUM_8 = "control.keyNum8";
    public static final String CONTROL_KEY_NUM_9 = "control.keyNum9";
    public static final String CONTROL_KEY_NUM_4 = "control.keyNum4";
    public static final String CONTROL_KEY_NUM_5 = "control.keyNum5";
    public static final String CONTROL_KEY_NUM_6 = "control.keyNum6";
    public static final String CONTROL_KEY_NUM_1 = "control.keyNum1";
    public static final String CONTROL_KEY_NUM_2 = "control.keyNum2";
    public static final String CONTROL_KEY_NUM_3 = "control.keyNum3";
    public static final String CONTROL_KEY_STAR = "control.keyStar";
    public static final String CONTROL_KEY_CROSS = "control.keyCross";
    public static final String CONTROL_KEY_SCREEN_SHOT = "control.keyScreenShot";
    public static final String CONTROL_KEY_CAPTURE_VIDEO = "control.keyCaptureVideo";
    public static final String CONTROL_KEY_SHOW_HIDE_NOTIFY = "control.keyShowHideNotify";
    public static final String CONTROL_KEY_RESIZE_SCREEN = "control.keyResizeScreen";
    public static final String MONITOR_ENABLE = "monitor.enable";
    public static final String MONITOR_POS_X = "monitor.positionX";
    public static final String MONITOR_POS_Y = "monitor.positionY";
    public static final String MONITOR_WIDTH = "monitor.width";
    public static final String MONITOR_HEIGHT = "monitor.height";
    public static final String MONITOR_VARIABLE = "monitor.variable";
    public static final String MONITOR_PROFILE = "monitor.profile";
    public static final String IMAGE_VIEWER_POS_X = "imageViewer.positionX";
    public static final String IMAGE_VIEWER_POS_Y = "imageViewer.positionY";
    public static final String IMAGE_VIEWER_WIDTH = "imageViewer.width";
    public static final String IMAGE_VIEWER_HEIGHT = "imageViewer.height";
    public static final String ZOOM_VIEWER_SIZE = "zoomViewer.size";
    private static String endOfLine;
    
    public static final void saveParams() {
        if (ParamWriter.endOfLine == null) {
            ParamWriter.endOfLine = System.getProperty("line.separator");
        }
        try {
            final FileOutputStream fos = new FileOutputStream(getDataFilename());
            saveParameter(fos, "window.positionX", ConfigData.windowPositionX);
            saveParameter(fos, "window.positionY", ConfigData.windowPositionY);
            saveParameter(fos, "control.config", ConfigData.controlConfig ? 1 : 0);
            if (ConfigData.controlConfig) {
                saveParameter(fos, "control.keySoftLeft", Display.keySoftLeft);
                saveParameter(fos, "control.keySoftRight", Display.keySoftRight);
                saveParameter(fos, "control.keySoftCenter", Display.keySoftCenter);
                saveParameter(fos, "control.keyLeft", Display.keyLeft);
                saveParameter(fos, "control.keyRight", Display.keyRight);
                saveParameter(fos, "control.keyUp", Display.keyUp);
                saveParameter(fos, "control.keyDown", Display.keyDown);
                saveParameter(fos, "control.keyFire", Display.keyFire);
                saveParameter(fos, "control.keyNum0", Display.keyNum0);
                saveParameter(fos, "control.keyNum1", Display.keyNum1);
                saveParameter(fos, "control.keyNum2", Display.keyNum2);
                saveParameter(fos, "control.keyNum3", Display.keyNum3);
                saveParameter(fos, "control.keyNum4", Display.keyNum4);
                saveParameter(fos, "control.keyNum5", Display.keyNum5);
                saveParameter(fos, "control.keyNum6", Display.keyNum6);
                saveParameter(fos, "control.keyNum7", Display.keyNum7);
                saveParameter(fos, "control.keyNum8", Display.keyNum8);
                saveParameter(fos, "control.keyNum9", Display.keyNum9);
                saveParameter(fos, "control.keyStar", Display.keyStar);
                saveParameter(fos, "control.keyCross", Display.keyCross);
                saveParameter(fos, "control.keyScreenShot", Display.keyScreenShot);
                saveParameter(fos, "control.keyCaptureVideo", Display.keyCaptureVideo);
                saveParameter(fos, "control.keyShowHideNotify", Display.keyShowHideNotify);
                saveParameter(fos, "control.keyResizeScreen", Display.keyRotate);
            }
            if (ConfigData.zoomSize > 1) {
                saveParameter(fos, "zoomViewer.size", ConfigData.zoomSize);
            }
            if (ConfigData.imageViewerBounds != null) {
                saveParameter(fos, "imageViewer.positionX", ConfigData.imageViewerBounds.x);
                saveParameter(fos, "imageViewer.positionY", ConfigData.imageViewerBounds.y);
                saveParameter(fos, "imageViewer.width", ConfigData.imageViewerBounds.width);
                saveParameter(fos, "imageViewer.height", ConfigData.imageViewerBounds.height);
            }
            saveParameter(fos, "monitor.enable", ConfigData.classMonitor ? 1 : 0);
            if (ConfigData.classMonitor) {
                if (ConfigData.classMonitorBounds != null) {
                    saveParameter(fos, "monitor.positionX", ConfigData.classMonitorBounds.x);
                    saveParameter(fos, "monitor.positionY", ConfigData.classMonitorBounds.y);
                    saveParameter(fos, "monitor.width", ConfigData.classMonitorBounds.width);
                    saveParameter(fos, "monitor.height", ConfigData.classMonitorBounds.height);
                }
                final ClassManager cm = ClassManager.getInstance();
                for (final MonitorProfile profile : cm.getProfiles()) {
                    saveParameter(fos, "monitor.profile", profile.getName());
                    for (int varCount = profile.getSize(), j = 0; j < varCount; ++j) {
                        saveParameter(fos, "monitor.variable", profile.getItem(j));
                    }
                }
            }
            fos.flush();
            fos.close();
        }
        catch (Exception e) {
            System.out.println("Error: Pstros: ParamWriter.saveParams() " + e);
        }
    }
    
    private static final void saveParameter(final OutputStream os, final String name, final int value) throws Exception {
        os.write(name.getBytes());
        os.write(" = ".getBytes());
        os.write(Integer.toString(value).getBytes());
        os.write(ParamWriter.endOfLine.getBytes());
    }
    
    private static final void saveParameter(final OutputStream os, final String name, final String value) throws Exception {
        os.write(name.getBytes());
        os.write(" = ".getBytes());
        os.write(value.getBytes());
        os.write(ParamWriter.endOfLine.getBytes());
    }
    
    public static String getDataFilename() {
        String dir = System.getProperty("user.home");
        dir = String.valueOf(dir) + "/.pstros";
        final File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        return String.valueOf(dir) + "/params.cnf";
    }
}
