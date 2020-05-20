package service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DNodeTestRange {


	@Test
	void testRangeOneNodeLowID() {
		DNode nodeA = new DNode("34.567.3333"); // 28692
		
		assertEquals(nodeA.keyRange.getLowID(), 28692);

	}
	
	// should be one less than node ID when only 1 node in network
	@Test
	void testRangeOneNodeHighID() {
		DNode nodeA = new DNode("34.567.3333"); // 28692
		
		assertEquals(nodeA.keyRange.getHighID(), 28691);
	}
	
	@Test
	void testCorrectRangeValuesForNode() {
		
		DNode nodeA = new DNode("34.567.3333"); // 28692
		
		int[] vals = {0, 455, 3122, 10345, 5565};
		
		assertTrue(nodeA.keyRange.contains(vals[0]));
		assertTrue(nodeA.keyRange.contains(vals[1]));
		assertTrue(nodeA.keyRange.contains(vals[2]));
		assertTrue(nodeA.keyRange.contains(vals[3]));
		assertTrue(nodeA.keyRange.contains(vals[4]));
	}
	
	@Test
	void testTwoNodesCorrectRangeValues() {
		
		DNode nodeA = new DNode("34.567.3333"); // 28692
		DNode nodeB = new DNode("56.678.113423"); // 34243
		nodeA.sendJoinRequest(nodeB); 
		int[] vals = {0, 455, 3122, 34242, 34243, 28693};
		
		assertFalse(nodeA.keyRange.contains(vals[0]));
		assertFalse(nodeA.keyRange.contains(vals[1]));
		assertFalse(nodeA.keyRange.contains(vals[2]));
		assertTrue(nodeA.keyRange.contains(vals[3]));
		assertFalse(nodeA.keyRange.contains(vals[4]));
		assertTrue(nodeA.keyRange.contains(vals[5]));
	}
	
	
}
