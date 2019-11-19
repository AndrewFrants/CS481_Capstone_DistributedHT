package service2;

import java.util.ArrayList;
import java.util.HashMap;

public class Node {
	
	HashMap<Integer, String> localTable;  //HT on the local node
	Node predeccesor;  //Node's predecessor
	Node successor;  //Node's  successor
	ArrayList<Integer> keyResponsability; //List of keys this Node is responsible for
	int nodeID;	//unique node ID
	double angle;  //mapped angle of the nodeID on the Hash Ring
	String ipAddress; //address of the server/node
	RoutingTable routingTable; //routing table for the node
	


	public Node(String address) {
		ipAddress = address;  //set's the ip address of the node
		nodeID = hashCode();
		predeccesor = null;
		successor = null;
		
	}

	@Override
	public String toString() {
		return "Node [nodeID=" + nodeID + "]";
	}

	@Override
	public int hashCode() {
		
		HashFunction h = new HashFunction(16);
		int result = h.hash(ipAddress);
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (nodeID != other.nodeID)
			return false;
		return true;
	}
	
	

}
