package service;

/*
 * This class handles the protocol for updating nodes upon a node leaving the network.
 * 
 */
public class DNodeLeave {

	public static void updatePredecessor(DNode thisNode) {
		thisNode.successor = null;

	}

	public static void updateSuccessor(DNode thisNode) {
		thisNode.predecessor = null;
	}

	public static void updateKeyList(DNode thisNode, DNode otherNode) {
		// handles base case of when the first two nodes on a network join together..

		for (int i : thisNode.keyList) {
			otherNode.keyList.add(i);
		}

		thisNode.keyList.clear();

	}

	public static void updateRoutingTable(DNode thisNode, DNode leavingNode) {

		for (int i = 0; i < thisNode.router.forwardToNode.length; i++) {

			if (thisNode.router.forwardToNode[i].nodeID == leavingNode.nodeID) {
				System.out.println("Test");
				thisNode.router.forwardToNode[i] = leavingNode.successor;
				thisNode.router.addresses[i] = leavingNode.successor.name;
			}
		}

	}
}