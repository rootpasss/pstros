// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.reference;

import java.lang.ref.Reference;
import ole.pstros.ConfigData;
import javax.microedition.lcdui.Image;
import java.util.Vector;
import java.lang.ref.ReferenceQueue;

public class ImageReferenceManager
{
    private static ReferenceQueue referenceQueue;
    private static Vector references;
    private static int imageMemorySize;
    private static int peakMemorySize;
    
    static {
        ImageReferenceManager.referenceQueue = new ReferenceQueue();
        ImageReferenceManager.references = new Vector();
        ImageReferenceManager.imageMemorySize = 0;
        ImageReferenceManager.peakMemorySize = 0;
    }
    
    public static void addImage(final Image image) throws Exception {
        final ImageWeakReference iwr = new ImageWeakReference(image, ImageReferenceManager.referenceQueue);
        ImageReferenceManager.references.add(iwr);
        ImageReferenceManager.imageMemorySize += iwr.getImageSize();
        if (ImageReferenceManager.imageMemorySize > ImageReferenceManager.peakMemorySize) {
            ImageReferenceManager.peakMemorySize = ImageReferenceManager.imageMemorySize;
        }
        if (ImageReferenceManager.imageMemorySize > ConfigData.videoMemoryLimit) {
            throw new Exception("not enough video memory ! allocated=" + ImageReferenceManager.imageMemorySize + " limit=" + ConfigData.videoMemoryLimit);
        }
    }
    
    public static int getImageMemorySize() {
        return ImageReferenceManager.imageMemorySize;
    }
    
    public static int getPeakMemorySize() {
        return ImageReferenceManager.peakMemorySize;
    }
    
    public static int update() {
        for (Reference ir = ImageReferenceManager.referenceQueue.poll(); ir != null; ir = ImageReferenceManager.referenceQueue.poll()) {
            ImageReferenceManager.imageMemorySize -= ((ImageWeakReference)ir).getImageSize();
            ImageReferenceManager.references.remove(ir);
        }
        return ImageReferenceManager.imageMemorySize;
    }
    
    public static Vector getImages() {
        final Vector result = new Vector();
        final int size = ImageReferenceManager.references.size();
        int memorySize = 0;
        for (int i = 0; i < size; ++i) {
            final ImageWeakReference wr = (ImageWeakReference)ImageReferenceManager.references.get(i);
            if (wr != null) {
                final Object object = wr.get();
                if (object != null) {
                    result.add(object);
                }
                if (object instanceof Image) {
                    final Image img = (Image)object;
                    memorySize += img.getWidth() * img.getHeight();
                }
            }
        }
        System.out.println("Pstros: total size of the images :" + (memorySize >> 10) + " kPix");
        return result;
    }
}
