package application.java.controllers;

import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import application.java.models.AnimationManager;
import application.java.models.FileIO;
import application.java.models.MacronKeypad;
import application.java.models.SceneManager;
import application.java.models.SpeedToggle;
import application.java.models.WordPlayer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PracticeController implements Initializable {

	// FXML Injectable fields
	@FXML private Label hintLabel;
	@FXML private TextField textField;
	@FXML private Button submitButton;
	@FXML private Rectangle feedbackRect;
	@FXML private Label resultLabel;
	@FXML private Button hearAgainButton;
	@FXML private Button idkButton;
	@FXML private AnchorPane screenPane;

	// Other class fields
	private static String INCORRECT_MESSAGE = "Incorrect, Try Again";
	private static List<String> words = new ArrayList<String>();
	public static String currentWord = "";
	public static String userAnswer = "";
	public static boolean isCorrect;
	private int attempts = 1;
	private SceneManager sceneManager = new SceneManager();
	private Button[] disableButtons = null;
	private SpeedToggle speedToggle;
	private MacronKeypad macronKeypad;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// intialise our speed toggle and macron keypad and add them to the screen
		speedToggle = new SpeedToggle(screenPane, 25, 20);
		macronKeypad = new MacronKeypad(textField,screenPane, 580, 308);

		// automatically set focus to the text field.
		// Attribution: https://stackoverflow.com/questions/12744542/requestfocus-in-textfield-doesnt-work/38900429
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				textField.requestFocus();
			}
		});
		// Attribution End

		// Declares buttons to be disabled during word read-out
		this.disableButtons = new Button[] {
				submitButton,
				hearAgainButton,
				idkButton
		};

		// Obtain all the words from all the word list
		if (words.isEmpty()) {
			words = FileIO.getAllWordsFromWordsDirectory();
		}

		// Choose a random word and speak it
		setNextWord();
		readCurrentWord();
		updateLetterCount();
	}

	/**
	 * This is the action for the submit button
	 */
	public void submit(ActionEvent e) {

		PracticeController.userAnswer = this.textField.getText();

		// if the user gets the word correct, note that this could be either the first or the second attempt
		if (isAnswerCorrect()) {
			this.attempts = 1;
			FileIO.openGeneralWavFile("correct");
			PracticeController.isCorrect = true;
			sceneManager.switchScene(e, "PracticeComplete");

		// if the user gets the word wrong for the first time
		} else if (this.attempts == 1) {
			FileIO.openGeneralWavFile("wrong");
			this.attempts++;
			feedbackRect.setFill(Color.web("#f87676"));
			resultLabel.setText(INCORRECT_MESSAGE);
			this.readCurrentWord();
			this.giveHint();
			// automatically set focus to the text field.
			textField.requestFocus();

		// if the user gets the word wrong for the second time
		} else {
			FileIO.openGeneralWavFile("wrong");
			PracticeController.isCorrect = false;
			sceneManager.switchScene(e, "PracticeComplete");
		}
	}

	/**
	 * Pressing enter on the keyboard is the same as submit action
	 */
	public void keyPressed(KeyEvent e) throws IOException, InterruptedException {
		if (WordPlayer.reading) return;
		if (e.getCode() == KeyCode.ENTER) {
			ActionEvent event = new ActionEvent(this.submitButton, this.submitButton);
			this.submit(event);
		}
	}

	/**
	 * This is the action for the hear again button
	 */
	public void hearAgain() {
		readCurrentWord();
	}

	/**
	 * This method specifies the actions when a word is skipped
	 */
	public void dontKnow(ActionEvent e) {
		resultLabel.setText("Skipped");
		AnimationManager animationManager = new AnimationManager();
		animationManager.playDisplayToFadeAnimation(resultLabel);
		feedbackRect.setFill(Color.web("#d0d0d0"));		
		// get the next word, read it and show the letter counts
		this.setNextWord();
		this.readCurrentWord();
		this.updateLetterCount();
		this.attempts = 1;
	}

	/**
	 * This method leads the user back to the home screen
	 */
	public void returnHome(ActionEvent e) {
		sceneManager.switchScene(e, "Main");
	}

	/**
	 * Read the current word in a new thread
	 */
	private void readCurrentWord() {
		new Thread(new WordPlayer(PracticeController.currentWord, speedToggle.getSpeed(), true, this.disableButtons)).start();
	}

	/**
	 * Based on the current word, generate the underscores as letter count
	 */
	private void updateLetterCount() {
		StringBuilder sb = new StringBuilder();

		// the max number of characters that can be displayed on one line
		// note that this is not the max number of underscores
		int maxCharPerLine = 50; 

		char[] letters = PracticeController.currentWord.toCharArray();
		for (char c: letters) {
			if (c == ' ') {
				sb.append("  ");
			} else {
				sb.append("_ ");
			}
		}

		char[] hint = sb.toString().toCharArray();

		// handle the long word
		if (hint.length > maxCharPerLine) {
			for (int i = maxCharPerLine; i > 0; i--) {
				if (hint[i] == ' ' && hint[i-1] == ' ' && hint[i+1] == ' ') {
					hint[i] = '\n';
					break;
				}
			}
		}
		this.hintLabel.setText(new String(hint));
	}

	/**
	 * This is method checks if the answer is correct
	 */
	private boolean isAnswerCorrect() {
		boolean b = this.textField.getText().trim().equalsIgnoreCase(currentWord);
		this.textField.clear();
		return b;
	}

	/**
	 * This method gets the next word when a user skips and and starts practice 
	 * for the first time
	 */
	private void setNextWord() {
		int wordIndex = new Random().nextInt(words.size());
		currentWord = words.get(wordIndex);
	}

	/**
	 * This method provides hint to user if they get the word wrong in the first time
	 */
	private void giveHint() {

		// the ratio of the letters that will be displayed
		double displayRatio = 0.5;

		// blank space will not be displayed as hint
		String temp = PracticeController.currentWord.replace(" ", "");
		int letterCount = (int)(temp.length() * displayRatio);

		// generate random letters that will be displayed
		// note that, there is no repeated elements in Set
		Set<Integer> indexes = new HashSet<Integer>();
		Random random = new Random();
		char[] letters = PracticeController.currentWord.toCharArray();
		while (indexes.size() < letterCount) {
			int i = random.nextInt(PracticeController.currentWord.length());
			if (letters[i] != ' ') {
				indexes.add(i);
			}
		}

		// re-build the underscores
		StringBuilder sb = new StringBuilder();
		int maxCharPerLine = 50;  // IMPORTANT: if you want to change this value, make sure also change the one in updateLetterCount

		for (int i = 0; i < letters.length; i++) {
			if (indexes.contains(i)) {
				sb.append(letters[i] + " ");
			} else if (letters[i] != ' ') {
				sb.append("_ ");
			} else {
				sb.append("  ");
			}
		}

		char[] hint = sb.toString().toCharArray();

		// handle the long word
		if (hint.length > maxCharPerLine) {
			for (int i = maxCharPerLine; i > 0; i--) {
				if (hint[i] == ' ' && hint[i-1] == ' ' && hint[i+1] == ' ') {
					hint[i] = '\n';
					break;
				}
			}
		}

		// display the newly generated underscores to the user
		this.hintLabel.setText(new String(hint));
	}

	/**
	 * setter method to set the words.
	 * @param wordsList
	 */
	public static void wordsSetter(List<String> wordsList) {
		words = wordsList;
	}
}
