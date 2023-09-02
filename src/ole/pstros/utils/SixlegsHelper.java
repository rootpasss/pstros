// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import java.awt.image.BufferedImage;
import java.io.InputStream;
//import com.sixlegs.png.PngImage;

public class SixlegsHelper
{
    private static java.awt.Image png;
    private static byte status;
    private static boolean available;
    
    public static boolean isAvailable() {
        if (SixlegsHelper.status == 0) {
            try {
                final Class sl = Class.forName("com.sixlegs.png.PngImage");
                //SixlegsHelper.png = sl.newInstance();
                SixlegsHelper.available = true;
                System.out.println("Sixlegs found!");
            }
            catch (Exception e) {
                System.out.println("Sixlegs not available on classpath. Using default image loader.");
                System.out.println(e);
                SixlegsHelper.available = false;
            }
            SixlegsHelper.status = 1;
        }
        return SixlegsHelper.available;
    }
    
    public static BufferedImage getImage(final InputStream stream) {
        try {
            //return SixlegsHelper.png.read(stream, false);
          return javax.imageio.ImageIO.read(stream);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
