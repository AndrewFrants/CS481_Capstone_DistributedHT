package service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DHServerInstanceTest {

	@Test
	void test() {
		DHServerInstance instance = new DHServerInstance();
		//DHServerInstance instance2 = new DHServerInstance("Address", false);
		//DHServerInstance instance3 = new DHServerInstance("Address2", false, true);
		
		DNode node = new DNode("Amazon rainforest");
		//instance.addNode(node);
		//instance.getNode(DNode.GetComputerBasedHash("Amazon rainforest"));
		instance.addEntry("rainforest");
		instance.getEntry("rainforest");
		instance.insertFile("rainforestData.pdf");
		
		//assertEquals();
	}

}
