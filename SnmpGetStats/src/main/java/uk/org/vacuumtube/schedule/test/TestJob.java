/**
 * 
 */
package uk.org.vacuumtube.schedule.test;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author clivem
 *
 */
public class TestJob extends QuartzJobBean {

	private static final Logger LOGGER = Logger.getLogger(TestJob.class);
	
	/* (non-Javadoc)
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
	}

	//@Scheduled(fixedDelay=5000)
	public void ping() {
		LOGGER.info("PING!");
	}
}
