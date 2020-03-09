package webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import service.DHServerInstance;
import service.DHService;

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

	public static DHService DhtService;
	
	public static DHServerInstance dhtServiceInstance;
	
	public static void main(String[] args) {
		
		String currentInstanceUrl = "localhost:8080";
		Boolean isFirstInstance = true;
		
		// Read parameters
		for(String arg:args) {
			
			String[] params = arg.split("=",2);
			
			if (params[0].equalsIgnoreCase("--server.port"))
			{
				currentInstanceUrl = String.format("localhost:%s", params[1]);
			}
			else if (params[0].equalsIgnoreCase("--first"))
			{
				isFirstInstance = Boolean.parseBoolean(params[1]);
			}
			else
			{
				System.out.println("Ignored command line: " + arg);
			}
        }
		
		if (isFirstInstance)
		{
			System.out.println("Setting port as 8080 because this is first instance");
			currentInstanceUrl = "localhost:8080";
		}
		
		
		System.out.println(String.format("Instance URL: %s", currentInstanceUrl));
		System.out.println(String.format("First Instance: %s", isFirstInstance.toString()));
		dhtServiceInstance = new DHServerInstance(currentInstanceUrl, isFirstInstance, true);
		
		SpringApplication.run(DhtWebService.class, args);
	}
}
