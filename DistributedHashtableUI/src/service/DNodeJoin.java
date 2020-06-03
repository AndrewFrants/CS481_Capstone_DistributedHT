package service;

// This class handles changes in the key responsibility range for node's and updating their routing tables upon join requests
public abstract class DNodeJoin {

	
	
	//updates the range of keys for the node
	public static void updateKeyRange(DNode thisNode) {
		
		if(thisNode.successor != null) {
			thisNode.keyRange.setLowID(thisNode.nodeID);
		if(thisNode.successor.nodeID != 0) {
			
		
		thisNode.keyRange.setHighID(thisNode.successor.nodeID - 1);
		}
		
		// if the suc node's id is 0, set the range of this node to highest node value (size - 1)
		else {
			System.out.println(thisNode.size);
			thisNode.keyRange.setHighID((int)(Math.pow(2, thisNode.size) - 1));
		}
		}
		else {
			thisNode.keyRange.setLowID(thisNode.nodeID);
			thisNode.keyRange.setHighID(thisNode.nodeID - 1);
		}
	}
	

	public static void updateKeyList(DNode thisNode, DNode otherNode) {
		// handles base case of when the first two nodes on a network join together..
	/*
		thisNode.keyList.clear();

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
		*/
	}

	
	public static void updateRoutingTable(DNode thisNode) {

		if (thisNode.successor == null) {
			for (int i = 0; i < thisNode.router.size; i++) {
				thisNode.router.forwardToNode[i] = thisNode;
				thisNode.router.addresses[i] = thisNode.name;
			}
		} else {
			RoutingTable successorTable = thisNode.successor.router;

			DNode[] sucNodeList = successorTable.getForwardNodeList();
			thisNode.router.forwardToNode[0] = thisNode.successor; // sets the first entry in the routing table to the
			thisNode.router.addresses[0] = thisNode.successor.name; // successor
			/*
			 * !!Make sure to update successor's predecessor successor's node to thisNode!!
			 */

			for (int i = 1; i < thisNode.router.n.length; i++) {
				DNode closestNode = thisNode.successor;
				int shortestDist = Integer.MAX_VALUE;
				int curDist = CalculateDistance.calculateClockWiseDistance(thisNode.router.n[i],
						thisNode.router.forwardToNode[i].nodeID, thisNode.size);
				int sucDist = CalculateDistance.calculateClockWiseDistance(thisNode.router.n[i],
						thisNode.successor.nodeID, thisNode.size);

				for (int j = 0; j < sucNodeList.length; j++) {

					// calculates the clockwise distance of the successor's list the beginning range
					// for n
					int sucListDist = CalculateDistance.calculateClockWiseDistance(thisNode.router.n[i],
							sucNodeList[j].nodeID, thisNode.size);

					// calculates the clockwise distance of the the node's list the beginning range
					// for n

					if (sucDist < curDist && sucDist < shortestDist) {

						shortestDist = sucDist;
						closestNode = thisNode.successor;
					} else if (sucListDist < curDist && sucListDist < shortestDist) {

						shortestDist = sucListDist;
						closestNode = sucNodeList[j];
					} else if (sucListDist < shortestDist) {
						shortestDist = curDist;
						closestNode = thisNode.router.forwardToNode[i];
					}

				}

				if (closestNode.nodeID.equals(thisNode.nodeID)) {
					closestNode = thisNode.successor;
				}

				thisNode.router.forwardToNode[i] = closestNode;
				thisNode.router.addresses[i] = closestNode.name;

			}
		}

	}

	public static void updatePredecessor(DNode thisNode, DNode otherNode) {
		if (thisNode.predecessor == null) {
			thisNode.predecessor = otherNode;
		}

		else if (thisNode.nodeID > otherNode.nodeID || thisNode.nodeID < otherNode.successor.nodeID) {
			otherNode.successor = thisNode.successor;
			otherNode.predecessor = thisNode;

		}

		else {
			otherNode.successor = thisNode;
			otherNode.predecessor = thisNode.predecessor;
		}

	}

	public static void updateSuccessor(DNode thisNode, DNode otherNode) {
		if (thisNode.successor == null) {
			thisNode.successor = otherNode;
		}
	}

	
	public static void updateAdjacentNodes(DNode currNode, DNode reqNode) {
		if(currNode == null || !currNode.keyRange.contains(reqNode.nodeID)) {
			return;
		}
		else if(currNode.successor == null) {
			reqNode.successor = currNode;
			reqNode.predecessor = currNode;
			currNode.successor = reqNode;
			currNode.predecessor = reqNode;
			return;
		}
		else {
		reqNode.successor = currNode;
		reqNode.predecessor = currNode.predecessor;
		currNode.predecessor.successor = reqNode;
		currNode.predecessor = reqNode;
		}
	}
}

