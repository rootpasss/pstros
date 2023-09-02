// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.media;

import javax.microedition.io.Connector;
import java.io.IOException;
import ole.pstros.MainApp;
import ole.pstros.ConfigData;
import java.io.InputStream;
import java.util.Vector;

public class Manager
{
    public static final String TONE_DEVICE_LOCATOR = "device://tone";
    private static Vector players;
    
    static {
        Manager.players = new Vector();
    }
    
    public static synchronized Player createPlayer(final InputStream stream, final String type) throws IOException, MediaException {
        if (ConfigData.forceMute) {
            return new DummyPlayer();
        }
        if (MainApp.verbose) {
            System.out.println("Manager.createPlayer(InputStream,..) type =" + type);
        }
        if (type.equals("audio/midi")) {
            final Player p = new MidiPlayer(stream);
            Manager.players.add(p);
            return p;
        }
        if (type.equals("audio/x-wav")) {
            final Player p = new SampledPlayer(stream);
            Manager.players.add(p);
            return p;
        }
        if (type.equals("audio/amr") && ConfigData.storeImages) {
            SampledPlayer.saveAudio(stream, ConfigData.storeImagePath, ".amr");
        }
        return new DummyPlayer();
    }
    
    public static synchronized Player createPlayer(final String locator) throws IOException, MediaException {
        if (MainApp.verbose) {
            System.out.println("Manager.createPlayer(locator) locator=" + locator);
        }
        final InputStream is = Connector.openInputStream(locator);
        if (is == null) {
            return new DummyPlayer();
        }
        final Player p = new SampledPlayer(is);
        Manager.players.add(p);
        return p;
    }
    
    public static synchronized String[] getSupportedContentTypes(final String protocol) {
        System.out.println("Manager.getSupportedContentTypes protocol=" + protocol);
        return new String[0];
    }
    
    public static synchronized String[] getSupportedProtocols(final String content_type) {
        System.out.println("Manager.getSupportedPotocol content type=" + content_type);
        return new String[0];
    }
    
    public static synchronized void playTone(final int note, final int duration, final int volume) throws MediaException {
    }
    
    public static synchronized void emuRemovePlayer(final Player p) {
        Manager.players.remove(p);
    }
    
    public static synchronized void emuUpdatePlayers() {
        for (int size = Manager.players.size(), i = 0; i < size; ++i) {
            final Player player = (Player)Manager.players.get(i);
            player.emuUpdatePlayer();
        }
    }
    
    public static void emuStopPlayers() {
        for (int size = Manager.players.size(), i = 0; i < size; ++i) {
            final Player player = (Player)Manager.players.get(i);
            try {
                player.stop();
            }
            catch (Exception ex) {}
        }
    }
}
