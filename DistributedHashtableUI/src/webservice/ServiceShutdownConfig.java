package webservice;

import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Configuration;

import nodes.storage.NodesStorage;

@Configuration
public class ServiceShutdownConfig {
	
	NodesStorage nodesStorage;
	
	public ServiceShutdownConfig() {
		this.nodesStorage = new NodesStorage();
	}
	
	@PreDestroy
	public void onShutDown() {
		System.out.println("Shutting down ...");
		this.nodesStorage.writeToStorage(DhtWebService.DhtService.getAllNodes());
		System.out.println("Nodes saved ...");
	 }
}