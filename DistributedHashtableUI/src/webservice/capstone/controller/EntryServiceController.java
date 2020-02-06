package webservice.capstone.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import webservice.capstone.entry.*;

@RestController
public class EntryServiceController {
   private static Map<String, Entry> entryRepo = new HashMap<>();
   static {
      Entry honey = new Entry();
      honey.setId("1");
      honey.setName("Honey");
      entryRepo.put(honey.getId(), honey);
      
      Entry almond = new Entry();
      almond.setId("2");
      almond.setName("Almond");
      entryRepo.put(almond.getId(), almond);
   }
   
   @RequestMapping(value = "/entries/{id}", method = RequestMethod.DELETE)
   public ResponseEntity<Object> delete(@PathVariable("id") String id) { 
	   entryRepo.remove(id);
      return new ResponseEntity<>("Entry is deleted successsfully", HttpStatus.OK);
   }
   
   @RequestMapping(value = "/entries/{id}", method = RequestMethod.PUT)
   public ResponseEntity<Object> updateEntry(@PathVariable("id") String id, @RequestBody Entry entry) { 
	   entryRepo.remove(id);
	   entry.setId(id);
      entryRepo.put(id, entry);
      return new ResponseEntity<>("Entry is updated successsfully", HttpStatus.OK);
   }
   
   @RequestMapping(value = "/entries", method = RequestMethod.POST)
   public ResponseEntity<Object> createEntry(@RequestBody Entry entry) {
	   entryRepo.put(entry.getId(), entry);
      return new ResponseEntity<>("Entry is created successfully", HttpStatus.CREATED);
   }
   
   @RequestMapping(value = "/entries")
   public ResponseEntity<Object> getEntry() {
      return new ResponseEntity<>(entryRepo.values(), HttpStatus.OK);
   }
}