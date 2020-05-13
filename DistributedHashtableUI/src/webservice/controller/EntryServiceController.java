package webservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/entries")
public class EntryServiceController {
	
	public DHService getWS() {
		return null; //DhtWebService.DhtService;
	}

	//prints all entries   
	@RequestMapping(method = RequestMethod.GET)
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
	   @RequestMapping(value = "{id}", method = RequestMethod.GET)
	   public ResponseEntity<DHashEntry> get(@PathVariable("id") String id) {
		   DNode node = getWS().findNodeByName(id);
		   Integer hash = Integer.parseInt(id);
		   DHashEntry result = null;
		   
		   if (node.getTable().getLocalHT().containsKey(hash))
		   {
			   result = node.getTable().getEntry(hash);
		   }
		   else
		   {
			   result = DhtWebService.dhtServiceInstance.getEntry(id);
		   }
		   
		   if (result != null)
		   {
			   return new ResponseEntity<DHashEntry>(result, HttpStatus.OK);
		   }
		   
		   return new ResponseEntity(HttpStatus.NOT_FOUND);
	   }
	
	   //deletes entries
	   @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	   public ResponseEntity<Object> delete(@PathVariable("id") String id) {
		   getWS().RemoveEntry(id);
		   return new ResponseEntity<>("Entry is deleted successfully", HttpStatus.OK);
	   }
	   
	   
	   //creates an  entry
	   @RequestMapping(method = RequestMethod.POST)
	   public ResponseEntity<Object> createEntry(@RequestBody DHashEntry newNode) {

		   DhtWebService.dhtServiceInstance.addEntry(/*TODO pass DHashEntry in */newNode.value);
		   
		   return new ResponseEntity<>("Entry is created successfully", HttpStatus.CREATED);
	   }
	   
	   // updates an entry
	   @RequestMapping(value = "{id}", method = RequestMethod.PUT)
	   public ResponseEntity<Object> updateEntry(
			   @PathVariable("id") int id,
			   @RequestBody DHashEntry updatedEntry) {

		   DhtWebService.dhtServiceInstance.updateEntry(id, updatedEntry.value);
		   
		   return new ResponseEntity<>("Entry is updated successfully", HttpStatus.OK);
	   }
	   
}