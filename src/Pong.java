
/*
 * This code is protected under the Gnu General Public License (Copyleft), 2005 by
 * IBM and the Computer Science Teachers of America organization. It may be freely
 * modified and redistributed under educational fair use.
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * The main driver class, everything is contained within this class, it extends
 * the game class. Contains a ball, two paddles, and GUI element. Handles
 * general logic like bounds of ball and paddle, winning condition etc.
 */

// TODO: MAKE JAVADOC NOT HAVE A COMMA AFTER PARAMS AND RETURN
public class Pong extends Game {

	// symbolic constants

	private static final int REF_WIDTH = 1600; // the screen size the pixel
												// values are written for
	private static final int REF_HEIGHT = 900;
	private static final int DELAY_TIME = 25;
	private static final int PADDLE_WIDTH = 30;
	private static final int PADDLE_HEIGHT = 200;
	private static final int GOAL_WIDTH = 10;
	private static final int PADDLE_MARGIN = 15;
	private static final int BALL_SIZE = 20;
	private static final int WINNING_SCORE = 5; // change for testing right now
	private static final int SCORE_DELAY = 60; // roughly 1.5 second delay
	private static final int INDEFINITE = -1;
	private static final int SPAWN_ODDS = 10; // 1 in 10 each frame, so about
												// 0.2 seconds to spawn
	private static final int PICKUP_SIZE = 75;
	private static final int WINNER_FONT_SIZE = 50;
	private static final int MESSAGE_HIDE_FRAMES = 8;
	private static final float SCORE_VOLUME = -15f;
	private static final String MENU_TEXT = "Press P to return to menu";
	// to prevent p from constantly pausing and unpausing
	private boolean pHeld = false;
	// state for when game is over
	private boolean gameOver = false;

	private Font popUpFont;

	private Random rand = new Random();

	private int pauseTimer = 0;

	public enum GameState {
		PLAYING, PAUSED,
	}

	private GameState state = GameState.PLAYING;

	private Paddle p1;
	private Paddle p2;
	private Barrier barrier1;
	private Barrier barrier2;

	private Ball b;
	private int p1Score = 0;
	private int p2Score = 0;
	private DisplayGUI display;
	private TimeStop theWorld;
	private BigPaddle bigPaddle;
	private Shield shield;
	private Flashbang flashbang;
	private SmallPaddle smallPaddle;

	private Glitch glitch;

	private MainMenu menu;
	private ArrayList<Pickup> pickUps = new ArrayList<Pickup>();
	private PowerUp[] powers;

	/**
	 * Set up the game before play begins, creating two paddles, a ball, setting
	 * locations and display overlay, adding them to the game as well as setting
	 * delay
	 */
	public void setup() {
		SoundEffects.loadAudioSystem();
		menu = new MainMenu(getFieldWidth(), getFieldHeight(), this);
		add(menu);
		createGame();
		stop();

	}

	/**
	 * Runs each frame of the game based on DELAY_TIME, only runs when the game
	 * is not paused. Handles movement of paddles, scoring, paddle bounds, and
	 * etc.
	 */
	public void act() {
		pauseKey();
		if (state == GameState.PAUSED) {
			if (pauseTimer != INDEFINITE) {
				pauseTimer--;
				if (pauseTimer == MESSAGE_HIDE_FRAMES) {
					display.hidePopup();
				}
				if (pauseTimer <= 0) {
					resume();
					b.setVisible(true);
				}
			}
			return;
		}

		if (rand.nextInt(SPAWN_ODDS) == 0) {
			PowerUp power = powers[rand.nextInt(powers.length)];

			// only one orb of each power at a time
			boolean onField = false;
			for (Pickup p : pickUps) {
				if (p.getPower() == power) {
					onField = true;
				}
			}

			if (!onField) {
				Pickup p = new Pickup(PICKUP_SIZE, power, getFieldWidth(), getFieldHeight());
				add(p);
				pickUps.add(p);
			}
		}

		runAllEffects();

		// a paddle only counts as moving if its key is held during this frame
		p1.idle();
		p2.idle();
		// key presses
		if (ZKeyPressed()) {
			if (glitch.isGlitched(1)) {
				p1.moveDown();

			} else {
				p1.moveUp();
			}
		}
		if (XKeyPressed()) {
			if (glitch.isGlitched(1)) {
				p1.moveUp();

			} else {
				p1.moveDown();
			}
		}

		if (NKeyPressed()) {
			if (glitch.isGlitched(2)) {
				p2.moveDown();

			} else {
				p2.moveUp();
			}

		}
		if (MKeyPressed()) {
			if (glitch.isGlitched(2)) {
				p2.moveUp();

			} else {
				p2.moveDown();
			}
		}

		keepInBounds(p1);
		keepInBounds(p2);

		// collisions

		if (b.getY() <= 0) {
			b.bounceDown();
		} else if (b.getY() >= getFieldHeight() - b.getHeight()) {
			b.bounceUp();
		}

		if (b.collides(p1)) {
			b.bounceRight(p1.getY(), p1.getHeight(), p1.getState());

		}
		if (b.collides(p2)) {
			b.bounceLeft(p2.getY(), p2.getHeight(), p2.getState());

		}

		// scoring and when shielded
		if (b.getX() > getFieldWidth()) {
			if (!barrier2.getShielded()) {
				score(1); // 1 for player 1
			} else {
				b.deflectLeft();
			}
		} else if (b.getX() + b.getWidth() < 0) {
			if (!barrier1.getShielded()) {
				score(2); // 2 for player 2
			} else {
				b.deflectRight();
			}
		}

		// check for when a pickup is found and then use and remove
		for (int i = pickUps.size() - 1; i >= 0; i--) {
			Pickup p = pickUps.get(i);
			if (b.collides(p) && b.getOwner() != 0) {
				if (p.getPower().canUse(b.getOwner())) {
					p.use(b.getOwner());
					remove(p);
					pickUps.remove(i);
					repaint();
				}
			}
		}
	}

	/**
	 * Stops paddle from leaving the screen by moving it back to the nearest
	 * edge if it goes past the top or bottom.
	 *
	 * @param p
	 *            the paddle to keep on the screen
	 */
	private void keepInBounds(Paddle p) {
		if (p.getY() < 0) {
			p.setY(0);
		}
		if (p.getY() + p.getHeight() > getFieldHeight()) {
			p.setY(getFieldHeight() - p.getHeight());
		}
	}

	/**
	 * Gives a point to a player and gets the game ready for the next serve.
	 * 
	 * The ball is set to serve toward the player who just lost the point, the
	 * score board is updated, and the ball and paddles are moved back to the
	 * center. If the player has now reaches winning score, the winner message
	 * is shown, and the game is stopped. Otherwise the game pauses before the
	 * game continues.
	 *
	 * @param player
	 *            the player who scored, represented as an integer
	 */
	private void score(int player) {
		if (player == 1) {
			p1Score++;
			b.setVisible(false);
			b.serveRight();
			if (p1Score >= WINNING_SCORE - 1 && p1Score == p2Score) {
				display.showPopup("Tiebreaker, win by 2!", Color.WHITE, popUpFont);
			} else {
				display.showPopup("Player 1 Scores!", Color.RED, popUpFont);
			}
			if (p1Score != WINNING_SCORE) {
				SoundEffects.play("SFX/ScorePoint.wav", SCORE_VOLUME);
			}
			pause(SCORE_DELAY);

		} else if (player == 2) {
			p2Score++;
			b.setVisible(false);
			b.serveLeft();
			if (p2Score >= WINNING_SCORE - 1 && p2Score == p1Score) {
				display.showPopup("Tiebreaker, win by 2!", Color.WHITE, popUpFont);
			} else {
				display.showPopup("Player 2 Scores!", Color.BLUE, popUpFont);
			}
			if (p2Score != WINNING_SCORE) {
				SoundEffects.play("SFX/ScorePoint.wav", SCORE_VOLUME);
			}

			pause(SCORE_DELAY);

		}
		display.updateScore(p1Score, p2Score);
		resetPositions();
		resetAllEffects();
		/// winning conditions
		if (p1Score >= WINNING_SCORE && p1Score > p2Score + 1) {

			// https://stackoverflow.com/questions/6635730/how-do-i-put-html-in-a-jlabel-in-java
			display.showPopup("<html><center>Player 1 wins!<br>" + MENU_TEXT + "</center></html>", Color.RED,
					popUpFont);
			b.setVisible(false);
			pause(INDEFINITE);

			SoundEffects.play("SFX/Winner!.wav", SCORE_VOLUME);
			gameOver = true;

		} else if (p2Score >= WINNING_SCORE && p2Score > p1Score + 1) {
			display.showPopup("<html><center>Player 2 wins!<br>" + MENU_TEXT + "</center></html>", Color.BLUE,
					popUpFont);
			b.setVisible(false);
			pause(INDEFINITE);
			SoundEffects.play("SFX/Winner!.wav", SCORE_VOLUME);

			gameOver = true;

		}

	}

	/**
	 * Ends a pause by returning the game state to playing, as well as unpausing
	 * the ball and reseting timer to 0
	 */
	private void resume() {
		state = GameState.PLAYING;
		b.paused = false;
		p1.paused = false;
		p2.paused = false;

		pauseTimer = 0;
	}

	/**
	 * Pauses the game and all of its elements for a set amount of frames
	 *
	 * @param frames
	 *            the amount of frames pause lasts, or the constant INDEFINITE
	 *            which will not end by itself
	 */
	private void pause(int frames) {
		state = GameState.PAUSED;
		b.paused = true;
		p1.paused = true;
		p2.paused = true;
		pauseTimer = frames;
	}

	/**
	 * Pauses the game indefinetely and hides all of the game elements
	 */
	private void stop() {
		pause(INDEFINITE);
		p1.setVisible(false);
		p2.setVisible(false);
		b.setVisible(false);

		display.setVisible(false);
	}

	/**
	 * Starts the game, unpausing the game and setting game elements as visible
	 */
	public void start() {
		resume();
		setFocusable(true);
		// IN ORDER
		// https://stackoverflow.com/questions/31971277/button-remains-focused-after-clicking
		requestFocusInWindow();
		p1.setVisible(true);
		p2.setVisible(true);
		b.setVisible(true);
		display.setVisible(true);

	}

	/**
	 * Instantiates all the objects part of the game and positions them
	 * appropiately
	 */
	private void createGame() {
		int width = getFieldWidth();
		int height = getFieldHeight();
		int margin = PADDLE_MARGIN * width / REF_WIDTH;

		barrier1 = new Barrier(GOAL_WIDTH, width, height);
		barrier1.setX(0);
		barrier2 = new Barrier(GOAL_WIDTH, width, height);
		barrier2.setX(width - GOAL_WIDTH);

		p1 = new Paddle(PADDLE_WIDTH, PADDLE_HEIGHT, width, height);
		p1.setX(margin);
		p2 = new Paddle(PADDLE_WIDTH, PADDLE_HEIGHT, width, height);
		p2.setX(width - margin - p2.getWidth());

		b = new Ball(BALL_SIZE, BALL_SIZE, width, height);

		popUpFont = new Font("Courier New", Font.BOLD, WINNER_FONT_SIZE * height / REF_HEIGHT);

		display = new DisplayGUI(width, height);
		add(display);

		theWorld = new TimeStop(width, height, PICKUP_SIZE);
		// only necessary to add timestop label because it has an animation
		add(theWorld.getAnimationLabel());

		bigPaddle = new BigPaddle();
		smallPaddle = new SmallPaddle();
		shield = new Shield();

		flashbang = new Flashbang(width, height);
		glitch = new Glitch();
		// necessary to add because it has animation
		add(flashbang.getAnimationLabel());
		getContentPane().setComponentZOrder(flashbang.getAnimationLabel(), 0);

		powers = new PowerUp[] { glitch, theWorld, flashbang, bigPaddle, shield, smallPaddle };

		resetPositions();

		add(b);
		add(p1);
		add(p2);
		add(barrier1);
		add(barrier2);

		setDelay(DELAY_TIME);
	}

	/**
	 * Moves the ball to the center of the screen and both players to the
	 * vertical center of their sides. The paddles keep their x positions.
	 */
	private void resetPositions() {
		b.setX(getFieldWidth() / 2 - b.getWidth() / 2);
		b.setY(getFieldHeight() / 2 - b.getHeight() / 2);

		p1.setY(getFieldHeight() / 2 - p1.getHeight() / 2);
		p2.setY(getFieldHeight() / 2 - p2.getHeight() / 2);
	}

	/**
	 * Runs all powerups and related effects if possible
	 */
	private void runAllEffects() {
		b.setSlowed(theWorld.isTimeSlowed());

		p1.setFrozen(theWorld.isFrozen(1));
		p2.setFrozen(theWorld.isFrozen(2));

		p1.setEnlarged(bigPaddle.isEnlarged(1));
		p2.setEnlarged(bigPaddle.isEnlarged(2));
		p1.setShrunk(smallPaddle.isShrunk(1));
		p2.setShrunk(smallPaddle.isShrunk(2));
		barrier1.setShielded(shield.isShielded(1));
		barrier2.setShielded(shield.isShielded(2));
	}

	/**
	 * Clears all powerups and resets all objects so they dont linger during
	 * pause
	 */
	private void resetAllEffects() {

		for (int i = pickUps.size() - 1; i >= 0; i--) {
			Pickup p = pickUps.get(i);
			remove(p);
			pickUps.remove(i);
		}
		repaint();

		for (PowerUp p : powers) {
			p.endPower();
		}

		p1.setEnlarged(false);
		p2.setEnlarged(false);
		p1.setFrozen(false);
		p2.setFrozen(false);
		p1.setShrunk(false);
		p2.setShrunk(false);
		barrier1.setShielded(false);
		barrier2.setShielded(false);
	}

	/**
	 * Resets everything in the game to original state and makes the main menu
	 * visible again.
	 */
	private void returnToZero() {
		p1Score = 0;
		p2Score = 0;
		gameOver = false;
		display.updateScore(p1Score, p2Score);
		display.hidePopup();
		b.serveRight();
		resetPositions();
		resetAllEffects();
		stop();
		menu.showMenu();
	}

	/**
	 * P will pause and resumes while playing, returns to the menu after a win,
	 * and does nothing on menu screen.
	 */
	private void pauseKey() {
		if (menu.isVisible()) {
			return;
		}
		if (!PKeyPressed()) {
			pHeld = false;
			return;
		}
		if (pHeld) {
			return;
		}

		pHeld = true;

		// checks for all scenarios and does appropriate action
		if (gameOver) {
			returnToZero();
		} else if (state == GameState.PAUSED && pauseTimer == INDEFINITE) {
			display.hidePopup();
			resume();
		} else if (state == GameState.PLAYING) {
			display.showPopup("PAUSED", Color.WHITE, popUpFont);
			pause(INDEFINITE);
		}
	}

	/**
	 * This code has been provided for you, and should not be modified
	 */
	public static void main(String[] args) {
		Pong game = new Pong();
		game.setVisible(true);
		game.initComponents();
		// just in case to make sure the game resizes properly and is redrawn
		game.getContentPane().revalidate();
		game.getContentPane().repaint();
	}
}