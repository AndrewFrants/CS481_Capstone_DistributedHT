package service2;

public class RoutingEntry {
	private Range forwardRange;  // range of nodes this entry can forward to
	private int idealForwardID; // ideal forward of the entry (if the node exists).
	private int forwardID; // ID of node to forward requests to
	private String forwardAddress; // Address of node to forward requests to
	
	
	public RoutingEntry(int index, int startingID, int forwardID, String forwardAddress) {
		
		this.forwardID = forwardID;
		this.forwardAddress = forwardAddress;
		idealForwardID = startingID + 2^index%16;
		forwardRange = new Range();
		forwardRange.setLowID(startingID);		
		forwardRange.setHighID(idealForwardID + 2^(index+1) - 1);		
	}

	public int getForwardID() {
		return forwardID;
	}

	public String getForwardAddress() {
		return forwardAddress;
	}

	// changes the the node/address to forward requests to
	public void changeForwardNode(int forwardID, String forwardAddress) {
		this.forwardID = forwardID;
		this.forwardAddress = forwardAddress;
	}
	
	public Range geForwardRange() {
		return forwardRange;
	}
	
	public int getIdealForwardID() {
		return idealForwardID;
	}
	
	// checks if this entry can forward the node
	public boolean contains(int nodeID) {
		if (forwardRange.contains(nodeID)) {
			return true;
		}
		
		return false;
	}
	
	// prints contents of entry
	public void printRoutingEntry() {
		System.out.println("Forward ID: " + forwardID + " Forward Address: " + forwardAddress + " Range of Entry: " + forwardRange);
	}

	
}
