package data;

import java.util.List;

import service.DHashEntry;
import service.DNode;

/*
 * The nodes methods
 */
public interface IDhtNodes {

	/*
	 * Find the node by name
	 */
	DNode findNodeByName(String name);
	
	/*
	 * Find node by hash id
	 */
	DNode findNodeByName(Integer hash);
	DNode findNodeByName(DNode node, Integer hash);
	
	/*
	 * Add a node
	 */
	void addNode(String name);
	
	void addNode(DNode node);
	
	
	// removes a node
	void removeNode(String name);

	/*
	 * Get all nodes
	 */
	List<DNode> getAllNodes();
	
	/*
	 * Update the node
	 */
	void updateNode(DNode n);	
	
	
	// for entries
	
	//gets all entries 
	List <List <DHashEntry>> getAllEntries();
	
	//gets entries for a specific node
	List<DHashEntry> getAllEntriesforNode(String id);
	
	//adds entry
	void AddEntry(String text);
	
	void AddEntry(DNode node);
	//removing entry
	void RemoveEntry(String text);

	
}
