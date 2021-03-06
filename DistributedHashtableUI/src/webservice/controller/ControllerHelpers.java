package webservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import service.DhtLogger;

//the helpers for the entry service controller and node controller
public class ControllerHelpers {
	
	final static ObjectMapper mapper = new ObjectMapper();

	//http response
	public static ResponseEntity<Object> HttpResponse(final String text, final HttpStatus status) {
		return new ResponseEntity<>(text, status);
	}
	
	// Respond with error code
	public static ResponseEntity<Object> HttpResponse(final Object obj) {
			return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	//serialize to string
	public static String SerializeToString(String serializingName, Object obj)
	{
		String res = null;
		try {
			res = mapper.writeValueAsString(obj);
		} catch (final JsonProcessingException e) {
			DhtLogger.log.error("Failed serializing {} object: {} exception: {}", serializingName, obj, e.toString());
		}

		return res;
	}

	//http response with returns error message or OK message
	public static ResponseEntity<Object> HttpResponseObjectOrError(String res)
	{
		if (res == null)
		{
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
}