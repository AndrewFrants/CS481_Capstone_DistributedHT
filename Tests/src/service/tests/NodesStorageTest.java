package service.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nodes.storage.NodesStorage;
import service.DNode;

class NodesStorageTest {
	private NodesStorage nodesStorage;
	private Path nodesLocation;
	private final String NODES_FILENAME = "nodes.json";

	@BeforeEach
	void setUp() throws Exception {
		this.nodesStorage = new NodesStorage();
		ClassLoader classLoader = getClass().getClassLoader();
		String baseLocation = Paths.get(classLoader.getResource(".").toURI()).toString();
		nodesLocation = Paths.get(baseLocation, NODES_FILENAME);
	}

	@AfterEach
	void tearDown() throws Exception {
		Files.deleteIfExists(nodesLocation);
	}

	@Test
	void testNodesStored() {
		List<DNode> nodes = new ArrayList<DNode>();
		nodes.add(new DNode("1"));
		
		this.nodesStorage.writeToStorage(nodes);
		assertTrue(Files.exists(nodesLocation));
	}
	
	@Test
	void testNodesNotStoredIfNull() {
		List<DNode> nodes = null;
		
		this.nodesStorage.writeToStorage(nodes);
		assertFalse(Files.exists(nodesLocation));
	}

}
