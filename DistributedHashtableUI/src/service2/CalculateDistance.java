package service2;

//calculates the clock wise distance between 2 nodes given node1, node2, and bit size as inputs
public class CalculateDistance {
	
	public int calculateClockWiseDistance(int node_1, int node_2, int bitSize) {
		int size = (int) Math.pow(2, bitSize);
		
		if(node_1 <= node_2) {
			return node_2 - node_1;
		}
		
		return size - node_1 + node_2;
	}

}
