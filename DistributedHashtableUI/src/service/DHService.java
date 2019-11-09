/**
 * 
 */
package service;

import java.util.Hashtable;


/**
 * @author andreyf
 * This is the entry point into the service
 * it provides service level functionality
 */
public class DHService {

	Hashtable<Double, DNode> nodes;
	
	public DHService()
	{
		nodes = new Hashtable<Double, DNode>();
		
	}
	
	public void addNode(DNode newNode)
	{
		nodes.put(newNode.getHash(), newNode);
	}
	
	/*
	 * This is a 4 node sample cluster for testing/demo'ing
	 */
	public static DHService createFiveNodeCluster()
	{
		DHService dhService = new DHService();
		
		String[] nodeNames = new String[] { "Andrew", 
											"Daniyal",
											"Palak",
											"Rachana" };
		
		for (String nodeName : nodeNames)
		{
			DNode node = new DNode(nodeName);
			
			dhService.addNode(node);
		}
		
		return dhService;
	}
}
