package application.java.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import application.java.models.AnimationManager;
import application.java.models.FileIO;
import application.java.models.SceneManager;
import application.java.models.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class CompletedController {

	// FXML Injectable fields
	@FXML private TableView<Word> summaryTable;
	@FXML private TableColumn<Word, String> wordColumn;
	@FXML private TableColumn<Word, Integer> scoreColumn;
	@FXML private TableColumn<Word, String> resultColumn;
	@FXML private Label totalScoreLabel;
	@FXML private AnchorPane stars;
	@FXML private TextField nameTextField;

	// Other class fields
	private int totalScore = 0;
	private static String STAR_COLOUR_HEX = "#FFD700";
	private boolean isSaved = false;
	private SceneManager sceneManager = new SceneManager();

	/**
	 * This method is invoked when we switch from the quiz to completed scene. It passes
	 * useful data to this controller (e.g. the user's answer statistics), and calculates
	 * the total score and displays this in the appropriate label..
	 * 
	 * @param wordStats - the user's answer statistics
	 */
	public void setData(List<Word> wordStats) {

		// Write the data to the table
		ObservableList<Word> list = FXCollections.observableArrayList(wordStats);

		// note that, if you want to change the text display as result, please go to Word.java
		this.wordColumn.setCellValueFactory(new PropertyValueFactory<Word, String>("word"));
		this.scoreColumn.setCellValueFactory(new PropertyValueFactory<Word, Integer>("score"));
		this.resultColumn.setCellValueFactory(new PropertyValueFactory<Word, String>("result"));

		this.summaryTable.setItems(list);

		// iterates the list to calculate the total score and set it to the text of label
		for (Word word: wordStats) {
			this.totalScore += word.getScore();
		}

		String totalScoreString = Integer.toString(totalScore);
		int finalTotalScore = Integer.parseInt(totalScoreString);
		this.totalScoreLabel.setText(Integer.toString(finalTotalScore));
	}

	/**
	 * This method controls the star animation on the completed screen with 
	 * specific score boundary for each star.
	 */
	public void setAnimation() {
		int scoreBoundaryOne = 170;
		int scoreBoundaryTwo = 350;
		AnimationManager animationManager = new AnimationManager();
		if(totalScore == 0) {
			animationManager.playStarsAnimation(0, stars, STAR_COLOUR_HEX);
		}
		else if(totalScore <= scoreBoundaryOne) {
			animationManager.playStarsAnimation(1, stars, STAR_COLOUR_HEX);
		}
		else if(totalScore > scoreBoundaryOne && totalScore <= scoreBoundaryTwo ) {
			animationManager.playStarsAnimation(2, stars, STAR_COLOUR_HEX);
		}
		else if(totalScore > scoreBoundaryTwo) {
			animationManager.playStarsAnimation(3, stars, STAR_COLOUR_HEX);
		}
	}

	public void returnHome(ActionEvent e) {
		sceneManager.switchScene(e, "Main");
	}

	public void playAgain(ActionEvent e) {
		sceneManager.switchScene(e, "Quiz");
	}

	/**
	 * This methods saves the user score along with user name into scoreBoard HashMap entry.
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void save() throws InterruptedException, IOException {
		// if game score has been saved already
		if (isSaved) {
			// notify user that it has already previously saved.
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Warning!");
			alert.setHeaderText("Data has been saved previously already - you cannot save it again.");
			alert.showAndWait();
			// if game score has not yet been saved
		} else {
			HashMap<String, Integer> loadedData = FileIO.loadGame();
			// retrieve user name from text field
			String userName = this.nameTextField.getText();

			// if username already exist in scoreBoard
			if (loadedData.containsKey(userName)) {

				// if this round score is lower than the previously recorded score
				if (loadedData.get(userName) > this.totalScore) {
					// inform user, and do not update score
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Notification");
					alert.setHeaderText("You already have a higher score saved at the scoreboard! This score will not be saved.");
					alert.showAndWait();

					// if this round score is lower than the previously recorded score	
				} else {
					// save score
					loadedData.put(userName, this.totalScore);
					FileIO.saveGame(FileIO.sortByValue(loadedData));
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Data saved");
					alert.setHeaderText("Your score has been saved to the scoreboard!");
					alert.showAndWait();

					isSaved = true;
				}

				// if user name doesn't exist in scoreBard
			} else {
				// add user stats to scoreBoard
				loadedData.put(userName, this.totalScore);
				FileIO.saveGame(FileIO.sortByValue(loadedData));

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Data saved");
				alert.setHeaderText("Your score has been saved to the scoreboard!");
				alert.showAndWait();

				isSaved = true;
			}
		}
	}
}
