package service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
 * This class is a node on a network
 */
public class DNode implements Comparable<DNode> {

	DHashtable table;
	public Integer nodeID;
	String name;
	Double angleVal;
	public DNode successor;
	public DNode predecessor;
	public RoutingTable router;
	public ArrayList<Integer> keyList;
	public int size; // size of network

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
		keyList = new ArrayList<Integer>();
		router = new RoutingTable(this);

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
		int highest_node_val = 8;
		int lowest_node_val = 0;
		double max_degree = 360;
		double min_degree = 0;
		angleVal = (double) nodeID / ((double) (highest_node_val - lowest_node_val)) * ((max_degree - min_degree))
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeID == null) ? 0 : nodeID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DNode other = (DNode) obj;
		if (nodeID == null) {
			if (other.nodeID != null)
				return false;
		} else if (!nodeID.equals(other.nodeID))
			return false;
		return true;
	}

	public boolean sendJoinRequest(DNode receivingNode) {
		// https request sent to frontend of receiving node
		if (!receivingNode.receiveJoinRequest(this))
			return false;

		else {
			DNodeJoin.updatePredecessor(this, receivingNode);
			DNodeJoin.updateSuccessor(this, receivingNode);

			// communicate back with other node to update it's successor/predecessor and their successor/predecessor 
		}
		
		return true;

	}
	
	public boolean findIfRequestingNodeIsInRange(DNode reqNode) {
		int reqID = reqNode.nodeID;
		int sucID = this.successor.nodeID;
		int predID = this.predecessor.nodeID;
		
		if(predID < sucID && reqID < sucID && reqID > predID) {
			return true;
		}
		
		else if( predID > sucID && (reqID > predID || reqID < sucID)) {
			return true;
		}
		
		else {
			return false;
		}
		
	}
	
	// This function is called by the node requesting to join,
	// after it finds it's "range" between the two nodes it sets its successor and
	// predecessor based on their location.
	
	public void updateRequestingNodeUponJoin(DNode recNode, DNode connectingNode) {
		int recID = recNode.nodeID;
		int conID = connectingNode.nodeID;
		
		if(conID > recID && nodeID > recID && nodeID < conID) {
			this.setPredecessor(connectingNode);
			this.setSuccessor(recNode);
		}
		else if(conID < recID && (nodeID > conID || nodeID < recID)) {
			this.setPredecessor(connectingNode);
			this.setSuccessor(recNode);
		}
		
		else {
			this.setPredecessor(recNode);
			this.setSuccessor(connectingNode);
		}
	}
	
	// This function updates the receiving node that found if the requesting node is 
	// within range.  Depending on the location of the receiving node it will either
	// update its predecessor or successor.
	
	public void updateReceivingNodeUponJoin(DNode reqNode, DNode connectingNode) {
		int reqID = reqNode.nodeID;
		int conID = connectingNode.nodeID;
		
		if(conID > nodeID && conID > reqID && nodeID < reqID) {
		this.setSuccessor(reqNode);	
		}	
		
		else if(conID < nodeID && (reqID > nodeID || reqID < conID)) {
			this.setSuccessor(reqNode);
		}
		else {
		this.setPredecessor(reqNode);	
		}
					
	}
	
	// This function updates the connecting node (which is either the successor or the
	// predecessor of the Receiving Node) upon a requesting node finding a receiving node
	// within it's range.  This will either update its successor or predecessor based
	// on the requesting node's position.
	
	public void updateConnectingNodeUponJoinRequest(DNode reqNode, DNode recNode) {
		int reqID = reqNode.nodeID;
		int recID = recNode.nodeID;
		
		if(nodeID > recID && nodeID > reqID && recID < reqID) {
		this.setPredecessor(reqNode);	
		}	
		
		else if(nodeID < recID && (reqID > recID|| reqID < nodeID)) {
			this.setPredecessor(reqNode);
		}
		else {
		this.setSuccessor(reqNode);	
		}
	}
	

	public boolean receiveJoinRequest(DNode incomingNode) {
		
		if (incomingNode.size == this.size && incomingNode.nodeID != this.nodeID) {
			// DNodeJoin.updateNodes(this, incomingNode);
			return true;
		}
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
