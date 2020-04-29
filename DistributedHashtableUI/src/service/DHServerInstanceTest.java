package service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DHServerInstanceTest {

	@Test
	void test() {
		//DHServerInstance instance = new DHServerInstance();
		//DHServerInstance instance2 = new DHServerInstance("Address", false);
		DHServerInstance instance = new DHServerInstance("Address1", false, false);
		DHServerInstance instance2 = new DHServerInstance("AmazonRainforest", true, false);

		DNode addedNode = new DNode();
		
		addedNode = instance.getNode(DNode.GetComputerBasedHash("Address1"));
		assertEquals("Address1", addedNode.getName());

		addedNode = instance2.getNode(DNode.GetComputerBasedHash("AmazonRainforest"));
		assertEquals("AmazonRainforest", addedNode.getName());
		
		instance.addEntry("rainforest");
		assertEquals("rainforest", instance.getEntry("rainforest").value);
		
		instance.removeEntry("rainforest");
		assertEquals("rainforest", instance.getEntry("rainforest").value); //should break

		//instance.insertFile("rainforestData.pdf");
		
	}

}
