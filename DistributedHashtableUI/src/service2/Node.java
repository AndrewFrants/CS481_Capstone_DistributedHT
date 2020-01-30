package service2;

import java.util.ArrayList;
import java.util.HashMap;

import data.Movie;



public class Node {
	
	HashMap<Integer, Movie> localTable;  //HT on the local node
	int predecessor;  //Node's predecessor
	int successor;  //Node's  successor
	ArrayList<Integer> keyResponsability; //List of keys this Node is responsible for
	int nodeID;	//unique node ID
//	double angle;  //mapped angle of the nodeID on the Hash Ring
	String ipAddress; //address of the server/node
	RoutingTable routingTable; //routing table for the node
	


	public Node(String address) {
		ipAddress = address;  //set's the ip address of the node
		nodeID = hashCode();
		predecessor = -1;
		successor = -1;
		// angle = setAngle();
		
	}
	/**
	public double setAngle() {
		int highest_node_val = 15;
		int lowest_node_val = 0;
		double max_degree = 360;
		double min_degree = 0;
		double angle_val = (double) nodeID/((double)(highest_node_val - lowest_node_val))*((max_degree - min_degree)) 
				+ min_degree;
		
		return angle_val;
		
	
		
		
	}
		*/
	public void setSucessor(int successor) {
		this.successor = successor;
	}
	
	public void setPredecessor(int predecessor) {
		this.predecessor = predecessor;
		
	}
	
	public Node findSucessor() {
		
		return new Node("placeholder");
	}
	
	public Node findPredecessor() {
		
		return new Node("placeholder");
	}
	
	//this method takes files from another node
	public void receiveValues() {
		
	}
	
	// this method sends file to another node
	public void sendValues() {
		
	}
	
	public void updatePredecessor() {
		
		
	}
	
	public void updateSucessor() {
		
		
	}
		
	

	@Override
	public String toString() {
		return "Node [nodeID=" + nodeID + "]";
	}

	@Override
	public int hashCode() {
		
		HashFunction h = new HashFunction();
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
