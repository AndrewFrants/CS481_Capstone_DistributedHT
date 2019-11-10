package service;

public class Hasher {

	/*
	 * Returns a hash of a string into 360 Degrees
	 */
	public static double hashValue(String value)
	{
		double hash = 0;
		
		for (char c : value.toCharArray())
		{
			//System.out.println((Math.pow((int)c + 10, 2)) * Math.PI);
			
			hash += Math.round((Math.sqrt((int)c * 10000 * Math.PI))); // increase the spread a bit
		}
		
		//System.out.println(hash);
		
		hash = Math.round(hash * 10) / 100.0;
		
		//System.out.println(hash);
		
		double rem = hash - (double)Math.floor(hash); // keep the remainder
		
		//System.out.println(hash);
		//System.out.println(Math.round(hash));
		
		//System.out.println(rem);
		
		return (hash % 360) + rem;
	}
}
