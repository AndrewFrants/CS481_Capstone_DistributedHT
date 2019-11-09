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
	
	public String key;
	
	public String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
