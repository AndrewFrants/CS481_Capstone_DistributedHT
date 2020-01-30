package service2;

import java.util.ArrayList;
import java.util.Collections;

public class RoutingTable {
	int nodeID; // ID of the node that owns the routing table
	int size; // size of the routing table, which is log(n) of the network size
	int [] n;  // holds value of node incremented by 2^i
	ArrayList<ArrayList<Integer>>  searchValues; // list of search values for node n
	int[] sendToNode; // array of nodes to send request to
	int successor; // ID of successor node
	int predecessor; // ID of predecessor node
	
	
	public RoutingTable(int nodeID, int size, ArrayList<Integer> listOfActiveNodes) {
		
		this.nodeID = nodeID;
		this.size = size;
		n = new int[size];
		searchValues = new ArrayList<ArrayList<Integer>>();
		sendToNode = new int[size];

		
	}
}