/**
 * 
 */
package uk.org.vacuumtube.schedule;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import uk.org.vacuumtube.SnmpGetStats;
import uk.org.vacuumtube.snmp.GetSnmpInterfaceStatistics;

/**
 * @author clivem
 *
 */
//public class GetSnmpInterfaceStatisticsJob extends QuartzJobBean implements StatefulJob {
@DisallowConcurrentExecution
public class GetSnmpInterfaceStatisticsJob extends QuartzJobBean {
	
	private static final Logger LOGGER = Logger.getLogger(GetSnmpInterfaceStatisticsJob.class);

	private final static String PREV_TS = "prevTs";
	private final static String PREV_RX_BYTES = "prevRxBytes";
	private final static String PREV_TX_BYTES = "prevTxBytes";
	
	private final static String SNMP_ADDRESS = "snmp.address";
	private final static String SNMP_BYTES_IN_OID = "snmp.bytesInOid";
	private final static String SNMP_BYTES_OUT_OID = "snmp.bytesOutOid";
	private final static String SNMP_RRDDB_FILENAME = "snmp.rrdDbFileName";
	
	/**
	 * 
	 */
	public GetSnmpInterfaceStatisticsJob() {
		LOGGER.info("GetSnmpInterfaceStatisticsJob()");
	}

	/* (non-Javadoc)
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext context)
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
				SnmpGetStats.SERVICE_LOCATOR.getStatsDatabaseService());
		
		if (jdm.containsKey(PREV_TS)) {
			try {
				getStats.setTimestampPrev(jdm.getLong(PREV_TS));
			} catch (Exception e) {}
		}
		
		if (jdm.containsKey(PREV_RX_BYTES)) {
			try {
				getStats.setRxBytesPrev(jdm.getLong(PREV_RX_BYTES));
			} catch (Exception e) {}
		}
		
		if (jdm.containsKey(PREV_TX_BYTES)) {
			try {
				getStats.setTxBytesPrev(jdm.getLong(PREV_TX_BYTES));
			} catch (Exception e) {}
		}
		
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
