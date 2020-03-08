package webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import service.DHServerInstance;
import service.DHService;

@SpringBootApplication
public class DhtWebService {

	public static DHService DhtService;
	
	public static void main(String[] args) {
		// Read parameters
		for(String arg:args) {
            System.out.println("Received CommandLine: " + arg);
        }
		
		SpringApplication.run(DhtWebService.class, args);
	}
}
