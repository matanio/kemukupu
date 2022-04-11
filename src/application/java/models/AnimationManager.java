package application.java.models;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

/**
 * This class gives the ability to create and manage animations.
 */
public class AnimationManager {

	/**
	 * This method plays a 'painting-effect' like animation, loosely based on:
	 * https://stackoverflow.com/questions/36727777/how-to-animate-dashed-line-javafx
	 * 
	 * @param strokePath - the SVGPath you want to to 'paint'
	 * @param strokeArraySize - the size of the stroke dashes; the bigger, the more like a single stroke it will appear.
	 * @param durationSeconds - the duration of the effect
	 * @param strokeWidth - the stroke width of your painting stroke. 
	 */
	public void playStrokeAnimation(SVGPath strokePath, double strokeArraySize, int durationSeconds, int strokeWidth){
		strokePath.getStrokeDashArray().add(strokeArraySize);
		strokePath.setStrokeDashOffset(strokeArraySize);

		strokePath.setStrokeWidth(strokeWidth);

		// Create a SVG based animation and play
		Timeline timeline = new Timeline(
				new KeyFrame(Duration.seconds(durationSeconds),new KeyValue(strokePath.strokeDashOffsetProperty(), 0, Interpolator.LINEAR)),
				new KeyFrame(Duration.ZERO,new KeyValue(strokePath.strokeDashOffsetProperty(),strokeArraySize,Interpolator.LINEAR))
				);
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.setAutoReverse(true);
		timeline.play();
	}


	/**
	 * This method plays a fade from one colour to another for the specified shape.
	 * @param durationInMillis - the duration of the effect
	 * @param shape - the shape you want to change colour
	 * @param startColourHex - hexadecimal of the shape's start colour
	 * @param endColorHex - hexadecimal of the shape's end colour
	 */
	public void playColourFadeAnimation(double durationInMillis, Shape shape, String startColourHex, String endColorHex) {
		FillTransition fillTransition = new FillTransition(Duration.millis(durationInMillis),shape,Color.web(startColourHex), Color.web(endColorHex));
		fillTransition.play();
	}

	/**
	 * this method plays the star animation with specific number of 
	 * stars passed in as parameter
	 * @param numOfStars
	 * @param stars - parent AnchorPane of star shapes
	 * @param starEndColourHex - hexadecimal form of the colour we want the stars to turn into.
	 */
	public void playStarsAnimation(int numOfStars, AnchorPane stars, String starEndColourHex) {
		ObservableList<Node> starsObjects = stars.getChildren();

		SequentialTransition sequentialTransition = new SequentialTransition();

		// Error handling - max num of stars is 3
		if(numOfStars > 3) {
			numOfStars = 3;
		}

		// Create a simple scale for all three stars if our score isn't big enough
		if(numOfStars == 0) {
			ScaleTransition animation = createScaleAnimation(stars, 400, 0.1);
			sequentialTransition.getChildren().add(animation);
		}

		// Create a scale animation for each star
		for (int i = 0; i < numOfStars; i++) {
			SVGPath shape = (SVGPath)starsObjects.get(i);
			ScaleTransition animation = createScaleAnimation(shape, 400, 0.7);
			FillTransition colorChange = new FillTransition(Duration.millis(200), shape, Color.web("#dddddd"),  Color.web(starEndColourHex));
			sequentialTransition.getChildren().addAll(colorChange, animation);
		}

		// Pause so that it doesn't play right away on initialise
		int transitionDelay = 1500;
		sequentialTransition.getChildren().add(0, new PauseTransition(Duration.millis(transitionDelay)));


		sequentialTransition.play();
	}

	/*
	 * Creates a scale transition which zooms in and out with specified values. 
	 */
	private ScaleTransition createScaleAnimation(Node shape, int durationInMillis, double scale) {
		ScaleTransition animation = new ScaleTransition(Duration.millis(durationInMillis), shape);
		animation.setByX(scale);
		animation.setByY(scale);	
		animation.setCycleCount(2);
		animation.setAutoReverse(true);
		return animation;
	}

	/**
	 * this method plays the Score Increase animation after each round of game.
	 * @param roundScoreString
	 * @param newScore
	 */
	public void playScoreIncreaseAnimation(String roundScoreString,String newScore, Label additionLabel, Label totalScoreLabel) {
		additionLabel.setText("+ " + roundScoreString);

		// Animation that shows what we're incrementing by
		FadeTransition fadeAddition = new FadeTransition(Duration.millis(1000), additionLabel);
		fadeAddition.setFromValue(1.0);
		fadeAddition.setToValue(0);
		fadeAddition.setOnFinished(event -> {
			additionLabel.setText("");
			additionLabel.setOpacity(1);
		});

		// Animation that gives 'scaling' effect
		ScaleTransition scoreLabelScale= new ScaleTransition(Duration.millis(500), totalScoreLabel);

		scoreLabelScale.setByX(0.5);
		scoreLabelScale.setByY(0.5);	
		scoreLabelScale.setCycleCount(2);
		scoreLabelScale.setAutoReverse(true);

		// Animation gives an incrementing effect
		ParallelTransition incrementEffect = new ParallelTransition();
		incrementEffect.getChildren().addAll(fadeAddition,scoreLabelScale);

		// Play the whole score increment
		SequentialTransition scoreIncreaseAnimation = new SequentialTransition();
		PauseTransition pause = new PauseTransition(Duration.millis(1000));
		pause.setOnFinished(event -> {
			totalScoreLabel.setText(newScore);
		});
		scoreIncreaseAnimation.getChildren().addAll(pause, incrementEffect);
		scoreIncreaseAnimation.play();
	}
	/**
	 * This method displays then fades the specified label when called.
	 */
	public void playDisplayToFadeAnimation(Label label) {
		FadeTransition fadeTransition = new FadeTransition(Duration.millis(3000), label);
		fadeTransition.setFromValue(1.0);
		fadeTransition.setToValue(0);	
		fadeTransition.setOnFinished(event -> {
			label.setText("");
			label.setOpacity(1.0);
		});
		fadeTransition.play();
	}
}
