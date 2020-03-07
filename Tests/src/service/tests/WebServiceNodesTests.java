/*
 * Tests for NodeController
 */
package service.tests;


import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import data.WebServiceNodes;
import service.*;

public class WebServiceNodesTests {

	WebServiceNodes nodes;
	
	@BeforeEach
	void setUp() throws Exception {
	
		nodes = new WebServiceNodes();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	/* for Andrew: Sample code
	   for (Customer customer : customers) {
    		System.out.println(customer);
		}
	 */
	@Test
	public void testGetNodes() {
		
		nodes = new WebServiceNodes();
		List<DNode> allNodes = nodes.getAllNodes();

		allNodes.forEach(n -> {
		    System.out.println(n.getName());
		});
		
	}
	
	@Test
	public void testgetEntries() {
		
		
		
		
		nodes = new WebServiceNodes();
		List<DNode> allNodes = nodes.getAllNodes();
		List<DNode> nodess = new LinkedList<DNode>();

		   List<List<DHashEntry>> list = new ArrayList<List<DHashEntry>>();
		   for (int i = 0; i < nodess.size(); i++) {
			   list.add(nodess.get(i).getAllEntries());
			   System.out.println(list);
		   }
		
		
		allNodes.forEach(n -> {
		    System.out.println(n.getName());
		});
		

		
	}
		
	}
	
	/*
	@Test
	public void testAddAndRemoveNode() {

	    final String uri = "http://localhost:8080/nodes";
	    
	    HttpHeaders headers = new HttpHeaders();

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
	*/

	

