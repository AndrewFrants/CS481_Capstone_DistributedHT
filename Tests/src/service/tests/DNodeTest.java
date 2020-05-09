package service.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;



import java.util.List;
import org.junit.Assert;
//DO NOT USE import org.junit.Test; stupid java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import service.DNode;
import service.*;

public class DNodeTest {
	private DNode node1;
	private DNode node2;
	private DNode nodeA;
	private DNode nodeB;
	
	
	@BeforeEach
	public void setUp() throws Exception {
		node1 = new DNode("node_1");
		node2 = new DNode("node_2");
		
		nodeA = new DNode("node");
		nodeB = new DNode("node");
	}
	
	@Test
	public void testNodeAPredeccessorIsNull() {
		assertEquals(nodeA.predecessor, null);
	}
	
	@Test
	public void testNodeASuccessorIsNull() {
		assertEquals(nodeA.successor, null);
	}
	
	@Test
	public void testNodeBPredeccessorIsNull() {
		assertEquals(nodeB.predecessor, null);

	}
	
	@Test
	public void testNodeGetNodeAddress() {
		assertEquals(nodeB.getNodeAddress(), "http://" + nodeB.getName());
	}

	@Test
	public void testNodeNodeIsPointingAt() {
		assertEquals("http://" + nodeB.nodeID, true);
	}

	@Test
	public void testNodeSetSuccessor() {
		nodeB.setSuccessor(nodeA);
		assertEquals(nodeB.getSuccessor(), nodeA);
	}

	@Test
	public void testNodeSetPredecessor() {
		nodeB.setPredecessor(nodeA);
		assertEquals(nodeB.getPredecessor(), nodeA);
	}

	@Test
	public void testNodeAssignKeys() {
		String first = "first";
		String second = "second";

		DHashEntry firsthe = DHashEntry.getHashEntry(first);
		DHashEntry secondhe = DHashEntry.getHashEntry(second);

		nodeB.AssignKeys(firsthe, secondhe);
		DHashtable table = nodeB.getTable();
		nodeB.setTable(table);

		int firsthash = ChecksumDemoHashingFunction.hashValue(first);
		int secondhash = ChecksumDemoHashingFunction.hashValue(second);
		
		assertEquals(table.getEntry(firsthash), firsthe);
		assertEquals(table.getEntry(secondhash), secondhe);

		for (DHashEntry he : nodeB.getAllEntries())
		{
			assertTrue(0 == he.getKey().compareTo(firsthash) || 0 == he.getKey().compareTo(secondhash));
		}

	}

	@Test
	public void testNodeCompare() {
		assertNotEquals(nodeB, nodeA);
		assertEquals(nodeB, new DNode(nodeB.getName()));
		assertNotEquals(nodeB, null);
		assertNotEquals(nodeB, DHashEntry.getHashEntry("notequal"));
	}
	
	@Test
	public void testNodeHash() {
		assertEquals(nodeB.hashCode() != 0, "hashcode zero");
	}
	

	@Test
	public void testNodesWithSameNameAreEqual() {

		assertEquals(new DNode(nodeB.getName()), nodeB);
	}
	
	@Test
	public void testSendJoinRequestSameSize() {
		//if the node's are the same size it should return true,
		// which by default they are
		boolean accepted = node1.sendJoinRequest(node2);
		assertTrue(accepted);
	}
	
	@Test
	public void testSendJoinRequestDifferentSize() {
		DNode nodeC = new DNode("nodeC");
		nodeC.size = 5;
		DNode nodeD = new DNode("nodeD");
		nodeD.size = 6;
	
		boolean accepted = nodeC.sendJoinRequest(nodeD);
		// if nodes are not the same size it should return false
		assertFalse(accepted);
	}
	
	@Test
	public void testReceiveJoinRequestSameSize() {
		boolean accepted = node2.receiveJoinRequest(node1);
		assertTrue(accepted);
	}
	
	@Test
	public void testReceiveJoinRequestDifferentSize() {
		DNode nodeC = new DNode("nodeC");
		nodeC.size = 5;
		DNode nodeD = new DNode("nodeD");
		nodeD.size = 6;
		
		boolean accepted = nodeD.receiveJoinRequest(nodeC);
		assertFalse(accepted);
	}
	
	@Test
	public void testNode1IsNode2SuccessorAndPredecessor() {
		node1.sendJoinRequest(node2);
		assertEquals(node2.successor, node1);	
		assertEquals(node2.predecessor, node1);
}

	
	@Test
	public void testNode2IsNode1SuccessorAndPredecessor() {
		node1.sendJoinRequest(node2);
	assertEquals(node1.successor, node2);
	assertEquals(node1.predecessor, node2);
}
	
	@Test
	public void testJoiningKeyListSizeAfterJoin() {
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
	public void testReceingKeyListSizeAfterJoin() {
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

