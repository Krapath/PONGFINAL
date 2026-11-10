import javax.swing.*;
import java.awt.*;

public class BigPaddle extends PowerUp {
	private static final int DURATION = 7500; // ability duration in ms
	private static final int COOLDOWN = 10; // cooldown in ms not aplicable for this ability
	private static final int REF_WIDTH = 1600; 
	private static final int REF_HEIGHT = 900;


	/**
	 * Increases the size of the player's paddle for a duration.
	 * 
	 * @param boundsX screen width reference
	 * @param boundsY screen height reference
	 */
	public BigPaddle() {
		super(DURATION, "SPRITES/BigPaddle.gif");
		

	}

	//TODO: KINDA USELESS METHODS SUBJECT TO REMOVAL
	
	/**
	 * Return true if a paddle is currently enlarged.
	 */
	public boolean isEnlarged(int player) {
		return isActive[player];
	}
	

	
	public void use(int user) {
		if (canUse(user)) {
			SoundEffects.play("SFX/smbpowerup.wav", -15f);
			performAbility(user);
		}
	}

	
	public void onDurationEnd() {

	}
}