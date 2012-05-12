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

    private final Logger LOGGER = Logger.getLogger(GetSnmpInterfaceStatistics.class);
    
    public final static String DEFAULT_ARCHIVE_IN_NAME = "traffic_in";
    public final static String DEFAULT_ARCHIVE_OUT_NAME = "traffic_out";

    protected String rrdDbFileName;
    protected RrdDb rrdDb = null;
    protected String address;
    protected OID bytes_in_oid;
    protected OID bytes_out_oid;

	protected long prevRxBytes = -1;
	protected long prevTxBytes = -1;
	protected long prevTimestamp = -1;
	
    protected String archiveInName;
    protected String archiveOutName;
    
    protected StatsDatabaseService statsDatabaseService;
	
    /**
     * 
     */
    public GetSnmpInterfaceStatistics() {
    	super();
    	LOGGER.info("Created: GetSnmpInterfaceStatistics()");
    }
    
	/**
	 * @param address
	 * @param bytes_in_oid
	 * @param bytes_out_oid
	 */
	public GetSnmpInterfaceStatistics(String address, String bytes_in_oid, String bytes_out_oid, String rrdDbFileName, 
			String archiveInName, String archiveOutName, StatsDatabaseService statsDatabaseService) {
		this();
		this.rrdDbFileName = rrdDbFileName;
		this.address = address;
		this.bytes_in_oid = new OID(bytes_in_oid);
		this.bytes_out_oid = new OID(bytes_out_oid);
		this.archiveInName = archiveInName;
		this.archiveOutName = archiveOutName;
		this.statsDatabaseService = statsDatabaseService;
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("GetSnmpInterfaceStatistics(address=" + address + ", bytes_in_oid=" + bytes_in_oid + 
					", bytes_out_oid=" + bytes_out_oid + ", rrdDbFileName=" + rrdDbFileName + 
					", archiveInName=" + archiveInName + ", archiveOutName=" + archiveOutName + 
					", statsDatabaseService" + statsDatabaseService + ")");
		}
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
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("GetSnmpInterfaceStatistics.openRrdDb()");
		}
		
		File f = new File(rrdDbFileName);
		if (!f.exists()) {
			LOGGER.info("Creating new RRD: " + rrdDbFileName);

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
			LOGGER.info("Opening existing RRD: " + rrdDbFileName);

			rrdDb = new RrdDb(rrdDbFileName);
		}
	}
	
	/**
	 * 
	 */
	public void execute() throws IOException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("GetSnmpInterfaceStatistics.execute(): START");
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
			LOGGER.info("Sending SNMP request...");
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
								LOGGER.debug(oid + " " + var);
								logOut += ", oid: " + oid + " var: " + var;
							}
						}
						
						LOGGER.info(logOut + "]");
						
						if (txBytes > -1 && rxBytes > -1) {
							if (rrdDb != null) {
								try {
									String update = Math.round(timestamp / 1000.0) + ":" + rxBytes + ":" + txBytes;
									if (LOGGER.isDebugEnabled()) {
										LOGGER.debug("Updating RRD: " + update);
									}
									rrdDb.createSample().setAndUpdate(update);
									LOGGER.info("Updated RRD: " + update);
								} catch (Exception ioe) {
									LOGGER.warn("Error updating RRD!", ioe);
								}
							}
							
							if (statsDatabaseService != null) {
								Stats stats = new Stats(timestamp, rxBytes, txBytes);
								try {
									if (LOGGER.isDebugEnabled()) {
										LOGGER.debug("Updating MySQL: " + stats);
									}
									statsDatabaseService.createStats(stats);
									LOGGER.info("Updated MySQL: " + stats);
								} catch (Exception e) {
									LOGGER.warn("Error updating MySQL: " + stats, e);
								}
							}
														
							if (prevRxBytes > -1 && prevTxBytes > -1) {
								long time = timestamp - prevTimestamp;
								long seconds = Math.round((time / 1000.0));
								if (seconds > 0) {
									long rxbps = ((rxBytes - prevRxBytes) * 8) / seconds;
									long txbps = ((txBytes - prevTxBytes) * 8) / seconds;

									LOGGER.info("Update Delta: " + time + "ms (" + seconds + "s)" + 
											", Rx: " + ByteFormat.humanReadableByteCount(rxBytes - prevRxBytes, true) +
											" (" + ByteFormat.humanReadableByteCount(rxBytes - prevRxBytes, false) + ") " +
											ByteFormat.humanReadableBitCount(rxbps) +
											", Tx: " + ByteFormat.humanReadableByteCount(txBytes - prevTxBytes, true) +
											" (" + ByteFormat.humanReadableByteCount(txBytes - prevTxBytes, false) + ") " +
											ByteFormat.humanReadableBitCount(txbps));
								}
							}

							prevRxBytes = rxBytes;
							prevTxBytes = txBytes;
							prevTimestamp = timestamp;
						}
					}
				}
			} else {
				LOGGER.warn("Received null ResponseEvent!");
			}
		} catch (IOException ioe) {
			LOGGER.warn("Error getting SNMP interface stats!", ioe);
		} finally {
			if (snmpClient != null) {
				try {
					snmpClient.stop();
				} catch (Exception ioe) {}
			}
		}
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("GetSnmpInterfaceStatistics.execute(): END");
		}
	}
	
	/**
	 * 
	 */
	public void close() {
		if (rrdDb != null) {
			LOGGER.info("Closing RRD: " + rrdDbFileName);
			try {
				rrdDb.close();
			} catch (IOException ioe) {
				LOGGER.warn("Error closing RRD: " + rrdDbFileName, ioe);
			}
		}
	}

	/**
	 * @return the prevRxBytes
	 */
	public long getRxBytesPrev() {
		return prevRxBytes;
	}

	/**
	 * @param prevRxBytes the prevRxBytes to set
	 */
	public void setRxBytesPrev(long rxBytesPrev) {
		this.prevRxBytes = rxBytesPrev;
	}

	/**
	 * @return the prevTxBytes
	 */
	public long getTxBytesPrev() {
		return prevTxBytes;
	}

	/**
	 * @param prevTxBytes the prevTxBytes to set
	 */
	public void setTxBytesPrev(long txBytesPrev) {
		this.prevTxBytes = txBytesPrev;
	}

	/**
	 * @return the prevTimestamp
	 */
	public long getTimestampPrev() {
		return prevTimestamp;
	}

	/**
	 * @param prevTimestamp the prevTimestamp to set
	 */
	public void setTimestampPrev(long timestampPrev) {
		this.prevTimestamp = timestampPrev;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}
}
