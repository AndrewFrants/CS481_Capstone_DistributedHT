package webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import service.DHService;

@SpringBootApplication
public class DhtWebService {

	public static DHService DhtService;
	
	public static void main(String[] args) {
		SpringApplication.run(DhtWebService.class, args);
	}

}