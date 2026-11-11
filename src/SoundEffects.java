
import java.io.*;
import javax.sound.sampled.*;

/**
 * The sound effects that play throughout the game 
 * Author: Hugo To
 */
public class SoundEffects {

	/**
	 * @param fileName, the name of the audio file
	 * @param volume, the volume of the audio file
	 */
    public static void play(String fileName, float volume) {
        // use try to make sure that it closes
        try (AudioInputStream audio = AudioSystem.getAudioInputStream(new File(fileName))) {

            Clip clip = AudioSystem.getClip();
            clip.open(audio);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume);

            // listen for when the sound finishes playing, then clean up to prevent memory leak
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });

            clip.start(); // plays in background

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
