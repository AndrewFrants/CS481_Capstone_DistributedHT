package prototypeClientApp;

//import frontend.KeyValue;

public class Application {

	public static void main(String[] args) {
		
		/*DHClient client = new DHClient();
		
		System.out.println("Get call: " + client.get("test"));
		
		System.out.println("addOrUpdate call: " + client.addOrUpdate(new KeyValue("test", "value")));
		
		client.delete("test");
		*/
		
		NodesClient client = new NodesClient();
		
		System.out.println("Get call: " + client.get("0").getName());
		
		System.out.println("addOrUpdate call: " + client.addOrUpdate("test"));
		
		System.out.println("Get call: " + client.get("test").getName());
		
		client.delete("test");
	}

}
