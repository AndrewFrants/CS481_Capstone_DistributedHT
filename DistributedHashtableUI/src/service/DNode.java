package service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
 * This class is a node on a network
 */
public class DNode implements Comparable<DNode> {

	DHashtable table;
	Integer nodeID;
	String name;
	Double angleVal;
	DNode successor;
	DNode predecessor;
	public RoutingTable router;
	ArrayList<Integer> keyResponsability;
	int size;  // size of network 

	/*
	 * C'tor
	 */
	public DNode(String nodeName) {
		this.name = nodeName;
		this.nodeID = DNode.GetComputerBasedHash(nodeName);
		table = new DHashtable();
		setAngle(nodeID);
		this.size = 3;
		successor = null;
		predecessor = null;
	
		setKeyResponsability();
		router = new RoutingTable(this);

	}
	
	public void setKeyResponsability() {
		keyResponsability = new ArrayList<Integer>();
		for(int i = 0; i < Math.pow(2, size); i++) {
			keyResponsability.add(i);
		}
	}

	/*
	 * Get the backing table
	 */
	public DHashtable getTable() {
		return table;
	}

	/*
	 * Updating the table
	 */
	public void setTable(DHashtable table) {
		this.table = table;
	}

	/*
	 * Get the name of the node
	 */
	public String getName() {
		return name;
	}

	/*
	 * Get the node Id
	 */
	public Integer getNodeID() {
		return nodeID;
	}

	/*
	 * Set the node angle
	 */
	public void setAngle(int nodeID) {
		int highest_node_val = 512;
		int lowest_node_val = 0;
		double max_degree = 360;
		double min_degree = 0;
		angleVal = (double) nodeID/((double)(highest_node_val - lowest_node_val))*((max_degree - min_degree)) 
				+ min_degree;

	}

	/*
	 * Get the node angle
	 */
	public Double getAngle() {
		return angleVal;
	}

	/* 
	 * Get the node successor
	 */
	public DNode getSuccessor() {
		return successor;
	}

	/*
	 * Set the sucessfor
	 */
	public void setSuccessor(DNode successor) {
		this.successor = successor;
	}

	/*
	 * Get the predecessor node
	 */
	public DNode getPredecessor() {
		return predecessor;
	}

	/*
	 * Set the predecssecor
	 */
	public void setPredecessor(DNode predecessor) {
		this.predecessor = predecessor;
	}

	/*
	 * Assign new key ownership to the node
	 */
	public void AssignKeys(DHashEntry... hashEntries) {
		table.insert(hashEntries);
	}

	/*
	 * This method returns the consistent hash for a machine based on its name. The
	 * name can be a computer name+ip, etc.
	 */
	public static int GetComputerBasedHash(String computerId) {
		return ChecksumDemoHashingFunction.hashValue(computerId);
	}

	/*
	 * Gets all entries for visualization
	 */
	public List<DHashEntry> getAllEntries() {
		List<DHashEntry> allEntries = new LinkedList<DHashEntry>();

		for (Integer key : table.getHT().keySet()) {
			allEntries.add(table.getHT().get(key));
		}

		return allEntries;
	}

	/*
	 * CompareTo for sorting/ordering
	 */
	@Override
	public int compareTo(DNode arg0) {
		if (arg0 == null || arg0.getNodeID() == this.getNodeID())
			return 0;

		if (arg0.getNodeID() > this.getNodeID())
			return -1;

		return 1;
	}
	
	public boolean sendJoinRequest(DNode receivingNode) {
		// https request sent to frontend of receiving node
		if(!receivingNode.receiveJoinRequest(this))
			return false;
			if(receivingNode.predecessor == null) {
				System.out.println("take all inbetween");
			}
			else {
				System.out.println("more complex work too follow");
			}
			return true;
	
	}
	
	public boolean receiveJoinRequest(DNode incomingNode) {
		if (incomingNode.size == this.size) 
		return true;
	return false;
		
	}

	/*
	 * @Override public int compareTo(Object o) { if (o == null || o instanceof
	 * DNode || ((DNode)o).getHash() == this.getHash()) return 0;
	 * 
	 * if (arg0.getHash() > this.getHash()) return -1;
	 * 
	 * return 1; }
	 */
}
