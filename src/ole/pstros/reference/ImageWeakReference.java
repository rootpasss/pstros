// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.reference;

import java.lang.ref.ReferenceQueue;
import javax.microedition.lcdui.Image;
import java.lang.ref.WeakReference;

public class ImageWeakReference extends WeakReference
{
    private int imageSize;
    
    public ImageWeakReference(final Image image, final ReferenceQueue q) {
        super(image, q);
        this.imageSize = image.getWidth() * image.getHeight();
    }
    
    public int getImageSize() {
        return this.imageSize;
    }
}
