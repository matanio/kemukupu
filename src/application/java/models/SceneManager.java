package application.java.models;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class is used to switch scenes when required.
 */
public class SceneManager {
	
	private Stage stage;
	private Scene scene;
	
	/*
	 * This method is to switch scenes 
	 * The first parameter e is the ActionEvent that is received when user clicked a button
	 * The second parameter sceneName is the name of the fxml file, this string should not include 
	 * file extension and should be case-sensitive
	 */
	public void switchScene(ActionEvent e, String sceneName) {
		try {
			// establish the full relative path using the name of the scene
			// this is relative easy to do because all fxml files are stored in the same package
			String path = String.format("/application/resources/views/%s.fxml", sceneName);
			
			Parent root = FXMLLoader.load(getClass().getResource(path));
			stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}
