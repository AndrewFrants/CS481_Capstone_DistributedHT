package service;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;

/*
 * Distributed hashtable, there is an instance of 
 * this on every node
 */
public class DHashtable {

	HashMap<Double, DHashEntry> localHT;
	
	public HashMap<Double, DHashEntry> getHT() {
		return localHT;
	}

	public DHashtable()
	{
		localHT = new HashMap<Double, DHashEntry>();
	}
	
	public void insert(DHashEntry... hashEntries)
	{
		for (DHashEntry e : hashEntries)
		{
			localHT.put(e.getKey(), e);
		}
	}
	
	public void removeKeys(String... keys)
	{
		for (String k : keys)
		{
			localHT.remove(k);
		}
	}
	
	/*
	 * move the keys to a new owner
	 */
	public void copyValuesTo(DHashtable table)
	{
		Set<Double> set = localHT.keySet();
		
		for (Double key : set)
		{
			table.insert(localHT.get(key));
		}
	}
	
	public DHashtable moveKeysAboveTo(Double keysAbove)
	{
		DHashtable newTable = new DHashtable();
		
		Set<Double> set = localHT.keySet();
		
		for (Double key : set)
		{
			if (key >= keysAbove)
			{
				DHashEntry entryToCopy = localHT.get(key);
				newTable.insert(entryToCopy);
				set.remove(entryToCopy);
			}
		}
		
		return newTable;
	}
}
