package webservice.controller;

import java.util.LinkedList;
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

import data.IDhtNodes;
import data.WebServiceNodes;
import service.ChecksumDemoHashingFunction;
import service.DHServerInstance;
import service.DHashEntry;
//import service.DHashtable;
import service.DNode;
import service.DhtLogger;
import service.FormatUtilities;
import webservice.DhtWebService;

@RestController
@RequestMapping("/entries")
public class EntryServiceController {
	
	// Nodes interface
	IDhtNodes dhtNodes;

	public DHServerInstance getWS() {
		return DhtWebService.dhtServiceInstance;
	}

	//prints all entries   
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Object> getallentries() {
		
		//Create a new ObjectMapper object

		final List<DHashEntry> entries = new LinkedList<DHashEntry>();

		final DNode head = getWS().currentNode;
		DNode currNode = getWS().currentNode;

		do {
			final List<DHashEntry> entriesOfThisNode = currNode.getAllEntries();

			entries.addAll(entriesOfThisNode); // get all entries from this node

			DhtLogger.log.info("Added entries, count: {} from node: {}", entriesOfThisNode.size(), currNode.nodeID);

			if (currNode.successor.equals(head) || currNode.successor == null) {
				if (currNode.successor != null) {
					DhtLogger.log.info("Get all entries, reached end of ring at: {} successor: {}", currNode.nodeID,
							currNode.successor.nodeID);
				} else {
					DhtLogger.log.info("Get all entries, reached end of ring at: {} successor: NULL", currNode.nodeID);
				}
				break;
			}

			DhtLogger.log.info("Current node: {} getting successor: {}", currNode.nodeID, currNode.successor.nodeID);

			// next node
			currNode = NodesController.internalGetNode(currNode.successor, true);

		} while (true);

		DhtLogger.log.info("Get all entries, returning count of {} nodes", entries.size());

		return ControllerHelpers.HttpResponseObjectOrError(ControllerHelpers.SerializeToString("DhEntries", entries));
	}

	private DNode findNodeInRangeOf(final Integer entryHash, final Boolean refreshNode) {
		final DNode head = getWS().currentNode;
		DNode currNode = getWS().currentNode;

		String predecessorID = "NULL";

		if (currNode.predecessor != null)
		{
			predecessorID = currNode.predecessor.nodeID.toString();
		}

		if ((currNode.predecessor != null && currNode.predecessor.nodeID <= entryHash)) // check if predecessor
		{
			DhtLogger.log.info("Found that hash: {} is in range of node: {} predecessor: {}", entryHash,
					currNode.nodeID, predecessorID);

			if (refreshNode) {
				// refresh the predecessor
				currNode.predecessor = NodesController.internalGetNode(currNode.predecessor, true);
			}

			return currNode.predecessor;
		}

		do {

			String successorId = "NULL";

			if (currNode.successor != null)
			{
				successorId = currNode.successor.nodeID.toString();
			}

			if (currNode.nodeID <= entryHash && (currNode.successor == null || currNode.successor.nodeID > entryHash)) {
				DhtLogger.log.info("Found that hash: {} is in range of node: {} successor: {}", entryHash,
						currNode.nodeID, successorId);
				return currNode;
			}

			if (currNode.successor.equals(head) || currNode.successor == null)
			{
				DhtLogger.log.info("Reached end of ring at: {} successor: {}", currNode.nodeID, successorId);
				return head;
			}

			DhtLogger.log.info("[NodeSearch] Current node: {} getting successor: {}", currNode.nodeID,
					currNode.successor.nodeID);

			// next node
			currNode = NodesController.internalGetNode(currNode.successor, true);

		} while (true);
	}

	// gets all entries
	@RequestMapping(value = "{id}/all", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllNodeEntries(@PathVariable("id") final String id) {

		final Integer hash = FormatUtilities.SafeConvertStrToInt(id);

		if (hash == null) {
			return ControllerHelpers.HttpResponse("Provided id " + id + " coulnt be converted to an integer type.",
					HttpStatus.BAD_REQUEST);
		}

		final DNode ownerNode = findNodeInRangeOf(hash, true);

		if (ownerNode == null) {
			DhtLogger.log.error("Owner node wasnt found for hash {}", id);

			return ControllerHelpers.HttpResponse("Couldn't find owner node.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		final List<DHashEntry> entries = ownerNode.getAllEntries();

		if (entries != null) {
			return ControllerHelpers.HttpResponseObjectOrError(ControllerHelpers.SerializeToString("List of DHEntries", entries));
		}

		return ControllerHelpers.HttpResponse("Provided id " + id + " couldnt be found.", HttpStatus.NOT_FOUND);
	}
	
	// gets entry
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> get(@PathVariable("id") final String id) {

		final Integer hash = FormatUtilities.SafeConvertStrToInt(id);

		if (hash == null) {
			return ControllerHelpers.HttpResponse("Provided id " + id + " coulnt be converted to an integer type.",
					HttpStatus.BAD_REQUEST);
		}
		final DNode ownerNode = findNodeInRangeOf(hash, true);

		if (ownerNode == null) {
			DhtLogger.log.error("Owner node wasnt found for hash {}", id);

			return ControllerHelpers.HttpResponse("Couldn't find owner node.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		final DHashEntry entry = ownerNode.getEntry(hash);

		if (entry != null) {
			return ControllerHelpers.HttpResponseObjectOrError(ControllerHelpers.SerializeToString("DHEntry", entry));
		}

		return ControllerHelpers.HttpResponse("Provided id " + id + " couldnt be found.", HttpStatus.NOT_FOUND);
	}

	// creates an entry
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> createEntry(@RequestBody final DHashEntry newEntry) {

		if (newEntry.key == null)
		{
			newEntry.key = ChecksumDemoHashingFunction.hashValue(newEntry.value);
		}

		final DNode ownerNode = findNodeInRangeOf(newEntry.key, true);

		if (ownerNode == null) {
			DhtLogger.log.error("Owner node wasnt found for hash {}", newEntry.key);

			return ControllerHelpers.HttpResponse("Couldn't find owner node.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		ownerNode.AssignKeys(newEntry);

		WebServiceNodes connection = WebServiceNodes.getProxyFor(ownerNode);

		connection.updateNode(ownerNode);

		return new ResponseEntity<>("Entry is created successfully", HttpStatus.CREATED);
	}

	// removes an entry
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteByEntry(@RequestBody final DHashEntry updatedEntry) {

		return deleteById(updatedEntry.key.toString());
	}

	// deletes entries
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteById(@PathVariable("id") final String id) {

		final Integer hash = FormatUtilities.SafeConvertStrToInt(id);

		if (hash == null) {
			return ControllerHelpers.HttpResponse("Provided id " + id + " couldnt be converted to an integer type.",
					HttpStatus.BAD_REQUEST);
		}
		
		final DNode ownerNode = findNodeInRangeOf(hash, true);

		DhtLogger.log.info("Deleting id {} from node {}", hash, ownerNode.nodeID);

		if (ownerNode == null) {
			DhtLogger.log.error("Owner node wasnt found for hash {}", hash);

			return ControllerHelpers.HttpResponse("Couldn't find owner node.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (ownerNode.remove(hash))
		{
			WebServiceNodes connection = WebServiceNodes.getProxyFor(ownerNode);

			connection.updateNode(ownerNode);

			return new ResponseEntity<>("Entry is deleted successfully", HttpStatus.OK);
		}

		return new ResponseEntity<>("Entry was not found to be deleted.", HttpStatus.NOT_FOUND);
	}

	// updates an entry
	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateEntry(@PathVariable("id") final Integer hash,
			@RequestBody final DHashEntry updatedEntry) {

			if (hash == null) {
				return ControllerHelpers.HttpResponse("Provided id " + hash + " coulnt be converted to an integer type.",
						HttpStatus.BAD_REQUEST);
			}
			
			final DNode ownerNode = findNodeInRangeOf(hash, true);
	
			if (ownerNode == null) {
				DhtLogger.log.error("Owner node wasnt found for hash {}", hash);
	
				return ControllerHelpers.HttpResponse("Couldn't find owner node.", HttpStatus.INTERNAL_SERVER_ERROR);
			}

			ownerNode.UpdateEntries(updatedEntry);
		   
		   return new ResponseEntity<>("Entry is updated successfully", HttpStatus.OK);
	   }
	   
}