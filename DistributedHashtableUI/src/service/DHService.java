/**
 * This is the DH Service class
 */

package service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.IDhtEntries;
import data.IDhtNodes;
import data.InMemoryNodes;
import data.WebServiceEntries;
import data.WebServiceNodes;


/**
 * This is the entry point into the service
 * It provides service level functionality
 */
public class DHService {
	
	//fields initialization
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
	
	//constructor
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
	
	//adding a node given name
	public void addNode(String name)
	{
		log.debug("addNode(String) {}", name);
		dhtNodes.addNode(name);
	}
	
	//adding a node
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
	
	//gets entry based on key
	public DHashEntry getEntry(Integer key)
	{
		return this.dhtEntries.get(key);
	}
	
	/*
	 * Find a node by its name (this is useful for UI).
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

	//creating a cluster; returns DHService	
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
