
/*
 * Author: Hugo To
 * November 8, 2026
 * Player bound ability that sets players paddle state to enlarged
 */
public class BigPaddle extends PowerUp {
	private static final int DURATION = 7500; // ability duration in ms
	private static final float SOUND_VOLUME = -15f;

	/**
	 * Increases the size of the player's paddle for a duration.
	 */
	public BigPaddle() {
		super(DURATION, "SPRITES/BigPaddle.gif");

	}

	/**
	 * @param player, the player indicated by 1 or 2
	 * @return true if a paddle is currently enlarged.
	 */
	public boolean isEnlarged(int player) {
		return isActive[player];
	}

	/**
	 * Player bound ability, increases size of paddle and plays sfx, courtesy of Mario
	 * @param user, the Player 1, or 2 who triggers the ability
	 */
	public void use(int user) {
		if (canUse(user)) {
			SoundEffects.play("SFX/smbpowerup.wav", SOUND_VOLUME);
			performAbility(user);
		}
	}

}