package service2;

import java.util.ArrayList;
import java.util.HashMap;

public class RoutingTable {
	int size;
	HashMap<Integer, String> table; 
	 
	
	public RoutingTable(int bitSize) {
		size = bitSize - 1;
		table = new HashMap<Integer, String>();
		init();
		
						
	}
	
	private void init() {
		for(int i = 0; i < size;i++) {
			table.put(i, null);
		}
		
		
	}
	
	

}
