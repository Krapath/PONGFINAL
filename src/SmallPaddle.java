

//TODO: ADD ALL OF THE HEADER COMMENTS
public class SmallPaddle extends PowerUp {
	private static final int DURATION = 7500; // ability duration in ms

	/**
	 * Decrease the size of the opponents paddle for a duration.
	 *
	 */
	public SmallPaddle() {
		super(DURATION, "SPRITES/SmallPaddle.gif");
		

	}
	
	/**
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
	 * Player bound ability, increases size of paddle, and plays Mario size down sfx
	 */
	public void use(int user) {
		if (canUse(user)) {
			SoundEffects.play("SFX/powerdown.wav", -15f);
			performAbility(user);
		}
	}

	
}