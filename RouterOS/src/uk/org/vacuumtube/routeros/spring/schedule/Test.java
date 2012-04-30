/**
 * 
 */
package uk.org.vacuumtube.routeros.spring.schedule;

import java.util.Scanner;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author clivem
 *
 */
public class Test {

	private final static Logger LOGGER = Logger.getLogger(Test.class);
	
	public Test() {
		Runtime.getRuntime().addShutdownHook(new TestClose());
	}
	
	private void doClose() {
		LOGGER.info("Closing...");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx  = new ClassPathXmlApplicationContext("classpath:schedule.xml");
		ctx.registerShutdownHook();
		
		Test test = new Test();
		Scanner scanner = new Scanner(System.in);
		while (true) {
			String cmd = scanner.nextLine();
			LOGGER.info("STDIN: [" + cmd + "]");
			if (cmd == null || cmd.equals("quit")) {
				break;
			}
		}
		System.exit(0);
	}

	private final class TestClose extends Thread {
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			doClose();
		}
	}
}
