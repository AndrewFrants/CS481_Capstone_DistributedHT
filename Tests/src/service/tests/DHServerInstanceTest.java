package service.tests;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import service.*;

public class DHServerInstanceTest {
	DHServerInstance instance;

	
	@BeforeAll
	public void setUp() throws Exception {
		instance = new DHServerInstance("instance", false);
	}

	@Test
	public void testAddFirstNode() {
		instance = new DHServerInstance("instance", false);
		assertNull(instance.currentNode.successor);
		assertNull(instance.currentNode.predecessor);
	}
	
	@Test
	public void testAddSecondNode() {
		instance = new DHServerInstance("instance", false, false);
		DHServerInstance incoming = new DHServerInstance("instance2", true, false);

		DhtLogger.log.info("First Node: {} Second Node: {}", instance.currentNode.nodeID, incoming.currentNode.nodeID);
		instance.dhtNodes.addNode(incoming.currentNode);
		
		assertEquals(incoming.currentNode.nodeID, instance.currentNode.successor.nodeID);
		assertEquals(incoming.currentNode.nodeID, instance.currentNode.predecessor.nodeID);
		assertEquals(instance.currentNode.successor.nodeID, incoming.currentNode.nodeID);
		assertEquals(instance.currentNode.predecessor.nodeID, incoming.currentNode.nodeID);
	}
	
	@Test
	public void testThreeNodes() {
		instance = new DHServerInstance("instance", false, false);
		DHServerInstance second = new DHServerInstance("instance2", true, false);

		instance.dhtNodes.addNode(second.currentNode);
		
		assertEquals(second.currentNode.nodeID, instance.currentNode.successor.nodeID);
		assertEquals(second.currentNode.nodeID, instance.currentNode.predecessor.nodeID);
		assertEquals(instance.currentNode.successor.nodeID, second.currentNode.nodeID);
		assertEquals(instance.currentNode.predecessor.nodeID, second.currentNode.nodeID);
		

		DHServerInstance third = new DHServerInstance("instance3", true, false);
		DhtLogger.log.info("First Node: {} Second Node: {} Third Node: {}", instance.currentNode.nodeID, second.currentNode.nodeID, third.currentNode.nodeID);
		instance.dhtNodes.addNode(third.currentNode);
		
		// Note First 38353 Second 30853 Third 38403
		assertNodeLinks(second.currentNode, instance.currentNode, third.currentNode, true);
	}
	
	@Test
	public void testFourNodes() {
		instance = new DHServerInstance("instance", false, false);
		DHServerInstance second = new DHServerInstance("instance2", true, false);

		instance.dhtNodes.addNode(second.currentNode);
		
		assertEquals(second.currentNode.nodeID, instance.currentNode.successor.nodeID);
		assertEquals(second.currentNode.nodeID, instance.currentNode.predecessor.nodeID);
		assertEquals(instance.currentNode.successor.nodeID, second.currentNode.nodeID);
		assertEquals(instance.currentNode.predecessor.nodeID, second.currentNode.nodeID);

		DHServerInstance third = new DHServerInstance("instance3", true, false);
		DhtLogger.log.info("First Node: {} Second Node: {} Third Node: {}", instance.currentNode.nodeID, second.currentNode.nodeID, third.currentNode.nodeID);
		instance.dhtNodes.addNode(third.currentNode);
		
		// Note First 38353 Second 30853 Third 38403
		assertNodeLinks(second.currentNode, instance.currentNode, third.currentNode, true);
		
		DHServerInstance fourth = new DHServerInstance("instance4", true, false);
		DhtLogger.log.info("First Node: {} Second Node: {} Third Node: {} Fourth Node: {}", instance.currentNode.nodeID, second.currentNode.nodeID, third.currentNode.nodeID, fourth.currentNode.nodeID);
		instance.dhtNodes.addNode(fourth.currentNode);
		
		// Note First 38353 Second 30853 Third 38403 Fourth 46257
		assertNodeLinks(second.currentNode, instance.currentNode, third.currentNode);
		assertNodeLinks(instance.currentNode, third.currentNode, fourth.currentNode);
		assertNodeLinks(fourth.currentNode, second.currentNode, instance.currentNode);
	}
	
	@Test
	public void testRemoveNodes() {
		instance = new DHServerInstance("instance", false, false);
		DHServerInstance second = new DHServerInstance("instance2", true, false);

		instance.dhtNodes.addNode(second.currentNode);
		
		assertEquals(second.currentNode.nodeID, instance.currentNode.successor.nodeID);
		assertEquals(second.currentNode.nodeID, instance.currentNode.predecessor.nodeID);
		assertEquals(instance.currentNode.successor.nodeID, second.currentNode.nodeID);
		assertEquals(instance.currentNode.predecessor.nodeID, second.currentNode.nodeID);

		DHServerInstance third = new DHServerInstance("instance3", true, false);
		DhtLogger.log.info("First Node: {} Second Node: {} Third Node: {}", instance.currentNode.nodeID, second.currentNode.nodeID, third.currentNode.nodeID);
		instance.dhtNodes.addNode(third.currentNode);
		
		// Note First 38353 Second 30853 Third 38403
		assertNodeLinks(second.currentNode, instance.currentNode, third.currentNode, true);
		
		DHServerInstance fourth = new DHServerInstance("instance4", true, false);
		DhtLogger.log.info("First Node: {} Second Node: {} Third Node: {} Fourth Node: {}", instance.currentNode.nodeID, second.currentNode.nodeID, third.currentNode.nodeID, fourth.currentNode.nodeID);
		instance.dhtNodes.addNode(fourth.currentNode);
		
		// Note First 38353 Second 30853 Third 38403 Fourth 46257
		assertNodeLinks(second.currentNode, instance.currentNode, third.currentNode);
		assertNodeLinks(instance.currentNode, third.currentNode, fourth.currentNode);
		assertNodeLinks(fourth.currentNode, second.currentNode, instance.currentNode);
		
		String testEntry = "Test Entry";
		Integer hashEntry = ChecksumDemoHashingFunction.hashValue(testEntry);
		
		fourth.addEntry(testEntry);
		instance.dhtNodes.removeNode(fourth.currentNode);
		
		// Back to three nodes
		// Note First 38353 Second 30853 Third 38403
		assertNodeLinks(second.currentNode, instance.currentNode, third.currentNode, true);
		
		// key gets copied to the new owner
		assertTrue(third.currentNode.table.getLocalHT().containsKey(hashEntry));
		assertFalse(second.currentNode.table.getLocalHT().containsKey(hashEntry));
		assertFalse(instance.currentNode.table.getLocalHT().containsKey(hashEntry));
		
		instance.dhtNodes.removeNode(third.currentNode);
		
		// back to 2 node system
		assertEquals(second.currentNode.nodeID, instance.currentNode.successor.nodeID);
		assertEquals(second.currentNode.nodeID, instance.currentNode.predecessor.nodeID);
		assertEquals(instance.currentNode.successor.nodeID, second.currentNode.nodeID);
		assertEquals(instance.currentNode.predecessor.nodeID, second.currentNode.nodeID);
		
		// key gets copied to the new owner
		assertTrue(second.currentNode.table.getLocalHT().containsKey(hashEntry));
		assertFalse(instance.currentNode.table.getLocalHT().containsKey(hashEntry));
		
		/* TODO removing second node is broken
		instance.dhtNodes.removeNode(second.currentNode);
		
		// back to single node system
		assertEquals(instance.currentNode.successor, null);
		assertEquals(instance.currentNode.predecessor, null);
		
		// key gets copied to the new owner
		assertTrue(instance.currentNode.table.getLocalHT().containsKey(hashEntry));
		*/
	}
	
	private static void assertNodeLinks(DNode prev, DNode mid, DNode succ)
	{
		assertNodeLinks(prev, mid, succ, false);
	}
	
	private static void assertNodeLinks(DNode prev, DNode mid, DNode succ, boolean isThreeNodes)
	{
		
		if (prev.nodeID < succ.nodeID)
		{
			// all 3 nodes are under each other
			assertTrue(prev.nodeID < mid.nodeID);
			assertTrue(prev.nodeID < succ.nodeID);
			assertTrue(mid.nodeID < succ.nodeID);
		}
		else if (prev.nodeID < mid.nodeID)
		{
			// only prev is under mid
			assertTrue(prev.nodeID < mid.nodeID);
			assertTrue(mid.nodeID > succ.nodeID);
			assertTrue(prev.nodeID > succ.nodeID);
		}
		else
		{
			// prev is the highest and mid/succ are spanned cross 0
			assertTrue(prev.nodeID > mid.nodeID);
			assertTrue(mid.nodeID < succ.nodeID);
			assertTrue(prev.nodeID > succ.nodeID);
		}
		
		assertEquals(prev.successor.nodeID, mid.nodeID);
		assertEquals(mid.predecessor.nodeID, prev.nodeID);
		assertEquals(mid.successor.nodeID, succ.nodeID);
		assertEquals(succ.predecessor.nodeID, mid.nodeID);
		
		if (isThreeNodes)
		{
			assertEquals(prev.predecessor.nodeID, succ.nodeID);
			assertEquals(succ.successor.nodeID, prev.nodeID);
		}
	}
	
	
}
	

