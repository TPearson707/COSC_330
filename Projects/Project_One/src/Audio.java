import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audio {

    String sound;

    Audio() {
        sound = " ";
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public void playSound(boolean loop) {
        try {
            File soundFile = new File(sound);
            System.out.println("Attempting to load: " + soundFile.getAbsolutePath());
    
            if (!soundFile.exists()) {
                throw new IOException("Sound file not found: " + soundFile.getAbsolutePath());
            }
    
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            clip.loop(loop ? Clip.LOOP_CONTINUOUSLY : 0);

            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }
}
