package service;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;

/*
 * Distributed hashtable, there is an instance of 
 * this on every node
 */
public class DHashtable implements Serializable {

	private HashMap<Integer, DHashEntry> localHT;
	
	/*
	 * Gets the backing hashtable
	 */
	public HashMap<Integer, DHashEntry> getLocalHT() {
		return localHT;
	}

	public void setLocalHT(HashMap<Integer, DHashEntry> ht) {
		localHT = ht;
	}

	/*
	 * C'tor
	 */
	public DHashtable()
	{
		localHT = new HashMap<Integer, DHashEntry>();
	}
	
	/*
	 * Get entry of a given value
	 */
	public DHashEntry getEntry(Integer angle)
	{
		if (localHT.containsKey(angle))
			return localHT.get(angle);
		
		Set<Integer> set = localHT.keySet();

		for (Integer key : set)
		{
			if (angle - key < 0.01 && angle - key > 0.0)
				return localHT.get(key);
		}
		return null;
	}
	
	/*
	 * Insert a new value into DHT
	 */
	public void insert(String value)
	{
		this.insert(DHashEntry.getHashEntry(value));
	}
	
	/*
	 * Insert hash entries into the distributed hashtable
	 */
	public void insert(DHashEntry... hashEntries)
	{
		for (DHashEntry e : hashEntries)
		{
			localHT.put(e.getKey(), e);
		}
	}

	public void updateEntries(DHashEntry... hashEntries)
	{
		for (DHashEntry e : hashEntries)
		{
			if (localHT.containsKey(e.key))
			{
				localHT.replace(e.key, localHT.get(e.key), e);
			}
			else
			{
				insert(e);
			}
		}
	}
	
	
	/*
	 * Remove the keys
	 */
	public void removeKeys(Integer... keys)
	{
		for (Integer k : keys)
		{
			localHT.remove(k);
		}
	}
	
	/*
	 * move the keys to a new owner
	 */
	public void copyValuesTo(DHashtable table)
	{
		Set<Integer> set = table.getLocalHT().keySet();
		localHT.clear();
		for (Integer key : set)
		{
			if (!localHT.containsKey(key)) {
				localHT.put(key, table.getLocalHT().get(key));
			}
		}
	}
	
	/*
	 * Move keys above a certain threshold to a new Hashtable
	 */
	public DHashtable moveKeysAboveTo(DHashtable newTable, Integer keysAbove)
	{
		if (newTable == null)
		{
			newTable = new DHashtable();
		}
		
		Set<Integer> set = localHT.keySet();
		Integer keysCopiedCounter = 0;

		for (Integer key : set)
		{
			if (key >= keysAbove)
			{
				DHashEntry entryToCopy = localHT.get(key);
				newTable.insert(entryToCopy);
				keysCopiedCounter++;
			}
		}
		
		// remove added keys from old owner
		set = newTable.getLocalHT().keySet();
		
		for (Integer key : set)
		{
			localHT.remove(key);
		}
		
		DhtLogger.log.info("moveKeysAboveTo keysAbove: {} copied: {} count of keys (and removed from source table)", 
					keysAbove,
					keysCopiedCounter);

		return newTable;
	}
}
