
/**
 * Author: Hugo To
 * November 10, 2026
 * Ability that activates barrier, preventing scorinng
 */
public class Shield extends PowerUp {
	private static final int DURATION = 5000; // ability duration in ms


	/**
	 * Activates the barrier for a short period of time, preventing scoring
	 */
	public Shield() {
		super(DURATION, "SPRITES/Shield.png");
		

	}

	
	/**
	 * @return if a paddle is currently shielded .
	 * @param player the Player 1 or 2 who triggered the ability
	 */
	public boolean isShielded(int player) {
		return isActive[player];
	}
	

	/**
	 * Uses ability and plays sfx courtesy of Kingdom Rush 
	 * @param user the Player 1, or 2 who triggers the ability
	 */
	public void use(int user) {
		if (canUse(user)) {
			SoundEffects.play("SFX/ReinforcementsEvent2.wav", -15f);
			performAbility(user);

		}
	}

}