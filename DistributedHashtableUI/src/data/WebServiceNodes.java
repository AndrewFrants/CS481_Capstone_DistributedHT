/**
 * 
 */
package data;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import service.ChecksumDemoHashingFunction;
import service.DHashEntry;
import service.DNode;

/**
 * @author andreyf
 * This is the webservice version of the Nodes Collection
 */
public class WebServiceNodes implements IDhtNodes {

    final String uri = "http://localhost:8081/nodes";
    
    static boolean isProxyEnabled = true;
    
	@Override
	public DNode findNodeByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DNode findNodeByName(Integer hash) {

		return getNodeByPath(uri + "/hash/" + hash);
	}

	@Override
	public void addNode(String name) {

	    HttpHeaders headers = new HttpHeaders();

	    DNode node = new DNode(name);
	    
	    HttpEntity<DNode> request = new HttpEntity<>(node, headers);
		
	    RestTemplate restTemplate = getProxyRestTemplate();
	    restTemplate.postForObject(uri, request, String.class);

	}

	@Override
	public void removeNode(String name) {

	    RestTemplate restTemplate = getProxyRestTemplate();
	    
	    restTemplate.delete("http://localhost:8080/nodes/" + name);

	}

	@Override
	public List<DNode> getAllNodes() {
		return getNodes("http://localhost:8080/nodes/");
	}

	public RestTemplate getProxyRestTemplate() {
		if (isProxyEnabled) {
		    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
	
		    Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress("localhost", 8888));
		    requestFactory.setProxy(proxy);
	
		    return new RestTemplate(requestFactory);
		}
		
		return new RestTemplate();
	}
	
	private String getUri(String uri) {
		
	    RestTemplate restTemplate = getProxyRestTemplate();
	    return restTemplate.getForObject(uri, String.class);
	}
	
	private DNode getNodeByPath(String uri) {
		
	    RestTemplate restTemplate = getProxyRestTemplate();
	    return restTemplate.getForObject(uri, DNode.class);
	}
	
	/*
		RestTemplate restTemplate = getProxyRestTemplate();
	    ResponseEntity<List<T>> response = restTemplate.exchange(
	      path,
	      method,
	      null,
	      new ParameterizedTypeReference<List<T>>(){});
	 */
	
	public void updateNode(DNode n) {
		String url = uri + "/" + n.getName();
		
		RestTemplate restTemplate = getProxyRestTemplate();
	    ObjectMapper mapper = new ObjectMapper();
	       
	    String updatedNode = null;
	       
	    try {
	    	updatedNode = mapper.writeValueAsString(n);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
		restTemplate.patchForObject(url, updatedNode, String.class);
	}
	
	private List<DNode> getNodes(String uri) {
		
	    RestTemplate restTemplate = getProxyRestTemplate();
	       ObjectMapper mapper = new ObjectMapper();
	       
	       //java.lang.reflect.Type typeOfListOfFoo = new com.google.gson.reflect.TypeToken<List<DNode>>();
	       //.getType()
	       DNode[] nodes = null;
	       
	        try {
				nodes = mapper.readValue(restTemplate.getForObject(uri, String.class), DNode[].class);
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RestClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		   List<DNode> list = new ArrayList<DNode>();
		   for (DNode node : nodes) {
	    		list.add(node);
		   }
		   return list;
	       /*
	    ResponseEntity<List<T>> response = restTemplate.exchange(
	      uri,
	      HttpMethod.GET,
	      null,
	      new ParameterizedTypeReference<List<T>>(){});
	    List<T> list = response.getBody();
	    return list;
	    */
	}
	
	////NEED TO CHANGE ALL METHODS BELOW
	
	// adding entry
	public void AddEntry(String text) {
		DNode node = findNodeByName(text);
		node.AssignKeys(DHashEntry.getHashEntry(text));
	}
	
	//removing entry
	public void RemoveEntry(String text) {
		DNode node = findNodeByName(text);
		node.getTable().removeKeys(ChecksumDemoHashingFunction.hashValue(text));
	}
	
	//gets all entries 
	public List<List<DHashEntry>> getAllEntries() {
		   List<DNode> nodes = new LinkedList<DNode>();
		   List<List<DHashEntry>> list = new ArrayList<List<DHashEntry>>();

		   for (int i = 0; i < nodes.size(); i++) {
			   list.add(nodes.get(i).getAllEntries());
		   }
		   return (list);
	}
	
	//gets  entries for a specific node
	public List<DHashEntry> getAllEntriesforNode(String id) {
		DNode node = findNodeByName(id);
		List<DHashEntry> specificEntries = node.getAllEntries();
		return(specificEntries);
	}
}