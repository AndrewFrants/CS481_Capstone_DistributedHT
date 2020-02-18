package data;

import java.util.List;

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
	
	
	/*
	 * Add a node
	 */
	void addNode(String name);
	
	/*
	 * Remove a node
	 */
	void removeNode(String name);

	/*
	 * Get all nodes
	 */
	List<DNode> getAllNodes();
}
