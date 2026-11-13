/**
 * Author: Hugo To
 * November 10, 2026
 * Player bound ability that inverts player controls
 */
public class Glitch extends PowerUp {
    private static final int DURATION = 4000; // 4 seconds of inverted controls

    public Glitch() {
        super(DURATION, "SPRITES/Glitch.png"); // glitch icon
    }

    /**
     * Checks if a player is glitched
     * @param player, the Player 1 or 2 who triggered the ability
     * @return true when the given players controls are inverted
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
     * Uses player bound ability and sound effect
     * @param the Player 1 or 2 who triggered the ability
     */
    public void use(int user) {
        if (canUse(user)) {
            SoundEffects.play("SFX/Glitch.wav", -15f); 
            performAbility(user);
        }
    }
}