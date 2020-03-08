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
 * @author Andrew
 * This is the webservice version of the Nodes Collection
 */
public class WebServiceNodes implements IDhtNodes {

    final String uriFmt = "http://%s/nodes/";
    
    String targetHostNodesController;
    
    String host = "localhost";
    String port = "8080";
    
    static boolean isProxyEnabled = false;
    
    public WebServiceNodes()
    {
    	String fqdn = String.format("%s:%s", host, port);
    	targetHostNodesController = String.format(uriFmt, fqdn);
    }
    
    public WebServiceNodes(String host, String port)
    {
    	String fqdn = String.format("%s:%s", host, port);
    	targetHostNodesController = String.format(uriFmt, fqdn);
    }
    
    public WebServiceNodes getProxyFor(String host, String port)
    {
    	return new WebServiceNodes(host, port);
    }
    
	@Override
	public DNode findNodeByName(String name) {
		int hash = ChecksumDemoHashingFunction.hashValue(name);
		return findNodeByName(hash);
	}

	@Override
	public DNode findNodeByName(Integer hash) {

		return getNodeByPath(targetHostNodesController + "hash/" + hash);
	}

	@Override
	public void addNode(String name) {

	    HttpHeaders headers = new HttpHeaders();

	    DNode node = new DNode(name);
	    
	    HttpEntity<DNode> request = new HttpEntity<>(node, headers);
		
	    RestTemplate restTemplate = getProxyRestTemplate();
	    restTemplate.postForObject(targetHostNodesController, request, String.class);
	}
	
	@Override
	public void addNode(DNode node) {

	    HttpHeaders headers = new HttpHeaders();

	    HttpEntity<DNode> request = new HttpEntity<>(node, headers);
		
	    RestTemplate restTemplate = getProxyRestTemplate();
	    restTemplate.postForObject(targetHostNodesController, request, String.class);
	}

	
	public void removeNode(DNode name) {

	    RestTemplate restTemplate = getProxyRestTemplate();
	    
	    restTemplate.delete(targetHostNodesController + name);

	}

	@Override
	public List<DNode> getAllNodes() {
		return getNodes(targetHostNodesController);
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
		String url = targetHostNodesController + n.getName();
		
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
		this.updateNode(node);
	}
	
	public void AddEntry(DNode node, String text) {
		node.AssignKeys(DHashEntry.getHashEntry(text));
		this.updateNode(node);
		 HttpHeaders headers = new HttpHeaders();
		  HttpEntity<String> request = new HttpEntity<>(text, headers);
		    RestTemplate restTemplate = getProxyRestTemplate();
		    restTemplate.postForObject(targetHostNodesController, request, String.class);
	}
	

	
	//removing entry
	public void RemoveEntry(String text) {
		DNode node = findNodeByName(text);
		node.getTable().removeKeys(ChecksumDemoHashingFunction.hashValue(text));
		this.updateNode(node);
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

	@Override
	public void removeNode(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AddEntry(DNode node) {
		// TODO Auto-generated method stub
		
	}
}