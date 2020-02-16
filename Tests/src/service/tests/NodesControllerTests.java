/*
 * Tests for NodeController
 */
package service.tests;


import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import service.*;

public class NodesControllerTests {

	final static boolean isProxyEnabled = true;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	public void testGetNodes() {
		
	    final String uri = "http://localhost:8080/nodes";
	     
	    String value = getUri(uri);
	    
	    System.out.print(value);
	}
	
	@Test
	public void testAddAndRemoveNode() {

	    final String uri = "http://localhost:8080/nodes";
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("X-COM-PERSIST", "true");    
	    headers.set("X-COM-LOCATION", "USA");      
	    
	    DNode node = new DNode("newNodeName");
	    
	    HttpEntity<DNode> request = new HttpEntity<>(node, headers);
		
	    RestTemplate restTemplate = getProxyRestTemplate();
	    restTemplate.postForObject(uri, request, String.class);
	    
	    String value = getUri(uri);
	    
	    Assert.assertTrue(value.contains("newNodeName"));
	    System.out.print(value);
	    
	    restTemplate.delete("http://localhost:8080/nodes/newNodeName");
	    
	    value = getUri(uri);
	    
	    Assert.assertFalse(value.contains("newNodeName"));
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
}
	

