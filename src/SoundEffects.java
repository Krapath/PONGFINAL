
import java.io.*;
import javax.sound.sampled.*;

/**
 * Author: Hugo To
 * November 7, 2026
 * The sound effects that play throughout the game 
 */
public class SoundEffects {

	/**
	 * @param fileName the name of the audio file
	 * @param volume the volume of the audio file
	 */
	public static void play(String fileName, float volume) {
		// use try to make sure that it closes
		try (AudioInputStream audio = AudioSystem.getAudioInputStream(new File(fileName))) {

			Clip clip = AudioSystem.getClip();
			clip.open(audio);

			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(volume);

			// listen for when the sound finishes playing, then clean up to prevent memory
			// leak
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

	/**
	 * To prevent lag/ prevent it from being noticeable, run the sound effect system
	 * immediately to prevent it affecting user experience, could load all the audio
	 * files but figured this would be easier and less messy as it might affect visibility
	 */
	public static void loadAudioSystem() {
		try {
			AudioSystem.getClip().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
