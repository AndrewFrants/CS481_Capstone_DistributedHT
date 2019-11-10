/**
 * 
 */
package service;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;


/**
 * @author andreyf
 * This is the entry point into the service
 * it provides service level functionality
 */
public class DHService {

	Hashtable<Double, DNode> nodes;
	
	public DHService()
	{
		nodes = new Hashtable<Double, DNode>();
	}
	
	public void addNode(DNode newNode)
	{
		nodes.put(newNode.getHash(), newNode);
	}
	
	public void insertValue(String newValue)
	{
		double key = Hasher.hashValue(newValue);
		
	}
	
	public void findOwnerNode(String value)
	{
		/*
		 * TODO. This part can be/should be optimized to a BST
		 */
		double key = Hasher.hashValue(value);
		Enumeration<Double> keysenu = nodes.keys();
		
		while(keysenu.hasMoreElements())
		{
			Double nodeAngle = keysenu.nextElement();
			
			if (key >= nodeAngle)
			{
				// assign this value to a node
				nodes.get(nodeAngle).AssignKeys(DHashEntry.getHashEntry(value));
			}
		}
	}
	
	public DNode findNodeByName(String name)
	{
		Enumeration<Double> keysenu = nodes.keys();
		
		while(keysenu.hasMoreElements())
		{
			DNode node = nodes.get(keysenu.nextElement());
			
			if (name.equalsIgnoreCase(node.getName()))
			{
				return node;
			}
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
		
		DHashtable movedKeys = existingNode.getTable().moveKeysAboveTo(newNode.getHash());
		
		newNode.setTable(movedKeys);
	}
	
	/*
	 * This is a 4 node sample cluster for testing/demo'ing
	 */
	public static DHService createFiveNodeCluster()
	{
		DHService dhService = new DHService();
		
		String[] nodeNames = new String[] { "Andrew", 
											"Daniyal",
											"Palak",
											"Rachana" };
		
		for (String nodeName : nodeNames)
		{
			DNode node = new DNode(nodeName);
			
			dhService.addNode(node);
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
