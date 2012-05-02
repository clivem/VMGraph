/**
 * 
 */
package uk.org.vacuumtube;

import java.util.Scanner;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author clivem
 *
 */
public class SnmpGetStats {

	private final static Logger LOGGER = Logger.getLogger(SnmpGetStats.class);
	
	public SnmpGetStats() {
		Runtime.getRuntime().addShutdownHook(new SnmpGetStatsClose());
	}
	
	private void doClose() {
		LOGGER.info("Closing...");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx  = 
				new ClassPathXmlApplicationContext("classpath:META-INF/spring/*-context.xml");
		ctx.registerShutdownHook();
		
		@SuppressWarnings("unused")
		SnmpGetStats snmpGetStats = new SnmpGetStats();
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

	private final class SnmpGetStatsClose extends Thread {
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			doClose();
		}
	}
}
