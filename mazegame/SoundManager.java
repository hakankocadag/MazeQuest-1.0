//Ali
package mazegame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;


public class SoundManager {
    private Clip backgroundClip;
    private float volume = 0.6f;


    public void loadBackground(String path) {
        stopBackground();
        closeClip();

        try {
            InputStream raw = getClass().getResourceAsStream(path.startsWith("/") ? path : "/" + path);
            if (raw != null) {
                try (BufferedInputStream buf = new BufferedInputStream(raw)) {
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(buf);
                    backgroundClip = AudioSystem.getClip();
                    backgroundClip.open(audioStream);
                    setVolume(volume);
                    return;
                }
            }
        } catch (Exception ignore) {
        }

        try {
            File audioFile = new File(path);
            if (!audioFile.exists()) {
                System.err.println("Background music file not found: " + audioFile.getAbsolutePath());
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioStream);
            setVolume(volume);
        } catch (Exception ex) {
            System.err.println("Could not load background music: " + ex.getMessage());
            backgroundClip = null;
        }
    }

    /** Start looping the background track. */
    public void playBackground() {
        if (backgroundClip == null) return;
        backgroundClip.setFramePosition(0);
        backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /** Stop playback but keep the clip loaded. */
    public void stopBackground() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
        }
    }

    /** Free the clip resources. */
    public void closeClip() {
        if (backgroundClip != null) {
            backgroundClip.close();
            backgroundClip = null;
        }
    }

    
    public void setVolume(float level) {
        volume = Math.max(0f, Math.min(1f, level));
        if (backgroundClip == null) return;
        try {
            FloatControl gainControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume == 0 ? 0.0001 : volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        } catch (Exception ignore) {
        }
    }

    public boolean isLoaded() {
        return backgroundClip != null;
    }
}
