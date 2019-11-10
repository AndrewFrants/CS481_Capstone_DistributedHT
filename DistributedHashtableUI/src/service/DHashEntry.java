/**
 * 
 */
package service;

/**
 * @author andreyf
 * Option 1. Caller sends key
 * Option 2. Caller passes file and we hash the file, e.g. SHA-1
 */
public class DHashEntry {
	
	public Double key;
	
	public String value;

	public Double getKey() {
		return key;
	}

	public void setKey(Double key) {
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
		this.setKey(Hasher.hashValue(value));
		this.setValue(value);
	}
	
	public static DHashEntry getHashEntry(String value)
	{
		return new DHashEntry(value);
	}
}
