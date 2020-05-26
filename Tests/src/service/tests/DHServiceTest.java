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

public class DHServiceTest {

	private DHService dhService;
	
	@BeforeEach
	public void setUp() throws Exception {
		this.dhService = new DHService();
	}

	@AfterEach
	public void tearDown() throws Exception {
	}

	@Test
	public void testDefaultDHService() {
		List<DNode> allNodes = this.dhService.getAllNodes();
		assertEquals(0, allNodes.size());
	}
	
	@Test
	public void testAddOneNode() {
		this.dhService.addNode("nodeName1");
		List<DNode> allNodes = this.dhService.getAllNodes();
		assertEquals(1, allNodes.size());
	}
	
	@Test
	public void testaddexistingNode() {
		this.dhService.addNode("nodeName1");		
		this.dhService.addNode("nodeName1");
		List<DNode> allNodes = this.dhService.getAllNodes();
		assertEquals(1, allNodes.size());
	}
	
	@Test
	public void testAddMultipleNode() {
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
	public void testFiveClusteredDHService() {
		DHService fiveNodeCluster = DHService.createFiveNodeCluster(false);
		List<DNode> fiveNodeClusterNodes = fiveNodeCluster.getAllNodes();
		assertEquals(4, fiveNodeClusterNodes.size());
	}
	
	@Test
	public void testfindNodeByName() {
		DNode node = this.dhService.findNodeByName("node1");
		assertNull(node);
		this.dhService.addNode("node1");
		node = this.dhService.findNodeByName("node1");
		assertNotNull(node);
	}
	
	@Test
	public void testRemoveNode() {
		this.dhService.addNode("node1");
		DNode node = this.dhService.findNodeByName("node1");
		assertNotNull(node);
		
		this.dhService.removeNode("node1");
		node = this.dhService.findNodeByName("node1");
		assertNull(node);
	}
	
	
}
	

