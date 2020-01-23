package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
 
public class CSVToJSON {

	public static void main(String[] args) throws IOException {
		String filePath = "movie_metadata.csv";
		
		BufferedReader reader = null;
		
		String line = "";
		reader = new BufferedReader(new FileReader(filePath));
		
		reader.readLine();
		int index = 0;
		while((line = reader.readLine()) != null) {
			
			
			String[] attributes = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
			
			JSONObject movie = new JSONObject();
			movie.put("movieID", attributes[0]);
			movie.put("title", attributes[2]);
			movie.put("color", attributes[1]);
			movie.put("duration", attributes[3]);
			movie.put("director", attributes[4]);
			
			JSONArray actor = new JSONArray();
			actor.add("Actor 1: " + attributes[5]);
			actor.add("Actor 2: " + attributes[6]);
			actor.add("Actor 3: " + attributes[7]);
			movie.put("actor", actor);
			movie.put("imdbLink", attributes[8]);
			movie.put("language", attributes[9]);
			movie.put("country", attributes[10]);
			movie.put("contentRating", attributes[11]);
			movie.put("year", attributes[12]);
			movie.put("imdbScore", attributes[13]);
			System.out.println(movie);
			String movieInformation = "Movie ID: " + attributes[0] +
									"\nMovie Title: " + attributes[2] +
									"\nColor: " + attributes[1] +
									"\nDuration: " + attributes[3] +
									"\nDirector Name: " + attributes[4] +
									"\n Actor 1: " + attributes[5] +
									"\n Actor 2: " + attributes[6] +
									"\n Actor 3: " + attributes[7] +
									"\n IMDB Link: " + attributes[8] +
									"\n Language: " + attributes[9] +
									"\n Country: " + attributes[10] +
									"\n Content Rating: " + attributes[11] +
									"\n Year: " + attributes[12] + 
									"\n IMDB Score " + attributes[13];
											
									
			try (FileWriter file = new FileWriter("JSON\\" + attributes[0] + ".json")) {
				file.write(movie.toJSONString());
		}
		
	}

}
}
