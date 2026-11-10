


/**
 * Author: Hugo To
 * November 5, 2026
 * A paddle that is to be controlled by a player, extends GameObject.
 * Paddles can only move up and down, the paddle itself does not read the keyboard, only the Pong class does
 */
public class Paddle extends GameObject {
	// speed value of paddle
	
	
	private static final int REF_WIDTH = 1600; // the screen size the pixel values are written for
	private static final int REF_HEIGHT = 900;
	private static final int SPEED = 15; // reference speed
	private int realSpeed;
	public PaddleState state = PaddleState.IDLE;
	private boolean frozen = false;
	public boolean paused;
	private boolean enlarged = false;
	private static double ENLARGED_SCALE = 1.5;
	private double sizeScale = 1.0; //base size scale
	private static final double EASE = 0.4; // rate at which it eases
	private int baseWidth;
	private int baseHeight;
	// three states of paddle can be moving
	public enum PaddleState {
	    IDLE,
	    MOVING_UP,
	    MOVING_DOWN
	}
	
	
	/**
	 * Creates an idle paddle of the provided size
	 *
	 * @param w the width of the paddle in pixels
	 * @param h the height of the paddle in pixels
	 */
	 
	public Paddle(int w, int h, int windowWidth, int windowHeight) {
		//store base sizes
		baseWidth = w * windowWidth / REF_WIDTH;
        baseHeight = h * windowHeight / REF_HEIGHT;
        
		setSize(w * windowWidth / REF_WIDTH, h * windowHeight / REF_HEIGHT);
		// make sure its never 0
		realSpeed = Math.max(1, (int) Math.round(SPEED * ((double) windowHeight / REF_HEIGHT)));		
	}

	public void setFrozen(boolean f) {
		frozen = f;
	}
	public void setEnlarged(boolean e) {
		enlarged = e;
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
	 * Gets what the paddle is doing in the current frame.
	 *
	 * returns PaddleState
	 */
	public PaddleState getState() {
		return state;
	}
	
	public void act() {
		if(paused) {
			return;
		}
		double target = 1.0;
		if (enlarged) {
			target = ENLARGED_SCALE;
		}
		// ease the current scale toward the target
		sizeScale += (target - sizeScale) * EASE;
		
		// resize from the base size, but make sure its centered properly
		int centerX = getX() + getWidth() / 2;
	    int centerY = getY() + getHeight() / 2;
	    
		setSize((int) (baseWidth * sizeScale), (int) (baseHeight * sizeScale));
		setX(centerX - getWidth() / 2);
	    setY(centerY - getHeight() / 2);
	}
}
 
	
