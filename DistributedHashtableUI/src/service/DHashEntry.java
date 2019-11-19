/**
 * 
 */
package service;

/**
 * @author andreyf
 * Option 1. Caller sends key
 * Option 2. Caller passes file and we hash the file, e.g. SHA-1
 */
public class DHashEntry implements Comparable<DHashEntry> {
	
	public Integer key;
	
	public String value;

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public DHashEntry(String value)
	{
		this.setKey(ChecksumDemoHashingFunction.hashValue(value));
		this.setValue(value);
	}
	
	public static DHashEntry getHashEntry(String value)
	{
		return new DHashEntry(value);
	}
	
	@Override
	public int compareTo(DHashEntry arg0) {
		if (arg0 == null || arg0.getKey() == this.getKey())
			return 0;
		
		if (arg0.getKey() > this.getKey())
			return -1;
		
		return 1;
	}
}
