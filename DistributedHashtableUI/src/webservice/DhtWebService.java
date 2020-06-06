package webservice;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

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

// the web service for distributed hash table
@SpringBootApplication
@ComponentScan
public class DhtWebService {

	//fields initialization
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
	
	//determines whether the port is being used
	public static boolean isPortBeingUsed(String host, int port)
	{
		Socket sock = null;
		try
		{
			sock = new Socket(host, port);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
		finally
		{
			if(sock != null)
				try {sock.close();}
				catch(Exception e){}
		}
	}
	
	//starting the web service
	public static void main(String[] args) {
		
		Integer foundPort = 8080;

		do
		{
			try
			{
				InetAddress currentAddress = null;
				try {
					currentAddress = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String ipAddress = currentAddress.getHostAddress();
				
				String currentInstanceUrl = ipAddress + ":8080";
				firstInstanceAddress = currentInstanceUrl;


				for (int i = 0; i < 100 && isPortBeingUsed(ipAddress, foundPort); i++)
				{
					DhtLogger.log.info("Testing ip: {} port: {}", ipAddress, foundPort);
					foundPort += 1;
				}

				currentInstanceUrl = ipAddress + ":" + foundPort;

				if (foundPort == 8080)
				{
					joinNetwork = false;
					firstInstanceAddress = null;
				}
				else
				{
					joinNetwork = true;
					firstInstanceAddress = ipAddress + ":" + 8080;
				}

				// Read parameters
				for(String arg:args) {
			
					String[] params = arg.split("=",2);
					DhtLogger.log.info("Argument: {} {}", arg, params[0]);

					if (params[0].equalsIgnoreCase("--first.server")) // allows us to run instances from other machines
					{
						DhtLogger.log.info("OVERRIDE DETECTED. Setting first server as: {}", params[1]);
						firstInstanceAddress = params[1];
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
								MDC.put("prefix", DhtWebService.dhtServiceInstance.currentNode.nodeID.toString());
								DhtWebService.dhtServiceInstance.joinNetwork();

							}
						}, 
						3000 
					);
					}
				else
				{
					DhtWebService.dhtServiceInstance = new DHServerInstance(DhtWebService.serverUrl, DhtWebService.joinNetwork, true, null);
					MDC.put("prefix", DhtWebService.dhtServiceInstance.currentNode.nodeID.toString());
					DhtWebService.dhtServiceInstance.joinNetwork();
				}
				
				SpringApplication.run(DhtWebService.class, "--server.port=" + foundPort.toString());
				break;
			}
			catch (Exception ex)
			{
				foundPort++;
			}
		} while (foundPort < 10000);

	}
}
