package service;

public class ChecksumDemoHashingFunction {

	/*
	 * Returns a hash of a string into 360 Degrees
	 * 
	 * This is a stupid/experimental checksum based hash
	 * this probably should never be used for anything
	 * Implemented for demo purposes only.
	 */
	public static int hashValue(String value)
	{
		int hash = 0;
		
		for (char c : value.toCharArray())
		{
			//System.out.println((Math.pow((int)c + 10, 2)) * Math.PI);
			
			hash += c; // increase the spread a bit
		}
		hash = hash%16;
		//System.out.println(hash);
		
		
		
		//System.out.println(hash);
		
	//	double rem = hash - (double)Math.floor(hash); // keep the remainder
		
	//	rem = Math.round(rem * 10) / 100.0;
		
		//System.out.println(hash);
		//System.out.println(Math.round(hash));
		
		//System.out.println(rem);
		return hash;
		// return (hash % 360) + rem;
	}
}
