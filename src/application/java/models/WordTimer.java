package application.java.models;

import java.util.Timer;
import java.util.TimerTask;

import application.java.controllers.QuizController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class WordTimer {

	private Label timerLabel = null;
	private ProgressBar scoreBar = null;
	private int time = 0;
	private Timer timer = new Timer();
	private TimerTask timerTask = null;
	private int score = 100;

	public static int finalScore = 100/QuizController.attemptTimes;

	// the time increases after [precision] milliseconds
	private int precision = 200;

	/**
	 * Based on the time, the score label and the progress bar for the score need to be changed
	 * @param timerLabel
	 * @param scoreBar
	 */
	public WordTimer(Label timerLabel, ProgressBar scoreBar) {
		this.timerLabel = timerLabel;
		this.scoreBar = scoreBar;

	}

	/**
	 * Start the timer. Note that the timer only decreases after a certain amount of time based on the length of the word
	 * This is to give the user some time to think and type the answer
	 * @param wordLengthTime: the length of the word
	 */
	public void start(int wordLengthTime) {

		// stop the previous timer, there can only be one timer running at the same time
		this.timer.cancel();

		// create the timer (from java.util) for this word
		this.timer = new Timer();

		// if this is the first attemps, then the full score is 100, otherwise it's 50
		// and for the second chance, the timer is slower than the first chance
		if (QuizController.attemptTimes == 1) {
			score = 100;
		} else if (QuizController.attemptTimes == 2) {
			score = 50;
			precision = 500;
		}

		// assign the task for the timer
		this.timerTask = new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						time = time + precision;

						// only decrease the score after some time
						if (time > wordLengthTime) {

							// for the first chance, the min score is 50, and for the second chance, the min score is 25
							if (score <= 50/QuizController.attemptTimes + 1) {
								score = 50/QuizController.attemptTimes + 1;
							}

							// update related values
							score--;
							timerLabel.setText("  "+ score);
							scoreBar.setProgress((double)score / 100);
							finalScore = score;

						}
					}
				});
			}
		};

		this.timer.scheduleAtFixedRate(timerTask, 0, (int)(this.precision));

	}

	/**
	 * This method stops the current running timer
	 */
	public void stop() {
		timer.cancel();

		// give timer and timer task a value to avoid unexpected exceptions
		this.timer = new Timer();
		this.timerTask = new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						timerLabel.setText(String.format("Time: %.2f", time = time + precision));
					}
				});
			}
		};
		this.time = 0;
	}

}
