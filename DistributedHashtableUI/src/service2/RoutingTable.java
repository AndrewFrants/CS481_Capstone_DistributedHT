package service2;

import java.util.ArrayList;

public class RoutingTable {
	private int localNodeID;
	private String localAddress;
	private ArrayList<RoutingEntry> forwardList;
	private int size; // size of network

	// condense into object id and ranges
	private ArrayList<Integer> idealForwardID; // ID of the ideal node to send the request (if it exists).
	private ArrayList<Range> forwardRanges; // list of ranges.

	public RoutingTable(int size, int localNodeID, String localAddress) {
		this.size = size;
		forwardList = new ArrayList<RoutingEntry>();
		this.localNodeID = localNodeID;
		this.localAddress = localAddress;
		forwardRanges = new ArrayList<Range>();
		initialize();
	}



	// initialize the list after creating the table
	private void initialize() {
		// creates the default list which forwards to itself for every entry
		for (int i = 0; i < (size - 1); i++) {
			RoutingEntry entry = new RoutingEntry(i, localNodeID, localNodeID, localAddress);
			forwardList.add(entry);
			idealForwardID.add((localNodeID + 2 ^ i) % 2 ^ size);

		}

	}

	// returns the correct routing entry for the node query
	public RoutingEntry getForwardEntry(int nodeToForward) {

		for (int i = 0; i < forwardList.size(); i++) {
			if (forwardList.get(i).contains(nodeToForward)) {
				return forwardList.get(i);
			}
		}
		return null;
	}

}
