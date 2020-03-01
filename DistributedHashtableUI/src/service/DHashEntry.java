/**
 * 
 */
package service;

import java.io.Serializable;

/**
 * @author andreyf Option 1. Caller sends key Option 2. Caller passes file and
 *         we hash the file, e.g. SHA-1
 */
public class DHashEntry implements Comparable<DHashEntry>, Serializable {

	public Integer key;

	public String value;

	public DHashEntry() {
	
	}
	
	/*
	 * The c'tor
	 */
	public DHashEntry(String value) {
		this.setKey(ChecksumDemoHashingFunction.hashValue(value));
		this.setValue(value);
	}

	/*
	 * get key
	 */
	public Integer getKey() {
		return key;
	}

	/*
	 * set key
	 */
	public void setKey(Integer key) {
		this.key = key;
	}

	/*
	 * get the key value
	 */
	public String getValue() {
		return value;
	}

	/*
	 * set the value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/*
	 * Convert a string to a hash entry
	 */
	public static DHashEntry getHashEntry(String value) {
		return new DHashEntry(value);
	}

	/*
	 * Compare two different hash entries
	 */
	@Override
	public int compareTo(DHashEntry arg0) {
		if (arg0 == null || arg0.getKey() == this.getKey())
			return 0;

		if (arg0.getKey() > this.getKey())
			return -1;

		return 1;
	}
}
