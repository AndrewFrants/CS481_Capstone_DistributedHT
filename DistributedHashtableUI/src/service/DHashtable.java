package service;

import java.util.Hashtable;

/*
 * Distributed hashtable, there is an instance of 
 * this on every node
 */
public class DHashtable {

	Hashtable<String, String> localHT;
	
	public void insert(DHashEntry... hashEntries)
	{
		for (DHashEntry e : hashEntries)
		{
			localHT.put(e.getKey(), e.getValue());
		}
	}
	
	public void removeKeys(String... keys)
	{
		for (String k : keys)
		{
			localHT.remove(k);
		}
	}
}
