package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException{
		Parent root = FXMLLoader.load(getClass().getResource("/application/resources/views/Main.fxml")); 
		primaryStage.setTitle("Kēmu Kupu");
		Scene scene = new Scene(root);
		String css = this.getClass().getResource("/application/resources/css/application.css").toExternalForm();
		scene.getStylesheets().add(css);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		launch(args);

	}
}
