package service.tests;


import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;

import service.DNode;

public class DNodeJoinTest {
	private static DNode node1;
	private static DNode node2;
	

	@BeforeAll
	public static void setup() {
		node1 = new DNode("node_1");
		node2 = new DNode("node_2");
	}
	
	@Test
	public void testNode1PredeccessorIsNull() {
		assertEquals(node1.predecessor, null);
	}
	
	@Test
	public void testNode1SuccessorIsNull() {
		assertEquals(node1.successor, null);
	}
	
	@Test
	public void testNode2PredeccessorIsNull() {
		assertEquals(node2.predecessor, null);

	}
	
	@Test
	public void testNode2SuccessorIsNull() {	
		assertEquals(node2.successor, null);
	}
	
	@Test
	public void testNodesWithSameNameAreEqual() {
		DNode nodeA = new DNode("node");
		DNode nodeB = new DNode("node");
		System.out.println(nodeA.nodeID);
		System.out.println(nodeB.nodeID);
		assertEquals(nodeA, nodeB);
	}


	
	

	
}
