package data;

import java.util.List;

import service.DHashEntry;
import service.DNode;

/*
 * The nodes methods
 */
public interface IDhtEntries {

	/*
	 * Find the node by name
	 */
	void insert(String name);
	
    public DHashEntry get(String name);
    
    public DHashEntry get(DNode node, String name);
	/*
	 * Insert into node
	 */
	void insert(DNode node, String name);
	
	// removes an entry
	void remove(DNode node, String name);
	
	//updates an entry
	void update(DNode node, int entryId, String entryValue);
}
