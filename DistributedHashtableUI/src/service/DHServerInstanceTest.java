package service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DHServerInstanceTest {

	@Test
	void test() {
		//DHServerInstance instance = new DHServerInstance();
		//DHServerInstance instance2 = new DHServerInstance("Address", false);
		DHServerInstance instance3 = new DHServerInstance("Address2", false, false);
		
		DNode node = new DNode("AmazonRainforest");
		instance3.addNode(node);
		DNode addedNode = new DNode();
		addedNode = instance3.getNode(DNode.GetComputerBasedHash("AmazonRainforest"));
		System.out.println(addedNode.name);
		
		
		//instance.addEntry("rainforest");
		//instance.getEntry("rainforest");
		//instance.insertFile("rainforestData.pdf");
		
		//assertEquals();
	}

}
