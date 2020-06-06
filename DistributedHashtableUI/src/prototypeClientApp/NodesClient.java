package prototypeClientApp;

import java.util.HashMap;
import java.util.Map;
import service.DNode;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

//client facing Nodes
public class NodesClient {
	
	//gets node given id
	public DNode get(String id)
	{
	    final String uri = "http://localhost:8080/nodes/{id}";
	     
	    Map<String, String> params = new HashMap<String, String>();
	    params.put("id", id);
	     
	    RestTemplate restTemplate = new RestTemplate();
	    DNode value = restTemplate.getForObject(uri, DNode.class, params);
	    
	    return value;
	}

	//add or update given node's name
	public Boolean addOrUpdate(String nodeName)
	{
	    final String uri = "http://localhost:8080/nodes";
	     
	    RestTemplate restTemplate = new RestTemplate();
	    ResponseEntity<String> result = restTemplate.postForEntity(uri, nodeName, String.class);
	    
	    return result.getStatusCode() == HttpStatus.CREATED ? true: false;
	}

	//delete node
	public void delete(String id)
	{
	    final String uri = "http://localhost:8080/nodes/{id}";
	     
	    Map<String, String> params = new HashMap<String, String>();
	    params.put("id", id);
	     
	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.delete(uri, params);
	}

}
