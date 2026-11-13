import java.awt.Graphics;
import java.util.Random;


/**
 * Author: Hugo To
 * November 7, 2026
 * An orb that exists only while it is on the field. It carries one power and
 * picks a random spot when created.
 */
public class Pickup extends GameObject {

	private PowerUp held; // the power this orb carries
	private Random rand = new Random();
	private static final int REF_HEIGHT = 900;

	/**
	 * Makes an orb at a random spot in the middle half of the field.
	 *
	 * @param size        width and height of the orb in pixels
	 * @param p           the power this orb gives
	 * @param windowWidth  width of the playing field in pixels
	 * @param windowWidth height of the playing field in pixels
	 */
	public Pickup(int size, PowerUp p, int windowWidth, int windowHeight) {
		int s = size * windowHeight / REF_HEIGHT; // takes reference size and scales it
		setSize(s, s);
		held = p;
		setX(windowWidth / 4 + rand.nextInt(windowWidth / 2 - s));
		setY(rand.nextInt(windowHeight - s));

	}

	/**
	 * @return the power this orb carries
	 */
	public PowerUp getPower() {
		return held;
	}

	/**
	 * Uses the power global or player specific and sets this orb's center for
	 * effects(like animation)
	 *
	 * @param user the player who collected it, 1 or 2
	 */
	public void use(int user) {
		// for powers that do not require x and y, this still works because the use
		// method simply recalls the no args use method, this way it allows powerups
		// that required x and y to use them, but ones that do not, are still unaffected
		held.use(user, getX() + getWidth() / 2, getY() + getHeight() / 2);
	}

	/**
	 * Makes sure to draw the icons image which all powerups have
	 */
	public void paint(Graphics g) {
		g.drawImage(held.getIcon(), 0, 0, getWidth(), getHeight(), null);
	}

	/**
	 * Updates to ensure image is drawn
	 */
	public void act() {
		repaint();
	}
}