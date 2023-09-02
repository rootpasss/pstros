// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.media;

import javax.sound.sampled.Control;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.AudioSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.MidiSystem;
import ole.pstros.MainApp;
import java.io.InputStream;
import javax.sound.midi.Sequencer;

class MidiPlayer extends DummyPlayer
{
    private Sequencer sequencer;
    private boolean initialized;
    private int loopCount;
    private int currentLoop;
    
    public MidiPlayer(final InputStream stream) {
        if (MainApp.soundVerbose) {
            System.out.println("Player@" + this.hashCode() + " constructor");
        }
        try {
            this.sequencer = MidiSystem.getSequencer();
            if (this.sequencer == null) {
                System.out.println("MidiPlayer: no sequencer available...");
                return;
            }
            this.sequencer.open();
            final Sequence seq = MidiSystem.getSequence(stream);
            this.sequencer.setSequence(seq);
            this.loopCount = 1;
            this.currentLoop = this.loopCount;
            this.initialized = true;
        }
        catch (Exception e) {
            System.out.println("Midi player:" + e);
        }
    }
    
    public void startImpl() throws MediaException {
        if (this.initialized) {
            if (MainApp.verbose) {
                System.out.println("MidiPlayer.start()" + this);
            }
            this.sequencer.start();
            --this.currentLoop;
        }
    }
    
    public void stopImpl() throws MediaException {
        if (this.initialized) {
            this.sequencer.stop();
            this.currentLoop = this.loopCount;
        }
    }
    
    public void close() {
        if (this.initialized) {
            this.sequencer.close();
        }
        Manager.emuRemovePlayer(this);
        super.close();
    }
    
    public long getDuration() throws IllegalStateException {
        if (MainApp.soundVerbose) {
            System.out.println("Player@" + this.hashCode() + " getDuration()");
        }
        if (!this.initialized) {
            return -1L;
        }
        return this.sequencer.getMicrosecondLength();
    }
    
    public long getMediaTime() {
        if (MainApp.soundVerbose) {
            System.out.println("Player@" + this.hashCode() + " getMediaTime()");
        }
        if (!this.initialized) {
            return -1L;
        }
        super.getMediaTime();
        return this.sequencer.getMicrosecondPosition();
    }
    
    public long setMediaTime(final long now) throws MediaException {
        if (!this.initialized) {
            throw new MediaException();
        }
        super.setMediaTime(now);
        this.sequencer.setMicrosecondPosition(now);
        return this.sequencer.getMicrosecondPosition();
    }
    
    public void setLoopCount(final int count) {
        super.setLoopCount(count);
        if (MainApp.soundVerbose) {
            System.out.println("Player@" + this.hashCode() + " setLoopCount() " + count);
        }
        if (!this.initialized) {
            return;
        }
        if (MainApp.verbose) {
            System.out.println("setting loop count=" + this.loopCount + " to:" + this);
        }
        this.loopCount = count;
    }
    
    public void emuUpdatePlayer() {
        if (!this.initialized) {
            if (this.oldState != this.state && this.state == 300) {
                this.emuReportEvent("error", null);
                this.oldState = this.state;
            }
            return;
        }
        if (this.sequencer.isRunning()) {
            this.oldState = this.state;
            return;
        }
        if (this.state == 400) {
            try {
                if (this.loopCount == 1 && this.currentLoop == 0) {
                    super.stop();
                    this.currentLoop = 1;
                    this.emuReportEvent("endOfMedia", null);
                    return;
                }
                if (this.currentLoop > 1 || this.loopCount < 0) {
                    this.emuReportEvent("endOfMedia", null);
                    this.sequencer.setMicrosecondPosition(0L);
                    this.start();
                }
            }
            catch (Exception e) {
                if (MainApp.verbose) {
                    e.printStackTrace();
                }
            }
        }
        if (this.oldState != this.state && this.state == 300) {
            this.emuReportEvent("stopped", null);
            this.oldState = this.state;
        }
    }
    
    public int emuSetVolumeLevel(final int level) {
        super.emuSetVolumeLevel(level);
        return level;
    }
    
    public static void emuGetController() {
        final Mixer.Info[] mInfo = AudioSystem.getMixerInfo();
        final Mixer.Info curInfo = emuSelectMixerInfo(mInfo);
        if (curInfo == null) {
            return;
        }
        final Mixer mixer = AudioSystem.getMixer(curInfo);
        final Line.Info[] lInfo = mixer.getTargetLineInfo();
        final Line.Info curLineInfo = emuSelectLineInfo(lInfo);
        if (curLineInfo == null) {
            return;
        }
        try {
            final Line line = mixer.getLine(curLineInfo);
            final Control[] controls = line.getControls();
            emuSelectControl(controls);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void emuSelectControl(final Control[] controls) {
        if (controls == null) {
            return;
        }
        for (int i = 0; i < controls.length; ++i) {
            System.out.println("Control name=" + controls[i].toString());
        }
    }
    
    private static Mixer.Info emuSelectMixerInfo(final Mixer.Info[] info) {
        if (info == null) {
            return null;
        }
        for (int i = 0; i < info.length; ++i) {
            System.out.println("Mixer.Info name=" + info[i].getName() + " desc=" + info[i].getDescription() + " vendor=" + info[i].getVendor());
        }
        if (info.length > 1) {
            return info[1];
        }
        return null;
    }
    
    private static Line.Info emuSelectLineInfo(final Line.Info[] info) {
        if (info == null) {
            return null;
        }
        for (int i = 0; i < info.length; ++i) {
            System.out.println("Line.Info =" + info[i]);
        }
        if (info.length > 0) {
            return info[0];
        }
        return null;
    }
}
