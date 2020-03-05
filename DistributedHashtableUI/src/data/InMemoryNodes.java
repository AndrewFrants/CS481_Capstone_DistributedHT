/**
 * 
 */
package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import service.ChecksumDemoHashingFunction;
import service.DHashEntry;
import service.DHashtable;
import service.DNode;

/**
 * @author Andrew Frantsuzov
 * This class simulates nodes functionality inMemory Nodes Collection
 */
public class InMemoryNodes implements IDhtNodes {

	HashMap<Integer, DNode> nodes;
	
	public InMemoryNodes() {
		nodes = new HashMap<Integer, DNode>();
	}
	
	@Override
	public DNode findNodeByName(String name) {
		int hash = ChecksumDemoHashingFunction.hashValue(name);
		
		if (this.nodes.containsKey(hash))
			return this.nodes.get(hash);
		
		return findNodeByName(hash);
	}

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
				return this.nodes.get(prev);
			}
			else if (!iter.hasNext())
			{
				return this.nodes.get(curr);
			}
			
			prev = curr;
			index++;
		}
		
		return null;
	}
	
	@Override
	public void addNode(String name) {
		
		DNode newNode = new DNode(name);
		
		DNode existingNode = findNodeByName(name);
		
		if (existingNode != null)
		{
			/*
			 * TODO. Following is a hack, it will not necessarily always return prev node.
			 */
			//DNode prevNode = findNodeByName(existingNode.nodeID - 1);
			
			existingNode.getTable().moveKeysAboveTo(newNode.getTable(), newNode.getNodeID());
			//prevNode.getTable().moveKeysAboveTo(newNode.getTable(), newNode.getHash());
		}
		
		this.nodes.put(newNode.getNodeID(), newNode);
		
	}

	@Override
	public void removeNode(String name) {
		DNode node = findNodeByName(name);
		
		DHashtable table = node.getTable();
		
		nodes.remove(node.getNodeID());
		
		// find the next node
		node = findNodeByName(name);
		
		// copy values to new node
		node.getTable().copyValuesTo(table);
		
	}
	
	@Override
	public List<DNode> getAllNodes() {
		List<DNode> allEntries = new LinkedList<DNode>();
		
		for (Integer key : nodes.keySet())
		{
			allEntries.add(nodes.get(key));
		}
		
		return allEntries;
	}

	@Override
	public void updateNode(DNode n) {
	   DNode patchNode = findNodeByName(n.getName());
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
		DNode node = findNodeByName(text);
		node.AssignKeys(DHashEntry.getHashEntry(text));
	}
	
	@Override
	//removing entry
	public void RemoveEntry(String text) {
		DNode node = findNodeByName(text);
		node.getTable().removeKeys(ChecksumDemoHashingFunction.hashValue(text));
	}

}
