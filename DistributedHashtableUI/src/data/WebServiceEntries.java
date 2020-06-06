package data;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import service.DHashEntry;
import service.DNode;
import service.DhtLogger;

// Entries methods for the Web Service (implementation of the interface in IDhtEntries.java)
public class WebServiceEntries implements IDhtEntries {

	//url format
    final String uriFmt = "http://%s/entries/";
    String targetHostEntriesController;
    
    //host and port information
    String host = "localhost";
    String port = "8080";
    
    //constructor
    public WebServiceEntries()
    {
    	//formatting the controller
    	String fqdn = String.format("%s:%s", host, port);
    	targetHostEntriesController = String.format(uriFmt, fqdn);
    }
    
    //constructor with host and port
    public WebServiceEntries(String host, String port)
    {
    	//formatting the controller
    	String fqdn = String.format("%s:%s", host, port);
    	targetHostEntriesController = String.format(uriFmt, fqdn);
    }
    
    //constructor with string input
    public WebServiceEntries(String fqdn)
    {
    	//formatting the controller
    	targetHostEntriesController = String.format(uriFmt, fqdn);
    }
    
    //get Proxy given node input
    public WebServiceEntries getProxyFor(DNode node)
    {
    	//returns entries
    	return new WebServiceEntries(node.getName());
    }
    
    //get Proxy given host and port information
    public WebServiceEntries getProxyFor(String host, String port)
    {
    	return new WebServiceEntries(host, port);
    }
    
    /*
	 * Insert an entry
	 */
    @Override
	public void insert(String name) {
		
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);

	    ObjectMapper mapper = new ObjectMapper();
	       
	    String serializedEntry = null;
	       
	    try {
	    	serializedEntry = mapper.writeValueAsString(DHashEntry.getHashEntry(name));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	    DhtLogger.log.info("POST new entry {} url: {}", name, targetHostEntriesController);

    	HttpEntity<String> entity = new HttpEntity<String>(serializedEntry ,headers);
	    RestTemplate restTemplate = getProxyRestTemplate();
	    restTemplate.postForEntity(targetHostEntriesController, entity, String.class);
	}
	
    //removes entry
    @Override
	public void remove(int entryId) {
	    DhtLogger.log.info("DELETE entry {} url: {}", entryId, targetHostEntriesController + entryId);

	    RestTemplate restTemplate = getProxyRestTemplate();
	    restTemplate.delete(targetHostEntriesController + entryId);
    }
        
	/*
	 * insert entry
	 */
    @Override
	public void insert(DNode node, String name) {
		
		if (node.isUrlPointingAt(this.targetHostEntriesController))
		{
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.setContentType(MediaType.APPLICATION_JSON);

		    ObjectMapper mapper = new ObjectMapper();
		       
		    String serializedEntry = null;
		       
		    try {
		    	serializedEntry = mapper.writeValueAsString(DHashEntry.getHashEntry(name));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

		    DhtLogger.log.info("POST new entry {}", name, targetHostEntriesController);
		    
	    	HttpEntity<String> entity = new HttpEntity<String>(serializedEntry ,headers);
		    RestTemplate restTemplate = getProxyRestTemplate();
		    restTemplate.postForEntity(targetHostEntriesController, entity, String.class);
		}
		else {
			this.getProxyFor(node).insert(name);
		}
	}
    
    //updates an entry given entry ID and value
    @Override
    public void update(int entryId, String entryValue) {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);

	    ObjectMapper mapper = new ObjectMapper();
	       
	    String serializedEntry = null;
	       
	    try {
	    	serializedEntry = mapper.writeValueAsString(DHashEntry.getHashEntry(entryValue));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    DhtLogger.log.info("PUT entry {} url: {}", entryValue, targetHostEntriesController);

    	HttpEntity<String> entity = new HttpEntity<String>(serializedEntry ,headers);
	    RestTemplate restTemplate = getProxyRestTemplate();
	    restTemplate.put(targetHostEntriesController + entryId, entity, String.class);
    }
	
    //gets entry based on key
	public DHashEntry get(Integer key)
    {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper mapper = new ObjectMapper();
        
	    RestTemplate restTemplate = getProxyRestTemplate();
	    
	    DHashEntry entry = null;
       
	    String url = targetHostEntriesController + key;
	    DhtLogger.log.info("Getting key by hash {} at url: {}", key, url);
	    
        try {
			entry = mapper.readValue(restTemplate.getForObject(url, String.class), DHashEntry.class);
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
        return entry;
	}

	//gets entry given name
    public DHashEntry get(String name)
    {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper mapper = new ObjectMapper();
        
	    RestTemplate restTemplate = getProxyRestTemplate();
	    
	    DHashEntry entry = null;
       
        try {
        	entry = mapper.readValue(restTemplate.getForObject(targetHostEntriesController + name, String.class), DHashEntry.class);
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
        
        return entry;
    }
    
    //gets entry given node and name
    //@Override
	public DHashEntry get(DNode node, String name) {
		
		if (node.isUrlPointingAt(this.targetHostEntriesController))
		{
	    	return this.get(name);
		}
		else {
			return this.getProxyFor(node).get(name);
		}
	}

	//getProxyRestTemplate method
	public RestTemplate getProxyRestTemplate() {
		if (WebServiceNodes.isProxyEnabled) {
		    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
	
		    Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress("localhost", 8888));
		    requestFactory.setProxy(proxy);
	
		    return new RestTemplate(requestFactory);
		}
		
		return new RestTemplate();
	}
	
}