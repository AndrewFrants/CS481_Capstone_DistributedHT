package webservice;

import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Configuration;

import nodes.storage.NodesStorage;
import service.DHServerInstance;
import service.DhtLogger;

@Configuration
public class ServiceShutdownConfig {
	
	NodesStorage nodesStorage;
	
	public ServiceShutdownConfig() {
		this.nodesStorage = new NodesStorage();
	}
	
	@PreDestroy
	public void onShutDown() {
		DhtLogger.log.info("Shutting down ...");
		//this.nodesStorage.writeToStorage();
		DhtLogger.log.info("Nodes saved ...");
	 }
}