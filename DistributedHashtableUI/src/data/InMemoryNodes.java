
package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import service.ChecksumDemoHashingFunction;
import service.DHServerInstance;
import service.DHashEntry;
import service.DHashtable;
import service.DNode;
import service.DhtLogger;
import webservice.DhtWebService;

/**
 * @author Andrew Frantsuzov
 * This class simulates nodes functionality; inMemory Nodes Collection
 */

public class InMemoryNodes implements IDhtNodes {

	// HashMap of nodes
	static HashMap<Integer, DHServerInstance> nodes;
	
	// instance
	DHServerInstance dhServiceInstance;
	
	//constructor
	public InMemoryNodes(DHServerInstance dhServiceInstance) {

		if (nodes == null)
		{
			nodes = new HashMap<Integer, DHServerInstance>();
		}
		
		this.dhServiceInstance = dhServiceInstance;
	}
	
	// finds node given its name
	@Override
	public DNode findNodeByName(String name) {
		int hash = ChecksumDemoHashingFunction.hashValue(name);
		
		if (this.nodes.containsKey(hash))
			return this.nodes.get(hash).currentNode;
		
		return findNodeByName(hash);
	}
	
	// finds a node given its hash value
	@Override
	public DNode findNodeByName(Integer hash) {
		Set<Integer> keysenu = nodes.keySet();
		List<Integer> numbersList = new ArrayList<Integer>(keysenu);
		
		Collections.sort(numbersList);
		
		Iterator<Integer> iter = numbersList.iterator();
		int prev = 0;
		int first = 0;
		int index = 0;
		
		while (iter.hasNext())
		{
			Integer curr = iter.next();

			if (first == 0)
				first = curr;
			
			if (index != 0 && hash >= prev && hash <= curr)
			{
				return this.nodes.get(prev).currentNode;
			}
			else if (!iter.hasNext())
			{
				return this.nodes.get(curr).currentNode;
			}
			
			prev = curr;
			index++;
		}
		
		return null;
	}
	
	// finds node
	@Override
	public DNode findNodeByName(DNode n) {
		Set<Integer> keysenu = nodes.keySet();
		List<Integer> numbersList = new ArrayList<Integer>(keysenu);
		
		Collections.sort(numbersList);
		
		Iterator<Integer> iter = numbersList.iterator();
		int prev = 0;
		int first = 0;
		int index = 0;
		
		while (iter.hasNext())
		{
			Integer curr = iter.next();

			if (first==0)
				first = curr;
			
			if (index != 0 && n.nodeID >= prev && n.nodeID <= curr)
			{
				return this.nodes.get(prev).currentNode;
			}
			else if (!iter.hasNext())
			{
				return this.nodes.get(curr).currentNode;
			}
			
			prev = curr;
			index++;
		}
		// if node not found
		return null;
	}
	
	// creates a proxy for the node
	public IDhtNodes createProxyFor(DNode node) {
		return new InMemoryNodes(nodes.getOrDefault(node.nodeID, new DHServerInstance(node.name, true)));
	}
	
	// adds node
	@Override
	public void addNode(DNode newNode) {
		
		if (dhServiceInstance != null)
		{
			// emulates a network call to add node
			dhServiceInstance.addNode(newNode);
			nodes.put(newNode.nodeID, new DHServerInstance(newNode, false, true));
		}
		else
		{
			DNode existingNode = findNodeByName(newNode.getName());
			
			if (existingNode != null)
			{
				existingNode.getTable().moveKeysAboveTo(newNode.getTable(), newNode.getNodeID());
			}
			
			this.nodes.put(newNode.getNodeID(), new DHServerInstance(newNode, false, true));
		}
	}
	
	// adds node
	@Override
	public void addNode(String name) {
		
		DNode newNode = new DNode(name);
		
		addNode(newNode);
	}

	// removes a node
	@Override
	public void removeNode(DNode node) {
		DHashtable table = node.getTable();
		
		//removing the node
		nodes.remove(node.getNodeID());
		
		DNode pred = node.predecessor;
		node.predecessor.successor = node.successor;
		node.successor.predecessor = pred;
		
		node = this.findNodeByName(node.nodeID);
		
		// copy values to new node
		node.getTable().copyValuesTo(table);
		
	}
	
	//returns all the nodes
	@Override
	public List<DNode> getAllNodes() {
		List<DNode> allNodes = new LinkedList<DNode>();
		
		for (Integer key : nodes.keySet())
		{
			allNodes.add(nodes.get(key).currentNode);
		}
		
		return allNodes;
	}

	// gets the current node
	@Override
	public DNode getNode() {
		return this.dhServiceInstance.currentNode;
	}

	// updates the node
	@Override
	public void updateNode(DNode n) {
	   DNode patchNode = findNodeByName(n.getName());
	   if(patchNode != null)
		   n.getTable().copyValuesTo(patchNode.getTable());
	}
	
	@Override
	//gets all entries 
	public List<List<DHashEntry>> getAllEntries() {
		   List<DNode> nodes = new LinkedList<DNode>();
		   List<List<DHashEntry>> list = new ArrayList<List<DHashEntry>>();

		   for (int i = 0; i < nodes.size(); i++) {
			   list.add(nodes.get(i).getAllEntries());
		   }
		   return (list);
	}
	
	@Override
	//gets  entries for a specific node
	public List<DHashEntry> getAllEntriesforNode(String id) {
		DNode node = findNodeByName(id);
		List<DHashEntry> specificEntries = node.getAllEntries();
		return(specificEntries);
	}
	
	@Override
	//adds entry
	public void AddEntry(String text) {
		DNode node = findNodeByName(ChecksumDemoHashingFunction.hashValue(text));
		node.AssignKeys(DHashEntry.getHashEntry(text));
	}
	
	@Override
	//removing entry
	public void RemoveEntry(String text) {
		DNode node = findNodeByName(text);
		node.getTable().removeKeys(ChecksumDemoHashingFunction.hashValue(text));
	}

	// adding entry
	@Override
	public void AddEntry(DNode node) {

		DhtLogger.log.info("Adding node to inMemoryNetwork {}", node.nodeID);
		DhtWebService.InMemoryWebService.addNode(node);
	}
}
