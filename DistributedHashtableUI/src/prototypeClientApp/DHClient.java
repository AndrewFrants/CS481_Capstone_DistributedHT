package prototypeClientApp;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import frontend.KeyValue;

public class DHClient {
	
	public String get(String key)
	{
	    final String uri = "http://localhost:8080/dht/{key}";
	     
	    Map<String, String> params = new HashMap<String, String>();
	    params.put("key", key);
	     
	    RestTemplate restTemplate = new RestTemplate();
	    String value = restTemplate.getForObject(uri, String.class, params);
	    
	    return value;
	}

	
	public String addOrUpdate(KeyValue keyValue)
	{
	    final String uri = "http://localhost:8080/dht";
	     
	    RestTemplate restTemplate = new RestTemplate();
	    String value = restTemplate.postForObject(uri, keyValue, String.class);
	    
	    return value;
	}

	
	public void delete(String key)
	{
	    final String uri = "http://localhost:8080/dht/{key}";
	     
	    Map<String, String> params = new HashMap<String, String>();
	    params.put("key", key);
	     
	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.delete(uri, params);
	}

}
