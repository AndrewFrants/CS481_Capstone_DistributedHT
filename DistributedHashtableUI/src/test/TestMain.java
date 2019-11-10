/**
 * 
 */
package test;

import service.DHService;
import service.Hasher;

/**
 * @author andreyf
 *
 */
public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Test the hasher
		System.out.println(Hasher.hashValue("1afaS2s=2abcdef"));
		
		DHService dhs = DHService.createFiveNodeCluster();
		
	}

}
