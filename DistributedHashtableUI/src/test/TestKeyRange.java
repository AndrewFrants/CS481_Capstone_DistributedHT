package test;

import service.DNode;
import service.DNodeJoin;

public class TestKeyRange {
	
	
	public static void main(String[] args) {
	DNode nodeA = new DNode("34.567.3333");
		
	DNode nodeB = new DNode("56.678.113423");
	DNode nodeC = new DNode("45345345.345");
	System.out.println("Node A " + nodeA.nodeID);
	System.out.println("Node B " + nodeB.nodeID);	
	System.out.println("Node C " + nodeC.nodeID);
	nodeB.sendJoinRequest(nodeA);
	nodeC.sendJoinRequest(nodeB);
	System.out.println("node A successor " + nodeA.successor.nodeID);
	System.out.println("node A pred " + nodeA.predecessor.nodeID);
	System.out.println("node B successor " + nodeB.successor.nodeID);
	System.out.println("node B pred " + nodeB.predecessor.nodeID);
	System.out.println("node C successor " + nodeC.successor.nodeID);
	System.out.println("node C pred " + nodeC.predecessor.nodeID);
	nodeA.keyRange.printNodeRange();
	nodeB.keyRange.printNodeRange();
	nodeC.keyRange.printNodeRange();
	}
	

}
