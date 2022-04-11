package application.java.models;

import javafx.scene.control.Button;

/*
 * This is a class to read the word out to the user in another thread
 */
public class WordPlayer implements Runnable {

	private String word;
	private double speed;

	// the lock that ensures only one thread is being executed at a time (no two word speaking at the 
	// same time)
	// to ensure that there is only one instance of lock, I made this statis
	private static Object lock = new Object();

	// if there is a word being read at the moment and another thread wants to start, it will start
	// after the current thread finishes only if the next thread is marked as important
	public static boolean reading = false;
	private boolean isImportant;


	private static double readingTimeSeconds = -1;

	private Button[] disableButtons = null;

	private WordTimer timer = null;

	/*
	 * There are two constructors
	 * The first one is for when there should not be a new timer
	 * such as when submitting word, click dont know
	 * The second is for then there should be a new timer
	 * such as when hearing the word again
	 */
	public WordPlayer(String word, double speed, boolean isImportant, Button[] disableButtons) {
		this.word = word;
		this.speed = speed;
		this.isImportant = isImportant;
		this.disableButtons = disableButtons;
	}

	public WordPlayer(String word, double speed, boolean isImportant, Button[] disableButtons, WordTimer timer) {
		this.word = word;
		this.speed = speed;
		this.isImportant = isImportant;
		this.disableButtons = disableButtons;
		this.timer = timer;
	}

	@Override
	public void run() {


		// if another thread is executing currently and this thread is not important, then ignore this 
		if (WordPlayer.reading && !this.isImportant) return;
		// if (MyRunnable.reading) return;

		// set the reading to true because this thread starts to read the word
		WordPlayer.reading = true;

		this.toggleButtons(true);

		// this block ensures only one word being read at a time
		synchronized (lock) {
			FileIO.speakMaori(this.word, this.speed);
		}

		this.toggleButtons(false);

		if (this.timer != null) {
			this.startTimer();
		}

		// after reading, set it to false
		WordPlayer.reading = false;

	}

	/**
	 * @return the time taken for reading the previous word
	 */
	public static double getReadingTime() {
		return WordPlayer.readingTimeSeconds;
	}

	/**
	 * When reading a word, some buttons need to be disabled, and after reading, those buttons need to be enbled
	 * @param disable: true when the buttons need to be disabled, false when the buttons need to be enabled
	 */
	private void toggleButtons(boolean disable) {
		for (int i = 0; i < this.disableButtons.length; i++) {
			this.disableButtons[i].setDisable(disable);
		}
	}

	/*
	 * After reading the word, start the timer so that the score will be updated as time goes
	 */
	private void startTimer() {
		this.timer.start(calculateTypingTimeInMs());
	}

	/*
	 * This method calculates how long the user should be given for thinking and typing
	 */
	private int calculateTypingTimeInMs() {
		return 400 * this.word.length() + 800;
	}

}





