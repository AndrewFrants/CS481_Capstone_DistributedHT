package service;

import data.IDhtEntries;
import data.IDhtNodes;
import data.InMemoryNodes;
import data.WebServiceNodes;
import webservice.DhtWebService;

public class DHServerInstance {
	Integer networkBitSize;
	public DNode currentNode = new DNode("new");
	public IDhtNodes dhtNodes;
	public IDhtEntries dhtEntries;
	
	/*
	Example of loading resources
	https://howtodoinjava.com/spring-boot2/read-file-from-resources/
	*/

	public DHServerInstance()
	{
		dhtNodes = new WebServiceNodes();
		//currentNode = new DNode();
	}
	
	public DHServerInstance(String address, Boolean joinNetwork)
	{
		// when you change this to webservice
		// nodes, Create starts failing
		dhtNodes = new WebServiceNodes();
		currentNode = new DNode(address);
		
		DhtLogger.log.info("initialize address={} joinNetwork={}", address, joinNetwork);

		if (joinNetwork)
		{
			this.addNode(this.currentNode);
		}
		else
		{
			DhtWebService.DhtService.addNode(currentNode);
		}
	}
	
	public DHServerInstance(String address, Boolean joinNetwork, Boolean web)
	{
		DhtLogger.log.info("initialize address={} joinNetwork={}", address, joinNetwork, web);

		if (web)
		{
			// when you change this to webservice
			// nodes, Create starts failing
			currentNode = new DNode(address);
			dhtNodes = new WebServiceNodes();
			
			if (!joinNetwork)
			{
				this.addNode(this.currentNode);
			}
			else
			{
				DhtWebService.DhtService.addNode(currentNode);
			}
		}
		else	
		{
			currentNode = new DNode(address);
			dhtNodes = new InMemoryNodes();
		}
	}
	
	public void addNode(DNode reqNode)
	{
		DhtLogger.log.info("Node name={} nodeID={} joining the network", reqNode.name, reqNode.nodeID);

		if(currentNode.nodeID == reqNode.nodeID) {
			DhtLogger.log.info("Adding the node {} to thisinstance={} partition {}", reqNode.name, currentNode.name, currentNode.nodeID);
			dhtNodes.addNode(reqNode);
			System.out.println("Im here 1");
		}
		
		else if(currentNode.successor == null) {
		
			// update reqNode by passing the current node
			// wait
			reqNode.successor = currentNode;
			reqNode.predecessor = currentNode;
			dhtNodes.updateNode(reqNode);
			currentNode.successor = reqNode;
			currentNode.predecessor = reqNode;
			System.out.println("Im here 2");			
		}
		
		else if(currentNode.findIfRequestingNodeIsInRange(reqNode) != null) {			
			DNode conNode = currentNode.findIfRequestingNodeIsInRange(reqNode);
			reqNode.updateRequestingNodeUponJoin(currentNode, conNode);
			dhtNodes.updateNode(reqNode);
			System.out.println("Im here 3");

			// requesting node becomes the successor of conNode
			if(reqNode.successor.nodeID == currentNode.nodeID) {
				
				// conNode -> reqNode -> currentNode
				currentNode.predecessor = reqNode;
				conNode.successor = reqNode;

				// copy key ownership
				conNode.getTable().moveKeysAboveTo(reqNode.getTable(), reqNode.nodeID);
				//connection node is now the predecessor
				dhtNodes.updateNode(conNode);		
				System.out.println("Im here 4");

			}			
			else {
				
				// conNode -> currentNode -> reqNode
				currentNode.successor = reqNode;
				conNode.predecessor = reqNode;

				// copy key ownership
				currentNode.getTable().moveKeysAboveTo(reqNode.getTable(), reqNode.nodeID);
				dhtNodes.updateNode(conNode);
				System.out.println("Im here 5");

			}
			
			// !update reqNode by passing the the currentNode and  the connecting node!
			// wait
			currentNode.updateReceivingNodeUponJoin(reqNode, conNode);
			System.out.println("Im here 6");

			// !update connecting node by passing the requesting node, and the current node!
			// wait
		}
		
		// send request to successor
		else {
			dhtNodes.addNode(currentNode.successor);
			System.out.println("Im here 7");

		}
		
	}
	
	public DNode getNode(Integer nodeId)
	{
		System.out.println(String.format("getNode %d request for node: %d", this.currentNode.nodeID, nodeId));
		System.out.println("Im here 8");

		if (this.currentNode.nodeID.equals(nodeId)) // insert any preceding keys here
		{
			System.out.println(String.format("Returning current node: %d", this.currentNode.nodeID));
			System.out.println("Im here 9");

			return this.currentNode;
		}
		else
		{
			String successorName = null;
			Integer successorNodeId = null;
			System.out.println("Im here 10");

			if (this.currentNode.successor != null) {
				successorName = this.currentNode.successor.name;
				successorNodeId = this.currentNode.successor.nodeID;
				
				System.out.println(String.format("Forwarding getNode %d to successor %s (%d)", nodeId, successorName, successorNodeId));
				System.out.println("Im here 11");
				//return dhtNodes.findNodeByName(successorName);
				return dhtNodes.findNodeByName(this.currentNode.successor, nodeId);
			}
			System.out.println("Im here 12");		
			return null;
		}
	}
	
	public void addEntry(String entry)
	{
		int fileID = ChecksumDemoHashingFunction.hashValue(entry);
		System.out.println(fileID);
		System.out.println(currentNode.successor);
		System.out.println(currentNode.nodeID);
		System.out.println(currentNode.predecessor.nodeID);
		
		System.out.println("Im here 13");
		if (this.currentNode.successor == null ||
			(this.currentNode.nodeID > fileID &&
			this.currentNode.predecessor.nodeID < fileID)) // insert any preceding keys here
		{
			System.out.println(String.format("Inserted key %s into node %s (%s)", entry, this.currentNode.nodeID, this.currentNode.name));
			System.out.println("Im here 14");
			this.currentNode.getTable().insert(DHashEntry.getHashEntry(entry));
		}
		else
		{
			String successorName = null;
			Integer successorNodeId = null;
			System.out.println("Im here 15");
			if (this.currentNode.successor != null) {
				successorName = this.currentNode.successor.name;
				successorNodeId = this.currentNode.successor.nodeID;	
				System.out.println("Im here 16");
				
				/**System.out.println(fileID);
				System.out.println(currentNode.successor);
				System.out.println(currentNode.nodeID);
				System.out.println(currentNode.predecessor.nodeID);**/
				
			}
			
			/**System.out.println(fileID);
			System.out.println(currentNode.successor);
			System.out.println(currentNode.nodeID);
			System.out.println(currentNode.predecessor.nodeID);**/
			
			
			System.out.println("Im here 17");
			System.out.println(String.format("Forwarding %s to successor %s (%s)", entry, successorName, this.currentNode.nodeID));
			dhtEntries.insert(this.currentNode.successor, entry);
			System.out.println("Im here 18");
		}
	}
	
	public DHashEntry getEntry(String entry)
	{
		int fileID = ChecksumDemoHashingFunction.hashValue(entry);
		
		if (this.currentNode.successor == null ||
			(this.currentNode.nodeID > fileID &&
			this.currentNode.predecessor.nodeID < fileID)) // insert any preceding keys here
		{
			System.out.println(String.format("Inserted key %s into node %s (%s)", entry, this.currentNode.nodeID, this.currentNode.name));
			return this.currentNode.getTable().getLocalHT().get((DHashEntry.getHashEntry(entry)).key);
		}
		else
		{

			System.out.println(String.format("Forwarding %s to successor %s (%s)", entry, this.currentNode.successor.name, this.currentNode.nodeID));
			return dhtEntries.get(this.currentNode.successor, entry);
		}
	}
	
	public void insertFile(String file) {
		int fileID = ChecksumDemoHashingFunction.hashValue(file);
		if(currentNode.keyList.contains(fileID)) {
			currentNode.localTable.put(fileID, file);
		}
		
		else {
			dhtNodes.AddEntry(file); 
		}
		
	}
	
	
	
}
