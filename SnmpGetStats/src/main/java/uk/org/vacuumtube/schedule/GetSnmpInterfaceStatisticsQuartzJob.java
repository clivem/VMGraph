/**
 * 
 */
package uk.org.vacuumtube.schedule;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

import uk.org.vacuumtube.service.StatsDatabaseService;
import uk.org.vacuumtube.snmp.GetSnmpInterfaceStatistics;

/**
 * @author clivem
 *
 */
//public class GetSnmpInterfaceStatisticsQuartzJob extends QuartzJobBean implements StatefulJob {
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class GetSnmpInterfaceStatisticsQuartzJob implements Job {
	
	private static final Logger LOGGER = Logger.getLogger(GetSnmpInterfaceStatisticsQuartzJob.class);

	private final static String PREV_TS = "prevTimestamp";
	private final static String PREV_RX_BYTES = "prevRxBytes";
	private final static String PREV_TX_BYTES = "prevTxBytes";
	
	private final static String SNMP_ADDRESS = "snmp.address";
	private final static String SNMP_BYTES_IN_OID = "snmp.bytesInOid";
	private final static String SNMP_BYTES_OUT_OID = "snmp.bytesOutOid";
	private final static String SNMP_RRDDB_FILENAME = "snmp.rrdDbFileName";
	
	private StatsDatabaseService statsDatabaseService = null;
	
	private long prevTimestamp = -1;
	private long prevRxBytes = -1;
	private long prevTxBytes = -1;
	
	/**
	 * 
	 */
	public GetSnmpInterfaceStatisticsQuartzJob() {
		super();
		LOGGER.info("Created: GetSnmpInterfaceStatisticsQuartzJob()");
	}
	
	/**
	 * @param statsDatabaseService
	 */
	public void setStatsDatabaseService(StatsDatabaseService statsDatabaseService) {
		this.statsDatabaseService = statsDatabaseService;
	}
	
	/**
	 * @param prevTimestamp the prevTimestamp to set
	 */
	public void setPrevTimestamp(long prevTimestamp) {
		this.prevTimestamp = prevTimestamp;
	}

	/**
	 * @param prevRxBytes the prevRxBytes to set
	 */
	public void setPrevRxBytes(long prevRxBytes) {
		this.prevRxBytes = prevRxBytes;
	}

	/**
	 * @param prevTxBytes the prevTxBytes to set
	 */
	public void setPrevTxBytes(long prevTxBytes) {
		this.prevTxBytes = prevTxBytes;
	}

	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("executeInternal(context=" + context + "): START");
		}
		
		LOGGER.info("Timer Job: Getting internet gateway stats from router...");

		JobDataMap jdm = context.getJobDetail().getJobDataMap();

		if (!jdm.containsKey(SNMP_ADDRESS) || !jdm.containsKey(SNMP_BYTES_IN_OID) || 
				!jdm.containsKey(SNMP_BYTES_OUT_OID) || !jdm.containsKey(SNMP_RRDDB_FILENAME)) {
			throw new JobExecutionException("SNMP Properties not set!", false);
		}
		
		GetSnmpInterfaceStatistics getStats = new GetSnmpInterfaceStatistics(
				jdm.getString(SNMP_ADDRESS), jdm.getString(SNMP_BYTES_IN_OID), 
				jdm.getString(SNMP_BYTES_OUT_OID), jdm.getString(SNMP_RRDDB_FILENAME), 
				statsDatabaseService);
		
		getStats.setTimestampPrev(prevTimestamp);
		getStats.setRxBytesPrev(prevRxBytes);
		getStats.setTxBytesPrev(prevTxBytes);
		
		try {
			getStats.execute();
		} catch (IOException ioe) {
			LOGGER.warn("Error getting SNMP stats from router!", ioe);
		} finally {
			getStats.close();
		}

		jdm.put(PREV_TS, getStats.getTimestampPrev());
		jdm.put(PREV_RX_BYTES, getStats.getRxBytesPrev());
		jdm.put(PREV_TX_BYTES, getStats.getTxBytesPrev());
		
		LOGGER.info("Timer Job: Finished.");

		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("executeInternal(context=" + context + "): END");
		}		
	}
}
