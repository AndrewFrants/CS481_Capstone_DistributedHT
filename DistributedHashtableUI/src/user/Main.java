package user;

	
import java.io.IOException;

import com.sun.glass.ui.MenuItem;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;



public class Main extends Application {
	
	@FXML
	MenuItem search;
	
	GUIController controller = new GUIController();
	
	ListView<String> metadata;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
			primaryStage.setScene(new Scene(root));
			 primaryStage.setTitle("Meta-Movie Database");
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	
	
	
	public static void main(String[] args) {
		launch(args);
	}
}