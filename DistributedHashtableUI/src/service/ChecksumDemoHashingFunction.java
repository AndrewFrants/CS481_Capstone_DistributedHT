package service;

// Hashing function 
public class ChecksumDemoHashingFunction {

	/*
	 * Returns a hash of a string into 360 Degrees
	 * This is an experimental checksum based hash
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
		
		return (int)((hash % 360) + rem);
	}
	
	/*
	 * Returns a checksum hash
	 */
	public static int hashValue(String value)
	{
		long hash = 1;

		for (char c : value.toCharArray())
		{
			if (hash % 2 == 0)
			{
				hash += Math.pow(c, 2) + Math.pow(-c, 3); // increase the spread a bit
			}
			else 	
			{
				hash -= Math.pow(c, 2) + Math.pow(-c, 3); // increase the spread a bit
			}
		}
		
		int intHash = Math.abs((int)((hash % 65000)));
		

		DhtLogger.log.debug("Hashing value: {} seed: {} hashed to {}", value, hash, intHash);
		
		return intHash;
	}
	
	// revised hash function
	public static int originalHashFunction(String value)
	{
		DhtLogger.log.info("Hashing value: {}", value);
		
		long hash = 1;

		for (char c : value.toCharArray())
		{
			if (hash % 2 == 0)
			{
				hash += Math.pow(2, c); // increase the spread a bit
			}
			else	
			{
				hash -= Math.pow(2, c); // increase the spread a bit
			}
		}
		
		int intHash = (int)(hash % 65000);
		

		DhtLogger.log.info("Hashing value: {} seed: {} hashed to {}", value, hash, intHash);
		
		return intHash;
	}
}
