package webservice.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import service.DHService;
import service.DNode;
import webservice.DhtWebService;
import webservice.entry.*;
import webservice.entry.Entry;

@RestController
@RequestMapping("/nodes")
public class NodesController {
   

	public DHService getWS() {
		return DhtWebService.DhtService;
	}
	
   static {
	   // initialize mock service
	   DhtWebService.DhtService = DHService.createFiveNodeCluster(); 
   }
   
   private ResponseEntity<Object> HttpResponse(Object obj) {
	      return new ResponseEntity<>(obj, HttpStatus.OK);
   }
   
   @RequestMapping(value = "/nodes")
   public ResponseEntity<Object> getEntry() {
      return new ResponseEntity<>(getWS().getAllNodes(), HttpStatus.OK);
   }
   
   @RequestMapping(value = "/node/{id}", method = RequestMethod.GET)
   public ResponseEntity<Object> delete(@PathVariable("id") String id) { 
	   return HttpResponse(getWS().findNodeByName(id));
   }
   
   @RequestMapping(value = "/nodes", method = RequestMethod.POST)
   public ResponseEntity<Object> createEntry(@RequestBody String newNode) {
	   getWS().addNode(newNode);
      return new ResponseEntity<>("Entry is created successfully", HttpStatus.CREATED);
   }
   
}