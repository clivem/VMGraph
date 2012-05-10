/**
 * 
 */
package uk.org.vacuumtube;

import java.util.Scanner;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import uk.org.vacuumtube.spring.DatabaseConfiguration;
import uk.org.vacuumtube.spring.ScheduleConfiguration;

/**
 * @author clivem
 *
 */
public class SnmpGetStats {

	private final static Logger LOGGER = Logger.getLogger(SnmpGetStats.class);

	public static ApplicationContext CONTEXT = null;
	
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
		//final ClassPathXmlApplicationContext ctx = 
		//		new ClassPathXmlApplicationContext("classpath:META-INF/spring/*-context.xml");
		
		final AnnotationConfigApplicationContext ctx = 
				new AnnotationConfigApplicationContext(DatabaseConfiguration.class, ScheduleConfiguration.class);
		ctx.registerShutdownHook();
		
		CONTEXT = ctx;
		
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
