import javax.swing.*;
import java.awt.*;

/**
 * Global ability that blinds the entire screen like in CS:GO
 * Author: Hugo To
 */
public class Flashbang extends PowerUp {
	private static final int HOLD_MS = 1000; // how long it stays fully white
	private static final int FADE_MS = 1500; // how long the opacity takes to reach 0
	private static final int TICK_MS = 30; // time between opacity steps
	private static final int DURATION = HOLD_MS + FADE_MS; // whole effect

	private float opacity = 0f; // 1 is solid white, 0 is invisible

	/**
	 * Creates the flashbang and its screen-sized white image.
	 *
	 * @param windowWidth  the width of the playing field in pixels
	 * @param windowHeight the height of the playing field in pixels
	 */
	public Flashbang(int windowWidth, int windowHeight) {
		super(DURATION, "SPRITES/FlashbangImage.png");

		Image white = new ImageIcon(
				new ImageIcon("SPRITES/WHITE_RECTANGLE.png").getImage().getScaledInstance(windowWidth, windowHeight, Image.SCALE_SMOOTH))
				.getImage();

		// create a label draws the white image with the opacity
		animationLabel = new JLabel() {
			protected void paintComponent(Graphics g) {
				((Graphics2D) g).setComposite(AlphaComposite.SrcOver.derive(opacity));
				g.drawImage(white, 0, 0, null);
			}
		};
		animationLabel.setBounds(0, 0, windowWidth, windowHeight);
		animationLabel.setVisible(false);
	}

	/**
	 * Global ability, so it ignores which player collected it.
	 */
	public void use(int user) {
		if (canUse()) {
			SoundEffects.play("SFX/FlashbangSFX.wav", -30f);
			startFlash();
			performAbility();
		}
	}

	/**
	 * use the global state
	 */
	public boolean canUse(int user) {
		return canUse();
	}

	/**
	 * Shows the white image, hold if for a duration, then begins to fade then fades it out, all with one timer.
	 */
	public void startFlash() {
		opacity = 1f;
		animationLabel.setVisible(true);

		int steps = FADE_MS / TICK_MS;
		for (int i = 1; i <= steps; i++) {
			int step = i;
			delay(HOLD_MS + step * TICK_MS, () -> {
				opacity = 1f - (float) step / steps;
				animationLabel.repaint();
			});
		}
	}
	
	public void endPower() {
	    super.endPower();
	    opacity = 0f;
	}
	
	public void onDurationEnd() {
		animationLabel.setVisible(false);
	}
	
	
}