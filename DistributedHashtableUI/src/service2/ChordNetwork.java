package service2;

import java.util.ArrayList;

public class ChordNetwork {

	int bitSize; // Number of bits the network is represented by
	int networkSize; // Number of nodes on the Server
	ArrayList<Node> nodeList; // list of Active nodes

	public ChordNetwork(int bitSize) {
		this.bitSize = bitSize;
		networkSize = (int) Math.pow(bitSize, 2);
	}

	public void addNode(String ipAddress) {
		Node node = new Node(ipAddress);
		nodeList.add(node);
	}

}
