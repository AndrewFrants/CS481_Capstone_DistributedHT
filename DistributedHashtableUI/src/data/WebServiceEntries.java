package data;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.List;

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

public class WebServiceEntries implements IDhtEntries {

    final String uriFmt = "http://%s/entries/";
    
    String targetHostEntriesController;
    
    String host = "localhost";
    String port = "8080";
    
    public WebServiceEntries()
    {
    	String fqdn = String.format("%s:%s", host, port);
    	targetHostEntriesController = String.format(uriFmt, fqdn);
    }
    
    public WebServiceEntries(String host, String port)
    {
    	String fqdn = String.format("%s:%s", host, port);
    	targetHostEntriesController = String.format(uriFmt, fqdn);
    }
    
    public WebServiceEntries(String fqdn)
    {
    	targetHostEntriesController = String.format(uriFmt, fqdn);
    }
    
    public WebServiceEntries getProxyFor(DNode node)
    {
    	return new WebServiceEntries(node.getName());
    }
    
    public WebServiceEntries getProxyFor(String host, String port)
    {
    	return new WebServiceEntries(host, port);
    }
    
    


	/*
	 * Insert
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    DhtLogger.log.info("POST new entry {} url: {}", name, targetHostEntriesController);

    	HttpEntity<String> entity = new HttpEntity<String>(serializedEntry ,headers);
	    RestTemplate restTemplate = getProxyRestTemplate();
	    restTemplate.postForEntity(targetHostEntriesController, entity, String.class);
	}
	
    
    
    @Override
	public void remove(DNode node, String name) {

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
				// TODO Auto-generated catch block
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
    
    @Override
    public void update(DNode node, int entryId, String entryValue) {
    	// TODO Rachana
    }
	
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

	public RestTemplate getProxyRestTemplate() {
		if (WebServiceNodes.isProxyEnabled) {
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
	
}