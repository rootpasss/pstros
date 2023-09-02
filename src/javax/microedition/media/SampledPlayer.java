// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.media;

import javax.sound.sampled.LineEvent;
import java.io.FileOutputStream;
import java.io.DataInputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Line;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import ole.pstros.utils.SoundConvertor;
import ole.pstros.ConfigData;
import ole.pstros.MainApp;
import java.io.InputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineListener;

class SampledPlayer extends DummyPlayer implements LineListener
{
    private static int fileCounter;
    private Clip clip;
    private int loopCount;
    private int currentLoop;
    private boolean initialized;
    static /* synthetic */ Class class$0;
    
    public SampledPlayer(final InputStream stream) {
        if (MainApp.soundVerbose) {
            System.out.println("Player@" + this.hashCode() + " constructor");
        }
        try {
            final String storeImagePath = ConfigData.storeImagePath;
            final InputStream convertedStream = SoundConvertor.convertData(stream);
            final String storeImagePath2 = ConfigData.storeImagePath;
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(convertedStream);
                AudioFormat format = ais.getFormat();
                boolean mp3active = false;
                if (format.getClass().getName().indexOf("MpegAudioFormat") > 0) {
                    mp3active = true;
                    final AudioFormat decodedFormat = format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 16, format.getChannels(), format.getChannels() * 2, format.getSampleRate(), false);
                    ais = AudioSystem.getAudioInputStream(format, ais);
                }
                Class class$0;
                if ((class$0 = SampledPlayer.class$0) == null) {
                    try {
                        class$0 = (SampledPlayer.class$0 = Class.forName("javax.sound.sampled.Clip"));
                    }
                    catch (ClassNotFoundException ex2) {
                        throw new NoClassDefFoundError(ex2.getMessage());
                    }
                }
                final DataLine.Info info = new DataLine.Info(class$0, format);
                if (!AudioSystem.isLineSupported(info)) {
                    System.out.println("Error: SapmledPlayer: input data not supported! " + format.getEncoding());
                    return;
                }
                this.clip = (Clip)AudioSystem.getLine(info);
                if (!mp3active) {
                    ais.reset();
                }
                this.clip.open(ais);
            }
            catch (LineUnavailableException ex) {
                System.out.println("Error: SapmledPlayer: " + ex);
                ex.printStackTrace();
            }
            this.loopCount = 1;
            this.currentLoop = this.loopCount;
            this.initialized = true;
            this.clip.addLineListener(this);
            this.clip.setFramePosition(0);
        }
        catch (Exception e) {
            System.out.println("Sampled player:" + e);
            e.printStackTrace();
        }
    }
    
    public static void saveAudio(final InputStream is, final String path, final String extension) {
        final DataInputStream dis = new DataInputStream(is);
        try {
            final int size = dis.available();
            final byte[] data = new byte[size];
            dis.readFully(data);
            final FileOutputStream fos = new FileOutputStream(String.valueOf(path) + "sample" + Integer.toString(SampledPlayer.fileCounter) + extension);
            ++SampledPlayer.fileCounter;
            fos.write(data);
            fos.flush();
            fos.close();
        }
        catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
    
    public void start() throws MediaException {
        super.start();
        if (this.initialized) {
            this.clip.start();
            --this.currentLoop;
        }
    }
    
    public void stop() throws MediaException {
        super.stop();
        if (this.initialized) {
            this.clip.stop();
            this.currentLoop = this.loopCount;
        }
    }
    
    public void close() {
        super.close();
        if (this.initialized) {
            this.clip.close();
        }
        this.clip.addLineListener(this);
        Manager.emuRemovePlayer(this);
    }
    
    public long getDuration() throws IllegalStateException {
        if (MainApp.soundVerbose) {
            System.out.println("Player@" + this.hashCode() + " getDuration()");
        }
        if (!this.initialized) {
            return -1L;
        }
        return this.clip.getMicrosecondLength();
    }
    
    public long getMediaTime() {
        if (MainApp.soundVerbose) {
            System.out.println("Player@" + this.hashCode() + " getMediaTime()");
        }
        if (!this.initialized) {
            return -1L;
        }
        return this.clip.getMicrosecondPosition();
    }
    
    public long setMediaTime(final long now) throws MediaException {
        if (MainApp.soundVerbose) {
            System.out.println("Player@" + this.hashCode() + " setMediaTime() " + now);
        }
        if (!this.initialized) {
            return -1L;
        }
        this.clip.setMicrosecondPosition(now);
        return now;
    }
    
    public void setLoopCount(final int count) {
        if (MainApp.soundVerbose) {
            System.out.println("Player@" + this.hashCode() + " setLoopCount() " + count);
        }
        if (!this.initialized) {
            return;
        }
        super.setLoopCount(count);
        this.loopCount = count;
    }
    
    public void emuUpdatePlayer() {
        if (this.clip.isRunning()) {
            this.oldState = this.state;
            return;
        }
        if (this.state == 400) {
            this.update(new LineEvent(this.clip, LineEvent.Type.STOP, 0L));
        }
    }
    
    public void update(final LineEvent event) {
        if (event.getType() == LineEvent.Type.STOP && this.state == 400) {
            try {
                if (this.loopCount == 1 && this.currentLoop == 0) {
                    super.stop();
                    this.currentLoop = 1;
                    this.clip.stop();
                    this.clip.setFramePosition(0);
                    this.emuReportEvent("endOfMedia", null);
                    return;
                }
                if (this.currentLoop > 1 || this.loopCount < 0) {
                    this.emuReportEvent("endOfMedia", null);
                    this.clip.stop();
                    this.clip.setFramePosition(0);
                    this.start();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
