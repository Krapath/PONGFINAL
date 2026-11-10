/**
 * Player bound ability that inverts player controls
 * Author: Hugo To
 */
public class Glitch extends PowerUp {
    private static final int DURATION = 4000; // 4 seconds of inverted controls

    public Glitch() {
        super(DURATION, "SPRITES/Glitch.png"); // Make a cool glitch icon
    }

    public boolean isGlitched(int player) {
        return isActive[player];
    }

    public void use(int user) {
        if (canUse(user)) {
            SoundEffects.play("SFX/Glitch.wav", -15f); // Optional sound effect
            performAbility(user);
        }
    }
}