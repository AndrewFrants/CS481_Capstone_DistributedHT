package service.tests;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import java.util.List;
import org.junit.Assert;
//DO NOT USE import org.junit.Test; stupid java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import service.ChecksumDemoHashingFunction;
import service.DHServerInstance;
import service.DHService;
import service.DNode;
import service.DNodeJoin;
import service.DNodeLeave;

public class NetworkTest {

	@Test
	public void runNetworkTest() {	
		
		/**DHServerInstance instance = new DHServerInstance();
		//DHServerInstance instance2 = new DHServerInstance("Address", false);
		//DHServerInstance instance3 = new DHServerInstance("Address2", false, true);
		
		DNode node = new DNode("AmazonRainforest");
		instance.addNode(node);
		instance.getNode(DNode.GetComputerBasedHash("AmazonRainforest"));
		instance.addEntry("rainforest");
		instance.getEntry("rainforest");
		instance.insertFile("rainforestData.pdf");
		
		//assertEquals();**/
		
		/**
		DHService service = new DHService();

		InetAddress ip = null;
		try {
			ip = InetAddress.getLocalHost();
			// System.out.println("Your current IP address : " + ip);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DNode node1 = new DNode("23.232.32122");
		System.out.println("NODE 1 CREATED NODE ID: " + node1.nodeID + " \n  suc: " + node1.successor + "  pred: "
				+ node1.predecessor + "\n");
		System.out.println("NODE 1 KEY LIST: " + node1.keyList);

		System.out.println("Node 1's Routing Table");
		node1.router.printRoutingTable();
		DNode node2 = new DNode("123325.467 ");
		System.out.println("NODE 2 CREATED NODE ID: " + node2.nodeID + " \n  suc: " + node2.successor + "  pred: "
				+ node2.predecessor + "\n");
		System.out.println("NODE 2 KEY LIST: " + node2.keyList);
		System.out.println("Node 2's Routing Table");
		node2.router.printRoutingTable();
		System.out.println("NODE 1 SENDS JOIN REQUEST TO NODE 2");

		node1.sendJoinRequest(node2);

		System.out.println("NODE 1 KEY LIST: " + node1.keyList);
		System.out.println("NODE 2 KEY LIST: " + node2.keyList);
		System.out.println("NODE 1 AFTER JOIN: " + node1.nodeID + " \n  suc: " + node1.successor.nodeID + " pred: "
				+ node1.predecessor.nodeID + "\n");
		System.out.println("NODE 2 AFTER JOIN: " + node2.nodeID + " \n suc: " + node2.successor.nodeID + "  pred: "
				+ node2.predecessor.nodeID + "\n");
		System.out.println("Node 1's Routing Table");
		node1.router.printRoutingTable();
		System.out.println("Node 2's Routing Table");
		node2.router.printRoutingTable();
		System.out.println("Node 2 calls to update it's routing Table");
		DNodeJoin.updateRoutingTable(node2);
		node2.router.printRoutingTable();
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		DNode node3 = new DNode("test");
		System.out.println("NODE 3 CREATED NODE ID: " + node3.nodeID + " \n suc: " + node3.successor + "pred: "
				+ node3.predecessor + "\n");
		System.out.println("Node 3's Routing Table");
		node3.router.printRoutingTable();
		System.out.println("NODE 3 SENDS JOIN REQUEST TO NODE 1");

		node3.sendJoinRequest(node1);
		node3.router.printRoutingTable();
		System.out.println("Node 2 calls to update it's routing Table");
		DNodeJoin.updateRoutingTable(node2);
		node2.router.printRoutingTable();
		System.out.println("Node 1 calls to update it's routing Table");
		DNodeJoin.updateRoutingTable(node1);
		node1.router.printRoutingTable();
		System.out.println("NODE 1 KEY LIST: " + node1.keyList);
		System.out.println("NODE 2 KEY LIST: " + node2.keyList);
		System.out.println("NODE 3 KEY LIST: " + node3.keyList);
		System.out.println("NODE 1 AFTER JOIN ID: " + node1.nodeID + " \n  suc: " + node1.successor.nodeID + "  pred: "
				+ node1.predecessor.nodeID + "\n");
		System.out.println("NODE 2 AFTER JOIN ID: " + node2.nodeID + " \n  suc: " + node2.successor.nodeID + "  pred: "
				+ node2.predecessor.nodeID + "\n");
		System.out.println("NODE 3 AFTER JOIN ID: " + node3.nodeID + " \n  suc: " + node3.successor.nodeID + "  pred: "
				+ node3.predecessor.nodeID + "\n");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		DNode node4 = new DNode("test11111");
		System.out.println("NODE 4 CREATED NODE ID: " + node4.nodeID + " \n  suc: " + node4.successor + "  pred: "
				+ node4.predecessor + "\n");
		System.out.println("Node 4's Routing Table before joining the network");
		node4.router.printRoutingTable();
		node4.sendJoinRequest(node1);
		System.out.println("Node 4's Routing Table After Joining Network");
		node4.router.printRoutingTable();
		System.out.println("NODE 4 SENDS JOIN REQUEST TO NODE 2");
		System.out.println("NODE 1 KEY LIST: " + node1.keyList);
		System.out.println("NODE 2 KEY LIST: " + node2.keyList);
		System.out.println("NODE 3 KEY LIST: " + node3.keyList);
		System.out.println("NODE 4 KEY LIST: " + node4.keyList);
		System.out.println("NODE 1 AFTER JOIN ID: " + node1.nodeID + " \n  suc: " + node1.successor.nodeID + "  pred: "
				+ node1.predecessor.nodeID + "\n");
		System.out.println("NODE 2 AFTER JOIN ID: " + node2.nodeID + " \n  suc: " + node2.successor.nodeID + "  pred: "
				+ node2.predecessor.nodeID + "\n");
		System.out.println("NODE 3 AFTER JOIN ID: " + node3.nodeID + " \n  suc: " + node3.successor.nodeID + "  pred: "
				+ node3.predecessor.nodeID + "\n");
		System.out.println("NODE 4 AFTER JOIN ID: " + node4.nodeID + " \n  suc: " + node4.successor.nodeID + "  pred: "
				+ node4.predecessor.nodeID + "\n");

		System.out.println("\nNode 3 calls to update it's routing Table");
		DNodeJoin.updateRoutingTable(node3);
		node3.router.printRoutingTable();

		System.out.println("\nNode 2 calls to update it's routing Table");
		DNodeJoin.updateRoutingTable(node2);
		node2.router.printRoutingTable();
		System.out.println("\nNode 1 calls to update it's routing Table");
		DNodeJoin.updateRoutingTable(node1);
		node1.router.printRoutingTable();

		
		 * System.out.println("Node ip name: " + node1.getName());
		 * System.out.println("Node ID: " + node1.getNodeID());
		 * node1.router.printRoutingTable();
		 * 
		 * System.out.println("Node ip name: " + node2.getName());
		 * System.out.println("Node ID: " + node2.getNodeID());
		 * node2.router.printRoutingTable();
		 * 
		 * 
		 * 
		 * System.out.println(node1.getNodeID() + " requesting to join " +
		 * node2.getNodeID()); System.out.println("node " + node1.getNodeID() + " key
		 * responsability: " + node1.keyList); System.out.println("node " +
		 * node2.getNodeID() + " key responsability: " + node2.keyList);
		 * 
		 * System.out.println("Node ip name: " + node1.getName());
		 * System.out.println("Node ID: " + node1.getNodeID());
		 * node1.router.printRoutingTable();
		 * 
		 * System.out.println("Node ip name: " + node2.getName());
		 * System.out.println("Node ID: " + node2.getNodeID());
		 * node2.router.printRoutingTable();
		 * 
		 * 
		 * System.out.println(node3.nodeID);
		 * 
		 * 
		 * 
		 * System.out.println("ID: " +node1.nodeID + " pred: " +
		 * node1.predecessor.nodeID + " suc: " + node1.successor.nodeID);
		 * System.out.println("ID: " +node2.nodeID + " pred: " +
		 * node2.predecessor.nodeID + " suc: " + node2.successor.nodeID);
		 * System.out.println("ID: " +node3.nodeID + " pred: " +
		 * node3.predecessor.nodeID + " suc: " + node3.successor.nodeID);
		 
	*/
		DNode nodeA = new DNode("A.12342.13.1254EFDASD");
		DNode nodeB = new DNode("B.12342.13.354GHJGFJG");
		DNode nodeC = new DNode("C.12342.123.124259029303");
		DNode nodeD = new DNode("D.12342.13.12354XCBVZJKZKJV");
		DNode nodeE = new DNode("E.42.123.354AFKJHAFKJHKAJ");
		DNode nodeF = new DNode("F.12342.13.1254EFS111D");
		DNode nodeG = new DNode("G.12342.13.354GHJGG11213345");
		DNode nodeH = new DNode("H.12342.123.124259029333453");
		DNode nodeI = new DNode("I.12342.13.12354XCBV12321ZJKZKJV");
		DNode nodeJ = new DNode("J.42.123.354AFKJHAFKJHKA1111J");
		System.out.println("Node A id: " + nodeA.nodeID);
		System.out.println("Node B id: " + nodeB.nodeID);
		System.out.println("Node C id: " + nodeC.nodeID);
		System.out.println("Node D id: " + nodeD.nodeID);
		System.out.println("Node E id: " + nodeE.nodeID);
		System.out.println("Node F id: " + nodeF.nodeID);
		System.out.println("Node G id: " + nodeG.nodeID);
		System.out.println("Node H id: " + nodeH.nodeID);
		System.out.println("Node I id: " + nodeI.nodeID);
		System.out.println("Node J id: " + nodeJ.nodeID);
		nodeA.sendJoinRequest(nodeB);
		nodeC.sendJoinRequest(nodeB);
		nodeD.sendJoinRequest(nodeA);
		nodeE.sendJoinRequest(nodeD);
		nodeF.sendJoinRequest(nodeE);
		nodeG.sendJoinRequest(nodeC);
		nodeH.sendJoinRequest(nodeC);
		nodeI.sendJoinRequest(nodeB);
		nodeJ.sendJoinRequest(nodeD);
		System.out.println("Node A's routing table " + nodeA.nodeID);
		nodeA.router.printRoutingTable();
		System.out.println("Node B's routing table " + nodeB.nodeID);
		nodeB.router.printRoutingTable();
		System.out.println("Node C's routing table " + nodeC.nodeID);
		nodeC.router.printRoutingTable();
		System.out.println("Node D's routing table " + nodeD.nodeID);
		nodeD.router.printRoutingTable();
		System.out.println("Node E's routing table "  + nodeE.nodeID);
		nodeE.router.printRoutingTable();
		
		System.out.println("Node F's routing table " + nodeF.nodeID);
		nodeF.router.printRoutingTable();
		System.out.println("Node G's routing table " + nodeG.nodeID);
		nodeG.router.printRoutingTable();
		System.out.println("Node H's routing table " + nodeH.nodeID);
		nodeH.router.printRoutingTable();
		System.out.println("Node I's routing table " + nodeI.nodeID);
		nodeI.router.printRoutingTable();
		System.out.println("Node J's routing table "  + nodeJ.nodeID);
		nodeJ.router.printRoutingTable();
		
		/*
		System.out.println("Node A ID: " + nodeA.nodeID + " suc: " + nodeA.successor.nodeID + " pred: " + nodeA.predecessor.nodeID);
		System.out.println("Key Range: " + nodeA.getKeyRange()[0] + " - " + nodeA.getKeyRange()[1]);
		System.out.println("Node B ID: " + nodeB.nodeID + " suc: " + nodeB.successor.nodeID + " pred: " + nodeB.predecessor.nodeID);
		System.out.println("Key Range: " + nodeB.getKeyRange()[0] + " - " + nodeB.getKeyRange()[1]);
		System.out.println("Node C ID: " + nodeC.nodeID + " suc: " + nodeC.successor.nodeID + " pred: " + nodeC.predecessor.nodeID);
		System.out.println("Key Range: " + nodeC.getKeyRange()[0] + " - " + nodeC.getKeyRange()[1]);
		System.out.println("Node D ID: " + nodeD.nodeID + " suc: " + nodeD.successor.nodeID + " pred: " + nodeD.predecessor.nodeID);
		System.out.println("Key Range: " + nodeD.getKeyRange()[0] + " - " + nodeD.getKeyRange()[1]);
		System.out.println("Node E ID: " + nodeE.nodeID + " suc: " + nodeE.successor.nodeID + " pred: " + nodeE.predecessor.nodeID);
		System.out.println("Key Range: " + nodeE.getKeyRange()[0] + " - " + nodeE.getKeyRange()[1]);
		
		System.out.println("Node F ID: " + nodeF.nodeID + " suc: " + nodeF.successor.nodeID + " pred: " + nodeF.predecessor.nodeID);
		System.out.println("Key Range: " + nodeF.getKeyRange()[0] + " - " + nodeF.getKeyRange()[1]);
		System.out.println("Node G ID: " + nodeG.nodeID + " suc: " + nodeG.successor.nodeID + " pred: " + nodeG.predecessor.nodeID);
		System.out.println("Key Range: " + nodeG.getKeyRange()[0] + " - " + nodeG.getKeyRange()[1]);
		System.out.println("Node H ID: " + nodeH.nodeID + " suc: " + nodeH.successor.nodeID + " pred: " + nodeH.predecessor.nodeID);
		System.out.println("Key Range: " + nodeH.getKeyRange()[0] + " - " + nodeH.getKeyRange()[1]);
		System.out.println("Node I ID: " + nodeI.nodeID + " suc: " + nodeI.successor.nodeID + " pred: " + nodeI.predecessor.nodeID);
		System.out.println("Key Range: " + nodeI.getKeyRange()[0] + " - " + nodeI.getKeyRange()[1]);
		System.out.println("Node J ID: " + nodeJ.nodeID + " suc: " + nodeJ.successor.nodeID + " pred: " + nodeJ.predecessor.nodeID);
		System.out.println("Key Range: " + nodeJ.getKeyRange()[0] + " - " + nodeJ.getKeyRange()[1]);
				*/
		DNode[] nodeList = {nodeA, nodeB, nodeC, nodeD, nodeE, nodeF, nodeG, nodeH, nodeI, nodeJ};
		
		for(int i = 0; i < 500; i++ ) {
		Random r = new Random();
		int val = r.nextInt(10);
		DNodeJoin.updateRoutingTable(nodeList[val]);
		}

		
		System.out.println("Node A's routing table " + nodeA.nodeID);
		nodeA.router.printRoutingTable();
		System.out.println("Node B's routing table " + nodeB.nodeID);
		nodeB.router.printRoutingTable();
		System.out.println("Node C's routing table " + nodeC.nodeID);
		nodeC.router.printRoutingTable();
		System.out.println("Node D's routing table " + nodeD.nodeID);
		nodeD.router.printRoutingTable();
		System.out.println("Node E's routing table "  + nodeE.nodeID);
		nodeE.router.printRoutingTable();
		System.out.println("Node F's routing table " + nodeF.nodeID);
		nodeF.router.printRoutingTable();
		System.out.println("Node G's routing table " + nodeG.nodeID);
		nodeG.router.printRoutingTable();
		System.out.println("Node H's routing table " + nodeH.nodeID);
		nodeH.router.printRoutingTable();
		System.out.println("Node I's routing table " + nodeI.nodeID);
		nodeI.router.printRoutingTable();
		System.out.println("Node J's routing table "  + nodeJ.nodeID);
		
		nodeA.leaveNetwork();
		for(int i = 0; i < nodeList.length; i++ ) {	
			DNodeLeave.updateRoutingTable(nodeList[i], nodeA);
			}

		System.out.println("~~~~~~!!!!!Node A has left the network!!!!!~~~~~~");
		System.out.println("Node A's routing table " + nodeA.nodeID);
		nodeA.router.printRoutingTable();
		System.out.println("Node B's routing table " + nodeB.nodeID);
		nodeB.router.printRoutingTable();
		System.out.println("Node C's routing table " + nodeC.nodeID);
		nodeC.router.printRoutingTable();
		System.out.println("Node D's routing table " + nodeD.nodeID);
		nodeD.router.printRoutingTable();
		System.out.println("Node E's routing table "  + nodeE.nodeID);
		nodeE.router.printRoutingTable();
		System.out.println("Node F's routing table " + nodeF.nodeID);
		nodeF.router.printRoutingTable();
		System.out.println("Node G's routing table " + nodeG.nodeID);
		nodeG.router.printRoutingTable();
		System.out.println("Node H's routing table " + nodeH.nodeID);
		nodeH.router.printRoutingTable();
		System.out.println("Node I's routing table " + nodeI.nodeID);
		nodeI.router.printRoutingTable();
		System.out.println("Node J's routing table "  + nodeJ.nodeID);
		nodeJ.router.printRoutingTable();
		/**
		String fileA = "testing1234567";
		System.out.println("file A fileID: " + ChecksumDemoHashingFunction.hashValue(fileA));		
		nodeA.insert(fileA);
		System.out.println("node A Size " + nodeA.localTable.size());
		System.out.println("node B Size " + nodeB.localTable.size());
		System.out.println("node C Size " + nodeC.localTable.size());
		System.out.println("node D Size " + nodeD.localTable.size());
		System.out.println("node E Size " + nodeE.localTable.size());
		System.out.println("node F Size " + nodeF.localTable.size());
		System.out.println("node G Size " + nodeG.localTable.size());
		System.out.println("node H Size " + nodeH.localTable.size());
		System.out.println("node I Size " + nodeI.localTable.size());
		System.out.println("node J Size " + nodeJ.localTable.size());
		*/
		//String retrieve = nodeA.get(fileA);
		
		//System.out.println(retrieve);
		/*
		//nodeA.remove(fileA);
		System.out.println("node A Size " + nodeA.localTable.size());
		System.out.println("node B Size " + nodeB.localTable.size());
		System.out.println("node C Size " + nodeC.localTable.size());
		System.out.println("node D Size " + nodeD.localTable.size());
		System.out.println("node E Size " + nodeE.localTable.size());
		System.out.println("node F Size " + nodeF.localTable.size());
		System.out.println("node G Size " + nodeG.localTable.size());
		System.out.println("node H Size " + nodeH.localTable.size());
		System.out.println("node I Size " + nodeI.localTable.size());
		System.out.println("node J Size " + nodeJ.localTable.size());
		*/
		
	}

}
