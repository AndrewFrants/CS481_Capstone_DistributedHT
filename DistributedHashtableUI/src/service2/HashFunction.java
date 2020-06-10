package service2;

public class HashFunction {
	
	public  Integer hash(String fileName, Integer size) {
		Integer hashVal = 0;
		
		for(int i = 0; i < fileName.length(); i++) {
			hashVal += fileName.charAt(i);			
		}	
				
		hashVal = hashVal%size;
		return hashVal;
	}
}
