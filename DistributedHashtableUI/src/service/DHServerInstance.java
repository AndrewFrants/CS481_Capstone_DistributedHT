package service;

import data.IDhtEntries;
import data.IDhtNodes;
import data.InMemoryNodes;
import data.WebServiceNodes;
import webservice.DhtWebService;

public class DHServerInstance {
	Integer networkBitSize;
	public DNode currentNode;
	public IDhtNodes dhtNodes;
	public IDhtEntries dhtEntries;
	
	/*
	Example of loading resources
	https://howtodoinjava.com/spring-boot2/read-file-from-resources/
	*/

	public DHServerInstance()
	{
		dhtNodes = new WebServiceNodes();
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
		}
		
		else if(currentNode.successor == null) {
		
			// update reqNode by passing the current node
			// wait
			reqNode.successor = currentNode;
			reqNode.predecessor = currentNode;
			dhtNodes.updateNode(reqNode);
			currentNode.successor = reqNode;
			currentNode.predecessor = reqNode;
		}
		
		else if(currentNode.findIfRequestingNodeIsInRange(reqNode) != null) {			
			DNode conNode = currentNode.findIfRequestingNodeIsInRange(reqNode);
			reqNode.updateRequestingNodeUponJoin(currentNode, conNode);
			dhtNodes.updateNode(reqNode);

			// requesting node becomes the successor of conNode
			if(reqNode.successor.nodeID == currentNode.nodeID) {
				
				// conNode -> reqNode -> currentNode
				currentNode.predecessor = reqNode;
				conNode.successor = reqNode;

				// copy key ownership
				conNode.getTable().moveKeysAboveTo(reqNode.getTable(), reqNode.nodeID);
				//connection node is now the predecessor
				dhtNodes.updateNode(conNode);				
			}			
			else {
				
				// conNode -> currentNode -> reqNode
				currentNode.successor = reqNode;
				conNode.predecessor = reqNode;

				// copy key ownership
				currentNode.getTable().moveKeysAboveTo(reqNode.getTable(), reqNode.nodeID);
				dhtNodes.updateNode(conNode);
			}
			
			// !update reqNode by passing the the currentNode and  the connecting node!
			// wait
			currentNode.updateReceivingNodeUponJoin(reqNode, conNode);
			
			// !update connecting node by passing the requesting node, and the current node!
			// wait
		}
		
		// send request to successor
		else {
			dhtNodes.addNode(currentNode.successor);
		}
		
		
	}
	
	public DNode getNode(Integer nodeId)
	{
		System.out.println(String.format("getNode %d request for node: %d", this.currentNode.nodeID, nodeId));
		
		if (this.currentNode.nodeID.equals(nodeId)) // insert any preceding keys here
		{
			System.out.println(String.format("Returning current node: %d", this.currentNode.nodeID));
			
			return this.currentNode;
		}
		else
		{
			String successorName = null;
			Integer successorNodeId = null;
			
			if (this.currentNode.successor != null) {
				successorName = this.currentNode.successor.name;
				successorNodeId = this.currentNode.successor.nodeID;
				
				System.out.println(String.format("Forwarding getNode %d to successor %s (%d)", nodeId, successorName, successorNodeId));
				return dhtNodes.findNodeByName(this.currentNode.successor, nodeId);
			}
					
			return null;
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
	
	public void addEntry(String entry)
	{
		int fileID = ChecksumDemoHashingFunction.hashValue(entry);
		//System.out.println(fileID);
		//System.out.println(currentNode.successor);
		//System.out.println(currentNode.nodeID);
		//System.out.println(currentNode.predecessor.nodeID);
		
		if (this.currentNode.successor == null ||
			(this.currentNode.nodeID > fileID &&
			this.currentNode.predecessor.nodeID < fileID)) // insert any preceding keys here
		{
			System.out.println(String.format("Inserted key %s into node %s (%s)", entry, this.currentNode.nodeID, this.currentNode.name));
			
			this.currentNode.getTable().insert(DHashEntry.getHashEntry(entry));
		}
		else
		{
			String successorName = null;
			Integer successorNodeId = null;
			
			if (this.currentNode.successor != null) {
				successorName = this.currentNode.successor.name;
				successorNodeId = this.currentNode.successor.nodeID;	
			}
			
			System.out.println(String.format("Forwarding %s to successor %s (%s)", entry, successorName, this.currentNode.nodeID));
			dhtEntries.insert(this.currentNode.successor, entry);
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
