/**
 * 
 */
package uk.org.vacuumtube.schedule;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.springframework.scheduling.quartz.QuartzJobBean;

import uk.org.vacuumtube.snmp.GetSnmpInterfaceStatistics;

/**
 * @author clivem
 *
 */
public class GetSnmpInterfaceStatisticsJob extends QuartzJobBean implements StatefulJob {

	private static final Logger LOGGER = Logger.getLogger(GetSnmpInterfaceStatisticsJob.class);

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

		GetSnmpInterfaceStatistics getStats = new GetSnmpInterfaceStatistics(jdm.getString("address"), 
				jdm.getString("bytesInOid"), jdm.getString("bytesOutOid"), jdm.getString("rrdDbFileName"));
		
		try {
			getStats.setTimestampPrev(jdm.getLong("prevTs"));
		} catch (Exception e) {
			getStats.setTimestampPrev(-1L);
		}
		
		try {
			getStats.setRxBytesPrev(jdm.getLong("prevRxBytes"));
		} catch (Exception e) {
			getStats.setRxBytesPrev(-1L);
		}
		
		try {
			getStats.setTxBytesPrev(jdm.getLong("prevTxBytes"));
		} catch (Exception e) {
			getStats.setTxBytesPrev(-1L);
		}
		
		try {
			getStats.execute();
		} catch (IOException ioe) {
			LOGGER.warn("Error getting SNMP stats from router!", ioe);
		} finally {
			getStats.close();
		}

		jdm.put("prevTs", getStats.getTimestampPrev());
		jdm.put("prevRxBytes", getStats.getRxBytesPrev());
		jdm.put("prevTxBytes", getStats.getTxBytesPrev());
		
		LOGGER.info("Timer Job: Finished.");

		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("executeInternal(context=" + context + "): END");
		}		
	}
}
