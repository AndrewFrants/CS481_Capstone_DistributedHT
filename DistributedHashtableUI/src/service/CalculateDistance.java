package service;

public class CalculateDistance {
	
	public static int calculateClockWiseDistance(int node_1, int node_2, int bitSize) {
		int size = (int) Math.pow(2, bitSize);
		
		if(node_1 <= node_2) {
			return node_2 - node_1;
		}
		
		return size - node_1 + node_2;
	}

}
