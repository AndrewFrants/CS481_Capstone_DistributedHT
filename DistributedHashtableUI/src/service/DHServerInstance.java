package service;

import data.IDhtEntries;
import data.IDhtNodes;
import data.InMemoryNodes;
import data.WebServiceEntries;
import data.WebServiceNodes;

// the server instance class
public class DHServerInstance {
	
	//fields initialization
	Integer networkBitSize;
	public DNode currentNode;
	public IDhtNodes dhtNodes;
	public IDhtEntries dhtEntries;
	Boolean web;
	String address;
	Boolean joinNetwork;
	String firstInstanceAddress;
	
	//constructor
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
	
	//constructor
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
	
	//constructor
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
	
	//constructor
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
		this.currentNode = new DNode(address);

		DhtLogger.log.info("initialize address={} joinNetwork={} web={} firstInstanceAddress={}", address, joinNetwork, web, firstInstanceAddress);
	}
	
	//joining the network
	public void joinNetwork()
	{
		if (web)
		{
			// when you change this to webservice
			// nodes, Create starts failing
			dhtNodes = new WebServiceNodes(firstInstanceAddress);
			
			if (!joinNetwork)
			{
				// this is the first instance or don't join network
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
							DhtLogger.log.info("Waiting 3 seconds to rejoin network, address={} nodeId={}, firstS={} ex={}", currentNode.getNodeAddress(), currentNode.nodeID, firstInstanceAddress, ex);
							Thread.sleep(3000);
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

	// adding a node
	public void addNode(DNode reqNode) {
	
		DhtLogger.log.info("Node name={}({}) nodeID={}({}) joining the network", currentNode.name, currentNode.nodeID, reqNode.name, reqNode.nodeID);

		// Case 1: NodeID matches, this is an edge case
		// nodeIDs should be unique
		if(currentNode.nodeID.equals(reqNode.nodeID)) {
			DhtLogger.log.error("Case 1. CurrNodeID {} ReqNodeId: {} wasnt unique/overlapping", currentNode.name, reqNode.nodeID);
			currentNode = reqNode.Clone(true);
			return;			
		}
		
		// Case 2: currentNode has no successor
		// This means this there is only one node in the network
		// initialize the "loop"
		else if(currentNode.successor == null) {
			
			// update curr and req nodes
			// to create a ring
			DNodeJoin.updateAdjacentNodes(currentNode, reqNode);
			dhtNodes.updateNode(reqNode);
			
			
			DhtLogger.log.info("Case 2. Adding the node {} to currentNode={} currentNode.P.Name = {} currentNode.S.Name = {} as successor", 
								reqNode.name,
								currentNode.name,
								currentNode.predecessor.nodeID,
								currentNode.successor.nodeID);
			return;
		}
	
		currentNode.predecessor = this.getNode(currentNode.predecessor);
		currentNode.successor = this.getNode(currentNode.successor);
		
		if (reqNode.isNodeInRange(this.currentNode))
		{
			DNodeJoin.updateAdjacentNodes(currentNode, reqNode);
			
			dhtNodes.updateNode(reqNode.predecessor);
			dhtNodes.updateNode(reqNode.successor);		
			dhtNodes.updateNode(reqNode);
		}
		// send request to successor
		else {

			IDhtNodes successorProxy = dhtNodes.createProxyFor(this.currentNode.successor);
			successorProxy.addNode(reqNode);
		}	
	
	}
	// Retrieve a node by id from the network
	public DNode getNode(DNode node)
	{
		IDhtNodes nodeProxy = this.dhtNodes.createProxyFor(node);

		return nodeProxy.getNode();
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
				IDhtNodes successorProxy = this.dhtNodes.createProxyFor(this.currentNode.successor);
				return successorProxy.findNodeByName(nodeId);
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
	
	// add entry into the network
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
	
	//gets entry from the network
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
	
	//removes entry from the network
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
		}
	}	
}
