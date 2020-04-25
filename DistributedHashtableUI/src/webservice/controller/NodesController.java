package webservice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import service.DHService;
import service.DHashEntry;
import service.DNode;
import webservice.DhtWebService;

@RestController
@RequestMapping("/nodes")
public class NodesController {
   

	public DHService getWS() {
		return DhtWebService.DhtService;
	}
   
   private ResponseEntity<Object> HttpResponse(Object obj) {
	      return new ResponseEntity<>(obj, HttpStatus.OK);
   }
   
   @RequestMapping()
   public ResponseEntity<Object> getEntry() {
	 //Create a new ObjectMapper object
       ObjectMapper mapper = new ObjectMapper();

       List<DNode> nodes = getWS().getAllNodes();
       
       String res = null;
		try {
			res = mapper.writeValueAsString(nodes);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       
      return new ResponseEntity<>(res, HttpStatus.OK);
   }
   
   @RequestMapping(value = "/{id}", method = RequestMethod.GET)
   public ResponseEntity<DNode> get(@PathVariable("id") String id) {

	   DNode node = getWS().findNodeByName(id);
	   Integer hash = Integer.parseInt(id);
	   
	   System.out.println(String.format("Retrieved node: %d from memory, looking for %d", node.nodeID, hash));
	   
	   if (node.nodeID != hash)
	   {
		   DNode networkNode = DhtWebService.dhtServiceInstance.getNode(hash);
		   
		   if (networkNode != null)
		   {
			   node = networkNode;
			   System.out.println(String.format("Memory node did not match issued DHT request, received: %d, expected: %d", node.nodeID, hash));
		   }
	   }
	   
	   return new ResponseEntity<DNode>(node, HttpStatus.OK);
   }
   
   @RequestMapping(value = "/{id}/entries", method = RequestMethod.GET)
   public ResponseEntity<List<DHashEntry>> getEntries(@PathVariable("id") String id) {
	   // TODO: change this to read from instance
	   DNode node = getWS().findNodeByName(id);
	   List<DHashEntry> specificEntries = node.getAllEntries();
	   return new ResponseEntity<List<DHashEntry>>(specificEntries, HttpStatus.OK);
   }
   
   @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
   public ResponseEntity<Object> get(@PathVariable("id") String id, @RequestBody String patchNodeStr) {
	   
       ObjectMapper mapper = new ObjectMapper();
       
       DNode patchNode = null;
       
        try {
        	patchNode = mapper.readValue(patchNodeStr, DNode.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        
        if (patchNode != null) {
		   
        	DNode tableNode = getWS().findNodeByName(patchNode.getName());
		    patchNode.getTable().copyValuesTo(tableNode.getTable());
		   
        }
        
 	   return HttpResponse(HttpStatus.ACCEPTED);
   }
   
   @RequestMapping(value = "hash/{id}", method = RequestMethod.GET)
   public ResponseEntity<Object> getByHash(@PathVariable("id") String id) {
	   DNode node = getWS().findNodeByName(Integer.parseInt(id));
	   
	   return new ResponseEntity<>(node, HttpStatus.OK);
   }

   @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
   public ResponseEntity<Object> delete(@PathVariable("id") String id) {

	   DNode node = getWS().findNodeByName(id);
	   getWS().removeNode(node.getName());
	   
	   return HttpResponse(HttpStatus.NO_CONTENT);
   }
   
   @RequestMapping(method = RequestMethod.POST)
   public ResponseEntity<Object> createNode(@RequestBody DNode newNode) {
	  getWS().addNode(newNode);
      return new ResponseEntity<>("Node is created successfully", HttpStatus.CREATED);
   }
   
   /*
   @RequestMapping(method = RequestMethod.POST)
   public ResponseEntity<Object> createEntry(@RequestBody String newNode) {
	  getWS().addNode(newNode);
      return new ResponseEntity<>("Node is created successfully", HttpStatus.CREATED);
   }
   */
}