package data;

import service.DHashEntry;
import service.DNode;

// interface for Entries methods (refer to WebServiceEntries.java for implementation)
public interface IDhtEntries {

	// inserts an entry
	void insert(String name);

	// gets entry based on name
	public DHashEntry get(String name);

	// gets entry based on key
	public DHashEntry get(Integer key);

	// get an entry based on node and name
	public DHashEntry get(DNode node, String name);

	/*
	 * Insert into node
	 */
	void insert(DNode node, String name);

	/*
	 * Remove an entry
	 */
	void remove(int entryId);

	/*
	 * Update an entry
	 */
	void update(int entryId, String entryValue);
}
