package service;

import data.IDhtNodes;
import data.InMemoryNodes;
import data.WebServiceNodes;

public class DHServerInstance {
	Integer networkBitSize;
	public DNode currentNode;
	public IDhtNodes dhtNodes;
	
	public DHServerInstance()
	{
		// when you change this to webservice
		// nodes, Create starts failing
		dhtNodes = new WebServiceNodes();
		currentNode = new DNode("ip address");
	}
	public DHServerInstance(Boolean web)
	{
		
		if (web)
		{
			// when you change this to webservice
			// nodes, Create starts failing
			currentNode = new DNode("ip address");
			dhtNodes = new WebServiceNodes();
		}
		else	
		{
			currentNode = new DNode("ip address");
			dhtNodes = new InMemoryNodes();
		}
	}
	public void addNode(DNode reqNode)
	{
		if(currentNode.nodeID == reqNode.nodeID) {
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

			// requesting node becomes the successor
			if(reqNode.successor.nodeID == currentNode.nodeID) {
				currentNode.predecessor = reqNode;
				conNode.successor = reqNode;
				//connection node is now the predecessor
				dhtNodes.updateNode(conNode);				
			}			
			else {
				currentNode.successor = reqNode;
				conNode.predecessor = reqNode;
				
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
	
	public void insertFile(String file) {
		int fileID = ChecksumDemoHashingFunction.hashValue(file);
		if(currentNode.keyList.contains(fileID)) {
			currentNode.localTable.put(fileID, file);
		}
		
		else {
			dhtNodes.addEntry(file); 
		}
		
	}
	
	
	
}
