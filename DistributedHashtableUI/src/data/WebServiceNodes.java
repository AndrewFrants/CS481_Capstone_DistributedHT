
package data;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import service.ChecksumDemoHashingFunction;
import service.DHashEntry;
import service.DNode;
import service.DhtLogger;

/**
 * @author Andrew This is the web service version of the Nodes Collection
 * Refer to the interface in IDhtNodes.java
 */
public class WebServiceNodes implements IDhtNodes {

	//url format
	final String uriFmt = "http://%s/nodes/"; // keep the trailing slash because its coded for this assumption
	String targetHostNodesController;

    //host and port information
	String host = "localhost";
	String port = "8080";

	//proxy is not enabled
	public static boolean isProxyEnabled = false;

	//constructor
	public WebServiceNodes() {
    	//formatting the controller
		String fqdn = String.format("%s:%s", host, port);
		targetHostNodesController = String.format(uriFmt, fqdn);
	}

	//constructor with host and port
	public WebServiceNodes(String host, String port) {
    	//formatting the controller
		String fqdn = String.format("%s:%s", host, port);
		targetHostNodesController = String.format(uriFmt, fqdn);
	}

    //constructor with string input
	public WebServiceNodes(String fqdn) {
    	//formatting the controller
		targetHostNodesController = String.format(uriFmt, fqdn);
	}

    //get Proxy given node input
	public static WebServiceNodes getProxyFor(DNode node) {
		return new WebServiceNodes(node.getName());
	}

    //create Proxy given node input
	public IDhtNodes createProxyFor(DNode node) {
		return new WebServiceNodes(node.getName());
	}
	
    //get Proxy given host and port information
	public static WebServiceNodes getProxyFor(String host, String port) {
		return new WebServiceNodes(host, port);
	}

	// find node given its name
	@Override
	public DNode findNodeByName(String name) {
		int hash = ChecksumDemoHashingFunction.hashValue(name);
		return findNodeByName(hash);
	}

	//find node given hash value
	@Override
	public DNode findNodeByName(Integer hash) {

		DhtLogger.log.info("Getting node {}", hash);
		
		return getNodeByPath(targetHostNodesController + hash);
	}

	//find node 
	@Override
	public DNode findNodeByName(DNode n) {

		if (n.isUrlPointingAt(targetHostNodesController)) {
			return findNodeByName(n.nodeID);
		} else {
			return this.createProxyFor(n).getNode();
		}
	}

	//adding a node given name
	@Override
	public void addNode(String name) {

		HttpHeaders headers = new HttpHeaders();

		DNode node = new DNode(name);

		HttpEntity<DNode> request = new HttpEntity<>(node, headers);

		RestTemplate restTemplate = getProxyRestTemplate();
		restTemplate.postForObject(targetHostNodesController, request, String.class);
	}

	//adding a node
	@Override
	public void addNode(DNode node) {

		HttpHeaders headers = new HttpHeaders();

		HttpEntity<DNode> request = new HttpEntity<>(node, headers);

		RestTemplate restTemplate = getProxyRestTemplate();
		restTemplate.postForObject(targetHostNodesController, request, String.class);
	}
	
	//removing node
	@Override
	public void removeNode(DNode node) {
		DNode predecessor = findNodeByName(node.predecessor.nodeID);
		
		RestTemplate restTemplate = getProxyRestTemplate();
		restTemplate.delete(targetHostNodesController + predecessor.name);
	}

	//gets all the nodes
	@Override
	public List<DNode> getAllNodes() {
		return getNodes(targetHostNodesController);
	}

	//gets current node
	@Override
	public DNode getNode() {
		RestTemplate restTemplate = getProxyRestTemplate();
		String url = this.targetHostNodesController + "self"; // get itself, as by default DHT returns all nodes
		DhtLogger.log.info("Getting node {}", url);
		return restTemplate.getForObject(url, DNode.class);
	}
	
	//getProxyRestTemplate method
	public RestTemplate getProxyRestTemplate() {
		if (isProxyEnabled) {
			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

			Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress("localhost", 8888));
			requestFactory.setProxy(proxy);

			return new RestTemplate(requestFactory);
		}

		return new RestTemplate();
	}

	//gets current node
	public DNode get() {
		RestTemplate restTemplate = getProxyRestTemplate();
	
		return restTemplate.getForObject(this.targetHostNodesController, DNode.class);
	}
	
	//gets a node given path information
	private DNode getNodeByPath(String uri) {
		RestTemplate restTemplate = getProxyRestTemplate();
		return restTemplate.getForObject(uri, DNode.class);
	}
	
	//updates node
	public void updateNode(DNode n) {

		if (n.isUrlPointingAt(targetHostNodesController)) {
			String url = targetHostNodesController + n.nodeID;
			String updatedNode = null;

			try
			{
				RestTemplate restTemplate = getProxyRestTemplate();
				ObjectMapper mapper = new ObjectMapper();

				try {
					updatedNode = mapper.writeValueAsString(n);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String successorId = n.successor == null ? "NULL" : n.successor.nodeID.toString();
				String predecessorId = n.predecessor == null ? "NULL" : n.predecessor.nodeID.toString();
				DhtLogger.log.debug("Patching node at url {} n.S={} n.P={} updatedNode={}", url, successorId, predecessorId, updatedNode);

				restTemplate.put(url, updatedNode, String.class);
			}
			catch (Exception ex)
			{
				DhtLogger.log.warn("Couldnt patch node possibly server inst up yet {} body {} ex {}", url, updatedNode, ex.toString());
			}
		} else {

			WebServiceNodes wsNodes = this.getProxyFor(n);
			wsNodes.updateNode(n);
		}
	}
	
	//gets nodes
	private List<DNode> getNodes(String uri) {

		DhtLogger.log.info("GET to {}", uri);
		
		RestTemplate restTemplate = getProxyRestTemplate();
		ObjectMapper mapper = new ObjectMapper();

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
	}

	// adding entry
	public void AddEntry(String text) {
		DNode node = findNodeByName(text);
		node.AssignKeys(DHashEntry.getHashEntry(text));
		DhtLogger.log.info("Adding key {} to node: {}({})", text, node.name, node.nodeID);
		this.updateNode(node);
	}
	
	//adding entry
	public void AddEntry(DNode node, String text) {
		node.AssignKeys(DHashEntry.getHashEntry(text));
		this.updateNode(node);
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> request = new HttpEntity<>(text, headers);
		RestTemplate restTemplate = getProxyRestTemplate();
		DhtLogger.log.info("Posting entry {} to node {}({})", text, node.name, node.nodeID);
		restTemplate.postForObject(targetHostNodesController, request, String.class);
	}

	// removing entry
	public void RemoveEntry(String text) {
		DNode node = findNodeByName(text);
		DhtLogger.log.info("Removing entry {} from node {}({})", text, node.name, node.nodeID);
		node.getTable().removeKeys(ChecksumDemoHashingFunction.hashValue(text));
		this.updateNode(node);
	}

	// gets all entries
	public List<List<DHashEntry>> getAllEntries() {
		List<DNode> nodes = new LinkedList<DNode>();
		List<List<DHashEntry>> list = new ArrayList<List<DHashEntry>>();

		for (int i = 0; i < nodes.size(); i++) {
			list.add(nodes.get(i).getAllEntries());
		}
		return (list);
	}

	// gets entries for a specific node
	public List<DHashEntry> getAllEntriesforNode(String id) {
		DNode node = findNodeByName(id);
		List<DHashEntry> specificEntries = node.getAllEntries();
		return (specificEntries);
	}

	@Override
	public void AddEntry(DNode node) {
	}
}