package service2;

import java.io.Serializable;

/*
 * This class represents the range of Keys a node is responsible for on the Hash Ring
 */
public class Range {
	

	public int lowID;
	public int highID;
	
	//sets lowID
	public void setLowID(int low) {
		this.lowID = low;
	}
	
	//sets highID
	public void setHighID(int high) {
		this.highID = high;
	}
	
	//gets highID
	public int getHighID() {
		return highID;
	}
	
	//gets lowID
	public int getLowID() {
		return lowID;	
	}
	
	// checks if a Key Value is in range
	public boolean contains(int val) {
		
		// case 1: high is greater than low, and value is in between
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
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "LOW: " + lowID + " HIGH: " + highID;
	}

	// prints the node range
	public void printNodeRange() {
		System.out.println("The Range of the Node is from " + lowID + " to " + highID + " on the circular Hash Ring");
	}

}