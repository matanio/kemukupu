package application.java.controllers;

import java.io.IOException;
import application.java.models.Topic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class is the controller for individual Topic buttons (dynamically loaded)
 */
public class TopicController {

	// FXML Injectable fields
	@FXML Button btn;
	@FXML ImageView img;

	// Other class fields
	private Button startButton = null;
	private static String IMAGE_DIRECTORY = "./data/images/"; // Path is outside src for future add-topic functionality

	/**
	 * This method sets the selected topic, ready to press p
	 * 
	 * @param event created by Topic Button press
	 * @throws IOException 
	 */
	public void setSelectedTopic(ActionEvent event) throws IOException {

		TopicsScreenController.topicName = this.btn.getText();
		this.startButton.setText(String.format("START >> \nTopic: %s", this.btn.getText()));
		this.startButton.setStyle("-fx-background-color: #91b2eb;");

	}

	/**
	 * This method uses a Topic object to set the required button and image element
	 * for display
	 * 
	 * @param topic to use text and image values of 
	 */
	public void setData(Topic topic) {
		btn.setText(topic.getName());
		Image image  = new Image("file:" + IMAGE_DIRECTORY + topic.getIconSrc());
		img.setImage(image);
	}

	public void setStartButton(Button b) {
		this.startButton = b;
	}
}
