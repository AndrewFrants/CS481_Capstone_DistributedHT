package service;

/*
 * This class represents the range of Keys a node is responsible for on the Hash Ring
 */
public class Range {
	private int lowID;
	private int highID;
	
	public void setLowID(int low) {
		this.lowID = low;
	}
	
	public void setHighID(int high) {
		this.highID = high;
	}
	
	public int getHighID() {
		return highID;
	}
	
	public int getLowID() {
		return lowID;	
	}
	
	// checks if a Key Value is in range
	public boolean contains(int val) {
		
		// case 1: high is greater than low, and value is inbetween
		if(highID > lowID && val >= lowID && val <=highID) {
			return true;	
		}	
		// case 2: high is less than low, and value is greater than low or less than high
		if(highID < lowID && (val >= lowID || val <= highID)) {		
			return true;
		}
		
		// case 3: value is equal to the node ID
		if(highID == lowID && val == highID) {
			return true;
		}
		// otherwise value is not in range
		return false;
	}
	
	// prints the node range
	public void printNodeRange() {
		System.out.println("The Range of the Node is from " + lowID + " to " + highID + " on the circular Hash Ring");
	}

}