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

	// updates the table upon joining
	public void updateTableUponNodeJoin(int incomingNodeID, String incomingAddress) {
		for (int i = 0; i < forwardList.size(); i++) {
			CalculateDistance distance = new CalculateDistance();

			// checks distance of entry to new node
			int incomingDistance = distance.calculateClockWiseDistance(incomingNodeID,
					forwardList.get(i).getIdealForwardID(), size);
			// checks current distance of entry to current forwarding node
			int currentDistance = distance.calculateClockWiseDistance(forwardList.get(i).getForwardID(),
					forwardList.get(i).getIdealForwardID(), size);
			if (incomingDistance < currentDistance) {
				forwardList.get(i).changeForwardNode(incomingNodeID, incomingAddress);
			}
		}
	}

	// udpates the routing table upon node leaving by searching for all references
	// to that node and changing them
	// to the next closest(counter-clockwise) node
	public void updateTableUponNodeLeaving(int departingNodeID) {

		for (int i = 0; i < forwardList.size(); i++) {
			if (forwardList.get(i).getForwardID() == departingNodeID) {
				RoutingEntry nextLowestEntry = findNextLowestEntry(departingNodeID);
				forwardList.get(i).changeForwardNode(nextLowestEntry.getForwardID(),
						nextLowestEntry.getForwardAddress());
			}
		}

	}

	// finds the replacement entry
	private RoutingEntry findNextLowestEntry(int nodeID) {
		CalculateDistance distance = new CalculateDistance();
		// checks distance of entry to new node
		RoutingEntry nextLowestEntry = null;
		int lowestDistance = -1;
		for (int i = 0; i < forwardList.size(); i++) {

			// checks current distance of entry to current forwarding node
			int departingDistance = distance.calculateClockWiseDistance(nodeID, forwardList.get(i).getForwardID(),
					size);

			if (departingDistance > lowestDistance) {
				lowestDistance = departingDistance;
				nextLowestEntry = forwardList.get(i);
			}

		}

		return nextLowestEntry;

	}
	
	public void printRoutingTable() {
		
		System.out.println("Routing Table for Node: " + localNodeID + " With Address: " + localAddress);
		System.out.println("-------------------------------------------------------------------------");
		for(int i = 0; i < forwardList.size(); i++) {
			forwardList.get(i).printRoutingEntry();
		}
		System.out.println("-------------------------------------------------------------------------");
	}

}
