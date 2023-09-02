// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import ole.pstros.ConfigData;
import java.io.ByteArrayInputStream;
import ole.pstros.MainApp;
import java.io.InputStream;
import java.net.URLStreamHandlerFactory;
import java.net.URL;

public abstract class BaseClassLoader extends ClassLoader
{
    private static final char[] HEX;
    static BaseClassLoader instance;
    
    static {
        HEX = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    }
    
    public BaseClassLoader() {
        BaseClassLoader.instance = this;
    }
    
    public BaseClassLoader(final ClassLoader masterCl) {
        super(masterCl);
        BaseClassLoader.instance = this;
        URL.setURLStreamHandlerFactory(new BaseUrlFactory(this));
    }
    
    public InputStream getResourceAsStream(final String name) {
        if (MainApp.verbose) {
            System.out.println("JCL: getResource as stream=" + name);
        }
        final byte[] data = this.loadData(name);
        if (data != null) {
            this.delayRead(data);
            return new ByteArrayInputStream(data);
        }
        return super.getResourceAsStream(name);
    }
    
    public static InputStream getSystemResourceAsStream(final String name) {
        if (MainApp.verbose) {
            System.out.println("JCL: getSystemResource as stream=" + name);
        }
        final byte[] data = BaseClassLoader.instance.loadData(name);
        if (data != null) {
            BaseClassLoader.instance.delayRead(data);
            return new ByteArrayInputStream(data);
        }
        return ClassLoader.getSystemResourceAsStream(name);
    }
    
    public byte[] getResourceAsByteArray(String name) {
        if (MainApp.verbose) {
            System.out.println("JCL: getResource as byte array=" + name);
        }
        if (name.startsWith("/")) {
            name = name.substring(1, name.length());
        }
        final byte[] data = this.loadData(name);
        if (data != null) {
            this.delayRead(data);
        }
        return data;
    }
    
    private void delayRead(final byte[] data) {
        if (ConfigData.readSpeed > 0) {
            try {
                final int sleepVal = ConfigData.readSpeed * data.length / 1024;
                if (sleepVal > 0) {
                    Thread.sleep(sleepVal);
                }
            }
            catch (Exception ex) {}
        }
    }
    
    protected Class findClass(final String name) throws ClassNotFoundException {
        if (MainApp.verbose) {
            System.out.println("BaseClassLoader: findClass=" + name);
        }
        Class result = null;
        String loadName = name.replace('.', '/');
        if (!loadName.endsWith(".class")) {
            loadName = String.valueOf(loadName) + ".class";
        }
        final byte[] data = this.loadData(loadName);
        if (data != null) {
            result = this.defineClass(name, data, 0, data.length);
        }
        else {
            result = super.findClass(name);
        }
        return result;
    }
    
    protected abstract byte[] loadData(final String p0);
    
    protected void dumpByteArray(final byte[] data) {
        final int size = data.length;
        System.out.println("------ size=" + size + " -----");
        for (int i = 0; i < size; ++i) {
            int b = data[i];
            if (b < 0) {
                b += 256;
            }
            this.dumpByte(b, i);
        }
        System.out.println();
    }
    
    protected void dumpByte(final int b, final int index) {
        if (index % 16 == 0) {
            System.out.print(BaseClassLoader.HEX[index >> 12 & 0xF]);
            System.out.print(BaseClassLoader.HEX[index >> 8 & 0xF]);
            System.out.print(BaseClassLoader.HEX[index >> 4 & 0xF]);
            System.out.print(String.valueOf(BaseClassLoader.HEX[index & 0xF]) + "  | ");
        }
        System.out.print(BaseClassLoader.HEX[b >> 4]);
        System.out.print(String.valueOf(BaseClassLoader.HEX[b & 0xF]) + " ");
        if (index % 16 == 15) {
            System.out.println();
        }
    }
    
    public URL getResource(final String name) {
        System.out.println("Pstros BaseClassLoader: getResource called name=" + name);
        return null;
    }
    
    protected URL findResource(final String name) {
        System.out.println("Pstros BaseClassLoader: findResource called name=" + name);
        return null;
    }
}
