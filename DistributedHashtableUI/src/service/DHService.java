/**
 * This is the DH Service
 */
package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import data.IDhtEntries;
import data.IDhtNodes;
import data.InMemoryNodes;
import data.WebServiceEntries;
import data.WebServiceNodes;



/**
 * This is the entry point into the service
 * it provides service level functionality
 */
public class DHService {
	
//	public DNode node;
	//HashMap<Integer, DNode> nodes;
	Integer networkBitSize;

	IDhtNodes dhtNodes;
	DNode node;
	//IDhtEntries dhtEntries;
	
	/*
	 * C'tor
	 */
	public DHService()
	{
		// when you change this to webservice
		// nodes, Create starts failing
		dhtNodes = new WebServiceNodes();
	}

	public DHService(Boolean web)
	{
		if (web)
		{
			// when you change this to webservice
			// nodes, Create starts failing
			dhtNodes = new WebServiceNodes();
		}
		else	
		{
			dhtNodes = new InMemoryNodes();
		}
	}
	
	
	
	/* TODO, convert to
	 * 
	 * 	POST <url>/addNode
	 * 	{
	 *  	  "name" : "blah"
	 *  	  /...
	 * 	}
	 * 
	 * Simulate adding a node
	 */
	public void addNode(String name)
	{
		// Case 1 IF node is myself
		// just send to network
		// Case 2 ELSE is this my peer?
		// update own links
		// update peer
		// Case 3 IF not peer
		// send to successor

	
		dhtNodes.addNode(name);
	}
	
	public void addNode(DNode node)
	{
		// Case 1 IF node is myself
		// just send to network
		// Case 2 ELSE is this my peer?
		// update own links
		// update peer
		// Case 3 IF not peer
		// send to successor
		dhtNodes.addNode(node);
	}
	
	/*
	 * Locate what should be the owner of a given file
	 */
	public void insertValue(String value)
	{
		/*
		 * TODO. This part can be/should be optimized to a BST
		 */
		Integer key = ChecksumDemoHashingFunction.hashValue(value);
		List<DNode> allNodes = dhtNodes.getAllNodes();
		
		if (key == 1)
			key = 1;
		
		for (DNode node : allNodes)
		{
			Integer nodeID = node.getNodeID();
			
			if (key >= nodeID)
			{
				// assign this value to a node
				node.AssignKeys(DHashEntry.getHashEntry(value));
				dhtNodes.updateNode(node);
				return;
			}
		}
	}
	
	/*
	 * Find a node by it's name (this is useful for UI).
	 */
	public DNode findNodeByName(String name)
	{
		return dhtNodes.findNodeByName(name);
	}
	
	
	/*
	 * Find the node by a hash code
	 */
	public DNode findNodeByName(Integer hash)
	{
		return dhtNodes.findNodeByName(hash);
	}
	
	/*
	 * Simulate removing the node
	 */
	public void removeNode(String name)
	{
		dhtNodes.removeNode(name);
	}
	

	/*
	 * This is a 4 node sample cluster for testing/demo'ing
	 */
	
	//adding entry
		public void AddEntry(String text) {
			//DNode node = findNodeByName(text);
			//node.AssignKeys(DHashEntry.getHashEntry(text));
			dhtNodes.AddEntry(text);
			//RefreshControls();
		}
		
		// removing entry
		public void RemoveEntry(String text)
		{
			//dhService.removeEntry(name);
			
			//DNode node = findNodeByName(text);
			//node.getTable().removeKeys(ChecksumDemoHashingFunction.hashValue(text));
			
			//AssignKeys(DHashEntry.getHashEntry(text));
			dhtNodes.RemoveEntry(text);
			//RefreshControls();
		}

	public static DHService createFiveNodeCluster(Boolean web)
	{
		DHService dhService = new DHService(web);
		
		String[] nodeNames = new String[] { "Andrews PC", 
											"Daniyal Server",
											"Palak Tablet",
											"Rachana" };
		
		String[] keyNames = new String[] { "CS400 - Monday 9-17.pdf", 
											"CS400 - Monday 9-24.pdf",
											"CS400 - Friday 10-14.pdf",
											"CS400 - Wednesday 10-24.pdf",
											"CS400 - Monday 11-04.pdf",
											"CS400 - Monday 11-14.pdf",
											"CS400 - Monday 11-24.pdf",
											"CS400 - Friday 12-05.pdf",
											"CS411 - Monday 9-17.pdf", 
											"CS411 - Wednesday 9-24.pdf",
											"CS411 - Monday 10-14.pdf",
											"CS411 - Wednesday 10-24.pdf",
											"CS411 - Monday 11-04.pdf",
											"CS411 - Monday 11-14.pdf",
											"CS411 - Monday 11-24.pdf",
											"CS411 - Friday 12-05.pdf",
											"CS420 - Monday 9-17.pdf", 
											"CS420 - Monday 9-24.pdf",
											"CS420 - Friday 10-14.pdf",
											"CS420 - Monday 10-24.pdf",
											"CS420 - Wednesday 11-04.pdf",
											"CS420 - Monday 11-14.pdf",
											"CS420 - Friday 11-24.pdf",
											"CS420 - Monday 12-05.pdf" 
											};
		int index = 0;
		
		for (String nodeName : nodeNames)
		{
			DNode node = new DNode(nodeName);
			
			dhService.addNode(nodeName);
		}
		
		for (int i = 0; i < keyNames.length; i++)
		{
			Integer hash = ChecksumDemoHashingFunction.hashValue(keyNames[i]);
			if (hash == 1)
				hash = 1;
			DNode node = dhService.findNodeByName(hash);
			if (node != null)
			{
				System.out.println("Node: " + node.nodeID + " entry: " + hash);
				if (hash == 1)
					hash = 1;
				
				node.getTable().insert(keyNames[i]);
			}
		}
		
		return dhService;
	}

	/*
	 * Returns all the nodes for visualization
	 */
	public List<DNode> getAllNodes()
	{
		return dhtNodes.getAllNodes();
	}


}
