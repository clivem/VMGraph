/**
 * 
 */
package uk.org.vacuumtube.routeros;
import java.io.IOException;
import java.util.Scanner;

import mikrotik.routeros.libAPI.ApiConn;
import mikrotik.routeros.libAPI.Response;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import uk.org.vacuumtube.routeros.spring.service.RouterStatsService;
import uk.org.vacuumtube.routeros.spring.service.RouterStatsServiceImpl;
import uk.org.vacuumtube.util.ByteFormat;

/**
 * @author clivem
 * 
 */
public class RouterOSGetStatsMySQL {
	
	private final static Logger LOGGER = Logger.getLogger(RouterOSGetStatsMySQL.class);
	
	final ApiConn ret = new ApiConn("192.168.0.1", 8728);
	
	public final static boolean USE_JDBC = false;
	public final static boolean USE_RRD = true;

	public final static String RX_BYTE_ = "=rx-byte=";
	public final static String TX_BYTE_ = "=tx-byte=";

	//public final static String RX_BYTES = "rx-bytes";
	public final static String RX_BYTES_ = "=rx-bytes=";
	//public final static String TX_BYTES = "tx-bytes";
	public final static String TX_BYTES_ = "=tx-bytes=";
	//public final static String DRIVER_RX_BYTE = "driver-rx-byte";
	public final static String DRIVER_RX_BYTE_ = "=driver-rx-byte=";
	//public final static String DRIVER_TX_BYTE = "driver-tx-byte";
	public final static String DRIVER_TX_BYTE_ = "=driver-tx-byte=";
	
	private ClassPathXmlApplicationContext ctx = null;
	
	/**
	 * 
	 */
	public RouterOSGetStatsMySQL(ClassPathXmlApplicationContext ctx) {
		this.ctx = ctx;
	}
	
	/**
	 * 
	 */
	public void test() {
		if (!ret.isConnected()) {
			ret.start();
			try {
				ret.join();
			} catch (InterruptedException ex) {
				LOGGER.log(Level.WARN, "", ex);
				return;
			}
			
			if (ret.isConnected()) {

				boolean success = ret.login("admin", "d0gsb0ll0cks".toCharArray());
				if (success) {

					Thread t = new Thread() {
						//StatsDao dao = JdbcStatsDaoImpl.getStatsDao(ctx);
						RouterStatsService service = RouterStatsServiceImpl.getRouterStatsService(ctx);
						
						long rxBytesPrev = -1;
						long txBytesPrev = -1;
						long timestampPrev = -1;
						
						@Override
						public void run() {
							while (true) {
								try {
									Response data = ret.getData();
									String text = data.getText();
									long timestamp = data.getTimestamp();
									
									if (text.contains(RX_BYTE_) && text.contains(TX_BYTE_)) {
										
										//Long rxBytes = null;
										long rxBytes = -1;
										//Long txBytes = null;
										long txBytes = -1;
										
										String[] stringList = text.trim().split("\n");
										for (int i = 0; i < stringList.length; i++) {
											if (stringList[i].contains(RX_BYTE_)) {
												rxBytes = getLong(RX_BYTE_, stringList[i]);
											} else if (stringList[i].contains(TX_BYTE_)) {
												txBytes = getLong(TX_BYTE_, stringList[i]);
											}
										}
								
										if (txBytes > -1 && rxBytes > -1) {

											try {
												Stats stats = service.add(new Stats(timestamp, rxBytes, txBytes));
												LOGGER.info(stats);
											} catch (Exception e) {
												LOGGER.warn(null, e);
											}

											LOGGER.log(Level.INFO, "Rx: " + ByteFormat.humanReadableByteCount(rxBytes, true) +
													" (" + ByteFormat.humanReadableByteCount(rxBytes, false) + ")" +
													", Tx: " + ByteFormat.humanReadableByteCount(txBytes, true) +
													" (" + ByteFormat.humanReadableByteCount(txBytes, false) + ")");
											
											if (rxBytes > -1 && rxBytesPrev > -1 && txBytes > -1 && txBytesPrev > -1) {
												long time = timestamp - timestampPrev;
												long seconds = Math.round((time / 1000.0));
												if (seconds > 0) {
													long rxbps = ((rxBytes - rxBytesPrev) * 8) / seconds;
													long txbps = ((txBytes - txBytesPrev) * 8) / seconds;

													LOGGER.log(Level.INFO, "Delta: " + time + "ms (" + seconds + "s)" + 
															", Rx: " + ByteFormat.humanReadableByteCount(rxBytes - rxBytesPrev, true) +
															" (" + ByteFormat.humanReadableByteCount(rxBytes - rxBytesPrev, false) + ") " +
															ByteFormat.humanReadableBitCount(rxbps) +
															", Tx: " + ByteFormat.humanReadableByteCount(txBytes - txBytesPrev, true) +
															" (" + ByteFormat.humanReadableByteCount(txBytes - txBytesPrev, false) + ") " +
															ByteFormat.humanReadableBitCount(txbps));
												}
											}

											rxBytesPrev = rxBytes;
											txBytesPrev = txBytes;
											timestampPrev = timestamp;
										}
									}
								} catch (InterruptedException e) {
									LOGGER.log(Level.DEBUG, null, e);
									return;
								}
							}
						}
					};
					t.setDaemon(true);
					t.setName("ApiResponse");
					t.start();
					
					//ret.sendCommand("/ip/address/print");
					
					//int tag1 = ret.sendCommand("/interface/monitor-traffic\n=interface=ether1-gateway");
					//int tag2 = ret.sendCommand("/interface/monitor-traffic\n=interface=ether2-local-master");
					
					//int tag1 = ret.sendCommand("/interface/ethernet/get\n=.id=*1");
					//int tag1 = ret.sendCommand("/interface/ethernet/print\n?name=ether1-gateway\n=stats=\n=interval=10\n=.proplist=tx-bytes,driver-tx-byte,rx-bytes,driver-rx-byte");
					int tag1 = ret.sendCommand("/interface/print\n?name=ether1-gateway\n=stats=\n=interval=60\n=.proplist=tx-byte,rx-byte");
					
					/*
					try {
						//Thread.sleep(60000);
						System.in.read();
					} catch(Exception e) {}
					*/
					Scanner scanner = new Scanner(System.in);
					while (true) {
						String input = scanner.nextLine();
			        	if("quit".equals(input.trim())) {
			        		break;
			        	}
					}

					if (tag1 > -1) {
						ret.sendCommand("/cancel\n=tag=" + tag1);
					}
					
					/*
					if (tag2 > -1) {
						ret.sendCommand("/cancel\n=tag=" + tag2);
					}
					*/
					
					try {
						Thread.sleep(500);
					} catch(Exception e) {}

					ret.sendCommand("/quit");
					
					try {
						Thread.sleep(500);
					} catch(Exception e) {}

					t.interrupt();
				}
				
				try {
					LOGGER.log(Level.INFO, "Disconnecting from RouterOS API....");
					ret.disconnect();
					LOGGER.log(Level.INFO, "Disconnected from RouterOS API.");
				} catch (IOException ioe) {
					LOGGER.log(Level.WARN, "Error disconnecting from RouterOS API!", ioe);
				}
				
				//String result = ret.getData();
				//System.out.println(result);
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx  = new ClassPathXmlApplicationContext("classpath:db.xml");
		ctx.registerShutdownHook();

		RouterOSGetStatsMySQL test = new RouterOSGetStatsMySQL(ctx);
		test.test();
	}
	
	/**
	 * @param tag
	 * @param text
	 * @return
	 */
	public static long getLong(String tag, String text) {
		if (text.contains(tag)) {
			String[] tmp = text.split(tag);
			if (tmp.length > 1) {
				return Long.valueOf(tmp[1]).longValue();
			}
		}
		
		return -1;
	}
}
