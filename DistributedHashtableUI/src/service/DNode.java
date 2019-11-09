package service;

/*
 * This class is a node on a network
 */
public class DNode {

	DHashtable table;
	
	Double hash;
	
	public Double getHash() {
		return hash;
	}

	public DNode(String nodeName)
	{
		this.hash = DNode.GetComputerBasedHash(nodeName);
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
		int hash = 0;
		
		for (char c : computerId.toCharArray())
		{
			hash += ((int)c) * 1000 * Math.E; // increase the spread a bit
		}
		
		return hash % 360;
	}
}
