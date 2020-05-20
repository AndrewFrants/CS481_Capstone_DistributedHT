package service;

public class KeyRange {
	private int lowID;
	private int highID;
	
	public KeyRange(int low, int high) {
		this.lowID = low;
		this.highID = high;
	}
	
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
	public boolean contains(int val) {
		
		if(highID > lowID && val >= lowID && val <=highID) {
			return true;	
		}		
		if(highID < lowID && (val >= lowID || val <= highID)) {		
		return true;
		}
		
		if(highID == lowID && val == highID) {
			return true;
		}
		return false;
	}

}
