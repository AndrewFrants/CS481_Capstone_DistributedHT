package frontend;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DhtController {
	
    @GetMapping("/dht/{key}")
    @ResponseBody
    public String get(@PathVariable(value = "key") String key) {
        return key;
    }
    
    @PostMapping("/dht")
    @ResponseBody
    public String addOrUpdate(@RequestBody KeyValue keyValue) {
        return keyValue.getValue();
    }

    @DeleteMapping("/dht/{key}")
    @ResponseBody
    public Boolean delete(@PathVariable(value = "key") String key) {
        return true;
    }
}
