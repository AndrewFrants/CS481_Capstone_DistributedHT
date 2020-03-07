package data;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import service.DNode;

public class WebServiceEntries {

	/*
	 * Find the node by name
	 */
	void insert(String name) {
		
	    HttpHeaders headers = new HttpHeaders();

	    HttpEntity<String> request = new HttpEntity<>(name, headers);
		
	    RestTemplate restTemplate = getProxyRestTemplate();
	    restTemplate.postForObject(uri, request, String.class);
	}
	

    final String uri = "http://localhost:8081/nodes";
    
    static boolean isProxyEnabled = true;
    

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
	
}