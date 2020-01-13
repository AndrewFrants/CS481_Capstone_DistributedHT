package frontend;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
//import redis.clients.jedis.Jedis;

@RestController
public class MessageStoreController {

    private final String redis_host = System.getenv("REDIS_HOST");
    private final int redis_port = Integer.parseInt(System.getenv("REDIS_PORT"));
    //private final Jedis jedis = new Jedis(redis_host, redis_port);

    @PostMapping("/messages")
    public Digest postMessage(@RequestBody Message message) throws java.security.NoSuchAlgorithmException {

    	/*
        // Digest string encoding
        String text = message.getMessage();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            sb.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
        }
        String encoded = sb.toString();

        // Save to Redis k, v = encoded, message
        jedis.set(encoded, text);

        return new Digest(encoded);
        */
    	
    	return null;
    }

    @RequestMapping("/messages/{digest}")
    public Message getMessage(@PathVariable final String digest) {

    	/*
        String message = jedis.get(digest);

        if (message == null || message.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Digest not found"
              );              
        }        

        return new Message(message);
        */
    	
    	return null;
    }
}
