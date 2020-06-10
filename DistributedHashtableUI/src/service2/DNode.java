package service2;

import java.util.HashMap;

public class DNode {
	
	private String name;
	private int nodeID;
	private int successorID;
	private int predecessorID;
	private RoutingTable router;
	private HashMap<String, String> localTable;
	private Range keyRange;
	private int size;
	// constructor
	public DNode(String fileName) {
		this.name = name;
		this.size = 4;
		HashFunction hashedVal = new HashFunction();
		this.nodeID = hashedVal.hash(name, size);
		this.successorID = -1;
		this.predecessorID = -1;
		
		this.localTable = new HashMap<String, String>();
		
		this.router = new RoutingTable(size, nodeID, name);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSuccessorID() {
		return successorID;
	}

	public void setSuccessorID(int successorID) {
		this.successorID = successorID;
	}

	public int getPredecessorID() {
		return predecessorID;
	}

	public void setPredecessorID(int predecessorID) {
		this.predecessorID = predecessorID;
	}

	public RoutingTable getRouter() {
		return router;
	}

	public void setRouter(RoutingTable router) {
		this.router = router;
	}

	public HashMap<String, String> getLocalTable() {
		return localTable;
	}

	public void setLocalTable(HashMap<String, String> localTable) {
		this.localTable = localTable;
	}

	public Range getKeyRange() {
		return keyRange;
	}

	public void setKeyRange(Range keyRange) {
		this.keyRange = keyRange;
	}
	
	
}
