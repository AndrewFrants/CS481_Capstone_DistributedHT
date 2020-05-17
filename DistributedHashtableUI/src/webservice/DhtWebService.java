package webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import service.DHServerInstance;
import service.DHService;
import service.DhtLogger;

/*
 * Cmdline reference:
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_211
pushd C:\Users\andreyf\OneDrive\BC\Fall 19\Capstone\DistributedHashtable\DistributedHashtableUI
mvnw spring-boot:run -Drun.arguments="--server.port=8080,--first=true"
 
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_211
pushd C:\Users\andreyf\OneDrive\BC\Fall 19\Capstone\DistributedHashtable\DistributedHashtableUI
mvnw spring-boot:run -Drun.arguments="--server.port=8081,--first=false"

*/
@SpringBootApplication
public class DhtWebService {

	//
	// Use only for testing and when web=false!!!
	public static DHService InMemoryWebService;
	public static String serverUrl;
	public static Boolean joinNetwork;

	public static DHServerInstance dhtServiceInstance;
	
	static {
		// initialize mock service
		DhtWebService.InMemoryWebService = new DHService(false); // DHService.createFiveNodeCluster(false);
	}
	   
	public static void main(String[] args) {
		
		String currentInstanceUrl = "localhost:8080";
		Boolean joinNetwork = false;
		
		// Read parameters
		for(String arg:args) {
			
			
			String[] params = arg.split("=",2);
			System.out.println("Argument: " + arg + " " + params[0]);
			
			if (params[0].equalsIgnoreCase("--server.port"))
			{
				currentInstanceUrl = String.format("localhost:%s", params[1]);

				if (params[1].contentEquals("8080"))
				{
					System.out.println("Setting joinNetwork=false first instance");
					joinNetwork = false;
				}
			}
			else if (params[0].equalsIgnoreCase("--join"))
			{
				joinNetwork = true;
			}
			else
			{
				System.out.println("Ignored command line: " + arg);
			}
        }
		
		if (!joinNetwork)
		{
			System.out.println("Setting port as 8080 because this is first instance");
			currentInstanceUrl = "localhost:8080";
		}

		DhtLogger.log.info("--- ENTRY POINT ---: Instance URL: {} firstInstanceSetting={}", currentInstanceUrl, joinNetwork);

		DhtWebService.serverUrl = currentInstanceUrl;
		DhtWebService.joinNetwork = joinNetwork;

		if (DhtWebService.joinNetwork)
		{
			new java.util.Timer().schedule( 
				new java.util.TimerTask() {
					@Override
					public void run() {
						DhtWebService.dhtServiceInstance = new DHServerInstance(DhtWebService.serverUrl, DhtWebService.joinNetwork, true);
						DhtWebService.dhtServiceInstance.joinNetwork();
					}
				}, 
				10000 
			);
			}
		else
		{
			DhtWebService.dhtServiceInstance = new DHServerInstance(DhtWebService.serverUrl, DhtWebService.joinNetwork, true);
			DhtWebService.dhtServiceInstance.joinNetwork();
		}

		SpringApplication.run(DhtWebService.class, args);

	}
}
