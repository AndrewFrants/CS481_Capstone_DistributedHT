package service;

import data.IDhtEntries;
import data.IDhtNodes;
import data.InMemoryNodes;
import data.WebServiceEntries;
import data.WebServiceNodes;
import webservice.DhtWebService;

public class DHServerInstance {
	Integer networkBitSize;
	public DNode currentNode;
	public IDhtNodes dhtNodes;
	public IDhtEntries dhtEntries;
	Boolean web;
	String address;
	Boolean joinNetwork;
	String firstInstanceAddress;
	
	/*
	Example of loading resources
	https://howtodoinjava.com/spring-boot2/read-file-from-resources/
	*/

	/*
	public DHServerInstance()
	{
		dhtNodes = new WebServiceNodes();
		//currentNode = new DNode();
	}
	*/
	
	public DHServerInstance(DNode node, Boolean web, Boolean joinNetwork)
	{
		// when you change this to webservice
		// nodes, Create starts failing
		if (web)
		{
			dhtNodes = new WebServiceNodes();
			dhtEntries = new WebServiceEntries();
		}
		else
		{
			dhtNodes = new InMemoryNodes(this); // in memory for unit tests
		}
		
		currentNode = node;
		
		DhtLogger.log.info("initialize address={} nodeID={} joinNetwork={}", currentNode.getNodeAddress(), currentNode.nodeID, joinNetwork);
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
		this.address = address;
		this.joinNetwork = joinNetwork;
		this.web = web;
		this.currentNode = new DNode(address);
		
		if (web)
		{
			dhtNodes = new WebServiceNodes(firstInstanceAddress);
		}
		else
		{
			dhtNodes = new InMemoryNodes(this); // in memory for unit tests
		}

		DhtLogger.log.info("initialize address={} joinNetwork={} web={} firstInstanceAddress={}", address, joinNetwork, web, firstInstanceAddress);
	}
	
	
	public DHServerInstance(String address, Boolean joinNetwork, Boolean web, String firstInstanceAddress)
	{
		if (firstInstanceAddress == null)
		{
			firstInstanceAddress = "localhost:8080";
		}

		this.address = address;
		this.joinNetwork = joinNetwork;
		this.web = web;
		this.firstInstanceAddress = firstInstanceAddress;
		
		if (!joinNetwork)
		{
			// this is the first instance or dont join network
			this.addNode(this.currentNode);
		}
		else
		{
			dhtNodes = new InMemoryNodes(this);
		}
		
		DhtLogger.log.info("initialize address={} joinNetwork={} web={} firstInstanceAddress={}", address, joinNetwork, web, firstInstanceAddress);
	}
	
	public void joinNetwork()
	{
		if (web)
		{
			// when you change this to webservice
			// nodes, Create starts failing
			currentNode = new DNode(address);
			dhtNodes = new WebServiceNodes(firstInstanceAddress);
			
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
							DhtLogger.log.info("Waiting 15 seconds to rejoin network, address={} nodeId={}, ex={}", currentNode.getNodeAddress(), currentNode.nodeID, ex);
							Thread.sleep(15000);
						} catch (Exception e) {}
					}
				} while (!success);

			}
		}
		else if (joinNetwork)
		{
			// if Web is false, it means its the
			// old in memory execution model, useful for
			// local testing and/or unit tests
			
			dhtNodes.addNode(currentNode);	
		}
	}

	public void addNode(DNode reqNode)
	{
		DhtLogger.log.info("Node name={}({}) nodeID={}({}) joining the network", currentNode.name, currentNode.nodeID, reqNode.name, reqNode.nodeID);

		// Case 1: NodeID matches, this is an edge case
		// nodeIDs should be unique
		if(currentNode.nodeID.equals(reqNode.nodeID)) {
			DhtLogger.log.error("Case 1. CurrNodeID {} ReqNodeId: {} wasnt unique/overlapping", currentNode.name, reqNode.nodeID);
			//dhtNodes.addNode(reqNode);
		}
		// Case 2: currentNode has no successor
		// This means this there is only one node in the network
		// initialize the "loop"
		else if(currentNode.successor == null || currentNode.predecessor == null) {
		
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
		else if (reqNode.isNodeInRange(currentNode))
		{
			dhtNodes.updateNode(currentNode.predecessor);
			
			dhtNodes.updateNode(currentNode.successor);
			
			dhtNodes.updateNode(reqNode);
		}
		/*
		// Case 3 a & b. If the requesting node is supposed to be inserted
		// into the successor or predecessor of the current node
		else if(currentNode.findIfRequestingNodeIsInRange(reqNode) != null) {	

			DNode conNode = currentNode.findIfRequestingNodeIsInRange(reqNode);
			reqNode.updateRequestingNodeUponJoin(currentNode, conNode);
			dhtNodes.updateNode(reqNode);

			// Case 3a. reqNode is the predecessor of currentNode
			// requesting node becomes the successor of conNode
			//  SAME -> C.Predesessor -> R -> C -> C.Successor -> SAME
			if(reqNode.successor.nodeID.equals(currentNode.nodeID)) {
				
				// conNode -> reqNode -> currentNode
				currentNode.predecessor = reqNode;
				conNode.successor = reqNode;

				reqNode.predecessor = conNode;
				reqNode.successor = currentNode;

				// copy key ownership
				conNode.getTable().moveKeysAboveTo(reqNode.getTable(), reqNode.nodeID);
				//connection node is now the predecessor
				dhtNodes.updateNode(conNode);

				DhtLogger.log.info("Case 3a. Adding the node {}({}) to currentNode={}() currentNode.P.Name = {} currentNode.S.Name = {} as successor", 
							reqNode.name,
							reqNode.nodeID,
							currentNode.name,
							currentNode.nodeID,
							currentNode.predecessor.nodeID,
							currentNode.successor.nodeID);
			}
			// Case 3b. reqNode is the successor of currentNode
			//  SAME -> C.Predesessor -> C -> R -> C.Successor -> SAME
			else {
				
				// conNode -> currentNode -> reqNode
				DNode currSuccessor = currentNode.successor;
				currentNode.successor = reqNode;
				conNode.predecessor = reqNode;

				reqNode.predecessor = currentNode;
				reqNode.successor = currSuccessor;

				// copy key ownership
				currentNode.getTable().moveKeysAboveTo(reqNode.getTable(), reqNode.nodeID);
				dhtNodes.updateNode(conNode);
				dhtNodes.updateNode(reqNode);
				DhtLogger.log.info("Case 3b. Adding the node {}({}) to currentNode={}({}) currentNode.P.Name = {} currentNode.S.Name = {} as successor", 
					reqNode.name,
					reqNode.nodeID,
					currentNode.name,
					currentNode.nodeID,
					currentNode.predecessor.nodeID,
					currentNode.successor.nodeID);
			}
			
			// !update reqNode by passing the the currentNode and  the connecting node!
			// wait
			currentNode.updateReceivingNodeUponJoin(reqNode, conNode);

			// !update connecting node by passing the requesting node, and the current node!
			// wait
		}
		*/
		// send request to successor
		else {

			IDhtNodes successorProxy = dhtNodes.createProxyFor(this.currentNode.successor);
			successorProxy.addNode(reqNode);

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
				return dhtNodes.findNodeByName(this.currentNode.successor);
			}
			else
			{
				DhtLogger.log.warn("Current node {}  successor was null", this.currentNode.nodeID);
			}
					
			return null;
		}
	}
	
	// Retrieve a node by id from the network
	public void removeNode(DNode node)
	{
		DhtLogger.log.info("removing node={} name={}", node.nodeID, node.name);
		
		if (this.currentNode.successor.nodeID == node.nodeID)
		{
			DNode successor = dhtNodes.findNodeByName(this.currentNode.successor);
			this.currentNode.successor = successor.successor;
			successor.predecessor = this.currentNode;
			dhtNodes.updateNode(successor);
		}
		else if (this.currentNode.predecessor.nodeID == node.nodeID)
		{
			DNode predecessor = dhtNodes.findNodeByName(this.currentNode.predecessor);
			this.currentNode.predecessor = predecessor.predecessor;
			predecessor.successor = this.currentNode;
			dhtNodes.updateNode(predecessor);
		}
		else
		{
			IDhtNodes successorProxy = dhtNodes.createProxyFor(this.currentNode.successor);
			successorProxy.removeNode(node);
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
			dhtNodes.createProxyFor(this.currentNode.successor);
			dhtNodes.AddEntry(entry);
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
	
	public void updateEntry(int entryId, String entryValue)
	{
		if (this.currentNode.successor == null ||
			(this.currentNode.nodeID > entryId &&
			this.currentNode.predecessor.nodeID < entryId))
		{
			// If a parent node is found, remove the existing entry from it and add a new entry since the key
			// can be different for the new value; and the new entry can belong to a different node.
			DHashEntry existingEntry = this.currentNode.getTable().getLocalHT().get(entryId);
			this.currentNode.getTable().removeKeys(entryId);
			this.addEntry(entryValue);
			DhtLogger.log.info("Updating entry, changing value from {} to {}", entryId, existingEntry.getValue(), entryValue);
		}
		else
		{
			if (this.currentNode.successor != null) {
				DhtLogger.log.info(
						"Forwarding update of entry key {} with value {} to successor {} ({})",
						entryId,
						entryValue,
						this.currentNode.successor.name,
						this.currentNode.successor.nodeID);
				dhtEntries.update(this.currentNode.successor, entryId, entryValue);
			}			
		}
	}
	
}
