/**
 * 
 */
package uk.org.vacuumtube;

import java.util.Scanner;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import uk.org.vacuumtube.spring.WebServerConfiguration;

/**
 * @author clivem
 *
 */
public class WebStatsService {

	private final static Logger LOGGER = Logger.getLogger(WebStatsService.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final AnnotationConfigApplicationContext ctx = 
				new AnnotationConfigApplicationContext(WebServerConfiguration.class);
		ctx.registerShutdownHook();
		
		Scanner scanner = new Scanner(System.in);
		while (true) {
			String cmd = scanner.nextLine();
			LOGGER.info("STDIN: [" + cmd + "]");
			if ("quit".equals(cmd)) {
				break;
			}
		}
		System.exit(0);
	}
}
