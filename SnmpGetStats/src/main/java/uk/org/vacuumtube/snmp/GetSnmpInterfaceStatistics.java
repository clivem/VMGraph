/**
 * 
 */
package uk.org.vacuumtube.snmp;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.rrd4j.ConsolFun;
import org.rrd4j.DsType;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.snmp4j.PDU;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;

import uk.org.vacuumtube.dao.Stats;
import uk.org.vacuumtube.service.StatsDatabaseService;
import uk.org.vacuumtube.util.ByteFormat;

/**
 * @author clivem
 *
 */
public class GetSnmpInterfaceStatistics {

    private static final DateFormat DF_FULL = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS z");

    private final Logger logger = Logger.getLogger(GetSnmpInterfaceStatistics.class);
    
    public final static String DEFAULT_ARCHIVE_IN_NAME = "traffic_in";
    public final static String DEFAULT_ARCHIVE_OUT_NAME = "traffic_out";

    protected String rrdDbFileName;
    protected RrdDb rrdDb = null;
    protected String address;
    protected OID bytes_in_oid;
    protected OID bytes_out_oid;

	protected long rxBytesPrev = -1;
	protected long txBytesPrev = -1;
	protected long timestampPrev = -1;
	
    protected String archiveInName;
    protected String archiveOutName;
    
    protected StatsDatabaseService statsDatabaseService;
	
	
	/**
	 * @param address
	 * @param bytes_in_oid
	 * @param bytes_out_oid
	 */
	public GetSnmpInterfaceStatistics(String address, String bytes_in_oid, String bytes_out_oid, String rrdDbFileName, 
			String archiveInName, String archiveOutName, StatsDatabaseService statsDatabaseService) {
		if (logger.isTraceEnabled()) {
			logger.trace("GetSnmpInterfaceStatistics(address=" + address + ", bytes_in_oid=" + bytes_in_oid + 
					", bytes_out_oid=" + bytes_out_oid + ", rrdDbFileName=" + rrdDbFileName + 
					", archiveInName=" + archiveInName + ", archiveOutName=" + archiveOutName + 
					", statsDatabaseService" + statsDatabaseService + ")");
		}
		this.rrdDbFileName = rrdDbFileName;
		this.address = address;
		this.bytes_in_oid = new OID(bytes_in_oid);
		this.bytes_out_oid = new OID(bytes_out_oid);
		this.archiveInName = archiveInName;
		this.archiveOutName = archiveOutName;
		this.statsDatabaseService = statsDatabaseService;
	}
	
	/**
	 * @param address
	 * @param bytes_in_oid
	 * @param bytes_out_oid
	 * @param rrdDbFileName
	 */
	public GetSnmpInterfaceStatistics(String address, String bytes_in_oid, String bytes_out_oid, String rrdDbFileName, 
			StatsDatabaseService statsDatabaseService) {
		this(address, bytes_in_oid, bytes_out_oid, rrdDbFileName, DEFAULT_ARCHIVE_IN_NAME, DEFAULT_ARCHIVE_OUT_NAME, statsDatabaseService);
	}
	
	/**
	 * @return
	 * @throws IOException
	 */
	private void openRrdDb() throws IOException {
		if (logger.isTraceEnabled()) {
			logger.trace("GetSnmpInterfaceStatistics.openRrdDb()");
		}
		
		File f = new File(rrdDbFileName);
		if (!f.exists()) {
			logger.info("Creating new RRD: " + rrdDbFileName);

			RrdDef rrdDef = new RrdDef(rrdDbFileName);
			rrdDef.setStep(300 / 5);
			rrdDef.addDatasource(archiveInName, DsType.COUNTER, 600 / 5, Double.NaN, Double.NaN);
			rrdDef.addDatasource(archiveOutName, DsType.COUNTER, 600 / 5, Double.NaN, Double.NaN);
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
			logger.info("Opening existing RRD: " + rrdDbFileName);

			rrdDb = new RrdDb(rrdDbFileName);
		}
	}
	
	/**
	 * 
	 */
	public void execute() throws IOException {
		if (logger.isTraceEnabled()) {
			logger.trace("GetSnmpInterfaceStatistics.execute(): START");
		}
		
		if (rrdDb == null) {
			openRrdDb();
		}
		
		long timestamp = -1;
		long rxBytes = -1;
		long txBytes = -1;
		SnmpClient snmpClient = null;
		
		try {
			snmpClient = new SnmpClient(address);
			logger.info("Sending SNMP request...");
			ResponseEvent event = snmpClient.get(new OID[] {bytes_in_oid, bytes_out_oid});
			if (event != null) {
				PDU response = event.getResponse();
				if (response != null) {
					timestamp = System.currentTimeMillis();
					int size = response.size();
					if (size > 0) {
						String logOut = "Received SNMP Response [Timestamp: " + DF_FULL.format(new Date(timestamp));
						for (int i = 0; i < size; i++) {
							OID oid = response.get(i).getOid();
							Variable var = response.get(i).getVariable();
							if (oid.equals(bytes_in_oid)) {
								rxBytes = var.toLong();
								logOut += ", Bytes In: " + rxBytes;
							} else if (oid.equals(bytes_out_oid)) {
								txBytes = var.toLong();
								logOut += ", Bytes Out: " + txBytes;
							} else {
								logger.debug(oid + " " + var);
								logOut += ", oid: " + oid + " var: " + var;
							}
						}
						
						logger.info(logOut + "]");
						
						if (txBytes > -1 && rxBytes > -1) {
							if (rrdDb != null) {
								try {
									String update = Math.round(timestamp / 1000.0) + ":" + rxBytes + ":" + txBytes;
									if (logger.isDebugEnabled()) {
										logger.debug("Updating RRD: " + update);
									}
									rrdDb.createSample().setAndUpdate(update);
									logger.info("Updated RRD: " + update);
								} catch (Exception ioe) {
									logger.warn("Error updating RRD!", ioe);
								}
							}
							
							if (statsDatabaseService != null) {
								Stats stats = new Stats(timestamp, rxBytes, txBytes);
								try {
									if (logger.isDebugEnabled()) {
										logger.debug("Updating MySQL: " + stats);
									}
									statsDatabaseService.createStats(stats);
									logger.info("Updated MySQL: " + stats);
								} catch (Exception e) {
									logger.warn("Error updating MySQL: " + stats, e);
								}
							}
							
							if (logger.isTraceEnabled()) {
								long updateTs = rrdDb.getLastUpdateTime();
								logger.trace("RRD Last Update Time: (" + updateTs + ") " + 
										DF_FULL.format(updateTs * 1000L));
							}
							
							if (logger.isTraceEnabled()) {
								logger.trace("Rx: " + ByteFormat.humanReadableByteCount(rxBytes, true) +
										" (" + ByteFormat.humanReadableByteCount(rxBytes, false) + ")" +
										", Tx: " + ByteFormat.humanReadableByteCount(txBytes, true) +
										" (" + ByteFormat.humanReadableByteCount(txBytes, false) + ")");
							}
							
							if (rxBytesPrev > -1 && txBytesPrev > -1) {
								long time = timestamp - timestampPrev;
								long seconds = Math.round((time / 1000.0));
								if (seconds > 0) {
									long rxbps = ((rxBytes - rxBytesPrev) * 8) / seconds;
									long txbps = ((txBytes - txBytesPrev) * 8) / seconds;

									if (logger.isDebugEnabled()) {
										logger.debug("Delta: " + time + "ms (" + seconds + "s)" + 
												", Rx: " + ByteFormat.humanReadableByteCount(rxBytes - rxBytesPrev, true) +
												" (" + ByteFormat.humanReadableByteCount(rxBytes - rxBytesPrev, false) + ") " +
												ByteFormat.humanReadableBitCount(rxbps) +
												", Tx: " + ByteFormat.humanReadableByteCount(txBytes - txBytesPrev, true) +
												" (" + ByteFormat.humanReadableByteCount(txBytes - txBytesPrev, false) + ") " +
												ByteFormat.humanReadableBitCount(txbps));
									}
								}
							}

							rxBytesPrev = rxBytes;
							txBytesPrev = txBytes;
							timestampPrev = timestamp;
						}
					}
				}
			} else {
				logger.warn("Received null ResponseEvent!");
			}
		} catch (IOException ioe) {
			logger.warn("Error getting SNMP interface stats!", ioe);
		} finally {
			if (snmpClient != null) {
				try {
					snmpClient.stop();
				} catch (Exception ioe) {}
			}
		}
		if (logger.isTraceEnabled()) {
			logger.trace("GetSnmpInterfaceStatistics.execute(): END");
		}
	}
	
	/**
	 * 
	 */
	public void close() {
		if (rrdDb != null) {
			logger.info("Closing RRD: " + rrdDbFileName);
			try {
				rrdDb.close();
			} catch (IOException ioe) {
				logger.warn("Error closing RRD: " + rrdDbFileName, ioe);
			}
		}
	}

	/**
	 * @return the rxBytesPrev
	 */
	public long getRxBytesPrev() {
		return rxBytesPrev;
	}

	/**
	 * @param rxBytesPrev the rxBytesPrev to set
	 */
	public void setRxBytesPrev(long rxBytesPrev) {
		this.rxBytesPrev = rxBytesPrev;
	}

	/**
	 * @return the txBytesPrev
	 */
	public long getTxBytesPrev() {
		return txBytesPrev;
	}

	/**
	 * @param txBytesPrev the txBytesPrev to set
	 */
	public void setTxBytesPrev(long txBytesPrev) {
		this.txBytesPrev = txBytesPrev;
	}

	/**
	 * @return the timestampPrev
	 */
	public long getTimestampPrev() {
		return timestampPrev;
	}

	/**
	 * @param timestampPrev the timestampPrev to set
	 */
	public void setTimestampPrev(long timestampPrev) {
		this.timestampPrev = timestampPrev;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}
}
