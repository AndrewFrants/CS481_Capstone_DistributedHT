package service;

import java.util.LinkedList;
import java.util.List;

/*
 * This class is a node on a network
 */
public class DNode implements Comparable<DNode> {

	DHashtable table;
	
	Double hash;
	
	String name;
	
	
	public DHashtable getTable() {
		return table;
	}

	public void setTable(DHashtable table) {
		this.table = table;
	}

	public String getName() {
		return name;
	}

	public Double getHash() {
		return hash;
	}

	public DNode(String nodeName)
	{
		this.name = nodeName;
		this.hash = DNode.GetComputerBasedHash(nodeName);
		table = new DHashtable();
	}
	
	public void AssignKeys(DHashEntry... hashEntries)
	{
		table.insert(hashEntries);
	}
	
	/*
	 * This method returns the consistent hash for a machine based on its name.
	 * The name can be a computer name+ip, etc.
	 */
	public static double GetComputerBasedHash(String computerId)
	{
		return ChecksumDemoHashingFunction.hashValue(computerId);
	}

	/*
	 * Gets all entries for visualization
	 */
	public List<DHashEntry> getAllEntries()
	{
		List<DHashEntry> allEntries = new LinkedList<DHashEntry>();
		
		for (Double key : table.getHT().keySet())
		{
			allEntries.add(table.getHT().get(key));
		}
		
		return allEntries;
	}

	@Override
	public int compareTo(DNode arg0) {
		if (arg0 == null || arg0.getHash() == this.getHash())
			return 0;
		
		if (arg0.getHash() > this.getHash())
			return -1;
		
		return 1;
	}

	/*
	@Override
	public int compareTo(Object o) {
		if (o == null || o instanceof DNode || ((DNode)o).getHash() == this.getHash())
			return 0;
		
		if (arg0.getHash() > this.getHash())
			return -1;
		
		return 1;
	}
	*/
}
