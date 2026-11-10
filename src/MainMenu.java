import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/*
 * Author: Hugo To
 * November 5, 2026
 * Separate GUI that is responsible for drawing text on screen and other GUI elements
 */


public class MainMenu extends JPanel implements ActionListener {

	private static final int REF_WIDTH = 1600; 
	private static final int REF_HEIGHT = 900;
	private static final int LOGO_WIDTH = 800;
	private static final int LOGO_HEIGHT = 410;
	private static final int BUTTON_WIDTH = 300;
	private static final int BUTTON_HEIGHT = 100;
	private static final int BUTTON_FONT_SIZE = 48;
	private static final int MARGIN = 50; // space between the logo and buttons
	private JButton startButton = new JButton("START");
	private JButton exitButton = new JButton("EXIT");
	private JButton instructionsButton = new JButton("INSTRUCTIONS");
	Pong game;
	
	/**
	 * Builds the overlay for the labels. The panel covers the whole playing field
	 * but is transparent, acts only as a frame for other labels.
	 *
	 * @param windowWidth the width of the game in pixels
	 * @param windowHeight the height of the game in pixels
	 */
	public MainMenu(int windowWidth, int windowHeight, Pong pong)  {
		game = pong;
				
		setLayout(null);
		setOpaque(false); // transparent, so the game shows through
		setVisible(true);
		setBounds(0, 0, windowWidth, windowHeight);
		
		ImageIcon image = new ImageIcon("MENU/Pong3.png");
		JLabel logo = new JLabel(image);
	

		int scaledLogoWidth = (int) ((double) LOGO_WIDTH * windowWidth / REF_WIDTH);
		int scaledLogoHeight = (int) ((double) LOGO_HEIGHT * windowHeight / REF_HEIGHT);
		int scaledButtonWidth = (int) ((double) BUTTON_WIDTH * windowWidth / REF_WIDTH);
		int scaledButtonHeight = (int) ((double) BUTTON_HEIGHT * windowHeight / REF_HEIGHT);
		int scaledMargin = (int) ((double) MARGIN * windowHeight / REF_HEIGHT);
		int scaledFontSize = (int) ((double) BUTTON_FONT_SIZE * windowHeight / REF_WIDTH);
		Font scaledFont = new Font("Monospaced", Font.BOLD, scaledFontSize);

		startButton.setFont(scaledFont);
		exitButton.setFont(scaledFont);
		instructionsButton.setFont(scaledFont);
		// set location and size of logo and buttons (centered horizontally and offset from the center vertically)
		// remember change magic numbers
		
		logo.setBounds(
				windowWidth / 2 - scaledLogoWidth / 2, 
				windowHeight / 2 - scaledLogoHeight - scaledMargin, 
				scaledLogoWidth, 
				scaledLogoHeight
			);
		
		startButton.setBounds(
			windowWidth / 2 - scaledButtonWidth / 2, 
			windowHeight / 3,
			scaledButtonWidth, 
			scaledButtonHeight
		);
		
		instructionsButton.setBounds(
			windowWidth / 2 - scaledButtonWidth / 2, 
			windowHeight / 3 + scaledButtonHeight + scaledMargin,
			scaledButtonWidth, 
			scaledButtonHeight
		);
		
		exitButton.setBounds(
			windowWidth / 2 - scaledButtonWidth / 2, 
			windowHeight / 3 + (scaledButtonHeight + scaledMargin) * 2,
			scaledButtonWidth, 
			scaledButtonHeight
		);

		add(startButton);
		add(instructionsButton);
		add(exitButton);
		

		
		add(logo);

		
		startButton.addActionListener(this);
		exitButton.addActionListener(this);
		instructionsButton.addActionListener(this);
		
		
	}
	
	
	/*
	 * override method from ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == startButton) {
			setVisible(false);
			startButton.setFocusable(false); // stop the button from holding focus
			instructionsButton.setFocusable(false);
			exitButton.setFocusable(false);
			game.start();
		} else if (e.getSource() == instructionsButton) {
		} else if (e.getSource() == exitButton) {
			System.exit(0);
		}
	}
}