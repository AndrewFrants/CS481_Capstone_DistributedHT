package user;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import data.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GUIController implements Initializable {


	@FXML
	ListView<String> metadata;
	
	@FXML
	MenuItem search;
	
	@FXML
	Button search_button;
	
	//@FXML  
	//Button search_button;
	
	final ObservableList<String> list = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		metadata.getItems().add("Movie ID: ");
		metadata.getItems().add("Title: ");
		metadata.getItems().add("Actors: ");
		metadata.getItems().add("Year: ");
		metadata.getItems().add("Director: ");
		metadata.getItems().add("Duration: ");
		metadata.getItems().add("Content Rating: ");
		metadata.getItems().add("Director: ");
		metadata.getItems().add("Language: ");
		metadata.getItems().add("Country: ");
		metadata.getItems().add("Color: ");
		metadata.getItems().add("IMDB Rating: ");
		metadata.getItems().add("IMDB Link: ");
		
		metadata.getItems().get(0);
		

		
	}
	
	
	@FXML
	private void handleSearchMenu(ActionEvent event) throws IOException {
		   
		  
		  Stage stage = new Stage();
		       
			Parent root = FXMLLoader.load(getClass().getResource("searchBox.fxml"));
		        stage = new Stage(StageStyle.DECORATED);
		        stage.setTitle("Title");
		        stage.setScene(new Scene(root));
		         
		        
		        stage.show();
		   
		    
	
		
	}
	
	@FXML
	private void clickSearchButton(ActionEvent event) {
		 //Stage stage = (Stage) search_button.getScene().getWindow();
		 // stage.close();
	}
	
	@FXML
	private void populateMovie(Movie m) {
		Integer id = m.getMovieID();
		String index0 = metadata.getItems().get(0) + id;
		metadata.getItems().set(0, index0);
		
		String index1 = metadata.getItems().get(1) + m.getTitle();
		metadata.getItems().set(1,  index1);
		
		String index2 =  metadata.getItems().get(2) +m.getActor();
		metadata.getItems().set(2,  index2);
		
		Integer year = m.getYear();
		
		String index3 =  metadata.getItems().get(3) + year;
		metadata.getItems().set(3,  index3);
		
		String index4 = metadata.getItems().get(4) + m.getDirector();
		metadata.getItems().set(4,  index4);
		
		String index5 =  metadata.getItems().get(5) +m.getDuration();
		metadata.getItems().set(5,  index5);
		
		String index6 =  metadata.getItems().get(6) +m.getContentRating();
		metadata.getItems().set(6,  index6);
		
		String index7 = metadata.getItems().get(7) + m.getDirector();
		metadata.getItems().set(7,  index7);
		
		String index8 =  metadata.getItems().get(8) +m.getLanguage();
		metadata.getItems().set(8,  index8);
		
		String index9 =  metadata.getItems().get(9) +m.getCountry();
		metadata.getItems().set(9,  index9);
		
		String index10 = metadata.getItems().get(10) + m.getColor();
		metadata.getItems().set(10,  index10);
		
		String index11 =  metadata.getItems().get(11) +m.getImdbScore();
		metadata.getItems().set(1,  index11);
		
		String index12=  metadata.getItems().get(12) +m.getImdbLink();
		metadata.getItems().set(12,  index12);
		
	}

}
