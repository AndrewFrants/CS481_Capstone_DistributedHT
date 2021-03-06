package service;

import java.io.Serializable;
import java.util.ArrayList;

//the routing table structure of all the nodes
public class RoutingTable implements Serializable {
	
	//fields initialization
	static final long serialVersionUID = 0;
	public int nodeID; // ID of the node that owns the routing table
	public int size; // size of the routing table, which is the size of the network size or log(n) of
	public int[] n; // holds value of nodes incremented by 2^i
	public ArrayList<ArrayList<Integer>> searchValues; // list of search values for node n
	public DNode[] forwardToNode; // array of nodes to send request to
	public String[] addresses; // IP addresses to forward

	/*
	 * Default constructor for deserialization
	 */
	public RoutingTable() {
	}
	
	//constructor with given node
	public RoutingTable(DNode node) {

		this.nodeID = node.getNodeID();
		this.size = node.size; // make size private, replace with getters/setters
		n = new int[size]; //
		searchValues = new ArrayList<ArrayList<Integer>>();
		forwardToNode = new DNode[size];
		addresses = new String[size];
		initialize(node);
	}

	// initializes the routing table
	private void initialize(DNode node) {
		int x = 0;
		for (int i = 0; i < size; i++) {

			n[i] = nodeID + (int) Math.pow(2, i) % (int) Math.pow(2, size);
			if (n[i] > Math.pow(2, size) - 1) {
				n[i] = n[i] - (int) Math.pow(2, size);
			}
			addresses[i] = node.getName();
			forwardToNode[i] = node;

			ArrayList<Integer> currentIndex = new ArrayList<Integer>();
			for (int j = 0; j < (int) Math.pow(2, i); j++) {
				if (x + nodeID + 1 <= Math.pow(2, size) - 1) {
					currentIndex.add(x + nodeID + 1);
				} else {
					currentIndex.add(x + nodeID + 1 - (int) Math.pow(2, size));
				}
				x++;
			}
	
			searchValues.add(currentIndex);
		}

	}
	
	//gets forwarding node list
	public DNode [] getForwardNodeList() {
		return forwardToNode;
	}

	//prints the routing table
	public void printRoutingTable() {
		for (int i = 0; i < size; i++) {
		
			System.out.println("ID + 2^" + i + ": "  + n[i] 
					+ " forward to: " + forwardToNode[i].nodeID + " address: " + addresses[i] + "\n");

		}
	}

}
