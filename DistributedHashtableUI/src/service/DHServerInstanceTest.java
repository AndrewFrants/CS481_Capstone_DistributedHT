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
		assertNull(instance.getEntry("rainforest"));
		
		//instance.insertFile("rainforestData.pdf");
		
	}
	
	// Testing remove entry
	@Test
	void testRemoveEntry() {
		DHServerInstance instance = new DHServerInstance("AmazonRainforest", false, false);

		instance.addEntry("rainforest");
		DHashEntry addedEntry = instance.getEntry("rainforest");
		assertEquals("rainforest", addedEntry.value);
		instance.removeEntry("rainforest");
		
		assertNull(instance.getEntry("rainforest"));	
		//assertNotEquals(addedEntry.value, "rainforest");
	}
	
	
	// Test case to verify updateEntry
	@Test
	void testUpdateEntry() {
		DHServerInstance instance = new DHServerInstance("node1", false, false);

		instance.addEntry("entry1");
		DHashEntry addedEntry = instance.getEntry("entry1");
		assertEquals("entry1", addedEntry.value);
		
		instance.updateEntry(addedEntry.key, "entry2");
		
		DHashEntry updatedEntry = instance.getEntry("entry2");
		assertEquals("entry2", updatedEntry.value);
		assertNull(instance.getEntry("entry1"));	
		assertNotEquals(addedEntry.key, updatedEntry.key);
	}
}
