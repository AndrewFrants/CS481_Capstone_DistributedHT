package nodes.storage;

import service.DNode;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class NodesStorage {
	private final String NODES_FILENAME = "/nodes.json";
	
	public void writeToStorage(List<DNode> nodes) {
		if (nodes != null) {
			// Temporary: router is causing circular reference and gson.tojson is throwing stackoverflow error
			for (DNode node : nodes)
			{
				node.router = null;
			}
			
			// Serialize the nodes as string using Gson 
			Gson gson = new Gson();		
			String nodesJson = gson.toJson(nodes);
			
			// Get the path for resources folder while the service is running
			ClassLoader classLoader = getClass().getClassLoader();
			String filePath = classLoader.getResource(".").getFile() + NODES_FILENAME;
			System.out.println(filePath);
			
			// Save the serialized nodes to the file
			try (FileWriter file = new FileWriter(filePath)) {
				file.write(nodesJson);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public List<DNode> readFromStorage() {
		// Get the path for resources folder while the service is running
		ClassLoader classLoader = getClass().getClassLoader();
		String filePath = classLoader.getResource(".").getFile() + NODES_FILENAME;
		String nodesJson = readFileAsString(filePath);
		
		// Deserialize the string as List<DNode> using Gson 
		Gson gson = new Gson();
		List<DNode> nodes = gson.fromJson(nodesJson,  new TypeToken<List<DNode>>(){}.getType());
		
		return nodes;
	}
	
	public static String readFileAsString(String fileName) {
	    String text = "";
	    /*TODO
	    try {
	     
	      text = new String(Files.readString(Paths.get(fileName)));
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
		*/
	    
	    return text;
	  }
}
