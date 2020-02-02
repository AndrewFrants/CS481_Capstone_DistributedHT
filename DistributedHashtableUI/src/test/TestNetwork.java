package test;
import java.net.InetAddress;
import java.net.UnknownHostException;

import service.DHService;
import service.DNode;

public class TestNetwork {
	
	public static void main(String[] args) {
		DHService service = new DHService();
		
		 InetAddress ip = null;
		 try {
			ip = InetAddress.getLocalHost();
		//	System.out.println("Your current IP address : " + ip);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
		 DNode node1 = new DNode("dsfsdf");
		 DNode node2 = new DNode("123325.467 ");
		 
		 
		 System.out.println("Node ip name: " + node1.getName());
		 System.out.println("Node ID: " + node1.getNodeID()); 
		 node1.router.printRoutingTable();
		
		 System.out.println("Node ip name: " + node2.getName());
		 System.out.println("Node ID: " + node2.getNodeID());
		 node2.router.printRoutingTable();	
		
		
		node1.sendJoinRequest(node2);
		
		System.out.println(node1.getNodeID() + " requesting to join " + node2.getNodeID());
		System.out.println("node " + node1.getNodeID() + " key responsability: " + node1.keyList);
		System.out.println("node " + node2.getNodeID() + " key responsability: " + node2.keyList);
		
		 System.out.println("Node ip name: " + node1.getName());
		 System.out.println("Node ID: " + node1.getNodeID()); 
		 node1.router.printRoutingTable();
		
		 System.out.println("Node ip name: " + node2.getName());
		 System.out.println("Node ID: " + node2.getNodeID());
		 node2.router.printRoutingTable();	
		
	}

}
