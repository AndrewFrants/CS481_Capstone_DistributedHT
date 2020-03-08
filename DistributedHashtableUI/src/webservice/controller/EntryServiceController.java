package webservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import service.DHService;
import service.DHashEntry;
//import service.DHashtable;
import service.DNode;
import webservice.DhtWebService;

@RestController
public class EntryServiceController {
	
	public DHService getWS() {
		return DhtWebService.DhtService;
	}

	static {
		   // initialize mock service
		   DhtWebService.DhtService = DHService.createFiveNodeCluster(false);
	   }

	//prints all entries   
	@RequestMapping(value = "entries", method = RequestMethod.GET)
	   public ResponseEntity<List <List <DHashEntry>>> getallentries() {
		   List<DNode> nodes = getWS().getAllNodes();
		   List<List<DHashEntry>> list = new ArrayList<List<DHashEntry>>();

		   for (int i = 0; i < nodes.size(); i++) {
			   list.add(nodes.get(i).getAllEntries());
		   }
		   return new ResponseEntity<List <List <DHashEntry>>>(list, HttpStatus.OK);
	   }
	
//	@RequestMapping(value = "entries", method = RequestMethod.GET)
//	   public ResponseEntity<DHashtable> getallentries() {
//		   List<DNode> nodes = getWS().getAllNodes();
//		   DHashtable list = new DHashtable();
//
//		   for (int i = 0; i < nodes.size(); i++) {
//			   list.copyValuesTo(nodes.get(i).getTable()); 
//		   }
//		   return new ResponseEntity<>(list, HttpStatus.OK);
//	   }
//		
	   //prints entries for a specific node
	   @RequestMapping(value = "entries/{id}", method = RequestMethod.GET)
	   public ResponseEntity<List<DHashEntry>> get(@PathVariable("id") String id) {
		   DNode node = getWS().findNodeByName(id);
		   List<DHashEntry> specificEntries = node.getAllEntries();
		   return new ResponseEntity<List<DHashEntry>>(specificEntries, HttpStatus.OK);
	   }
	
	   //deletes entries
	   @RequestMapping(value = "entries/{id}", method = RequestMethod.DELETE)
	   public ResponseEntity<Object> delete(@PathVariable("id") String id) {
		   getWS().RemoveEntry(id);
		   return new ResponseEntity<>("Entry is deleted successfully", HttpStatus.OK);
	   }
	   
	   
	   //creates an  entry
	   @RequestMapping(value = "entries/{id}", method = RequestMethod.POST)
	   public ResponseEntity<Object> createEntry(@PathVariable("id") String id) {
			
		   return new ResponseEntity<>("Entry is created successfully", HttpStatus.CREATED);
	   }
	   
}