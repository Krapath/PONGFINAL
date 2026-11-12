import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;
import java.util.ArrayList;

/**
 * Timed ability with a duration in milliseconds, runs on its own timers, so
 * nothing in game needs to call it every frame. Each power also has an icon,
 * which is import for the pickup orbs abilities can either belong to a player
 * (use the methods that take a user) or be global and affect the whole game
 * (use the methods without a user). Author: Hugo To
 */

//While there are no shared abstract methods, the class is abstract in nature, meaning that it should not be instantiated as its own object
public abstract class PowerUp {

	private static final int PLAYERS = 2;

	// protected so only the inherited classes can use them
	// how long the ability lasts, in milliseconds
	protected int maxDuration;

	// whether the ability is currently active for each player, index 1 and 2 are
	// the players
	protected boolean[] isActive = new boolean[PLAYERS + 1];
	// whether a global ability is currently active
	protected boolean globalActive = false;
	private ArrayList<Timer> timers = new ArrayList<Timer>();

	// picture shown on the pickup orb for this power
	protected Image icon;

	// optional animation label if a powerup requires it
	protected JLabel animationLabel;

	/**
	 * Create an ability with a duration and icon.
	 *
	 * @param maxDur   how long the ability lasts, in milliseconds
	 * @param iconFile file path of the picture shown on the pickup
	 */
	public PowerUp(int maxDur, String iconFile) {
		maxDuration = maxDur;
		icon = new ImageIcon(iconFile).getImage();
	}

	/**
	 * return the picture shown on the pickup orb.
	 */
	public Image getIcon() {
		return icon;
	}

	public JLabel getAnimationLabel() {
		return animationLabel;
	}

	// flip image to denote player/ side
	protected ImageIcon flipHorizontally(ImageIcon icon) {
		int w = icon.getIconWidth();
		int h = icon.getIconHeight();
		BufferedImage flipped = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = flipped.createGraphics();
		g.drawImage(icon.getImage(), w, 0, -w, h, null); // start at the right edge and draw backward
		g.dispose();// always executes, preventing resource leaks and lag
		return new ImageIcon(flipped);
	}

	/**
	 * @return true if the ability is currently active for this player.
	 *
	 * @param user the player, 1 or 2
	 */
	public boolean isActiveFor(int user) {
		return isActive[user];
	}

	/**
	 * @return true if the ability is ready to be used by this player.
	 *
	 * @param user the player, 1 or 2
	 */
	public boolean canUse(int user) {
		return !isActive[user];
	}

	/**
	 * Called by the pickup when a player collects it. All power can override this
	 * to decide what to do with the player number (1 or 2). By default it just
	 * performs ability which runs the timer.
	 *
	 * @param user the player who collected it, 1 or 2
	 */
	public void use(int user) {
		performAbility(user);
	}

	/**
	 * Same as use(user), but also tells the power where the pickup was.
	 *
	 * @param user the player who collected it, 1 or 2
	 * @param x    x of the center of the pickup
	 * @param y    y of the center of the pickup
	 */
	public void use(int user, int x, int y) {
		use(user);
	}

	/**
	 * Trigger the ability for a player if it can be used. When the duration runs
	 * out, the subclass is notified. Each player has their own timer, so both
	 * players can have the ability at the same time.
	 *
	 * @param user the player, 1 or 2
	 */
	public void performAbility(int user) {
		if (canUse(user)) {
			isActive[user] = true;
			delay(maxDuration, () -> {
				isActive[user] = false;
				onDurationEnd(user); // notify subclass
			});
		}
	}

	/**
	 * called when ability duration ends for a player
	 *
	 * @param user the player, 1 or 2
	 */
	public void onDurationEnd(int user) {

	}

	// ----- global abilities, they affect the whole game and have no user -----

	/**
	 * @return true if the global ability is currently active.
	 */
	public boolean isGlobalActive() {
		return globalActive;
	}

	/**
	 * @return true if the global ability is ready to be used.
	 */
	public boolean canUse() {
		return !globalActive;
	}

	/**
	 * Trigger a global ability if it can be used. When the duration runs out, the
	 * subclass is notified.
	 */
	public void performAbility() {
		if (canUse()) {
			globalActive = true;
			delay(maxDuration, () -> {
				globalActive = false;
				onDurationEnd(); // notify subclass
			});
		}
	}

	public void endPower() {
		for (int i = 0; i < isActive.length; i++) {
			isActive[i] = false;
		}
		// clear and stop all timers
		// without stopping the timers/removing the, existing abilities timers would be
		// running even if its not doing anything
		for (Timer t : timers) {
			t.stop();
		}
		timers.clear();

		globalActive = false;
		if (animationLabel != null) {
			animationLabel.setVisible(false);
		}
	}

	/**
	 * Called when a global ability duration ends, designed to be overloaded by child classes
	 */
	public void onDurationEnd() {

	}

	/**
	 * Runs the specific action once after a number of milliseconds, without having
	 * to pause or convoluted timer systems
	 *
	 * @param ms     how long to wait, in milliseconds
	 * @param action the code to run when the wait is over
	 */
	public void delay(int ms, Runnable action) {
		Timer t = new Timer(ms, null);

		// in order to make sure we can remove the timer, we need to add the action
		// listener AFTER it is initialized so it can make a reference to itself to
		// remove from the arraylist
		// the importance of removing timer is that otherwise a new timer object that is
		// will be created and never garbaged collected
		// lambda expression because actionlistener is a functional interface
		
		t.addActionListener((e) -> {
			timers.remove(t);
			action.run();
		});
		t.setRepeats(false); // do it only once once, not over and over
		t.start();
	}
}