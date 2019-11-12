/**
 * 
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
 * @author andreyf
 * This is the entry point into the service
 * it provides service level functionality
 */
public class DHService {

	HashMap<Double, DNode> nodes;
	
	public DHService()
	{
		nodes = new HashMap<Double, DNode>();
	}
	
	public void addNode(DNode newNode)
	{
		nodes.put(newNode.getHash(), newNode);
	}
	
	public void insertValue(String newValue)
	{
		double key = ChecksumDemoHashingFunction.hashValue(newValue);
		
	}
	
	public void findOwnerNode(String value)
	{
		/*
		 * TODO. This part can be/should be optimized to a BST
		 */
		double key = ChecksumDemoHashingFunction.hashValue(value);
		Set<Double> keysenu = nodes.keySet();
		
		for (Double ikey : keysenu)
		{
			Double nodeAngle = ikey;
			
			if (ikey >= nodeAngle)
			{
				// assign this value to a node
				nodes.get(nodeAngle).AssignKeys(DHashEntry.getHashEntry(value));
			}
		}
	}
	
	public DNode findNodeByName(String name)
	{
		double hash = ChecksumDemoHashingFunction.hashValue(name);
		
		if (this.nodes.containsKey(hash))
			return this.nodes.get(hash);
		
		return findNodeByName(hash);
	}
	
	public DNode findNodeByName(Double hash)
	{
		Set<Double> keysenu = nodes.keySet();
		List<Double> numbersList = new ArrayList<Double>(keysenu);
		
		Collections.sort(numbersList);
		System.out.println();
		System.out.println();
		System.out.println();
		
		Iterator<Double> iter = numbersList.iterator();
		Double prev = 0.0;
		Double first = 0.0;
		
		while (iter.hasNext())
		{
			Double curr = iter.next();

			if (first == 0.0)
				first = curr;
			
			System.out.println(curr);
			
			if (prev != 0.0 && hash >= prev && hash <= curr)
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
		
		nodes.remove(node.getHash());
		
		// find the next node
		node = findNodeByName(name);
		
		// copy values to new node
		node.getTable().copyValuesTo(table);
	}
	
	/*
	 * Simulate adding a node
	 */
	public void addNode(String name)
	{
		DNode newNode = new DNode(name);
		
		DNode existingNode = findNodeByName(name);
		
		/*
		 * TODO. Following is a hack, it will not necessarily always return prev node.
		 */
		DNode prevNode = findNodeByName(existingNode.hash - 0.001);
		
		existingNode.getTable().moveKeysAboveTo(newNode.getTable(), newNode.getHash());
		//prevNode.getTable().moveKeysAboveTo(newNode.getTable(), newNode.getHash());

		this.nodes.put(newNode.getHash(), newNode);
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
		
		String[] keyNames = new String[] { "CS400 - Monday 9-17.pdf\nClick to Download", 
											"CS400 - Monday 9-24.pdf\nClick to Download",
											"CS400 - Friday 10-14.pdf\nClick to Download",
											"CS400 - Wednesday 10-24.pdf\nClick to Download",
											"CS400 - Monday 11-04.pdf\nClick to Download",
											"CS400 - Monday 11-14.pdf\nClick to Download",
											"CS400 - Monday 11-24.pdf\nClick to Download",
											"CS400 - Friday 12-05.pdf\nClick to Download",
											"CS411 - Monday 9-17.pdf\nClick to Download", 
											"CS411 - Wednesday 9-24.pdf\nClick to Download",
											"CS411 - Monday 10-14.pdf\nClick to Download",
											"CS411 - Wednesday 10-24.pdf\nClick to Download",
											"CS411 - Monday 11-04.pdf\nClick to Download",
											"CS411 - Monday 11-14.pdf\nClick to Download",
											"CS411 - Monday 11-24.pdf\nClick to Download",
											"CS411 - Friday 12-05.pdf\nClick to Download",
											"CS420 - Monday 9-17.pdf\nClick to Download", 
											"CS420 - Monday 9-24.pdf\nClick to Download",
											"CS420 - Friday 10-14.pdf\nClick to Download",
											"CS420 - Monday 10-24.pdf\nClick to Download",
											"CS420 - Wednesday 11-04.pdf\nClick to Download",
											"CS420 - Monday 11-14.pdf\nClick to Download",
											"CS420 - Friday 11-24.pdf\nClick to Download",
											"CS420 - Monday 12-05.pdf\nClick to Download" 
											};
		int index = 0;
		
		for (String nodeName : nodeNames)
		{
			DNode node = new DNode(nodeName);
			
			dhService.addNode(node);
		}
		
		for (int i = 0; i < keyNames.length; i++)
		{
			Double hash = ChecksumDemoHashingFunction.hashValue(keyNames[i]);
			DNode node = dhService.findNodeByName(hash);
			System.out.println("Node: " + node.hash + " entry: " + hash);
			node.getTable().insert(keyNames[i]);
		}
		
		return dhService;
	}
	
	/*
	 * Returns all the nodes for visualization
	 */
	public List<DNode> getAllNodes()
	{
		List<DNode> allEntries = new LinkedList<DNode>();
		
		for (Double key : nodes.keySet())
		{
			allEntries.add(nodes.get(key));
		}
		
		return allEntries;
	}
}
