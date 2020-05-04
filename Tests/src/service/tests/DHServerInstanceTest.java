package service.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
		instance = new DHServerInstance("instance", false);
		DHServerInstance incoming = new DHServerInstance("instance2", false);

		assertEquals(incoming, instance.currentNode.successor);
		assertEquals(incoming, instance.currentNode.predecessor);
	}
	
	@Test
	public void testThreeNodes() {
		instance = new DHServerInstance("instance", false);
		DNode node1 = new DNode("incoming");
		node1.nodeID = 5;
		DNode node2 = new DNode("second");
		node2.nodeID = 45000;
		instance.addNode(node1);
		instance.addNode(node2);
		assertEquals(node1, instance.currentNode.predecessor);
		assertEquals(node2, instance.currentNode.successor);
	}
	
	
}
	

