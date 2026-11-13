import java.awt.Color;

/*
 * Author: Hugo To
 * November 9, 2026
 * A physical barrier class that extends game object. 
 * It only shows while the player is shielded.
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
	 * @param s boolean value that comes from a powerup
	 */
	public void setShielded(boolean s) {
		shielded = s;
		setVisible(s); // the barrier is only drawn while shielded
	}

	/**
	 * @return The state of if barrier is shielded, which triggers deflection mechanic
	 */
	public boolean getShielded() {
		return shielded;
	}

	/**
	 * Does nothing, not necessary to be run in the class itself
	 */
	public void act() {

	}
}