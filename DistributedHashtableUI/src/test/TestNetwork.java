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
		 
		 
		 DNode node1 = new DNode("23.232.32122");
		 System.out.println("NODE 1 CREATED NODE ID: " + node1.nodeID +" \n  suc: " + node1.successor + "  pred: " + node1.predecessor + "\n");
		 System.out.println("NODE 1 KEY LIST: " + node1.keyList);
		 DNode node2 = new DNode("123325.467 ");
		 System.out.println("NODE 2 CREATED NODE ID: " + node2.nodeID +" \n  suc: " + node2.successor + "  pred: " + node2.predecessor + "\n");
		 System.out.println("NODE 2 KEY LIST: " + node2.keyList);
		 node1.sendJoinRequest(node2);
		 System.out.println("NODE 1 SENDS JOIN REQUEST TO NODE 2");
	
		 System.out.println("NODE 1 KEY LIST: " + node1.keyList);	 
		 System.out.println("NODE 2 KEY LIST: " + node2.keyList);
		 System.out.println("NODE 1 AFTER JOIN: " + node1.nodeID +" \n  suc: " + node1.successor.nodeID + " pred: " + node1.predecessor.nodeID + "\n");
		 System.out.println("NODE 2 AFTER JOIN: " + node2.nodeID +" \n suc: " + node2.successor.nodeID + "  pred: " + node2.predecessor.nodeID + "\n");
		 System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		  DNode node3 = new DNode("test"); 
		 System.out.println("NODE 3 CREATED NODE ID: " + node3.nodeID +" \n suc: " + node3.successor + "pred: " + node3.predecessor + "\n");
		 System.out.println("NODE 3 SENDS JOIN REQUEST TO NODE 1");
		
		  node3.sendJoinRequest(node1);
			 System.out.println("NODE 1 KEY LIST: " + node1.keyList);	 
			 System.out.println("NODE 2 KEY LIST: " + node2.keyList);
			 System.out.println("NODE 3 KEY LIST: " + node3.keyList);
		  System.out.println("NODE 1 AFTER JOIN ID: " + node1.nodeID +" \n  suc: " + node1.successor.nodeID + "  pred: " + node1.predecessor.nodeID + "\n");
		  System.out.println("NODE 2 AFTER JOIN ID: " + node2.nodeID +" \n  suc: " + node2.successor.nodeID + "  pred: " + node2.predecessor.nodeID + "\n");
		  System.out.println("NODE 3 AFTER JOIN ID: " + node3.nodeID +" \n  suc: " + node3.successor.nodeID + "  pred: " + node3.predecessor.nodeID + "\n");
		  System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		  DNode node4 = new DNode("test11111");
		  System.out.println("NODE 4 CREATED NODE ID: " + node4.nodeID +" \n  suc: " + node4.successor + "  pred: " + node4.predecessor + "\n");
		  node4.sendJoinRequest(node1);
		  System.out.println("NODE 4 SENDS JOIN REQUEST TO NODE 2");
			 System.out.println("NODE 1 KEY LIST: " + node1.keyList);	 
			 System.out.println("NODE 2 KEY LIST: " + node2.keyList);
			 System.out.println("NODE 3 KEY LIST: " + node3.keyList);
			 System.out.println("NODE 4 KEY LIST: " + node4.keyList);
		  System.out.println("NODE 1 AFTER JOIN ID: " + node1.nodeID +" \n  suc: " + node1.successor.nodeID + "  pred: " + node1.predecessor.nodeID + "\n");
		  System.out.println("NODE 2 AFTER JOIN ID: " + node2.nodeID +" \n  suc: " + node2.successor.nodeID + "  pred: " + node2.predecessor.nodeID + "\n");
		  System.out.println("NODE 3 AFTER JOIN ID: " + node3.nodeID +" \n  suc: " + node3.successor.nodeID + "  pred: " + node3.predecessor.nodeID + "\n");
		  System.out.println("NODE 4 AFTER JOIN ID: " + node4.nodeID +" \n  suc: " + node4.successor.nodeID + "  pred: " + node4.predecessor.nodeID + "\n");
		  
		 /**
		 System.out.println("Node ip name: " + node1.getName());
		 System.out.println("Node ID: " + node1.getNodeID()); 
		 node1.router.printRoutingTable();
		
		 System.out.println("Node ip name: " + node2.getName());
		 System.out.println("Node ID: " + node2.getNodeID());
		 node2.router.printRoutingTable();	
		
		
		
		System.out.println(node1.getNodeID() + " requesting to join " + node2.getNodeID());
		System.out.println("node " + node1.getNodeID() + " key responsability: " + node1.keyList);
		System.out.println("node " + node2.getNodeID() + " key responsability: " + node2.keyList);
		
		 System.out.println("Node ip name: " + node1.getName());
		 System.out.println("Node ID: " + node1.getNodeID()); 
		 node1.router.printRoutingTable();
		
		 System.out.println("Node ip name: " + node2.getName());
		 System.out.println("Node ID: " + node2.getNodeID());
		 node2.router.printRoutingTable();	
		 
		
		 System.out.println(node3.nodeID);
		 
		
		 
		 System.out.println("ID: " +node1.nodeID + " pred: "  + node1.predecessor.nodeID + " suc: " + node1.successor.nodeID);
		 System.out.println("ID: " +node2.nodeID + " pred: "  + node2.predecessor.nodeID + " suc: " + node2.successor.nodeID);
		 System.out.println("ID: " +node3.nodeID + " pred: "  + node3.predecessor.nodeID + " suc: " + node3.successor.nodeID);
		 */
	}

}
