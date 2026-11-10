import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;

/**
 * An orb that exists only while it is on the field. It carries one power and
 * picks a random spot when created.
 */
public class Pickup extends GameObject {

	private PowerUp held; // the power this orb carries
	private Random rand = new Random();
	private static final int REF_WIDTH = 1600; // the screen size the pixel values are written for
	private static final int REF_HEIGHT = 900;
	/**
	 * Makes an orb at a random spot in the middle half of the field.
	 *
	 * @param size        width and height of the orb in pixels
	 * @param p           the power this orb gives
	 * @param fieldWidth  width of the playing field in pixels
	 * @param fieldHeight height of the playing field in pixels
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
	 * Gives the power to a player, using this orb's center as the effect position.
	 *
	 * @param user the player who collected it, 1 or 2
	 */
	public void use(int user) {
		held.use(user, getX() + getWidth() / 2, getY() + getHeight() / 2);
	}

	public void paint(Graphics g) {
		g.drawImage(held.getIcon(), 0, 0, getWidth(), getHeight(), null);
	}

	public void act() {
		repaint();
	}
}