import javax.swing.*;
import java.awt.*;

public class TimeStop extends PowerUp {
    private static final int DURATION = 1500;// ability duration in ms
    private static final int COOLDOWN = 10; // not applicable for this ability
    private static final int FRAME_MS = 40; // how long each sprite shows, in milliseconds
    private static final int REF_WIDTH = 1600; 
    private static final int REF_HEIGHT = 900;

    // all the names of frames for animating
    private static final String SPRITE_PREFIX = "SPRITES/TheWorld_0-";
    private static final String SPRITE_TYPE = ".png";

    // sprite numbers in order, repeat a number to hold that frame
    private static final int[] TIME_STOP_FRAMES = {202, 203, 204, 204, 205, 206, 206, 206, 206, 206};
    // all of the frames
    private ImageIcon[] frames = new ImageIcon[TIME_STOP_FRAMES.length];
    private boolean animating = false;

    /**
     * Unleash the true power of The World to stop time against your opponent! 
     * Momentarily slows the ball as well (for balancing purposes)
     * @param locationX
     * @param locationY
     * @param user
     */
    public TimeStop(int windowWidth, int windowHeight, int pickUpSize) {
        super(DURATION, "SPRITES/ClockPowerup.gif");
        double basePickupSize = 50.0 * windowWidth / REF_WIDTH; 
        double scaleMultiplier = (double) pickUpSize / basePickupSize;

        for (int i = 0; i < TIME_STOP_FRAMES.length; i++) {
            ImageIcon rawImage = new ImageIcon(SPRITE_PREFIX + TIME_STOP_FRAMES[i] + SPRITE_TYPE);
            int w = (int) (rawImage.getIconWidth() * windowWidth / REF_WIDTH * scaleMultiplier);
            int h = (int) (rawImage.getIconHeight() * windowHeight / REF_HEIGHT * scaleMultiplier);
            frames[i] = new ImageIcon(rawImage.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
        }
        animationLabel = new JLabel("");
        animationLabel.setBounds(0, 0, windowWidth, windowHeight);
        animationLabel.setVisible(false);
    }

    /**
     * Return true from the moment the ability triggers until time resumes
     */
    public boolean isTimeSlowed() {
        return animating || isActive[1] || isActive[2];
    }
    
    /**
     * Return the player that is being frozen
     */
    public boolean isFrozen(int player) {
        if (player == 1) { 
        	return isActive[2]; // Player 1 is frozen if Player 2 activated it
        }
        if (player == 2) {
        	return isActive[1]; // Player 2 is frozen if Player 1 activated it
        }
        return false;
    }
    /**
     * Runs once after a number of milliseconds, without freezing the game.
     *
     * @param ms how long to wait, in milliseconds
     * @param action the code to run when the wait is over
     */

    /**
     * runs when the player uses the ability.
     */
    public void use(int user, int x, int y) {
        if (canUse(user) && !animating) {
            animating = true;
            
            // center the sprite on the pickup, then show it
            int w = frames[0].getIconWidth();
            int h = frames[0].getIconHeight();
            animationLabel.setBounds(x - w / 2, y - h / 2, w, h);
            animationLabel.setVisible(true);
            // SoundEffects.play("SFX/TheWorld.wav", -20f); //maybe keep, maybe add

            // show each sprite in turn
            for (int i = 0; i < frames.length; i++) {
                ImageIcon frame = frames[i];
                if (user == 1) {
                    delay(i * FRAME_MS, () -> animationLabel.setIcon(frame));
                } else if (user == 2) {
                    delay(i * FRAME_MS, () -> animationLabel.setIcon(flipHorizontally(frame)));
                }
            }

            // once the last sprite finishes had its time, stop time
            delay((frames.length - 5) * FRAME_MS, () -> {// remember change magic number
                animating = false;
                //SoundEffects.play("SFX/Timestop SE.wav", -20f);
                SoundEffects.play("SFX/TimestopSFX2.wav", -5f);
                super.performAbility(user);
            });
            
            delay((frames.length) * FRAME_MS, () -> animationLabel.setVisible(false));
        }
    }
    

}

