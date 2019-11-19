package service2;

import java.util.ArrayList;
import java.util.HashMap;

public class Node {
	
	HashMap<Integer, String> localTable;  //HT on the local node
	Node predecessor;  //Node's predecessor
	Node successor;  //Node's  successor
	ArrayList<Integer> keyResponsability; //List of keys this Node is responsible for
	int nodeID;	//unique node ID
	double angle;  //mapped angle of the nodeID on the Hash Ring
	String ipAddress; //address of the server/node
	RoutingTable routingTable; //routing table for the node
	


	public Node(String address) {
		ipAddress = address;  //set's the ip address of the node
		nodeID = hashCode();
		predecessor = null;
		successor = null;
		angle = setAngle();
		
	}
	
	public double setAngle() {
		int highest_node_val = 15;
		int lowest_node_val = 0;
		double max_degree = 360;
		double min_degree = 0;
		double angle_val = (double) nodeID/((double)(highest_node_val - lowest_node_val))*((max_degree - min_degree)) 
				+ min_degree;
		
		return angle_val;
		
		
		
		
	}
	
	public void setSucessor(Node successor) {
		this.successor = successor;
	}
	
	public void setPredecessor(Node predecessor) {
		this.predecessor = predecessor;
		
	}
	
	public Node findSucessor() {
		
		return new Node("placeholder");
	}
	
	public Node findPredecessor() {
		
		return new Node("placeholder");
	}

	@Override
	public String toString() {
		return "Node [nodeID=" + nodeID + "]";
	}

	@Override
	public int hashCode() {
		
		HashFunction h = new HashFunction(4);
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
