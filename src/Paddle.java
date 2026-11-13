
/**
 * Author: Hugo To 
 * November 5, 2026 
 * Paddle object that represents the players in Pong game
 */
public class Paddle extends GameObject {

	// Symbolic constants 

	private static final int REF_WIDTH = 1600; // the screen size the pixel values are written for
	private static final int REF_HEIGHT = 900;
	private static final int SPEED = 15; // reference speed
	private static final double ENLARGED_SCALE = 1.5;
	private static final double SHRUNK_SCALE = 0.75;
	private static final double EASE = 0.4; // rate at which it eases
	private int realSpeed;
	public PaddleState state = PaddleState.IDLE;
	private boolean frozen = false;
	public boolean paused;
	private boolean enlarged = false;
	private boolean shrunk = false;
	private double sizeScale = 1.0; // base size scale
	private int baseWidth;
	private int baseHeight;

	// three states of paddle can be moving
	public enum PaddleState {
		IDLE, MOVING_UP, MOVING_DOWN
	}

	/**
	 * Creates an idle paddle of the provided size, with scaled speed and size values
	 *
	 * @param w the width of the paddle in pixels
	 * @param h the height of the paddle in pixels
	 * @param windowWidth how wide the game window is 
	 * @param windowHeight how tall the game window is
	 */
	public Paddle(int w, int h, int windowWidth, int windowHeight) {
		// store base sizes
		baseWidth = w * windowWidth / REF_WIDTH;
		baseHeight = h * windowHeight / REF_HEIGHT;

		setSize(w * windowWidth / REF_WIDTH, h * windowHeight / REF_HEIGHT);
		// make sure its never 0
		realSpeed = Math.max(1, (int) Math.round(SPEED * ((double) windowHeight / REF_HEIGHT)));
	}

	/**
	 * Sets the frozen state of the paddle.
	 * 
	 * @param f boolean value that comes from a powerup
	 */
	public void setFrozen(boolean f) {
		frozen = f;
	}

	/**
	 * Sets the enlarged state of the paddle.
	 * 
	 * @param e boolean value that comes from a powerup
	 */
	public void setEnlarged(boolean e) {
		enlarged = e;
	}

	/**
	 * Sets the shrunk state of the paddle.
	 * 
	 * @param s a boolean value that comes from powerup
	 */
	public void setShrunk(boolean s) {
		shrunk = s;
	}

	/**
	 * Moves the paddle by the speed value upward and changes state to moving up
	 */
	public void moveUp() {
		if (frozen) {
			return;
		}
		setY(getY() - realSpeed);
		state = PaddleState.MOVING_UP;
	}

	/**
	 * Moves the paddle by the speed value downward and changes state to moving down
	 */
	public void moveDown() {
		if (frozen) {
			return;
		}
		setY(getY() + realSpeed);
		state = PaddleState.MOVING_DOWN;

	}

	/**
	 * Changes state to idle
	 */
	public void idle() {
		state = PaddleState.IDLE;
	}

	/**
	 * @return PaddleState, what the paddle is doing in the current frame. 
	 */
	public PaddleState getState() {
		return state;
	}

	/**
	 * Handles paddle size during ability states
	 */
	public void act() {
		if (paused) {
			return;
		}
		// base size value set at 1 under normal condition
		double target = 1.0;

		// set the target values for size
		if (enlarged) {
			target = ENLARGED_SCALE;
		}
		if (shrunk) {
			target = SHRUNK_SCALE;
		}

		// averages the two scale values when both active, as of currently
		// when both are active the paddle is slightly larger
		if (shrunk && enlarged) {
			target = ENLARGED_SCALE * SHRUNK_SCALE;
		}

		// ease the current scale toward the target rather than instantly change size.
		// Additionally, by having the scale be a constantly updating value, it
		// scales nonlinearly, making it more smooth. sizeScale will always be locked to
		// target. This method also allows sizeScale to work in both the positive and
		// negative direction. For example, target is 0.5, 1 += (0.5-1)*EASE, the right
		// side ends up being negative and size scale still decreases.
		sizeScale += (target - sizeScale) * EASE;

		// save the old center x and center y so that even after resize it remains the
		// same relative center, additionally makes the growth look more equal
		int centerX = getX() + getWidth() / 2;
		int centerY = getY() + getHeight() / 2;

		// set the size of the paddle based on size scale
		setSize((int) (baseWidth * sizeScale), (int) (baseHeight * sizeScale));
		// always set their center to the same place
		setX(centerX - getWidth() / 2);
		setY(centerY - getHeight() / 2);
	}
}
