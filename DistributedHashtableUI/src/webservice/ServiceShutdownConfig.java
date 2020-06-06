package webservice;

import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Configuration;

import nodes.storage.NodesStorage;
import service.DhtLogger;

// the shut down configuration for the web service
@Configuration
public class ServiceShutdownConfig {
	
	//nodes
	NodesStorage nodesStorage;
	
	//constructor
	public ServiceShutdownConfig() {
		this.nodesStorage = new NodesStorage();
	}
	
	//shutting down and saving nodes to storage
	@PreDestroy
	public void onShutDown() {
		DhtLogger.log.info("Shutting down ...");
		//this.nodesStorage.writeToStorage();
		DhtLogger.log.info("Nodes saved ...");
	 }
}