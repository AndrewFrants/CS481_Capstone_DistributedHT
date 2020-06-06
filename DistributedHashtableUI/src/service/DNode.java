package service;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 * This class is a node on a network
 * Also, defines the node structure
 */

public class DNode implements Comparable<DNode>, Serializable {

	//fields initialization
	private static final long serialVersionUID = 5418247889354114418L;
	public DHashtable table;
	public Integer nodeID;
	public String name;
	public Double angleVal;

	public int version;

	@JsonIgnoreProperties({"successor", "predecessor"})
	public DNode successor;
	@JsonIgnoreProperties({"successor", "predecessor"})
	public DNode predecessor;
	@JsonIgnore
	public RoutingTable router;
	public Range keyRange; 

	public int size; // size of networks
	
	/*
	 * Default constructor for deserialization
	 */
	public DNode() {
		table = new DHashtable();
		//router = new RoutingTable(this);

		DhtLogger.log.info("Initialized node (By Serializer)");
	}
	
	/*
	 * C'tor
	 */
	public DNode(final String nodeName) { // nodeName = 123.123.134.124:8888 or local localhost:8080
		this.name = nodeName;
		this.nodeID = DNode.GetComputerBasedHash(nodeName);
		table = new DHashtable();
		setAngle(nodeID);
		this.size = 16;
		successor = null;
		predecessor = null;
		router = new RoutingTable(this);
		keyRange = new Range();
		keyRange.setLowID(this.nodeID);
		keyRange.setHighID(this.nodeID - 1);
		
		DhtLogger.log.info("Initialized name: {} nodeId: {} angleVal: {}", nodeName, nodeID, this.angleVal);
	}

	//returns copy of node
	public DNode Clone(boolean cloneHashtable)
	{
		DNode clone = new DNode(this.name);
		
		if (this.successor != null)
		{
			clone.successor = this.successor.Clone(cloneHashtable);
		}

		if (this.predecessor != null)
		{
			clone.predecessor = this.predecessor.Clone(cloneHashtable);
		}

		if (cloneHashtable)
		{
			this.table.copyValuesTo(clone.table);
		}

		return clone;
	}

	//gets the node address
	@JsonIgnore
	public String getNodeAddress() {
		return "http://" + this.name;
	}
	
	//returns boolean for "url pointing"
	@JsonIgnore
	public Boolean isUrlPointingAt(final String url) {
		DhtLogger.log.info("Comparing {} to url: {}", this.getNodeAddress(), url);
		return url.toLowerCase().contains(this.getNodeAddress().toLowerCase());
	}
	
	/*
	 * Get the backing table
	 */
	public DHashtable getTable() {
		return table;
	}

	//gets the routing table
	@JsonIgnore
	public RoutingTable getRoutingTable() {
		return router;
	}

	/*
	 * Updating the table
	 */
	public void setTable(final DHashtable table) {
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
	public void setAngle(final int nodeID) {
		final int highest_node_val = 65536;
		final int lowest_node_val = 0;
		final double max_degree = 360;
		final double min_degree = 0;
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
	 * Set the successor
	 */
	public void setSuccessor(final DNode successor) {
		this.successor = successor;
	}

	/*
	 * Get the predecessor node
	 */
	public DNode getPredecessor() {
		return predecessor;
	}

	/*
	 * Set the predecessor
	 */
	public void setPredecessor(final DNode predecessor) {
		this.predecessor = predecessor;
	}

	/*
	 * Assign new key ownership to the node
	 */
	public void AssignKeys(final DHashEntry... hashEntries) {
		table.insert(hashEntries);
	}

	/*
	 * Assign new key ownership to the node
	 */
	public void UpdateEntries(final DHashEntry... hashEntries) {
		table.updateEntries(hashEntries);
	}

	/*
	 * This method returns the consistent hash for a machine based on its name. The
	 * name can be a computer name+ip, etc.
	 */
	public static int GetComputerBasedHash(final String computerId) {
		return ChecksumDemoHashingFunction.hashValue(computerId);
	}

	/*
	* Get entry from hashtable
	*/
	@JsonIgnore
	public DHashEntry getEntry(final int hash) {
	
		final DHashEntry entry = table.getLocalHT().getOrDefault(hash, null);
		return entry;
	}

	/*
	 * Gets all entries for visualization
	 */
	@JsonIgnore
	public List<DHashEntry> getAllEntries() {
		final List<DHashEntry> allEntries = new LinkedList<DHashEntry>();

		for (final Integer key : table.getLocalHT().keySet()) {
			allEntries.add(table.getLocalHT().get(key));
		}

		return allEntries;
	}

	/*
	 * CompareTo for sorting/ordering
	 */
	@Override
	public int compareTo(final DNode arg0) {
		if (arg0 == null || arg0.getNodeID().equals(this.getNodeID()))
			return 0;

		if (arg0.getNodeID() > this.getNodeID())
			return -1;

		return 1;
	}

	//hashCode method
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeID == null) ? 0 : nodeID.hashCode());
		return result;
	}

	//toString method
	@Override
	public String toString() {
		return this.nodeID.toString();
	}

	//equals method
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DNode other = (DNode) obj;
		if (nodeID == null) {
			if (other.nodeID != null)
				return false;
		} else if (!nodeID.equals(other.nodeID))
			return false;
		return true;
	}

	// this node has sent a join request to another node
	public boolean sendJoinRequest(final DNode receivingNode) {
		// https request sent to frontend of receiving node
		if (!receivingNode.receiveJoinRequest(this)) 
			return false;

		else {
			// final DNode connecting = receivingNode.findIfRequestingNodeIsInRange(this);
			DNode connecting = receivingNode.findIfIAmSuccessor(this);		
			// if the connecting node exists, the requesting node has found it's range placement
			// initialize it's successor/predecessor, keylist
			// change receiving and connecting node's id
			if(connecting != null) {
				updateRequestingNodeUponJoin(receivingNode, connecting);
				connecting.updateConnectingNodeUponJoinRequest(this, receivingNode);
				receivingNode.updateReceivingNodeUponJoin(this, connecting);
			
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
	public boolean receiveJoinRequest(final DNode incomingNode) {
		
		if (incomingNode.size == this.size && incomingNode.nodeID != this.nodeID) {
			 //DNodeJoin.updateNodes(this, incomingNode);
			return true;
		}
		return false;

	}
	
	//finds successor
	public DNode findIfIAmSuccessor(DNode reqNode) {
		int reqID = reqNode.nodeID;
		
		if(this.successor == null || this.predecessor == null) {
			return this;
		}	
		
		if(keyRange.contains(reqID)) {
			return this.successor;
		}		
		
		else {
			return null;
		}	
		
	}
	
	// This node find's if a requesting node is within this node(receivers)
	// range.  Meaning it is in the span between either this node
	// and its successor or predecessor.
	
	public DNode findIfRequestingNodeIsInRange(final DNode reqNode) {
		
		if(this.successor == null || this.predecessor == null) {
			return this;
		}
		
		if (this.successor.nodeID > reqNode.nodeID)
		{
			return this;
		}

		return null;


	}
	
	// This is called by the node requesting to join,
	// after it finds it's "range" between the two nodes it sets its successor and
	// predecessor based on their location.
	
	public void updateRequestingNodeUponJoin(final DNode recNode, final DNode connectingNode) {
		final int recID = recNode.nodeID;

		if(recNode.predecessor == null || recNode.successor == null) {
			this.setPredecessor(recNode);
			this.setSuccessor(recNode);
			DNodeJoin.updateKeyRange(this);
			DNodeJoin.updateKeyList(this, recNode);
			return;
		}
		final int conID = connectingNode.nodeID;
		
		if(conID > recID && nodeID > recID && nodeID < conID) {
			this.setSuccessor(connectingNode);
			this.setPredecessor(recNode);
			DNodeJoin.updateKeyRange(this);
			DNodeJoin.updateKeyList(this, recNode);// take keys from recNode (successor)
		}
		
	
		else if(conID < recID && (nodeID > conID && nodeID < recID)) {
			this.setPredecessor(connectingNode);
			this.setSuccessor(recNode);
			DNodeJoin.updateKeyRange(this);
			DNodeJoin.updateKeyList(this, connectingNode); //take keys from recNode (successor)
			
		}
		
	
		
		else if(recID < conID && (nodeID < conID && nodeID < recID)) {
			this.setPredecessor(connectingNode);
			this.setSuccessor(recNode);
			DNodeJoin.updateKeyRange(this);
			DNodeJoin.updateKeyList(this, connectingNode);//take keys from recNode (successor)
			
		}
		
		else {
			this.setPredecessor(recNode);
			this.setSuccessor(connectingNode);
			DNodeJoin.updateKeyRange(this);
			DNodeJoin.updateKeyList(this, recNode);// take keys from connectingNode (successor)
			
		}
	}

	//checks if the given node is in the range
	public boolean checkIfNodeIsInRange(final DNode loRange, final DNode hiRange) {

		boolean isInRange = false;
		boolean isNodeRangeOrdered = loRange.nodeID < hiRange.nodeID; // this can happen if the range spans 0
		
		if (isNodeRangeOrdered) // there is no flip over 0
		{
			if (this.nodeID > loRange.nodeID && this.nodeID < hiRange.nodeID)
			{
				isInRange = true;
			}
		}
		else //there range is including 0/circling
		{
			if (this.nodeID > loRange.nodeID && this.nodeID > hiRange.nodeID) // node is in the circle before the flip over
			{
				isInRange = true;
			}
			else if (this.nodeID < hiRange.nodeID && this.nodeID < loRange.nodeID)
			{
				isInRange = true;
			}
		}

		if (isInRange)
		{
			loRange.successor = this.Clone(false);
			this.predecessor = loRange.Clone(false);
			hiRange.predecessor = this.Clone(false);
			this.successor = hiRange.Clone(false);

			DhtLogger.log.info("loRange.successor={} loRange.predecessor={} this.successor={} this.predecessor={} hiRange.successor={} hiRange.predecessor={}", 
					loRange.successor.nodeID,
					loRange.predecessor.nodeID,
					this.successor.nodeID,
					this.predecessor.nodeID,
					hiRange.successor.nodeID,
					hiRange.predecessor.nodeID);
		}
		
		DhtLogger.log.info("Checked if node is in range. IsInRange={} loRange.nodeID={} this.nodeID={} hiRange.nodeID={} isNodeRangeOrdered={}", 
								isInRange,
								loRange.nodeID,
								this.nodeID,
								hiRange.nodeID,
								isNodeRangeOrdered);

		return isInRange;
	}

	
	// This node is the one being added
	// currNode is which node this code is executing on
	public boolean isNodeInRange(final DNode currNode) {

		AssertUtilities.ThrowIfNull(currNode, "preNode");
		AssertUtilities.ThrowIfNull(currNode.predecessor, "preNode.predecessor");
		AssertUtilities.ThrowIfNull(currNode.successor, "preNode.predecessor");
		
		if(currNode.successor == null) {
			return true;
		}
		else if (currNode.keyRange.contains(this.nodeID))
		{
			return true;
		}
		
		
		return false;
	}
	
	// Updates the receiving node that found if the requesting node is 
	// within range.  Depending on the location of the receiving node it will either
	// update its predecessor or successor.
	
	public void updateReceivingNodeUponJoin(final DNode reqNode, final DNode connectingNode) {
		final int reqID = reqNode.nodeID;
		final int conID = connectingNode.nodeID;
		
		if(conID > nodeID && conID > reqID && nodeID < reqID) {
			this.setSuccessor(reqNode);	
		}	
		
		else if(conID < nodeID && (reqID > nodeID || reqID < conID)) {
			this.setSuccessor(reqNode);
		}
		else {
			this.setPredecessor(reqNode);	
			DNodeJoin.updateKeyList(this, reqNode);// give keys to requesting node
		
		}
		DNodeJoin.updateKeyRange(this);	
					
	}
	
	// Updates the connecting node (which is either the successor or the
	// predecessor of the Receiving Node) upon a requesting node finding a receiving node
	// within it's range.  This will either update its successor or predecessor based
	// on the requesting node's position.
	
	public void updateConnectingNodeUponJoinRequest(final DNode reqNode, final DNode recNode) {
		final int reqID = reqNode.nodeID;
		final int recID = recNode.nodeID;
		
		if(nodeID > recID && nodeID > reqID && recID < reqID) {
		this.setPredecessor(reqNode);	
		DNodeJoin.updateKeyRange(this);
		DNodeJoin.updateKeyList(this, reqNode);// give keys to requesting node
		DNodeJoin.updateRoutingTable(this);
		
		}	
		
		else if(nodeID < recID && (reqID > recID|| reqID < nodeID)) {
			this.setPredecessor(reqNode);
			DNodeJoin.updateKeyRange(this);
			DNodeJoin.updateKeyList(this, reqNode);// give keys to requesting node
			DNodeJoin.updateRoutingTable(this);
			
		}
		else {
		this.setSuccessor(reqNode);	
		DNodeJoin.updateKeyRange(this);
		}
	}
	
	public void leaveNetwork() {					
	successor.updateSuccessorUponLeave(this.predecessor, this);
	predecessor.updatePredecessorUponLeave(this.successor, this);					
	}
	
	// update this node's successor upon leaving the network
	public void updateSuccessorUponLeave(final DNode predNode, final DNode leavingNode) {
		DNodeLeave.updateRoutingTable(this.successor, leavingNode);
		this.setPredecessor(predNode);
		
	}
	
	/// update this node's predecessor upon leaving the network
	public void updatePredecessorUponLeave(final DNode sucNode, final DNode leavingNode) {
		DNodeLeave.updateRoutingTable(this.predecessor, leavingNode);
		this.setSuccessor(sucNode);				
	}

	
	// Traverses the chord network to find the node with the key responsibility and inserts the file into that local node
	public void insert(final String file) {
		final int fileID = ChecksumDemoHashingFunction.hashValue(file);
		
		if(this.getTable().getLocalHT().containsKey(fileID)) {
			table.insert(DHashEntry.getHashEntry(file));
		}
		
		else {
			// will need to replace successor with using the routing table
			successor.insert(file);
			// forward request based on routing table
		}
		
	}
	
	// Traverses the chord network to find the node with the key responsibility and 
	public String get(final String title) {
		final int fileID = ChecksumDemoHashingFunction.hashValue(title);
		
		if(this.getTable().getLocalHT().containsKey(fileID)) {
			final DHashEntry file = table.getEntry(fileID);
			return file.value;
		}
		
		// uses recursion might not be best to retrieve a file
		// will need to replace successor with using the routing table
		else {
			return successor.get(title);
		}
		
	}

	//removes keys and returns boolean (True for success, False for failure)
	public Boolean remove(final Integer hash) {
	
		if(this.getTable().getLocalHT().containsKey(hash)) {
			table.removeKeys(hash);
			return true;
		}

		return false;
	}
	
	// Remove file from node if this node contains it, otherwise forward the request
	public Boolean remove(final String file) {
		final int fileID = ChecksumDemoHashingFunction.hashValue(file);
		
		if(this.getTable().getLocalHT().containsKey(fileID)) {
			table.removeKeys(fileID);
			return true;
		}
		
		return false;
	}
	
}
