import javax.swing.*;
import java.awt.*;

public class Shield extends PowerUp {
	private static final int DURATION = 5000; // ability duration in ms
	private static final int COOLDOWN = 10; // cooldown in ms not aplicable for this ability
	private static final int REF_WIDTH = 1600; 
	private static final int REF_HEIGHT = 900;


	/**
	 * Increases the size of the player's paddle for a short period of time.
	 * 
	 * @param boundsX screen width reference
	 * @param boundsY screen height reference
	 */
	public Shield() {
		super(DURATION, "SPRITES/Shield.png");
		

	}

	
	/**
	 * @return if a paddle is currently shielded .
	 */
	public boolean isShielded(int player) {
		return isActive[player];
	}
	

	/**
	 * uses ability and plays sfx from Kingdom Rush 
	 */
	public void use(int user) {
		if (canUse(user)) {
			SoundEffects.play("SFX/ReinforcementsEvent2.wav", -15f);
			performAbility(user);

		}
	}

	
	public void onDurationEnd() {

	}
}