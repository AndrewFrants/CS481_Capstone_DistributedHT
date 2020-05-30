package webservice.controller;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import data.IDhtEntries;
import data.IDhtNodes;
import data.WebServiceNodes;
import service.AssertUtilities;
import service.ChecksumDemoHashingFunction;
import service.DHServerInstance;
import service.DHashEntry;
import service.DNode;
import service.DhtLogger;
import service.FormatUtilities;
import webservice.DhtWebService;

@RestController
@RequestMapping("/nodes")
public class NodesController {
   
	// Nodes interface
	IDhtNodes dhtNodes;

	IDhtEntries dhtEntries;

	public static DHServerInstance getWS() {
		MDC.put("prefix", DhtWebService.dhtServiceInstance.currentNode.nodeID.toString());
		return DhtWebService.dhtServiceInstance;
	}
   
	@RequestMapping()
	public ResponseEntity<Object> getAllNodes() {
		//Create a new ObjectMapper object
		final ObjectMapper mapper = new ObjectMapper();

		List<DNode> nodes = new LinkedList<DNode>();

		DNode head = getWS().currentNode;
		DNode currNode = getWS().currentNode;

		nodes.add(currNode);
		
		do
		{
			if (currNode.successor == null || currNode.successor.equals(head))
			{
				if (currNode.successor != null)
				{
					DhtLogger.log.info("Get all nodes, reached end of ring at: {} successor: {}", currNode.nodeID, currNode.successor.nodeID);
				}
				else
				{
					DhtLogger.log.info("Get all nodes, reached end of ring at: {} successor: NULL", currNode.nodeID);
				}
				break;
			}
		
			DhtLogger.log.info("Current node: {} getting successor: {}", currNode.nodeID, currNode.successor.nodeID);

			// next node
			currNode = internalGetNode(currNode.successor, true);
			
			if (!nodes.contains(currNode))
			{
				nodes.add(currNode);
			}

		} while (true);
		
		currNode = getWS().currentNode;

		do
		{
			if (currNode.predecessor == null || currNode.predecessor.equals(head))
			{
				if (currNode.predecessor != null)
				{
					DhtLogger.log.info("Get all nodes, reached end of ring at: {} predecessor: {}", currNode.nodeID, currNode.predecessor.nodeID);
				}
				else
				{
					DhtLogger.log.info("Get all nodes, reached end of ring at: {} predecessor: NULL", currNode.nodeID);
				}
				break;
			}
			
			DhtLogger.log.info("Current node: {} getting predecessor: {}", currNode.nodeID, currNode.predecessor.nodeID);

			// next node
			currNode = internalGetNode(currNode.predecessor, true);
			
			if (!nodes.contains(currNode))
			{
				nodes.add(currNode);
			}

		} while (true);

		String res = null;
		try {
			res = mapper.writeValueAsString(nodes);
		} catch (final JsonProcessingException e) {
			DhtLogger.log.error("Failed serializing node list: {}", e.toString());
		}
		
		DhtLogger.log.info("Get all nodes, returning count of {} nodes", nodes.size());

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<DNode> get(@PathVariable("id") final String id) {

		final DNode foundNode = internalGetNode(id, true);
		if (foundNode == null) {
			return new ResponseEntity<DNode>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<DNode>(foundNode, HttpStatus.OK);
	}

	@RequestMapping(value = "/self", method = RequestMethod.GET)
	public ResponseEntity<DNode> getSelf() {
		return new ResponseEntity<DNode>(getWS().currentNode, HttpStatus.OK);
	}

	private DNode internalGetNode(final Integer id, final Boolean traceError) {
		return internalGetNode(id, false, traceError);
	}

	private DNode internalGetNode(final Integer id, final Boolean usingStringSearch, final Boolean traceError) {
		AssertUtilities.ThrowIfNull(id, "id was null");

		return internalGetNode(getWS(), id, usingStringSearch, traceError);
	}

	public static DNode internalGetNode(DNode node, final Boolean traceError) {

		AssertUtilities.ThrowIfNull(node, "node was null");

		DHServerInstance dhInstance = getWS();

		if (dhInstance.currentNode.nodeID.equals(node.nodeID))
		{
			return dhInstance.currentNode;
		}

		IDhtNodes nodeProxy = dhInstance.dhtNodes.createProxyFor(node);
		return nodeProxy.getNode();
	}

	public static DNode internalGetNode(DHServerInstance dhInstance, final Integer id, final Boolean usingStringSearch, final Boolean traceError) {
		AssertUtilities.ThrowIfNull(id, "id was null");
		AssertUtilities.ThrowIfNull(dhInstance, "dhInstance was null");
		
		if (dhInstance.currentNode.nodeID.equals(id))
		{
			return dhInstance.currentNode;
		}

		final DNode foundNode = dhInstance.getNode(id);
		
		if (foundNode == null && traceError)
		{
			DhtLogger.log.info("node requested id: {} not found on the network. Using string search = {} (hashed string id). Responding with 404", id, usingStringSearch);
		}

		return foundNode;
	}

	private DNode internalGetNode(final String id, final Boolean traceError) {
		AssertUtilities.ThrowIfNull(id, "id was null");

		Integer idAsInt = FormatUtilities.SafeConvertStrToInt(id);
		Boolean usingStringSearch = false;

		DhtLogger.log.info("Searching for node for node={} asInt=[{}].", id, idAsInt);

		if (idAsInt == null) {
			idAsInt = ChecksumDemoHashingFunction.hashValue(id);
			usingStringSearch = true;
		}

		if (getWS().currentNode.nodeID.equals(idAsInt))
		{
			DhtLogger.log.info("Returning self.");
			return getWS().currentNode;
		}

		final DNode foundNode = internalGetNode(idAsInt, usingStringSearch, traceError);

		return foundNode;
	}

	@RequestMapping(value = "/{id}/entries", method = RequestMethod.GET)
	public ResponseEntity<List<DHashEntry>> getEntries(@PathVariable("id") final String id) {

		final DNode node = internalGetNode(id, true);

		if (node == null)
		{
			return new ResponseEntity<List<DHashEntry>>(HttpStatus.NOT_FOUND);
		}

		final List<DHashEntry> specificEntries = node.getAllEntries();
		return new ResponseEntity<List<DHashEntry>>(specificEntries, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> put(@PathVariable("id") final String id, @RequestBody final String patchNodeStr) {
		
		final ObjectMapper mapper = new ObjectMapper();
		
		DNode patchNode = null;
		
		try {
			patchNode = mapper.readValue(patchNodeStr, DNode.class);
		} catch (final JsonMappingException e) {
			e.printStackTrace();
		} catch (final JsonProcessingException e) {
			e.printStackTrace();
		}
		
		if (patchNode == null)
		{
			DhtLogger.log.error("Patch node couldnt be deserialized: {}, returning 4xx.", patchNodeStr);
			return ControllerHelpers.HttpResponse(HttpStatus.BAD_REQUEST);
		}

		DNode networkNode = getWS().currentNode;
		
		if (!patchNode.nodeID.equals(networkNode.nodeID))
		{
			networkNode = internalGetNode(patchNode, true);
		}
		
		if (networkNode == null)
		{
			DhtLogger.log.error("Patch node {} couldnt be found on the network from {}, returning 4xx.", patchNode.getName(), getWS().currentNode.getName());
			return ControllerHelpers.HttpResponse(HttpStatus.NOT_FOUND);
		}
		/*
		if (networkNode.version > patchNode.version)
		{
			DhtLogger.log.error("Patch node couldnt be completed because the patch node version {} was below expected {}, returning 417.", patchNode.version, networkNode.version);
			return ControllerHelpers.HttpResponse(HttpStatus.EXPECTATION_FAILED);
		}
		*/
		
		DhtLogger.log.info("Patching node: {} successor: {} predecessor: {} patchedNodeStr: {}", patchNode.nodeID, patchNode.successor, patchNode.predecessor, patchNodeStr);

		if (!patchNode.nodeID.equals(getWS().currentNode.nodeID)) // no network call if itself!
		{
			WebServiceNodes connection = WebServiceNodes.getProxyFor(patchNode);
			connection.updateNode(patchNode);
		}
		else
		{
			getWS().currentNode.successor = patchNode.successor;
			getWS().currentNode.predecessor = patchNode.predecessor;
			getWS().currentNode.getTable().copyValuesTo(patchNode.getTable());
			getWS().currentNode.version += 1;
		}

		return ControllerHelpers.HttpResponse(HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<Object> patch(@PathVariable("id") final String id, @RequestBody final String patchNodeStr) {
		
		final ObjectMapper mapper = new ObjectMapper();
		
		DNode patchNode = null;
		
		try {
			patchNode = mapper.readValue(patchNodeStr, DNode.class);
		} catch (final JsonMappingException e) {
			e.printStackTrace();
		} catch (final JsonProcessingException e) {
			e.printStackTrace();
		}
		
		DhtLogger.log.info("Patching node: {} successor: {} predecessor: {}", patchNode.nodeID, patchNode.successor, patchNode.predecessor);

		if (patchNode == null)
		{
			DhtLogger.log.error("Patch node couldnt be deserialized: {}, returning 4xx.", patchNodeStr);
			return ControllerHelpers.HttpResponse(HttpStatus.BAD_REQUEST);
		}

		final DNode networkNode = internalGetNode(patchNode, true);

		/*
		if (networkNode.version > patchNode.version)
		{
			DhtLogger.log.error("Patch node couldnt be completed because the patch node version {} was below expected {}, returning 417.", patchNode.version, networkNode.version);
			return ControllerHelpers.HttpResponse(HttpStatus.EXPECTATION_FAILED);
		}
		 */
		if (networkNode == null)
		{
			DhtLogger.log.error("Patch node {} couldnt be found on the network from {}, returning 4xx.", patchNode.getName(), getWS().currentNode.getName());
			return ControllerHelpers.HttpResponse(HttpStatus.NOT_FOUND);
		}

		patchNode.getTable().copyValuesTo(networkNode.getTable());

		if (!patchNode.nodeID.equals(getWS().currentNode.nodeID)) // no network call if itself!
		{
			WebServiceNodes connection = WebServiceNodes.getProxyFor(patchNode);

			connection.updateNode(networkNode);
		}

		return ControllerHelpers.HttpResponse(HttpStatus.OK);
	}

	@RequestMapping(value = "hash/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getByHash(@PathVariable("id") final String id) {
		final DNode node = internalGetNode(id, true);
		
		return new ResponseEntity<>(node, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> delete(@PathVariable("id") final String id) {

		DNode nodeBeingRemoved = new DNode();
		nodeBeingRemoved.nodeID = FormatUtilities.SafeConvertStrToInt(id);
		
		if (nodeBeingRemoved.nodeID == null)
		{
			return ControllerHelpers.HttpResponse(HttpStatus.BAD_REQUEST);
		}
		
		DhtLogger.log.info("Removing node {}.", nodeBeingRemoved.nodeID);
		getWS().removeNode(nodeBeingRemoved);
		
		DhtLogger.log.info("Removed node {} successfully.", nodeBeingRemoved.nodeID);
		return ControllerHelpers.HttpResponse(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> createNode(@RequestBody final DNode newNode) {
		getWS().addNode(newNode);
		return new ResponseEntity<>("Node is created successfully", HttpStatus.CREATED);
	}
}