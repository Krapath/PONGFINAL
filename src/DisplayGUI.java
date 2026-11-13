import java.awt.*;
import javax.swing.*;

/*
 * Author: Hugo To
 * November 5, 2026
 * Separate GUI that is responsible for drawing text on screen and other GUI elements
 */

public class DisplayGUI extends JPanel {

	private JLabel scoreLabel;
	private JLabel popUpLabel;
	private static final int REF_WIDTH = 1600;
	private static final int REF_HEIGHT = 900;
	private static final int SCORE_FONT_SIZE = 50; // pixels on the reference screen

	/**
	 * Builds the overlay for the labels. The panel covers the whole playing field
	 * but is transparent, acts only as a frame for other labels.
	 *
	 * @param windowWidth  the width of the game in pixels
	 * @param windowHeight the height of the game in pixels
	 */
	public DisplayGUI(int windowWidth, int windowHeight) {
		setLayout(null);
		setOpaque(false); // transparent, so the game shows through
		setVisible(true);
		setBounds(0, 0, windowWidth, windowHeight);

		scoreLabel = new JLabel(
				"<html><font color='red'>PLAYER 1</font> | 0 : 0 | <font color='blue'>PLAYER 2</font></html>",
				JLabel.CENTER);
		scoreLabel.setForeground(Color.WHITE);
		scoreLabel.setFont(new Font("Courier New", Font.BOLD, SCORE_FONT_SIZE * windowHeight / REF_HEIGHT));
		scoreLabel.setBounds(0, 0, windowWidth, windowHeight / 8);
		add(scoreLabel);

		// at center of entire screen
		popUpLabel = new JLabel("", JLabel.CENTER);
		popUpLabel.setBounds(0, 0, windowWidth, windowHeight);
		popUpLabel.setVisible(false);
		add(popUpLabel);

	}

	/**
	 * Updates the scoreboard text to show both player scores.
	 *
	 * @param p1Score the current score of player 1
	 * @param p2Score the current score of player 2
	 */
	public void updateScore(int p1Score, int p2Score) {
		scoreLabel.setText("<html><font color='red'>PLAYER 1</font> | " + p1Score + " : " + p2Score
				+ " | <font color='blue'>PLAYER 2</font></html>");
	}

	/**
	 * Shows a message in the center of the screen
	 *
	 * @param text the message to show
	 * @param c    the color of the text
	 * @param f    the font of the text
	 */
	public void showPopup(String text, Color c, Font f) {
		popUpLabel.setText(text);
		popUpLabel.setForeground(c);
		popUpLabel.setFont(f);
		popUpLabel.setVisible(true);
	}

	/**
	 * Hides the winner message(not used right now because game stops when someone
	 * wins)
	 */
	public void hidePopup() {
		popUpLabel.setVisible(false);
	}

}