// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros;

import ole.pstros.rms.RmsManager;
import java.awt.event.KeyEvent;
import ole.pstros.reference.ImageReferenceManager;
import javax.microedition.media.Manager;
import java.security.AccessControlException;
import ole.pstros.utils.DirectoryClassLoader;
import ole.pstros.utils.JarClassLoader;
import java.util.zip.ZipEntry;
import java.util.Enumeration;
import ole.pstros.io.ConnectionManager;
import ole.pstros.utils.JadFileParser;
import java.util.jar.JarFile;
import java.io.File;
import ole.pstros.utils.ConfigLoader;
import ole.pstros.utils.OrvPlayer;
import javax.microedition.lcdui.Display;
import ole.pstros.utils.DecoderListener;
import java.io.InputStream;
import ole.pstros.utils.Orv2Tga;
import java.io.FileInputStream;
import ole.pstros.rms.RmsReader;
import ole.pstros.utils.ParamReader;
import java.awt.Component;
import javax.microedition.midlet.MIDlet;
import ole.pstros.utils.BaseClassLoader;
//import javax.bluetooth.LocalDevice;

public class MainApp implements EmuExecutor, Runnable
{
    public static boolean verbose;
    public static boolean soundVerbose;
    static final int CLASS_LOADER_SYSTEM = 0;
    static final int CLASS_LOADER_JAD = 1;
    static final int CLASS_LOADER_DIR = 2;
    static final int CLASS_LOADER_EXTERNAL = 3;
    static int classLoaderType;
    private static String execClassName;
    private static String jarFileName;
    private static String basePath;
    public static String appName;
    private static String fileName;
    private static String orvName;
    private static String orvExportPath;
    private static ClassLoader externalClassLoader;
    private static BaseClassLoader classLoader;
    public static MIDlet midlet;
    private static MainApp executor;
    private static int parametersCount;
    public static Component parentComponent;
    private static int imageMemorySize;
    private static int fps;
    private static boolean runPlayer;
    public static IEmuBridge midletBridge;
    private EmuListener emuListener;
    static /* synthetic */ Class class$0;
    
    static {
        MainApp.verbose = false;
        MainApp.soundVerbose = false;
        MainApp.classLoaderType = 0;
        MainApp.appName = "app";
        MainApp.orvExportPath = "./";
        MainApp.parametersCount = 0;
        MainApp.imageMemorySize = 0;
    }
    
    public static void main(final String[] args) {
        Thread.currentThread().setName("Pstros");
        printHeader();
        ParamReader.readParams();
        if (MainApp.verbose) {
            System.out.println("pstros started!");
        }
        checkArguments(args);
        setSystemProperties();
        executeEmu();
    }

    private static void setSystemProperties() {
      if (ConfigData.slaveMode) {
        return;
      }
      if (System.getProperty("microedition.profiles") == null) {
        System.setProperty("microedition.profiles", "MIDP-2");
      }
    }

    private static boolean executeEmu() {
        if (MainApp.execClassName != null) {
            System.out.println("jarName   :" + MainApp.jarFileName);
            System.out.println("jarPath   :" + MainApp.basePath);
            System.out.println("className :" + MainApp.execClassName);
            System.out.println("appName   :" + MainApp.appName);
            /*final RmsReader reader = new RmsReader();
            reader.readRms();*/
            executeClass();
            return true;
        }
        if (MainApp.orvName != null && !MainApp.runPlayer) {
            try {
                Orv2Tga.process(new FileInputStream(MainApp.orvName), MainApp.orvExportPath, null);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        if (MainApp.runPlayer) {
            if (MainApp.orvName != null) {
                MainApp.fileName = MainApp.orvName;
            }
            final OrvPlayer player = new OrvPlayer(MainApp.fileName, Display.WIDTH, Display.HEIGHT);
            player.open();
            return true;
        }
        printHelp();
        return false;
    }
    
    private static void printHeader() {
        System.out.println("Pstros: the J2ME execution environment. ver 1.6.1f");
        System.out.println("(c)2005-2009 Marek Olejnik ");
        System.out.println("see www.volny.cz/molej/pstros for further information");
        System.out.println();
    }
    
    private static void printHelp() {
        System.out.println("usage:");
        System.out.println("java -Xbootclasspath/a:pstros.jar;application.jar -jar pstros.jar -CMyMidlet -w176 -h208");
        System.out.println("java -jar pstros.jar application.jad -w176 -h208");
        System.out.println("parameters:");
        System.out.println("-wXX : set device screen width to XX pixels (default 176)");
        System.out.println("-hXX : set device screen height to XX pixels (default 220)");
        System.out.println("-sXX : set device screen scale ratio (default 1)");
        System.out.println("-as  : use alternative scaling method");
        System.out.println("-tXX : set delay of repaint in msecs (default 5, value of -1 means no delay)");
        System.out.println("-CclassName : set start class name - should be ancestor of the MIDlet class");
        System.out.println("-AappName : set start application name - usefull for RMS record distinction");
        System.out.println("-U : don't run update thread - disables Display.callSerially() functionality");
        System.out.println("-iPPP : store images to path PPP");
        System.out.println("-v : verbose mode");
        System.out.println("-dcl : use directory class loader - load from current directory");
        System.out.println("-dcl= : use directory class loader - load from specified directories delimited by ;");
        System.out.println("-dn=name: set device name. Rms will be read and stored to file rms_name.xml");
        System.out.println("-cm: enable class monitor (disabled by default)");
        System.out.println("-mute: disable sound output");
        System.out.println("-gamma=XXX: set gamma for color output, default=100, valid from 40 to 160");
        System.out.println("-fc=drive1;path1[;drive2;path2]: set file connection mapping (example: -fc=E:/;./eDrive/");
        System.out.println("-rcp=className;params: register connection provider");
        System.out.println("-rs=XXX: set read speed XXX msecs per 1kbyte of data size ");
        System.out.println("-ks: swap numeric block keys (1-7,2-8,3-9)");
        System.out.println("keyboard controls:");
        System.out.println(" Cursor keys - directional arrows");
        System.out.println(" Enter - center soft / fire ");
        System.out.println(" Esc - quit emulator");
        System.out.println(" F1 - left soft key");
        System.out.println(" F2 - right soft key");
        System.out.println(" NumPad * - star");
        System.out.println(" NumPad / - cross");
        System.out.println(" NumPad numbers - phone numbers");
        System.out.println(" F4 - rotate display");
        System.out.println(" F5 - pause/resume");
        System.out.println(" F9 - hide / show notify test");
        System.out.println(" F11 - capture screen shot");
        System.out.println(" F12 - capture video stream start / stop");
        System.out.println(" Alt+i - open image viewer");
        System.out.println(" Alt+m - open class monitor (class monitor must be enabled)");
        System.out.println(" Alt+d - open zoom/distance/color viewer");
        System.out.println(" Alt+k - swap numeric block keys (1-7,2-8,3-9)");
        System.out.println(" Alt+1,2,3 - set screen scale");
        System.out.println("Key controls customizable via ~/.pstros/params.cnf. Set 'control.config = 1' to enable it.");
        System.out.println(" hint: press CapsLock to view key-press codes");
        System.out.println("To bypass null-pointer-exception when reading resources use the parameter:");
        System.out.println(" -Djava.system.class.loader=ole.pstros.utils.JarClassLoader");
    }
    
    public static BaseClassLoader getClassLoader() {
        return MainApp.classLoader;
    }
    
    public static String getApplicationName() {
        return MainApp.appName;
    }
    
    private static void checkArguments(final String[] args) {
        if (args == null || args.length < 1) {
            return;
        }
        for (int i = 0; i < args.length; ++i) {
            final String arg = args[i];
            setEmuParameter(arg, i);
        }
        ConfigLoader.setScreenHeight();
    }
    
    private static void setEmuParameter(final String arg, final int index) {
        if (index == 0) {
            Label_0152: {
                if (!arg.endsWith(".jar")) {
                    if (!arg.endsWith("JAR")) {
                        break Label_0152;
                    }
                }
                try {
                    final File file = new File(arg);
                    if (!file.exists()) {
                        return;
                    }
                    final JarFile jarFile = new JarFile(arg);
                    final Enumeration entries = jarFile.entries();
                    final ZipEntry entry = jarFile.getEntry("META-INF/MANIFEST.MF");
                    final InputStream stream = jarFile.getInputStream(entry);
                    final JadFileParser parser = new JadFileParser(stream);
                    MainApp.execClassName = parser.getClassName();
                    MainApp.jarFileName = parser.getPackageName(arg);
                    MainApp.basePath = file.getParent();
                    MainApp.appName = parser.getApplicationName();
                    MainApp.classLoaderType = 1;
                    stream.close();
                }
                catch (Exception e) {
                    System.out.println("Pstros: error opening jar file:" + e);
                }
            }
            if (arg.endsWith(".jad") || arg.endsWith(".JAD")) {
                final JadFileParser parser2 = new JadFileParser(arg);
                MainApp.execClassName = parser2.getClassName();
                MainApp.jarFileName = parser2.getPackageName();
                MainApp.basePath = parser2.getPackagePath();
                MainApp.appName = parser2.getApplicationName();
                MainApp.classLoaderType = 1;
            }
            else if (arg.endsWith(".orv") || arg.endsWith(".ORV")) {
                MainApp.orvName = arg;
            }
            else if (!arg.startsWith("-")) {
                MainApp.fileName = arg;
            }
        }
        if (arg.startsWith("-player")) {
            MainApp.runPlayer = true;
        }
        else if (arg.startsWith("-rw")) {
            ConfigData.rotatedScreenWidth = Integer.parseInt(arg.substring(3));
            if (ConfigData.rotatedScreenHeight < 0) {
                ConfigData.rotatedScreenHeight = Display.WIDTH;
            }
            ConfigData.originalScreenWidth = Display.WIDTH;
            ConfigData.originalScreenHeight = Display.HEIGHT;
        }
        else if (arg.startsWith("-rh")) {
            ConfigData.rotatedScreenHeight = Integer.parseInt(arg.substring(3));
            if (ConfigData.rotatedScreenWidth < 0) {
                ConfigData.rotatedScreenWidth = Display.HEIGHT;
            }
            ConfigData.originalScreenWidth = Display.WIDTH;
            ConfigData.originalScreenHeight = Display.HEIGHT;
        }
        else if (arg.startsWith("-w")) {
            Display.WIDTH = Integer.parseInt(arg.substring(2));
            if (ConfigData.originalScreenWidth > 0) {
                ConfigData.originalScreenWidth = Display.WIDTH;
            }
        }
        else if (arg.startsWith("-h")) {
            Display.HEIGHT = Integer.parseInt(arg.substring(2));
            if (ConfigData.originalScreenHeight > 0) {
                ConfigData.originalScreenHeight = Display.HEIGHT;
            }
        }
        else if (arg.equals("-ro")) {
            ConfigData.readOnly = true;
            ConfigData.slaveMode = true;
        }
        else if (arg.equals("-cm")) {
            ConfigData.classMonitor = true;
        }
        else if (arg.startsWith("-c")) {
            ConfigLoader.readConfig(arg.substring(2));
        }
        else if (arg.startsWith("-o")) {
            MainApp.orvExportPath = arg.substring(2);
        }
        else if (arg.startsWith("-i")) {
            String path = arg.substring(2);
            if (!path.endsWith("/") && !path.endsWith("\\")) {
                path = String.valueOf(path) + '/';
            }
            ConfigData.storeImages = true;
            ConfigData.storeImagePath = path;
        }
        else if (arg.startsWith("-fc=")) {
            ConfigData.fileConnectionMapping = arg.substring(4);
        }
        else if (arg.startsWith("-rcp=")) {
            final String line = arg.substring(5);
            final int separatorIndex = line.indexOf(";");
            if (separatorIndex > 0) {
                final String className = line.substring(0, separatorIndex);
                final String params = line.substring(separatorIndex + 1);
                ConnectionManager.addConnectionProvider(className, params, true);
            }
            else {
                System.out.println("Ppstros Error: wrong parameter" + arg);
            }
        }
        if (arg.startsWith("-gamma=")) {
            ConfigData.displayGamma = Integer.parseInt(arg.substring(7));
            if (ConfigData.displayGamma > 160) {
                ConfigData.displayGamma = 160;
            }
            else if (ConfigData.displayGamma < 40) {
                ConfigData.displayGamma = 40;
            }
        }
        else if (arg.startsWith("-rs=")) {
            ConfigData.readSpeed = Integer.parseInt(arg.substring(4));
        }
        else if (arg.startsWith("-C")) {
            MainApp.execClassName = arg.substring(2);
        }
        else if (arg.startsWith("-A")) {
            MainApp.appName = arg.substring(2);
        }
        else if (arg.startsWith("-t")) {
            ConfigData.drawWait = Integer.parseInt(arg.substring(2));
        }
        else if (arg.startsWith("-s")) {
            ConfigData.scale = Integer.parseInt(arg.substring(2));
            if (ConfigData.scale > 3) {
                ConfigData.scale = 3;
            }
            else if (ConfigData.scale < 1) {
                ConfigData.scale = 1;
            }
        }
        else if (arg.startsWith("-dn=")) {
            ConfigData.deviceName = arg.substring(4);
        }
        else if (arg.startsWith("-as")) {
            ConfigData.externalScaler = true;
        }
        else if (arg.equals("-ks")) {
            ConfigData.numKeySwap = true;
        }
        else if (arg.startsWith("-xcaptureH=")) {
            ConfigData.captureHeight = Integer.parseInt(arg.substring(11));
        }
        else if (arg.startsWith("-xcaptureY=")) {
            ConfigData.captureOffsetY = Integer.parseInt(arg.substring(11));
        }
        else if (arg.startsWith("-xtch=")) {
            ConfigData.topConsoleHeight = Integer.parseInt(arg.substring(6));
        }
        else if (arg.startsWith("-xbch=")) {
            ConfigData.bottomConsoleHeight = Integer.parseInt(arg.substring(6));
        }
        else if (arg.equals("-dcl")) {
            MainApp.classLoaderType = 2;
        }
        else if (arg.startsWith("-dcl=")) {
            MainApp.classLoaderType = 2;
            MainApp.basePath = arg.substring(5);
        }
        else if (arg.startsWith("-device.")) {
            ConfigLoader.parseLine(arg.substring(1));
        }
        else if (arg.equals("-mute")) {
            ConfigData.forceMute = true;
        }
        else if (arg.startsWith("-U")) {
            ConfigData.updateSerialRunner = false;
        }
        else if (arg.startsWith("-v")) {
            MainApp.verbose = true;
        }
    }
    
    private static void executeClass() {
        if (MainApp.execClassName == null) {
            return;
        }
        try {
            ClassLoader currentClassLoader = null;
            if (MainApp.verbose) {
                System.out.println("Pstros: ClassLoader type=" + MainApp.classLoaderType);
            }
            Class execClass;
            if (MainApp.classLoaderType != 0) {
                if (MainApp.classLoaderType == 1) {
                    String fileName;
                    if (MainApp.basePath != null) {
                        fileName = String.valueOf(MainApp.basePath) + File.separator + MainApp.jarFileName;
                    }
                    else {
                        fileName = MainApp.jarFileName;
                    }
                    MainApp.classLoader = JarClassLoader.getInstance(fileName);
                    currentClassLoader = MainApp.classLoader;
                }
                else if (MainApp.classLoaderType == 2) {
                    String fileName;
                    if (MainApp.basePath == null || MainApp.basePath.length() < 1) {
                        fileName = new File("./").getAbsolutePath();
                    }
                    else {
                        fileName = MainApp.basePath;
                    }
                    MainApp.classLoader = new DirectoryClassLoader(fileName);
                    currentClassLoader = MainApp.classLoader;
                }
                else if (MainApp.classLoaderType == 3) {
                    currentClassLoader = MainApp.externalClassLoader;
                }
                if (MainApp.verbose) {
                    System.out.println("Pstros: CL=" + currentClassLoader);
                    if (currentClassLoader != null && !ConfigData.readOnly) {
                        System.out.println("Pstros: CL parent=" + currentClassLoader.getParent());
                    }
                }
                if (currentClassLoader == null) {
                    execClass = Class.forName(MainApp.execClassName);
                }
                else {
                    execClass = Class.forName(MainApp.execClassName, true, currentClassLoader);
                }
            }
            else {
                try {
                    currentClassLoader = ClassLoader.getSystemClassLoader();
                }
                catch (AccessControlException e) {
                    System.out.println(e);
                }
                if (MainApp.verbose) {
                    System.out.println("Pstros: CL=" + currentClassLoader);
                    if (currentClassLoader != null && !ConfigData.readOnly) {
                        System.out.println("Pstros: CL parent=" + currentClassLoader.getParent());
                    }
                }
                if (currentClassLoader == null) {
                    execClass = Class.forName(MainApp.execClassName);
                }
                else {
                    if (currentClassLoader instanceof JarClassLoader) {
                        if (MainApp.basePath != null) {
                            MainApp.fileName = String.valueOf(MainApp.basePath) + File.separator + MainApp.jarFileName;
                        }
                        else {
                            MainApp.fileName = MainApp.jarFileName;
                        }
                        ((JarClassLoader)currentClassLoader).setJarFilename(MainApp.fileName);
                    }
                    execClass = Class.forName(MainApp.execClassName, true, currentClassLoader);
                }
            }
            final Class superclass = execClass.getSuperclass();
            Class class$0;
            if ((class$0 = MainApp.class$0) == null) {
                try {
                    class$0 = (MainApp.class$0 = Class.forName("javax.microedition.midlet.MIDlet"));
                }
                catch (ClassNotFoundException ex) {
                    throw new NoClassDefFoundError(ex.getMessage());
                }
            }
            if (superclass != class$0 && MainApp.verbose) {
                System.out.println("Warning: class is not an MIDlet, class=" + execClass.getName());
            }
            executeMidlet(execClass);
        }
        catch (Exception e2) {
            System.out.println("Error: pstros:" + e2);
            e2.printStackTrace();
        }
    }
    
    private static void executeMidlet(final Class execClass) {
        final EmuCanvas emuCanvas = EmuCanvas.getInstance();
        final ThreadGroup tg = new ThreadGroup("pstrosTG");
        new Thread("pstros") {
            public void run() {
                try {
                    final MIDlet md = MainApp.midlet = (MIDlet)execClass.newInstance();
                    Thread.currentThread().setName("Pstros-mainLoop");
                    MainApp.midletBridge.handleEvent(3, MainApp.midlet);
                    int monitorCounter = 0;
                    if (ConfigData.updateSerialRunner) {
                        long time = 500L;
                        int checkCount = 1000 / (int)time;
                        boolean runnerExists = false;
                                //reader.readRms();
                        while (MainApp.midlet.emuIsRunning()) {
                            synchronized (emuCanvas) {
                                runnerExists = emuCanvas.callRunner();
                            }
                            // monitorexit(this.val$emuCanvas)
                            if (runnerExists) {
                                time = 10L;
                                checkCount = 1000 / (int)time;
                            }
                            if (checkCount-- < 0) {
                                Manager.emuUpdatePlayers();
                                checkCount = 1000 / (int)time;
                            }
                            Thread.sleep(5L);
                            if (!updateReferenceInfo()) {
                                refreshFrameTitle(false);
                            }
                            if (++monitorCounter % 8 == 0) {
                                emuCanvas.updateClassMonitor();
                            }
                        }
                    }
                    else {
                        while (MainApp.midlet.emuIsRunning()) {
                            Thread.sleep(100L);
                            updateReferenceInfo();
                            refreshFrameTitle(false);
                        }
                    }
                    Manager.emuStopPlayers();
                    System.out.println("Pstros ended execution.");
                    try {
                        int activeCount = tg.activeCount();
                        for (int safetyCheck = 10; activeCount > 0 && safetyCheck > 0; --safetyCheck, activeCount = tg.activeCount()) {
                            Thread.sleep(50L);
                        }
                        if (activeCount > 0) {
                            if (MainApp.verbose) {
                                System.out.println("Pstros: force TG to stop! active thread count=" + activeCount);
                            }
                            try {
                                tg.stop();
                            }
                            catch (ThreadDeath threadDeath) {}
                        }
                    }
                    catch (Exception e) {
                        System.out.println("Pstros: Thread group stop:" + e);
                    }
                }
                catch (Exception e2) {
                    System.out.println("Error: pstros:" + e2);
                    e2.printStackTrace();
                }
            }
        }.start();
    }
    
    private static void refreshFrameTitle(final boolean force) {
        final int currentFps = EmuCanvas.getInstance().getFps();
        if (force || currentFps != MainApp.fps) {
            Display.emuSetTitle(MainApp.imageMemorySize >> 10, ImageReferenceManager.getPeakMemorySize() >> 10, ConfigData.videoMemoryLimit >> 10, currentFps);
            MainApp.fps = currentFps;
        }
    }
    
    public static void forceGC() {
        if (ConfigData.slaveMode) {
            return;
        }
        System.out.println("Pstros: gc called!");
        Thread.yield();
        System.gc();
        Thread.yield();
        MainApp.imageMemorySize = ImageReferenceManager.update();
        refreshFrameTitle(true);
    }
    
    private static boolean updateReferenceInfo() {
        final int oldMemorySize = MainApp.imageMemorySize;
        MainApp.imageMemorySize = ImageReferenceManager.update();
        if (oldMemorySize != MainApp.imageMemorySize) {
            refreshFrameTitle(true);
            if (MainApp.imageMemorySize > ConfigData.videoMemoryLimit / 2) {
                Thread.yield();
                System.gc();
                Thread.yield();
            }
            return true;
        }
        return false;
    }
    
    static void repaintDisplay() {
        if (MainApp.midlet != null) {
            final Display d = Display.getDisplay(MainApp.midlet);
            d.emuRepaintDisplay();
        }
    }
    
    public static MainApp getInstance() {
        return MainApp.executor;
    }
    
    public static EmuExecutor getEmuExecutor() {
        if (MainApp.executor == null) {
            MainApp.executor = new MainApp();
        }
        ParamReader.readParams();
        MainApp.parametersCount = 0;
        return MainApp.executor;
    }
    
    public static EmuExecutor getEmuExecutorRo() {
        if (MainApp.executor == null) {
            MainApp.executor = new MainApp();
        }
        MainApp.parametersCount = 0;
        return MainApp.executor;
    }
    
    private MainApp() {
        this.emuListener = null;
        MainApp.parametersCount = 0;
    }
    
    public void setEmuListener(final EmuListener listener) {
        this.emuListener = listener;
    }
    
    public void keyPressed(final KeyEvent e) {
        EmuCanvas.getInstance().keyPressed(e);
    }
    
    public void keyReleased(final KeyEvent e) {
        EmuCanvas.getInstance().keyReleased(e);
    }
    
    public void keyAction(final int actionType, final int key) {
        EmuCanvas.getInstance().keyAction(actionType, key);
    }
    
    public void setClassLoader(final ClassLoader cLoader) {
        MainApp.externalClassLoader = cLoader;
        if (MainApp.externalClassLoader != null) {
            MainApp.classLoaderType = 3;
            MainApp.classLoader = null;
        }
    }
    
    public void setParameter(final String arg) {
        setEmuParameter(arg, MainApp.parametersCount);
        ++MainApp.parametersCount;
    }
    
    public Object getParameter(final String param) {
        if (param.equals("-w")) {
            return new Integer(Display.WIDTH);
        }
        if (param.equals("-h")) {
            return new Integer(Display.HEIGHT);
        }
        return ConfigData.getParameter(param);
    }
    
    public boolean execute() {
        final Thread thread = new Thread(this);
        thread.start();
        return true;
    }
    
    public void terminate() {
        if (MainApp.midlet != null) {
            MainApp.midletBridge.handleEvent(4, MainApp.midlet);
        }
    }
    
    public void pauseApp() {
        if (MainApp.midlet != null) {
            MainApp.midletBridge.handleEvent(5, MainApp.midlet);
        }
    }
    
    public void startApp() {
        if (MainApp.midlet != null) {
            MainApp.midletBridge.handleEvent(6, MainApp.midlet);
        }
    }
    
    public void showNotify() {
        EmuCanvas.getInstance().showNotify();
    }
    
    public void hideNotify() {
        EmuCanvas.getInstance().hideNotify();
    }
    
    public void setParentComponent(final Component component) {
        MainApp.parentComponent = component;
    }
    
    public void run() {
        executeEmu();
    }
    
    public void setEvent(final String event, final Object data) {
        if (this.emuListener != null) {
            this.emuListener.emuUpadate(event, data);
        }
    }
}
