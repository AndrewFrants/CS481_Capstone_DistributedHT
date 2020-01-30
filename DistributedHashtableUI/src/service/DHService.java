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


/**
 * This is the entry point into the service
 * it provides service level functionality
 */
public class DHService {

	HashMap<Integer, DNode> nodes;
	Integer networkBitSize;

	/*
	 * C'tor
	 */
	public DHService()
	{
		nodes = new HashMap<Integer, DNode>();

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
		DNode newNode = new DNode(name);
		
		DNode existingNode = findNodeByName(name);
		
		if (existingNode != null)
		{
			/*
			 * TODO. Following is a hack, it will not necessarily always return prev node.
			 */
			DNode prevNode = findNodeByName(existingNode.nodeID - 1);
			
			existingNode.getTable().moveKeysAboveTo(newNode.getTable(), newNode.getNodeID());
			//prevNode.getTable().moveKeysAboveTo(newNode.getTable(), newNode.getHash());
	
			this.nodes.put(newNode.getNodeID(), newNode);
		}
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
		Set<Integer> keysenu = nodes.keySet();
		
		for (Integer ikey : keysenu)
		{
			Integer nodeID = ikey;
			
			if (ikey >= nodeID)
			{
				// assign this value to a node
				nodes.get(nodeID).AssignKeys(DHashEntry.getHashEntry(value));
			}
		}
	}
	
	/*
	 * Find a node by it's name (this is useful for UI).
	 */
	public DNode findNodeByName(String name)
	{
		int hash = ChecksumDemoHashingFunction.hashValue(name);
		
		if (this.nodes.containsKey(hash))
			return this.nodes.get(hash);
		
		return findNodeByName(hash);
	}
	
	/*
	 * Find the node by a hash code
	 */
	public DNode findNodeByName(Integer hash)
	{
		Set<Integer> keysenu = nodes.keySet();
		List<Integer> numbersList = new ArrayList<Integer>(keysenu);
		
		Collections.sort(numbersList);
		
		Iterator<Integer> iter = numbersList.iterator();
		int prev = 0;
		int first = 0;
		
		while (iter.hasNext())
		{
			Integer curr = iter.next();

			if (first == 0)
				first = curr;
			
			if (prev != 0 && hash >= prev && hash <= curr)
			{
				return this.nodes.get(prev);
			}
			else if (!iter.hasNext())
			{
				return this.nodes.get(curr);
			}
			
			prev = curr;
		}
		
		return null;
	}
	
	/*
	 * Simulate removing the node
	 */
	public void removeNode(String name)
	{
		DNode node = findNodeByName(name);
		
		DHashtable table = node.getTable();
		
		nodes.remove(node.getNodeID());
		
		// find the next node
		node = findNodeByName(name);
		
		// copy values to new node
		node.getTable().copyValuesTo(table);
	}
	
	//removing entry
	public void removeEntry(String name) 
	{
		
	}
	/*
	 * This is a 4 node sample cluster for testing/demo'ing
	 */
	public static DHService createFiveNodeCluster()
	{
		DHService dhService = new DHService();
		
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
			DNode node = dhService.findNodeByName(hash);
			if (node != null)
			{
				System.out.println("Node: " + node.nodeID + " entry: " + hash);
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
		List<DNode> allEntries = new LinkedList<DNode>();
		
		for (Integer key : nodes.keySet())
		{
			allEntries.add(nodes.get(key));
		}
		
		return allEntries;
	}
}
