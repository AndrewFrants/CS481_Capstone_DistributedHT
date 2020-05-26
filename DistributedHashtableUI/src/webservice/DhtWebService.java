package webservice;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
	public static String firstInstanceAddress;
	
	public static DHServerInstance dhtServiceInstance;
	
	static {
		// initialize mock service
		DhtWebService.InMemoryWebService = new DHService(false); // DHService.createFiveNodeCluster(false);
	}
	   
	public static void main(String[] args) {
		
		Boolean joinNetwork = false;
		String firstInstanceAddress = null;
		
		InetAddress currentAddress = null;
		try {
			currentAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String currentInstanceUrl = currentAddress + ":8080";
		
		// Read parameters
		for(String arg:args) {
			
			String[] params = arg.split("=",2);
			System.out.println("Argument: " + arg + " " + params[0]);
			
			if (params[0].equalsIgnoreCase("--server.port"))
			{
				String ipAddress = currentAddress.getHostAddress();
				
				currentInstanceUrl = String.format(ipAddress + ":%s", params[1]);

				if (currentInstanceUrl.contains("8080"))
				{
					System.out.println("Setting joinNetwork=false first instance");
					joinNetwork = false;
				}
			}
			else if (params[0].equalsIgnoreCase("--first.server"))
			{
				DhtLogger.log.info("Setting first server as: {}", params[1]);

				firstInstanceAddress = params[1];

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

		DhtLogger.log.info("--- ENTRY POINT ---: Instance URL: {} firstInstanceSetting={} firstInstanceAddress={}", currentInstanceUrl, joinNetwork, firstInstanceAddress);

		DhtWebService.serverUrl = currentInstanceUrl;
		DhtWebService.joinNetwork = joinNetwork;
		DhtWebService.firstInstanceAddress = firstInstanceAddress;
		
		if (DhtWebService.joinNetwork)
		{
			new java.util.Timer().schedule( 
				new java.util.TimerTask() {
					@Override
					public void run() {
						DhtWebService.dhtServiceInstance = new DHServerInstance(DhtWebService.serverUrl, DhtWebService.joinNetwork, true, DhtWebService.firstInstanceAddress);
						DhtWebService.dhtServiceInstance.joinNetwork();
					}
				}, 
				10000 
			);
			}
		else
		{
			DhtWebService.dhtServiceInstance = new DHServerInstance(DhtWebService.serverUrl, DhtWebService.joinNetwork, true, null);
			DhtWebService.dhtServiceInstance.joinNetwork();
		}

		SpringApplication.run(DhtWebService.class, args);

	}
}
