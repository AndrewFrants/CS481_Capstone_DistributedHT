package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import service.DNode;
import service.*;
class DNodeTest {
	private static DNode node1;
	private static DNode node2;
	private static DNode nodeA;
	private static DNode nodeB;
	
	@BeforeAll
	static void setup() {
		node1 = new DNode("node_1");
		node2 = new DNode("node_2");
		
		 nodeA = new DNode("node");
		nodeB = new DNode("node");
	}
	
	@BeforeEach
	public void beforeEach() {
		node1 = new DNode("node_1");
		node2 = new DNode("node_2");
	}
	
	@Test
	void testNodeAPredeccessorIsNull() {
		assertEquals(nodeA.predecessor, null);
	}
	
	@Test
	void testNodeASuccessorIsNull() {
		assertEquals(nodeA.successor, null);
	}
	
	@Test
	void testNodeBPredeccessorIsNull() {
		assertEquals(nodeB.predecessor, null);

	}
	
	@Test
	void testNodesWithSameNameAreEqual() {

		System.out.println(nodeA.nodeID);
		System.out.println(nodeB.nodeID);
		assertEquals(nodeA, nodeB);
	}
	
	@Test
	void testSendJoinRequestSameSize() {
		//if the node's are the same size it should return true,
		// which by default they are
		boolean accepted = node1.sendJoinRequest(node2);
		assertTrue(accepted);
	}
	
	@Test
	void testSendJoinRequestDifferentSize() {
		DNode nodeC = new DNode("nodeC");
		nodeC.size = 5;
		DNode nodeD = new DNode("nodeD");
		nodeD.size = 6;
	
		boolean accepted = nodeC.sendJoinRequest(nodeD);
		// if nodes are not the same size it should return false
		assertFalse(accepted);
	}
	
	@Test
	void testReceiveJoinRequestSameSize() {
		boolean accepted = node2.receiveJoinRequest(node1);
		assertTrue(accepted);
	}
	
	@Test
	void testReceiveJoinRequestDifferentSize() {
		DNode nodeC = new DNode("nodeC");
		nodeC.size = 5;
		DNode nodeD = new DNode("nodeD");
		nodeD.size = 6;
		
		boolean accepted = nodeD.receiveJoinRequest(nodeC);
		assertFalse(accepted);
	}
	
	@Test
	void testNode1IsNode2SuccessorAndPredecessor() {
		node1.sendJoinRequest(node2);
		assertEquals(node2.successor, node1);	
		assertEquals(node2.predecessor, node1);
}

	
	@Test
	void testNode2IsNode1SuccessorAndPredecessor() {
		node1.sendJoinRequest(node2);
	assertEquals(node1.successor, node2);
	assertEquals(node1.predecessor, node2);
}
	
	@Test
	void testJoiningKeyListSizeAfterJoin() {
		node1.sendJoinRequest(node2);
		int keyListSize = node1.keyList.size();
		
		int num = -1;
		if(node1.nodeID > node2.nodeID) {
		num = node2.nodeID - node1.nodeID;
		}
		
		else {			
			num = ((int)Math.pow(2, node1.size) - node2.nodeID) + node1.nodeID;			
		}
		
		assertEquals(num, keyListSize);
		
	}
	
	@Test
	void testReceingKeyListSizeAfterJoin() {
		node1.receiveJoinRequest(node2);
		int keyListSize = node1.keyList.size();
		
		int num = -1;
		if(node1.nodeID > node2.nodeID) {
		num = node2.nodeID - node1.nodeID;
		}
		
		else {			
			num = ((int)Math.pow(2, node1.size) - node2.nodeID) + node1.nodeID;			
		}
		
		assertEquals(num, keyListSize);
		
	}
	
	}

