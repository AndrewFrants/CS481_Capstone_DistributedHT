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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	IDhtEntries dhtEntries;
	
	Logger log = LoggerFactory.getLogger(DHService.class);
	
	/*
	 * C'tor
	 */
	public DHService()
	{
		// when you change this to webservice
		// nodes, Create starts failing
		dhtNodes = new InMemoryNodes(null);
	}
	
	public DHService(Boolean web)
	{
		log.info("initialize web={}", web);

		if (web)
		{
			// when you change this to webservice
			// nodes, Create starts failing
			dhtNodes = new WebServiceNodes();
			dhtEntries = new WebServiceEntries();
		}
		else	
		{
			dhtNodes = new InMemoryNodes(null);
		}
	}
	
	public void addNode(String name)
	{
		log.debug("addNode(String) {}", name);
		dhtNodes.addNode(name);
	}
	
	public void addNode(DNode node)
	{
		log.debug("addNode(DNode)", node.name);
		dhtNodes.addNode(node);
	}
	
	/*
	 * Locate what should be the owner of a given file
	 */
	public void insertValue(String value)
	{
		DhtLogger.log.info("insertValue(DNode) {}", value);
		
		/*
		 * TODO. This part can be/should be optimized to a BST
		 */
		if (this.dhtEntries == null)
		{
			Integer key = ChecksumDemoHashingFunction.hashValue(value);
			List<DNode> allNodes = dhtNodes.getAllNodes();
			
			for (DNode node : allNodes)
			{
				Integer nodeID = node.getNodeID();
				
				if (key >= nodeID)
				{
					// assign this value to a node
					node.AssignKeys(DHashEntry.getHashEntry(value));
					dhtNodes.updateNode(node);
					log.info("added {} to node {}", value, node.name);
	
					return;
				}
			}
		}
		else 
		{
			DhtLogger.log.info("Invoking WebAPI to insert {}", value);
			this.dhtEntries.insert(value);
		}
	}
	
	public DHashEntry getEntry(Integer key)
	{
		return this.dhtEntries.get(key);
	}
	
	/*
	 * Find a node by it's name (this is useful for UI).
	 */
	public DNode findNodeByName(String name)
	{
		log.debug("findNodeByName(String) {}", name);
		return dhtNodes.findNodeByName(name);
	}

	/*
	 * Find the node by a hash code
	 */
	public DNode findNodeByName(Integer hash)
	{
		log.debug("findNodeByName(Int)", hash);
		return dhtNodes.findNodeByName(hash);
	}
	
	/*
	 * Simulate removing the node
	 */ 
	public void removeNode(String name)
	{
		Integer nodeId = ChecksumDemoHashingFunction.hashValue(name);
		DNode node = new DNode();
		node.nodeID = nodeId;
		
		dhtNodes.removeNode(node);
	}
	

	/*
	 * This is a 4 node sample cluster for testing/demo'ing
	 */
	
	//adding entry
		public void AddEntry(String text) {
			
			// If current node owns this entry
			//    currentNode.getTable.Insert()
			// If current node does not own entry
			// 	  WebServicesNodes.insert(text); //forward to successor
			//
			
			//DNode node = findNodeByName(text);
			//node.AssignKeys(DHashEntry.getHashEntry(text));
			//dhtNodes.AddEntry(text);
			//RefreshControls();
			
			insertValue(text);
		}
		
		// updating an entry
		public void UpdateEntry(int id, String text)
		{
			dhtEntries.update(id, text);
		}		
		
		// removing entry
		public void RemoveEntry(int entryId)
		{
			dhtEntries.remove(entryId);
		}

	public static DHService createCluster(Boolean web)
	{
		DHService dhService = new DHService(web);
		
		return dhService;
	}

	/*
	 * Returns all the nodes for visualization
	 */
	public List<DNode> getAllNodes()
	{
		DhtLogger.log.info("getting all nodes from WS.");
		return dhtNodes.getAllNodes();
		
	}


}
