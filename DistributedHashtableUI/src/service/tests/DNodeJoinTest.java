package service.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import service.DNode;

class DNodeJoinTest {
	private static DNode node1;
	private static DNode node2;
	

	@BeforeAll
	static void setup() {
		node1 = new DNode("node_1");
		node2 = new DNode("node_2");
	}
	
	@Test
	void testNode1PredeccessorIsNull() {
		assertEquals(node1.predecessor, null);
	}
	
	@Test
	void testNode1SuccessorIsNull() {
		assertEquals(node1.successor, null);
	}
	
	@Test
	void testNode2PredeccessorIsNull() {
		assertEquals(node2.predecessor, null);

	}
	
	@Test
	void testNode2SuccessorIsNull() {	
		assertEquals(node2.successor, null);
	}
	
	@Test
	void testNodesWithSameNameAreEqual() {
		DNode nodeA = new DNode("node");
		DNode nodeB = new DNode("node");
		System.out.println(nodeA.nodeID);
		System.out.println(nodeB.nodeID);
		assertEquals(nodeA, nodeB);
	}


	
	

	
}
