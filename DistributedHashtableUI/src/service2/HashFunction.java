package service2;

public class HashFunction {
	
	int hashVal;
	int bitSize;


	public HashFunction(int bitSize) {
		
		this.bitSize = bitSize; //bit-size of the HashFunction

			
	}
	public int hash(String strVal){
		
		int charIntVal = 0;
		
		//adds up the integer value of the String by summing up the char's Integer values
		for(char c : strVal.toCharArray()) {
			charIntVal += c;			
		}	
		
		int size = (int) (Math.pow(2, bitSize));
		//Convert's to a consistent hash function by modding over the size
		hashVal = charIntVal%size;
		
		return hashVal;		
	}
	


}
