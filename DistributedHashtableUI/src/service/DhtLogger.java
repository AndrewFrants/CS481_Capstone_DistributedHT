/**
 * 
 */
package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author andreyf
 *
 */
public class DhtLogger {
	
	static public Logger log = LoggerFactory.getLogger(DHServerInstance.class);

	
	public static void setLoggingLevel(ch.qos.logback.classic.Level level) {
	    ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
	    root.setLevel(level);
	}
}
