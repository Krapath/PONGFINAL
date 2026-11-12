import javax.swing.*;
import java.awt.*;

public class BigPaddle extends PowerUp {
	private static final int DURATION = 7500; // ability duration in ms
	private static final float SOUND_VOLUME = -15f;

	/**
	 * Increases the size of the player's paddle for a duration.
	 * 
	 * @param boundsX screen width reference
	 * @param boundsY screen height reference
	 */
	public BigPaddle() {
		super(DURATION, "SPRITES/BigPaddle.gif");

	}

	/**
	 * Return true if a paddle is currently enlarged.
	 */
	public boolean isEnlarged(int player) {
		return isActive[player];
	}

	/**
	 * Player bound ability, increases size of paddle, and plays Mario size up sfx
	 */
	public void use(int user) {
		if (canUse(user)) {
			SoundEffects.play("SFX/smbpowerup.wav", SOUND_VOLUME);
			performAbility(user);
		}
	}

}