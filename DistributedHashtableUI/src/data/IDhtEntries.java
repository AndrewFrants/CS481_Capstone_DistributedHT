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

	public DHashEntry get(Integer key);

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
