/**
 * 
 */
package uk.org.vacuumtube.routeros;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import mikrotik.routeros.libAPI.ApiConn;
import mikrotik.routeros.libAPI.Response;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rrd4j.ConsolFun;
import org.rrd4j.DsType;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Util;

import uk.org.vacuumtube.util.ByteFormat;

/**
 * @author clivem
 * 
 */
public class RouterOSGetStats {
	
	private final static Logger logger = Logger.getLogger(RouterOSGetStats.class);
	
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
	
	private Connection conn = null;
	private PreparedStatement stmt = null;
	
	private RrdDb rrdDb = null;

	/**
	 * 
	 */
	public RouterOSGetStats() {
	}
	
	/**
	 * @throws SQLException
	 */
	public void openJdbc() throws SQLException {
		conn = DriverManager.getConnection("jdbc:mysql://192.168.0.60/routeros?user=routeros&password=louise");
		String sql = "insert into stats values(?,?,?,?,?)";
		stmt = conn.prepareStatement(sql);
	}
	
	/**
	 * 
	 */
	public void releaseJdbc() {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException sqle) {}
		}
		
		if (conn != null) {
			try {
				conn.close();
			} catch(SQLException sqle) {} 
		}
	}
	
	/**
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public RrdDb createRrdDb(String fileName) 
			throws IOException {
		
		File f = new File(fileName);
		if (!f.exists()) {
			RrdDef rrdDef = new RrdDef(fileName);
			//rrdDef.setStartTime(System.currentTimeMillis() / 1000);
			rrdDef.setStep(300 / 5);
			rrdDef.addDatasource("traffic_in", DsType.COUNTER, 600 / 5, Double.NaN, Double.NaN);
			rrdDef.addDatasource("traffic_out", DsType.COUNTER, 600 / 5, Double.NaN, Double.NaN);
			rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 1, 600 * 5);
			rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 6 * 5, 700);
			rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 24 * 5, 775);
			rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 288 * 5, 797);
			rrdDef.addArchive(ConsolFun.MAX, 0.5, 1, 600 * 5);
			rrdDef.addArchive(ConsolFun.MAX, 0.5, 6 * 5, 700);
			rrdDef.addArchive(ConsolFun.MAX, 0.5, 24 * 5, 775);
			rrdDef.addArchive(ConsolFun.MAX, 0.5, 288 * 5, 797);
			rrdDb = new RrdDb(rrdDef);
		} else {
			rrdDb = new RrdDb(fileName);
		}
		
		return rrdDb;
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
				logger.log(Level.WARN, "", ex);
				return;
			}
			
			if (ret.isConnected()) {

				boolean success = ret.login("admin", "d0gsb0ll0cks".toCharArray());
				if (success) {

					Thread t = new Thread() {
						
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
											if (USE_JDBC) {
												try {
													stmt.setObject(1, null);
													stmt.setLong(2, timestamp);
													stmt.setLong(3, rxBytes);
													stmt.setLong(4, txBytes);
													stmt.setTimestamp(5, null);
	
													boolean success = stmt.execute();
													if (!success) {
													}
												} catch (SQLException sqle) {
													logger.log(Level.WARN, "Error insert into db: routeros.stats", sqle);
												}
											}
											
											if (rrdDb != null) {
												try {
													rrdDb.createSample().setAndUpdate(timestamp / 1000 + ":" + rxBytes + ":" + txBytes);
												} catch (IOException ioe) {
													logger.log(Level.WARN, "Error updating RrdDb", ioe);
												}
											}
											
											logger.log(Level.INFO, "Rx: " + ByteFormat.humanReadableByteCount(rxBytes, true) +
													" (" + ByteFormat.humanReadableByteCount(rxBytes, false) + ")" +
													", Tx: " + ByteFormat.humanReadableByteCount(txBytes, true) +
													" (" + ByteFormat.humanReadableByteCount(txBytes, false) + ")");
											
											if (rxBytes > -1 && rxBytesPrev > -1 && txBytes > -1 && txBytesPrev > -1) {
												long time = timestamp - timestampPrev;
												long seconds = Math.round((time / 1000.0));
												if (seconds > 0) {
													long rxbps = ((rxBytes - rxBytesPrev) * 8) / seconds;
													long txbps = ((txBytes - txBytesPrev) * 8) / seconds;

													logger.log(Level.INFO, "Delta: " + time + "ms (" + seconds + "s)" + 
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
									logger.log(Level.DEBUG, null, e);
									return;
								}
							}
						}
					};
					t.setDaemon(true);
					t.setName("Data");
					t.start();
					
					//ret.sendCommand("/ip/address/print");
					
					//int tag1 = ret.sendCommand("/interface/monitor-traffic\n=interface=ether1-gateway");
					//int tag2 = ret.sendCommand("/interface/monitor-traffic\n=interface=ether2-local-master");
					
					//int tag1 = ret.sendCommand("/interface/ethernet/get\n=.id=*1");
					//int tag1 = ret.sendCommand("/interface/ethernet/print\n?name=ether1-gateway\n=stats=\n=interval=10\n=.proplist=tx-bytes,driver-tx-byte,rx-bytes,driver-rx-byte");
					int tag1 = ret.sendCommand("/interface/print\n?name=ether1-gateway\n=stats=\n=interval=60\n=.proplist=tx-byte,rx-byte");
					
					try {
						//Thread.sleep(60000);
						System.in.read();
					} catch(Exception e) {}
					
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
					logger.log(Level.INFO, "Disconnecting from RouterOS API....");
					ret.disconnect();
					logger.log(Level.INFO, "Disconnected from RouterOS API.");
				} catch (IOException ioe) {
					logger.log(Level.WARN, "Error disconnecting from RouterOS API!", ioe);
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
		if (USE_JDBC) {
			try {
				// The newInstance() call is a work around for some
				// broken Java implementations
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (Exception ex) {
				logger.log(Level.WARN, "Error initialising MySQL!", ex);
				System.exit(-1);
			}
		}
		
		RrdDb rrdDb = null;		
		RouterOSGetStats test = new RouterOSGetStats();

		if (USE_JDBC) {
			try {
				test.openJdbc();
			} catch(SQLException sqle) {
				logger.log(Level.WARN, "Error initializing JDBC!", sqle);
				System.exit(-1);
			}
		}		
		
		if (USE_RRD) {
			try {
				rrdDb = test.createRrdDb(Util.getUserHomeDirectory() + "test.rrd");
			} catch (IOException ioe) {
				logger.log(Level.WARN, "Error creating RrdDb!", ioe);
				System.exit(-1);
			}
		}

		test.test();
		
		test.releaseJdbc();
		
		if (rrdDb != null) {
			try {
				rrdDb.close();
			} catch (IOException ioe) {}
		}
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
