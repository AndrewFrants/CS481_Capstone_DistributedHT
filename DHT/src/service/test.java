package service;

public class test {
	
	public static void main(String[] args) {
		KeyRange range = new KeyRange(9,5);
		
		System.out.println("Low is " + range.getLowID() + " High is " + range.getHighID());
		
		System.out.println(range.contains(8));
	}

}
