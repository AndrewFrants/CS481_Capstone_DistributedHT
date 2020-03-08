package service.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import service.*;

class DHServiceTests {

private DHService dhService;
	
	@BeforeEach
	void setUp() throws Exception {
		this.dhService = new DHService();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testDefaultDHService() {
		List<DNode> allNodes = this.dhService.getAllNodes();
		assertEquals(0, allNodes.size());
	}
	
	@Test
	void testAddOneNode() {
		this.dhService.addNode("nodeName1");
		List<DNode> allNodes = this.dhService.getAllNodes();
		assertEquals(1, allNodes.size());
	}
	
	@Test
	void testaddexistingNode() {
		this.dhService.addNode("nodeName1");		
		this.dhService.addNode("nodeName1");
		List<DNode> allNodes = this.dhService.getAllNodes();
		assertEquals(1, allNodes.size());
	}
	
	@Test
	void testAddMultipleNode() {
		String[] nodeNames = new String[] {
				"nodeName1", 
				"nodeName2",
				"nodeName3",
				"nodeName4" };
		
		for (String nodeName : nodeNames) {
			this.dhService.addNode(nodeName);
		}
		
		List<DNode> allNodes = this.dhService.getAllNodes();
		assertEquals(nodeNames.length, allNodes.size());
	}
		
	@Test
	void testFiveClusteredDHService() {
		DHService fiveNodeCluster = DHService.createFiveNodeCluster(false);
		List<DNode> fiveNodeClusterNodes = fiveNodeCluster.getAllNodes();
		assertEquals(4, fiveNodeClusterNodes.size());
	}
	
	@Test
	void testfindNodeByName() {
		DNode node = this.dhService.findNodeByName("node1");
		assertNull(node);
		this.dhService.addNode("node1");
		node = this.dhService.findNodeByName("node1");
		assertNotNull(node);
	}
	
	@Test
	void testRemoveNode() {
		this.dhService.addNode("node1");
		DNode node = this.dhService.findNodeByName("node1");
		assertNotNull(node);
		
		this.dhService.removeNode("node1");
		node = this.dhService.findNodeByName("node1");
		assertNull(node);
	}
	
	
}
	

