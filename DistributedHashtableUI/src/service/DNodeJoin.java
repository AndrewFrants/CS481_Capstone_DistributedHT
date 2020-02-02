package service;


// This class handles changes in the key responsibility range for node's and updating their routing tables upon join requests
public abstract class DNodeJoin {
	
	
	public static void updateNodes(DNode thisNode, DNode otherNode) {
		updateKeyList(thisNode, otherNode);
		updateSuccessor(thisNode, otherNode);
		updatePredecessor(thisNode, otherNode);
		updateRoutingTable(thisNode, otherNode);
	}
	

	public static void updateKeyList(DNode thisNode, DNode otherNode) {	
		// handles base case of when the first two nodes on a network join together..
		
		if (thisNode.predecessor == null) {


			if (thisNode.nodeID < otherNode.nodeID) {
				for (int i = otherNode.nodeID + 1; i < Math.pow(2, thisNode.size); i++) {
					thisNode.keyList.add(i);
				}

				for (int i = 0; i <= thisNode.nodeID; i++) {
					thisNode.keyList.add(i);
				}

			} else {
				for (int i = otherNode.nodeID + 1; i <= thisNode.nodeID; i++) {
					thisNode.keyList.add(i);
				}
			}
		}

	}
	
	public static void updateRoutingTable(DNode thisNode, DNode otherNode) {
		for(int i = 0; i < thisNode.size; i++) {
			thisNode.router.forwardToNode[i] = otherNode.nodeID;
			thisNode.router.addresses[i] = otherNode.name;
		}
		
	}
	public static void updatePredecessor(DNode thisNode, DNode otherNode) {
		if(thisNode.predecessor == null) {
			thisNode.predecessor = otherNode;
		}
		
	}
public static void updateSuccessor(DNode thisNode, DNode otherNode) {
	if(thisNode.successor == null) {
		thisNode.successor = otherNode;
	}
}


}


