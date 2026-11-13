
/*
 * Author: Hugo To
 * November 11, 2026
 * Player bound ability that sets opponents paddle state to shrunk
 */
public class SmallPaddle extends PowerUp {
	private static final int DURATION = 7500; // ability duration in ms

	/**
	 * Decrease the size of the opponents paddle for a duration.
	 */
	public SmallPaddle() {
		super(DURATION, "SPRITES/SmallPaddle.gif");
		

	}
	
	/**
	 * @param user the Player 1 or 2 who triggered the ability
	 * @return true if a paddle is currently shrunk.
	 */
	public boolean isShrunk(int player) {
        if (player == 1) { 
        	return isActive[2]; // Player 1 is small if Player 2 activated it
        }
        if (player == 2) {
        	return isActive[1]; // Player 2 is small if Player 1 activated it
        }
        return false;	
       
	}
	

	/**
	 * Player bound ability, decreases size of opponent paddle, and plays sfx, courtesy of Mario
	 * @param user Player 1 or 2, who triggered the ability
	 */
	public void use(int user) {
		if (canUse(user)) {
			SoundEffects.play("SFX/powerdown.wav", -15f);
			performAbility(user);
		}
	}

	
}