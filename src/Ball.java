
import java.awt.Color;

/*
 * Author: Hugo To
 * November 5, 2026
 * Ball class that extends GameObject, has a vertical and horizontal velocity and is always moving
 * Handles logic for whenever the ball needs to bounce.
 */
public class Ball extends GameObject {

	// symbolic constants
	// the screen size the pixel values are written for
	private static final int REF_WIDTH = 1600; 
	private static final int REF_HEIGHT = 900;
	private static final int SPEED = 20;
	private static final int MAX_SPEED = 40;
	private static final int SECTIONS = 5;
	private static final int DY_PER_SECTION = 4;
	private static final int PADDLE_MOTION_BONUS = 4;
	private static final int REFLECTION_BONUS = 1;
	private static final float SOUND_VOLUME = -30f;
	//TODO: remember 2 sounds
	private double speedScale = 1.0; // current speed multiplier
	private static final double EASE = 0.4; // rate at which it eases
	private static final double SLOW_SCALE = 0.4; // speed while slowed, 0.5 is half
	private boolean slowed = false;
	private double xLeftover = 0; // leftover pixels to deal with integer division
	private double yLeftover = 0;
	private double scaleX; // how many real pixels one pixel of speed is, sideways
	private double scaleY; // same, up and down
	private int ownership = 0; // 0 for no one, 1 for player 1 and 2 for player 2
	private int dx = SPEED;
	private int dy;
	public boolean paused = false;

	/**
	 * Makes a yellow ball of the given size, heading right at the normal speed.
	 *
	 * @param w how wide the ball is, in pixels
	 * @param h how tall the ball is, in pixels
	 */
	public Ball(int w, int h, int windowWidth, int windowHeight) {
		scaleX = (double) windowWidth / REF_WIDTH;
		scaleY = (double) windowHeight / REF_HEIGHT;
		setSize(w * windowHeight / REF_HEIGHT, h * windowHeight / REF_HEIGHT);
		setColor(Color.YELLOW);
	}

	/**
	 * Tells the ball to ease down to slow speed (true) or back up to normal speed
	 * (false).
	 */
	public void setSlowed(boolean s) {
		slowed = s;
	}

	public int getOwner() {
		return ownership;
	}

	/**
	 * Resets the ball to normal speed and sends it right with only horizontal
	 * velocity
	 */
	public void serveRight() {
		dx = SPEED;
		dy = 0;
		ownership = 0;
	}

	/**
	 * Resets the ball to normal speed and sends it left with only horizontal
	 * velocity
	 */
	public void serveLeft() {
		dx = -SPEED;
		dy = 0;
		ownership = 0;

	}

	/**
	 * Makes the ball head down, making its vertical velocity component
	 * positive(down)
	 */
	public void bounceDown() {
		dy = Math.abs(dy);
	}

	/**
	 * Makes the ball head up, making its vertical velocity component negative(up)
	 */
	public void bounceUp() {
		dy = -Math.abs(dy);
	}

	/**
	 * Bounces the ball off the left paddle, sending it to the right and increasing
	 * its speed each time hit, x speed is clamped
	 * 
	 * @param paddleY      the y coordinate of the top of the paddle that was hit
	 * @param paddleHeight the height of the paddle that was hit in pixels
	 * @param paddleState  whether the paddle was idle, moving up or moving down
	 *                     when it hit
	 */
	public void bounceRight(int paddleY, int paddleHeight, Paddle.PaddleState paddleState) {
		ownership = 1;
		dx = Math.min(Math.abs(dx) + REFLECTION_BONUS, MAX_SPEED); // ensure the ball moves to the right and speed
		SoundEffects.play("SFX/bounce.wav", SOUND_VOLUME); // capped, increment speed
		bouncePhysics(paddleY, paddleHeight, paddleState);

	}
	
	public void deflectRight() {
		if (dx < 0) { // only when heading into the left goal
			ownership = 0;
			dx = -dx;
			dy = -dy;
			SoundEffects.play("SFX/Parry.wav", -20f);
		}
	}

	public void deflectLeft() {
		if (dx > 0) { // only when heading into the right goal
			ownership = 0;
			dx = -dx;
			dy = -dy;
			SoundEffects.play("SFX/Parry.wav", -20f);
		}
	}

	
	/**
	 * Bounces the ball off the right paddle, sending it to the left and increasing
	 * its speed each time hit, x speed is clamped
	 * 
	 * @param paddleY      the y coordinate of the top of the paddle that was hit
	 * @param paddleHeight the height of the paddle that was hit in pixels
	 * @param paddleState  whether the paddle was idle, moving up or moving down
	 *                     when it hit
	 */
	public void bounceLeft(int paddleY, int paddleHeight, Paddle.PaddleState paddleState) {
		ownership = 2;
		dx = -Math.min(Math.abs(dx) + REFLECTION_BONUS, MAX_SPEED); // ensure the ball moves to the left and speed
		SoundEffects.play("SFX/bounce.wav", SOUND_VOLUME); // capped, increment speed

		bouncePhysics(paddleY, paddleHeight, paddleState);

	}

	/**
	 * Splits the paddle into sections, each section, gives a base vertical speed
	 * Paddle movement will add an additional vertical component, y speed is clamped
	 *
	 * @param paddleY      the y coordinate of the top of the paddle that was hit
	 * @param paddleHeight the height of the paddle that was hit in pixels
	 * @param paddleState  whether the paddle was idle, moving up or moving down
	 *                     when it hit
	 */
	private void bouncePhysics(int paddleY, int paddleHeight, Paddle.PaddleState paddleState) {
		int ballCenterY = getY() + (getHeight() / 2);
		// find the balls y relative to the paddle
		int relativeY = ballCenterY - paddleY;
		// find the section it was hit along the paddle
		int sectionHeight = paddleHeight / SECTIONS;

		// math.max and math.min handle a ball that is above or below the paddle,
		// so the section always ends up between 0 (top) and SECTIONS - 1 (bottom)
		int section = Math.max(0, Math.min(SECTIONS - 1, relativeY / sectionHeight));
		// subtracting SECTIONS / 2 shifts the middle section to 0, so sections above
		// it give a negative dy (up) and sections below it give a positive dy (down)
		dy = (section - SECTIONS / 2) * DY_PER_SECTION;

		// a moving paddle pushes the ball further in the direction it is moving

		if (paddleState == Paddle.PaddleState.MOVING_UP) {
			dy -= PADDLE_MOTION_BONUS;
		} else if (paddleState == Paddle.PaddleState.MOVING_DOWN) {
			dy += PADDLE_MOTION_BONUS;
		}

		// cap the max speed in both the y directions
		dy = Math.max(-MAX_SPEED, Math.min(MAX_SPEED, dy));
	}

	/**
	 * Moves the ball by the dx and dy value each frame, will not act while paused
	 */
	public void act() {
		if (paused) {
			return;
		}

		double target = 1.0;
		if (slowed) {
			target = SLOW_SCALE;
		}
		// ease the current scale toward the target
		speedScale += (target - speedScale) * EASE;

		// positions are whole numbers, so carry the leftover fraction to the next frame
		double moveX = dx * speedScale * scaleX + xLeftover;
		double moveY = dy * speedScale * scaleY + yLeftover;
		int stepX = (int) moveX;
		int stepY = (int) moveY;
		xLeftover = moveX - stepX;
		yLeftover = moveY - stepY;

		setX(getX() + stepX);
		setY(getY() + stepY);

	}

}