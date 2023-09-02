// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import java.awt.Rectangle;
import javax.microedition.lcdui.Display;
import java.io.Reader;
import java.io.LineNumberReader;
import java.io.FileReader;
import java.io.File;
import ole.pstros.ConfigData;
import ole.pstros.monitor.ClassManager;

public class ParamReader
{
    private static String lastMonitorProfile;
    private static ClassManager cm;
    
    public static final void readParams() {
        if (ConfigData.readOnly) {
            return;
        }
        String line = ".";
        try {
            final File file = new File(ParamWriter.getDataFilename());
            if (!file.exists()) {
                return;
            }
            final FileReader fileReader = new FileReader(file);
            final LineNumberReader reader = new LineNumberReader(fileReader);
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    parseLine(line);
                }
            }
            reader.close();
            fileReader.close();
        }
        catch (Exception e) {
            System.out.println("Error:ConfigLoader:" + e);
            e.printStackTrace();
        }
    }
    
    private static final void parseLine(final String line) {
        if (line.startsWith("window.positionX")) {
            ConfigData.windowPositionX = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("window.positionY")) {
            ConfigData.windowPositionY = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keySoftLeft")) {
            Display.keySoftLeft = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.config")) {
            final int config = ConfigLoader.getIntValue(line);
            ConfigData.controlConfig = (config > 0);
        }
        else if (line.startsWith("control.keySoftRight") || line.startsWith("control.keySoftRifgt")) {
            Display.keySoftRight = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keySoftCenter")) {
            Display.keySoftCenter = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keyLeft")) {
            Display.keyLeft = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keyRight")) {
            Display.keyRight = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keyUp")) {
            Display.keyUp = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keyDown")) {
            Display.keyDown = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keyFire")) {
            Display.keyFire = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keyNum0")) {
            Display.keyNum0 = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keyNum1")) {
            Display.keyNum1 = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keyNum2")) {
            Display.keyNum2 = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keyNum3")) {
            Display.keyNum3 = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keyNum4")) {
            Display.keyNum4 = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keyNum5")) {
            Display.keyNum5 = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keyNum6")) {
            Display.keyNum6 = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keyNum7")) {
            Display.keyNum7 = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keyNum8")) {
            Display.keyNum8 = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keyNum9")) {
            Display.keyNum9 = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keyStar")) {
            Display.keyStar = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keyCross")) {
            Display.keyCross = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keyScreenShot")) {
            Display.keyScreenShot = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keyCaptureVideo")) {
            Display.keyCaptureVideo = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keyShowHideNotify")) {
            Display.keyShowHideNotify = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("control.keyResizeScreen")) {
            Display.keyRotate = ConfigLoader.getIntValue(line);
        }
        else if (line.startsWith("monitor.enable")) {
            ConfigData.classMonitor = (ConfigLoader.getIntValue(line) != 0);
            ParamReader.cm = ClassManager.getInstance();
        }
        else if (line.startsWith("monitor.profile")) {
            ParamReader.lastMonitorProfile = ConfigLoader.getStringValue(line);
        }
        else if (line.startsWith("monitor.variable") && ParamReader.cm != null) {
            ParamReader.cm.addVariable(ConfigLoader.getStringValue(line), ParamReader.lastMonitorProfile);
        }
        else if (line.startsWith("monitor.positionX")) {
            ConfigData.classMonitorBounds = setBounds(ConfigData.classMonitorBounds, ConfigLoader.getIntValue(line), -1, -1, -1);
        }
        else if (line.startsWith("monitor.positionY")) {
            ConfigData.classMonitorBounds = setBounds(ConfigData.classMonitorBounds, -1, ConfigLoader.getIntValue(line), -1, -1);
        }
        else if (line.startsWith("monitor.width")) {
            ConfigData.classMonitorBounds = setBounds(ConfigData.classMonitorBounds, -1, -1, ConfigLoader.getIntValue(line), -1);
        }
        else if (line.startsWith("monitor.height")) {
            ConfigData.classMonitorBounds = setBounds(ConfigData.classMonitorBounds, -1, -1, -1, ConfigLoader.getIntValue(line));
        }
        else if (line.startsWith("imageViewer.positionX")) {
            ConfigData.imageViewerBounds = setBounds(ConfigData.imageViewerBounds, ConfigLoader.getIntValue(line), -1, -1, -1);
        }
        else if (line.startsWith("imageViewer.positionY")) {
            ConfigData.imageViewerBounds = setBounds(ConfigData.imageViewerBounds, -1, ConfigLoader.getIntValue(line), -1, -1);
        }
        else if (line.startsWith("imageViewer.width")) {
            ConfigData.imageViewerBounds = setBounds(ConfigData.imageViewerBounds, -1, -1, ConfigLoader.getIntValue(line), -1);
        }
        else if (line.startsWith("imageViewer.height")) {
            ConfigData.imageViewerBounds = setBounds(ConfigData.imageViewerBounds, -1, -1, -1, ConfigLoader.getIntValue(line));
        }
        else if (line.startsWith("zoomViewer.size")) {
            ConfigData.zoomSize = ConfigLoader.getIntValue(line);
        }
    }
    
    private static Rectangle setBounds(Rectangle result, final int x, final int y, final int w, final int h) {
        if (result == null) {
            result = new Rectangle();
        }
        if (x > -1) {
            result.x = x;
        }
        if (y > -1) {
            result.y = y;
        }
        if (w > -1) {
            result.width = w;
        }
        if (h > -1) {
            result.height = h;
        }
        return result;
    }
}
