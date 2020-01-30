package service2;

public class HashFunction {


	public int hash(String strVal){
		
		int hash = 1;

		for (char c : strVal.toCharArray())
		{	
	
			
			hash += (c*c); // increase the spread a bit
		}

		hash = hash%512;
		
		return hash;	
	}
	


}
