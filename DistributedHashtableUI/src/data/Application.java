package data;


import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.google.gson.Gson;

public class Application {

	
	public static void main(String[] args) {
		  Gson gson = new Gson();
		  	
	        try (Reader reader = new FileReader("JSON\\2.json")) {
	        	
	        
	            // Convert JSON File to Java Object
	            Movie m = gson.fromJson(reader, Movie.class);
		
		
	            System.out.println(m);

	        } catch (IOException e) {
	            e.printStackTrace();
	        }

			
	}
}