package service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*
 * This class is a node on a network
 */
public class DNode implements Comparable<DNode>, Serializable {

	DHashtable table; // we wont need this in actual implementation
	public HashMap<Integer, String> localTable;
	public Integer nodeID;
	String name;
	Double angleVal;
	public DNode successor;
	public DNode predecessor;
	@JsonIgnore
	public RoutingTable router;
	public ArrayList<Integer> keyList;
	public int size; // size of networks

	/*
	 * Default constructor for deserialization
	 */
	public DNode() {

	}
	
	/*
	 * C'tor
	 */
	public DNode(String nodeName) {
		this.name = nodeName;
		this.nodeID = DNode.GetComputerBasedHash(nodeName);
		table = new DHashtable();
		setAngle(nodeID);
		this.size = 16;
		successor = null;
		predecessor = null;
		keyList = new ArrayList<Integer>();
		router = new RoutingTable(this);
		localTable = new HashMap<Integer, String>();
	}

	/*
	 * Get the backing table
	 */
	public DHashtable getTable() {
		return table;
	}

	@JsonIgnore
	public RoutingTable getRoutingTable() {
		return router;
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
		int highest_node_val = 65536;
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

		for (Integer key : table.getLocalHT().keySet()) {
			allEntries.add(table.getLocalHT().get(key));
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

	// this node has sent a join request to another node
	public boolean sendJoinRequest(DNode receivingNode) {
		// https request sent to frontend of receiving node
		if (!receivingNode.receiveJoinRequest(this)) 
			return false;

		else {
		DNode connecting = receivingNode.findIfRequestingNodeIsInRange(this);
		
		// if the connecting node exists, the requesting node has found it's range placement
		// initialize it's successor/predecessor, keylist
		// change receiving and connecting node's id
		if(connecting != null) {
		updateRequestingNodeUponJoin(receivingNode, connecting);
		receivingNode.updateReceivingNodeUponJoin(this, connecting);
		connecting.updateConnectingNodeUponJoinRequest(this, receivingNode);
		}
		// forward request to the receiving node's successor
		// this will later need to be changed to go into routing table and find "closest" node, to 
		// to keep logn search time.
		else {
			sendJoinRequest(receivingNode.successor);
		}
		}
		
		return true;

	}
	
	// this node has received a join request from an incoming node
	public boolean receiveJoinRequest(DNode incomingNode) {
		
		if (incomingNode.size == this.size && incomingNode.nodeID != this.nodeID) {
			 DNodeJoin.updateNodes(this, incomingNode);
			return true;
		}
		return false;

	}
	
	// This node find's if a requesting node is within this node(receivers)
	// range.  Meaning it is in the span between either this node
	// and its successor or predecessor.
	
	// this method needs to be cleaned up, expressions can probably be reduced
	public DNode findIfRequestingNodeIsInRange(DNode reqNode) {
		int reqID = reqNode.nodeID;
		
		if(this.successor == null || this.predecessor == null) {
			return this;
		}
		
		
		int sucID = this.successor.nodeID;
		int predID = this.predecessor.nodeID;
		
		if(predID == sucID) {
			return this.successor;
		}
		else if(predID < sucID && reqID < nodeID && reqID > predID) {
			return this.predecessor;
		}	
		else if(predID < sucID && reqID > nodeID && reqID < sucID) {
			return this.successor;
		}		
		else if( predID > sucID && nodeID > predID && reqID > predID && reqID < nodeID) {
			return this.predecessor;
		}
		else if( predID > sucID && nodeID < predID && (reqID > predID || reqID < nodeID)) {
			return this.predecessor;
		}
		else if( predID > sucID && nodeID > predID && reqID > predID && (reqID > nodeID || reqID < sucID)) {
			return this.successor;
		}
		else if( predID > sucID && nodeID > predID && (reqID < sucID || reqID > nodeID)) {
			return this.successor;
		}
		else if(predID > sucID && nodeID < predID && reqID > nodeID && reqID < sucID)
		{
			return this.successor;
		}
		else {
			return null;
		}
		
	}
	
	// This is called by the node requesting to join,
	// after it finds it's "range" between the two nodes it sets its successor and
	// predecessor based on their location.
	
	public void updateRequestingNodeUponJoin(DNode recNode, DNode connectingNode) {
		int recID = recNode.nodeID;

		if(recNode.predecessor == null || recNode.successor == null) {
			this.setPredecessor(recNode);
			this.setSuccessor(recNode);
			DNodeJoin.updateKeyList(this, recNode);
			DNodeJoin.updateRoutingTable(this);
			return;
		}
		int conID = connectingNode.nodeID;
		
		if(conID > recID && nodeID > recID && nodeID < conID) {
			this.setSuccessor(connectingNode);
			this.setPredecessor(recNode);
			DNodeJoin.updateKeyList(this, recNode);// take keys from recNode (successor)
			DNodeJoin.updateRoutingTable(this);
		}
		
	
		else if(conID < recID && (nodeID > conID && nodeID < recID)) {
			this.setPredecessor(connectingNode);
			this.setSuccessor(recNode);
			DNodeJoin.updateKeyList(this, connectingNode);
			DNodeJoin.updateRoutingTable(this);
			//take keys from recNode (successor)
		}
		
	
		
		else if(recID < conID && (nodeID < conID && nodeID < recID)) {
			this.setPredecessor(connectingNode);
			this.setSuccessor(recNode);
			DNodeJoin.updateKeyList(this, connectingNode);//take keys from recNode (successor)
			DNodeJoin.updateRoutingTable(this);
			
		}
		
		else {
			this.setPredecessor(recNode);
			this.setSuccessor(connectingNode);
			DNodeJoin.updateKeyList(this, recNode);// take keys from connectingNode (successor)
			DNodeJoin.updateRoutingTable(this);
			
		}
	}
	
	// Updates the receiving node that found if the requesting node is 
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
		DNodeJoin.updateKeyList(this, reqNode);// give keys to requesting node
		DNodeJoin.updateRoutingTable(this);
		
		}
					
	}
	
	// Updates the connecting node (which is either the successor or the
	// predecessor of the Receiving Node) upon a requesting node finding a receiving node
	// within it's range.  This will either update its successor or predecessor based
	// on the requesting node's position.
	
	public void updateConnectingNodeUponJoinRequest(DNode reqNode, DNode recNode) {
		int reqID = reqNode.nodeID;
		int recID = recNode.nodeID;
		
		if(nodeID > recID && nodeID > reqID && recID < reqID) {
		this.setPredecessor(reqNode);	
		DNodeJoin.updateKeyList(this, reqNode);// give keys to requesting node
		DNodeJoin.updateRoutingTable(this);
		
		}	
		
		else if(nodeID < recID && (reqID > recID|| reqID < nodeID)) {
			this.setPredecessor(reqNode);
			DNodeJoin.updateKeyList(this, reqNode);// give keys to requesting node
			DNodeJoin.updateRoutingTable(this);
			
		}
		else {
		this.setSuccessor(reqNode);	
		}
	}
	
	// get's the key range of the node that has 2 entries
	// the first element being the start
	// the second element being the end
	@JsonIgnore
	public int[] getKeyRange() {

		int[] range = new int[2];
		
		range[0] = keyList.get(0);
		range[1] = keyList.get(keyList.size() - 1);
		
		return range;
	}
	// Traverses the chord network to find the node with the key responsibility and inserts the file into that local node
	public void insert(String file) {
		int fileID = ChecksumDemoHashingFunction.hashValue(file);
		
		if(keyList.contains(fileID)) {
			localTable.put(fileID, file);
		}
		
		else {
			// will need to replace successor with using the routing table
			successor.insert(file);
			// forward request based on routing table
		}
		
	}
	
	// Traverses the chord network to find the node with the key responsibility and 
	public String get(String title) {
		int fileID = ChecksumDemoHashingFunction.hashValue(title);
		
		if(keyList.contains(fileID)) {
			String file = localTable.get(fileID);
			return file;
		}
		
		// uses recursion might not be best to retrieve a file
		// will need to replace successor with using the routing table
		else {
			return successor.get(title);
		}
		
	}
	/*
	 * @Override public int compareTo(Object o) { if (o == null || o instanceof
	 * DNode || ((DNode)o).getHash() == this.getHash()) return 0;
	 * 
	 * if (arg0.getHash() > this.getHash()) return -1;
	 * 
	 * return 1; }
	 */
	
	// Remove file from node if this node contains it, otherwise forward the request
	public void remove(String file) {
		int fileID = ChecksumDemoHashingFunction.hashValue(file);
		
		if(keyList.contains(fileID)) {
			localTable.remove(fileID);
		}
		
		else {
			// will need to replace successor with using the routing table
			successor.remove(file);
			// forward request based on routing table
		}
	}
}
