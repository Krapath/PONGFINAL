import java.awt.*;
import javax.swing.*;

/*
 * Author: Hugo To
 * November 5, 2026
 * Separate menu element that is responsible for main menu and game creation/starting
 */

public class MainMenu extends JPanel {

	// symbolic constants, no magic numbers
	private static final int REF_WIDTH = 1600;
	private static final int REF_HEIGHT = 900;
	private static final int LOGO_WIDTH = 550;
	private static final int LOGO_HEIGHT = 410;
	private static final int BUTTON_WIDTH = 300;
	private static final int BUTTON_HEIGHT = 100;
	private static final int BUTTON_FONT_SIZE = 40;
	private static final float SOUND_VOLUME = 5f;
	private static final int MARGIN = 50; // space between the logo and buttons

	// JButton fields
	private JButton playButton = new JButton("START");
	private JButton exitButton = new JButton("EXIT");
	private JButton instructionsButton = new JButton("INSTRUCTIONS");
	Pong game;

	/**
	 * Builds the overlay for the labels. The panel covers the whole playing field
	 * but is transparent, acts only as a frame for other labels.
	 *
	 * @param windowWidth  the width of the game in pixels
	 * @param windowHeight the height of the game in pixels
	 */
	public MainMenu(int windowWidth, int windowHeight, Pong pong) {
		game = pong;

		// null layout to allow more freely customizable layout(not perfect, but still
		// nicer in my opinion
		setLayout(null);
		setOpaque(false); // transparent, so the game shows through
		setVisible(true);
		setBounds(0, 0, windowWidth, windowHeight);

		// scale ALL of the values using the scaling method consistent with other
		// methods

		int scaledLogoWidth = (int) ((double) LOGO_WIDTH * windowWidth / REF_WIDTH);
		int scaledLogoHeight = (int) ((double) LOGO_HEIGHT * windowHeight / REF_HEIGHT);
		int scaledButtonWidth = (int) ((double) BUTTON_WIDTH * windowWidth / REF_WIDTH);
		int scaledButtonHeight = (int) ((double) BUTTON_HEIGHT * windowHeight / REF_HEIGHT);
		int scaledMargin = (int) ((double) MARGIN * windowHeight / REF_HEIGHT);
		int scaledFontSize = (int) ((double) BUTTON_FONT_SIZE * windowHeight / REF_WIDTH);
		Font scaledFont = new Font("Monospaced", Font.BOLD, scaledFontSize);

		// apply scaled fonts
		playButton.setFont(scaledFont);
		exitButton.setFont(scaledFont);
		instructionsButton.setFont(scaledFont);

		// scaled image
		ImageIcon image = new ImageIcon(new ImageIcon("MENU/Pong3.png").getImage().getScaledInstance(scaledLogoWidth,
				scaledLogoHeight, Image.SCALE_SMOOTH));
		JLabel logo = new JLabel(image);

		// make sure the Jbuttons do not steal focus from main game
		playButton.setFocusable(false);
		instructionsButton.setFocusable(false);
		exitButton.setFocusable(false);

		// applying scaled formatting and margins
		logo.setBounds(windowWidth / 2 - scaledLogoWidth / 2, windowHeight / 2 - scaledLogoHeight - scaledMargin,
				scaledLogoWidth, scaledLogoHeight);

		playButton.setBounds(windowWidth / 2 - scaledButtonWidth / 2, windowHeight / 3, scaledButtonWidth,
				scaledButtonHeight);

		instructionsButton.setBounds(windowWidth / 2 - scaledButtonWidth / 2,
				windowHeight / 3 + scaledButtonHeight + scaledMargin, scaledButtonWidth, scaledButtonHeight);

		exitButton.setBounds(windowWidth / 2 - scaledButtonWidth / 2,
				windowHeight / 3 + (scaledButtonHeight + scaledMargin) * 2, scaledButtonWidth, scaledButtonHeight);

		add(playButton);
		add(instructionsButton);
		add(exitButton);
		add(logo);

		// Utilizes a lambda expression because action listener is a functional
		// interface!
		// A functional interface means it only has one abstract method so java knows
		// what method to refer to, allowing you use a lambda expression which omits
		// much of the unnecessary clutter that anonymous classes use.
		playButton.addActionListener((e) -> {
			startGame();
			SoundEffects.play("SFX/CLICK.wav", SOUND_VOLUME); 
		});

		instructionsButton.addActionListener((e) -> {
			SoundEffects.play("SFX/CLICK.wav", SOUND_VOLUME); 
			showInstructions();
		});

		exitButton.addActionListener((e) -> {
			SoundEffects.play("SFX/CLICK.wav", SOUND_VOLUME); 
			System.exit(0);
		});

	}

	/**
	 * Makes menu visible and is drawn on screen specifically, used for reseting
	 * game
	 */
	public void showMenu() {
		setVisible(true);
		repaint();
	}

	/**
	 * Opens a JOptionpane with instructions png made in Photoshop JOptionpane
	 * customized to be purely black background
	 */
	private void showInstructions() {
		ImageIcon image = new ImageIcon("MENU/TutorialHelp.png");
		JLabel howTo = new JLabel(image);
		UIManager.put("OptionPane.background", Color.BLACK);
		UIManager.put("Panel.background", Color.BLACK);
		JOptionPane.showMessageDialog(null, howTo, "Instructions", JOptionPane.PLAIN_MESSAGE);
		
	}

	/**
	 * Starts the game via Pong's start method. This is the ONLY class with a
	 * reference to Pong, as it will ultimately needs to be able to control it.
	 */
	public void startGame() {
		setVisible(false);
		game.start();
	}

}