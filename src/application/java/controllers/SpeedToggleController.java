package application.java.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class SpeedToggleController implements Initializable{

	// FXML Injectable fields
	@FXML private Label speedLabel;
	@FXML private Slider speedSlider;
	
	// Other class fields
	private double speedOfSpeech = 1;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		addSliderListener();
		this.speedOfSpeech = 1;
	}

	/**
	 * This method sets the slider action; it uses a listener for
	 * change in the speed of speech to display the speech speed, and
	 * clearly indicate whether the current speed is the default.
	 */
	private void addSliderListener() {
		this.speedSlider.valueProperty().addListener(c ->{
			this.speedOfSpeech = 0 - (this.speedSlider.getValue());
			String roundedSpeed= String.format("%.2f", 1/speedOfSpeech);
			if(roundedSpeed.equals("1.00")) {
				this.speedLabel.setText(roundedSpeed+ "x "+ " (default)");
			} else {
				this.speedLabel.setText(roundedSpeed + "x ");
			}
		});
	}

	/**
	 * This method resets the speed of speech slider
	 * It doesn't need to manually reset speedOfSpeech since we read from the 
	 * change listener.
	 */
	public void resetSpeedToDefault() {
		this.speedSlider.setValue(-1.0);
	}

	public double getSpeed() {
		return this.speedOfSpeech;
	}
}
