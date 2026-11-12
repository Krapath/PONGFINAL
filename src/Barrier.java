import java.awt.Color;

/**
 * The goal line at the edge of a player's side. It only shows while the player
 * is shielded.
 */
public class Barrier extends GameObject {

	private static final Color BARRIER_COLOR = new Color(135, 206, 250);
	private static final int REF_WIDTH = 1600;
	private boolean shielded = false;

	/**
	 * @param w            width of the barrier in pixels on the reference screen
	 * @param windowWidth  the width of the playing field in pixels
	 * @param windowHeight the height of the playing field in pixels
	 */
	public Barrier(int w, int windowWidth, int windowHeight) {
		setSize(w * windowWidth / REF_WIDTH, windowHeight); // always the full height
		setColor(BARRIER_COLOR);
		setVisible(false);
	}

	/**
	 * Sets the shielded state of a barrier, making it visible, and providing
	 * protection against scoring
	 * 
	 * @param s
	 */
	public void setShielded(boolean s) {
		shielded = s;
		setVisible(s); // the barrier is only drawn while shielded
	}

	/**
	 * Returns the state of if barrier is shielded, which triggers deflection mechanic
	 * @return
	 */
	public boolean getShielded() {
		return shielded;
	}

	public void act() {

	}
}