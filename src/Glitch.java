/**
 * Player bound ability that inverts player controls
 * Author: Hugo To
 */
public class Glitch extends PowerUp {
    private static final int DURATION = 4000; // 4 seconds of inverted controls

    public Glitch() {
        super(DURATION, "SPRITES/Glitch.png"); // glitch icon
    }

    /**
     * Checks if a player is glitched
     * @param player
     * @return the given player that is glitched
     */
    public boolean isGlitched(int player) {
        if (player == 1) { 
        	return isActive[2]; // Player 1 is glitched if Player 2 activated it
        }
        if (player == 2) {
        	return isActive[1]; // Player 2 is glitched if Player 1 activated it
        }
        return false;
    }

    /**
     * uses player bound ability and sound effect
     */
    public void use(int user) {
        if (canUse(user)) {
            SoundEffects.play("SFX/Glitch.wav", -15f); 
            performAbility(user);
        }
    }
}