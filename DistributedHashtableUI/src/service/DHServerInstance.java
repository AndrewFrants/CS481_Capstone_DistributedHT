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
			DhtLogger.log.info("Current Instance and Node initialized at address={} nodeId={}", currentNode.getNodeAddress(), currentNode.nodeID);
		}
	}
	
	public DHServerInstance(String address, Boolean joinNetwork, Boolean web)
	{
		DhtLogger.log.info("initialize address={} joinNetwork={} web={}", address, joinNetwork, web);

		if (web)
		{
			// when you change this to webservice
			// nodes, Create starts failing
			currentNode = new DNode(address);
			dhtNodes = new WebServiceNodes();
			
			if (!joinNetwork)
			{
				// this is the first instance or dont join network
				DhtLogger.log.info("Current Instance and Node initialized at address={} nodeId={}", currentNode.getNodeAddress(), currentNode.nodeID);
			}
			else
			{
				Boolean success = false;

				do
				{
					try
					{
						// this is a second node, hence join the 
						// DHT service
						dhtNodes.addNode(currentNode);
						success = true;
					}
					catch (Exception ex)
					{
						try
						{
							DhtLogger.log.info("Waiting 15 seconds to rejoin network, address={} nodeId={}", currentNode.getNodeAddress(), currentNode.nodeID);
							Thread.sleep(15000);
						} catch (Exception e) {}
					}
				} while (!success);

			}
		}
		else	
		{
			// if Web is false, it means its the
			// old in memory execution model, useful for
			// local testing and/or unit tests
			currentNode = new DNode(address);
			dhtNodes = new InMemoryNodes();
			
			if (!joinNetwork)
			{
				// this is the first instance or dont join network
				this.addNode(this.currentNode);
			}
			else
			{
				dhtNodes.AddEntry(currentNode);
			}
		}
	}
	
	public void addNode(DNode reqNode)
	{
		DhtLogger.log.info("Node name={} nodeID={} joining the network", reqNode.name, reqNode.nodeID);

		// Case 1: NodeID matches, this is an edge case
		// nodeIDs should be unique
		if(currentNode.nodeID == reqNode.nodeID) {
			DhtLogger.log.warn("Case 1. CurrNodeID {} ReqNodeId: {} wasnt unique/overlapping", currentNode.name, reqNode.nodeID);
			dhtNodes.addNode(reqNode);
			System.out.println("Im here 1");
		}
		// Case 2: currentNode has no successor
		// This means this there is only one node in the network
		// initialize the "loop"
		else if(currentNode.successor == null) {
		
			// update curr and req nodes
			// to create a ring
			reqNode.successor = currentNode;
			reqNode.predecessor = currentNode;
			dhtNodes.updateNode(reqNode);
			currentNode.successor = reqNode;
			currentNode.predecessor = reqNode;

			DhtLogger.log.info("Case 2. Adding the node {} to currentNode={} currentNode.P.Name = {} currentNode.S.Name = {} as successor", 
								reqNode.name,
								currentNode.name,
								currentNode.predecessor.nodeID,
								currentNode.successor.nodeID);

		}
		
		// Case 3 a & b. If the requesting node is supposed to be inserted
		// into the successor or predecessor of the current node
		else if(currentNode.findIfRequestingNodeIsInRange(reqNode) != null) {	

			DNode conNode = currentNode.findIfRequestingNodeIsInRange(reqNode);
			reqNode.updateRequestingNodeUponJoin(currentNode, conNode);
			dhtNodes.updateNode(reqNode);
			System.out.println("Im here 3");

			// Case 3a. reqNode is the predecessor of currentNode
			// requesting node becomes the successor of conNode
			//  SAME -> C.Predesessor -> R -> C -> C.Successor -> SAME
			if(reqNode.successor.nodeID == currentNode.nodeID) {
				
				// conNode -> reqNode -> currentNode
				currentNode.predecessor = reqNode;
				conNode.successor = reqNode;

				// copy key ownership
				conNode.getTable().moveKeysAboveTo(reqNode.getTable(), reqNode.nodeID);
				//connection node is now the predecessor
				dhtNodes.updateNode(conNode);
				
				DhtLogger.log.info("Case 3a. Adding the node {} to currentNode={} currentNode.P.Name = {} currentNode.S.Name = {} as successor", 
							reqNode.name,
							currentNode.name,
							currentNode.predecessor.nodeID,
							currentNode.successor.nodeID);
			}
			// Case 3b. reqNode is the successor of currentNode
			//  SAME -> C.Predesessor -> C -> R -> C.Successor -> SAME
			else {
				
				// conNode -> currentNode -> reqNode
				currentNode.successor = reqNode;
				conNode.predecessor = reqNode;

				// copy key ownership
				currentNode.getTable().moveKeysAboveTo(reqNode.getTable(), reqNode.nodeID);
				dhtNodes.updateNode(conNode);

				DhtLogger.log.info("Case 3b. Adding the node {} to currentNode={} currentNode.P.Name = {} currentNode.S.Name = {} as successor", 
					reqNode.name,
					currentNode.name,
					currentNode.predecessor.nodeID,
					currentNode.successor.nodeID);
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
	
	// Retrieve a node by id from the network
	public DNode getNode(Integer nodeId)
	{
		DhtLogger.log.info("getNode {} request for node: {}", this.currentNode.nodeID, nodeId);
		
		if (this.currentNode.nodeID.equals(nodeId)) // insert any preceding keys here
		{
			DhtLogger.log.info("Returning current node: {}", this.currentNode.nodeID);
			
			return this.currentNode;
		}
		else
		{
			String successorName = null;
			Integer successorNodeId = null;

			if (this.currentNode.successor != null) {
				successorName = this.currentNode.successor.name;
				successorNodeId = this.currentNode.successor.nodeID;
				
				DhtLogger.log.info("Forwarding getNode {} to successor {} ({}})", nodeId, successorName, successorNodeId);
				return dhtNodes.findNodeByName(this.currentNode.successor, nodeId);
			}
			else
			{
				DhtLogger.log.warn("Current node {}  successor was null", this.currentNode.nodeID);
			}
					
			return null;
		}
	}
	
	public void addEntry(String entry)
	{
		int fileID = ChecksumDemoHashingFunction.hashValue(entry);
		
		if (this.currentNode.successor == null ||
			(this.currentNode.nodeID > fileID &&
			this.currentNode.predecessor.nodeID < fileID)) // insert any preceding keys here
		{
			DhtLogger.log.info("Inserted key {} into node {} ({})", entry, this.currentNode.nodeID, this.currentNode.name);
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

			DhtLogger.log.info("Forwarding {} to successor {} ({})", entry, successorName, this.currentNode.nodeID);
			dhtEntries.insert(this.currentNode.successor, entry);
		}
	}
	
	public void removeEntry(String entry)
	{
		int fileID = ChecksumDemoHashingFunction.hashValue(entry);
		
		if (this.currentNode.successor == null ||
			(this.currentNode.nodeID > fileID &&
			this.currentNode.predecessor.nodeID < fileID)) // insert any preceding keys here
		{
			DhtLogger.log.info("Inserted key {} into node {} ({})", entry, this.currentNode.nodeID, this.currentNode.name);
			this.currentNode.getTable().removeKeys(ChecksumDemoHashingFunction.hashValue(entry));
		}
		else
		{
			String successorName = null;
			Integer successorNodeId = null;
			if (this.currentNode.successor != null) {
				successorName = this.currentNode.successor.name;
				successorNodeId = this.currentNode.successor.nodeID;
			}

			DhtLogger.log.info("Forwarding {} to successor {} ({})", entry, successorName, this.currentNode.nodeID);
			dhtEntries.remove(this.currentNode.successor, entry);
		}
	}
	
	public DHashEntry getEntry(String entry)
	{
		int fileID = ChecksumDemoHashingFunction.hashValue(entry);
		
		if (this.currentNode.successor == null ||
			(this.currentNode.nodeID > fileID &&
			this.currentNode.predecessor.nodeID < fileID)) // insert any preceding keys here
		{
			DhtLogger.log.info("Inserted key {} into node {} ({})", entry, this.currentNode.nodeID, this.currentNode.name);
			return this.currentNode.getTable().getLocalHT().get((DHashEntry.getHashEntry(entry)).key);
		}
		else
		{

			DhtLogger.log.info("Forwarding {} to successor {} ({})", entry, this.currentNode.successor.name, this.currentNode.nodeID);
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
