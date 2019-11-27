package service;

public class ChecksumDemoHashingFunction {

	/*
	 * Returns a hash of a string into 360 Degrees
	 * 
	 * This is a stupid/experimental checksum based hash
	 * this probably should never be used for anything
	 * Implemented for demo purposes only.
	 */
	public static int hashValueByDegree(String value)
	{
		int hash = 0;
		
		for (char c : value.toCharArray())
		{
			hash += (Math.sqrt(Math.pow((int)c, Math.PI)));
		}
		
		double rem = hash - (double)Math.floor(hash); // keep the remainder

		rem = Math.round(rem * 10) / 100.0;
		
		// TODO: here we want to make the hash generic
		// so that we can use different hash types.
		return (int)((hash % 360) + rem);
	}
	
	/*
	 * Returns a checksum hash
	 */
	public static int hashValue(String value)
	{
		int hash = 0;
		
		for (char c : value.toCharArray())
		{	
			hash += c; // increase the spread a bit
		}

		hash = hash%255;
		
		return hash;
	}
}
