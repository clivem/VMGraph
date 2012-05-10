/**
 * 
 */
package uk.org.vacuumtube;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.StopWatch;
import org.springframework.util.StopWatch.TaskInfo;

import uk.org.vacuumtube.dao.Stats;
import uk.org.vacuumtube.service.RemoteStatsDatabaseService;
import uk.org.vacuumtube.spring.RemoteClientConfiguration;
import uk.org.vacuumtube.spring.ServiceLocator;

/**
 * @author clivem
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
  classes = RemoteClientConfiguration.class, 
  loader = AnnotationConfigContextLoader.class)
public class RemoteStatsDatabaseServiceTest implements ApplicationContextAware {

	private static final Logger LOGGER = Logger.getLogger(RemoteStatsDatabaseServiceTest.class);

	private ServiceLocator serviceLocator = null;
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		serviceLocator = new ServiceLocator(applicationContext);
	}

	@Test
	public void testGetStatsList() throws Exception {
		try {
			StopWatch watch = new StopWatch();

			RemoteStatsDatabaseService sds = serviceLocator.getRemoteStatsDatabaseService();
			
			int count = 11;
			/*
			 * Get without join
			 */
			boolean lazy = true;
			long totalMs = 0;
			for (int i = 0; i < count; i++) {
				getStatsList(watch, sds, lazy);
				if (i > 0) {
					totalMs += watch.getLastTaskInfo().getTimeMillis();
				}
			}
			String logInfo = ("getStatsList(" + lazy + "): " + (count - 1) + " times. Avg time: " + (totalMs / (count - 1)) + "ms");
			
			/*
			 * Get with join
			 */
			lazy = false;
			totalMs = 0;
			for (int i = 0; i < count; i++) {
				getStatsList(watch, sds, lazy);
				if (i > 0) {
					totalMs += watch.getLastTaskInfo().getTimeMillis();
				}
			}
			
			LOGGER.info(logInfo);
			LOGGER.info("getStatsList(" + lazy + "): " + (count - 1) + " times. Avg time: " + (totalMs / (count - 1)) + "ms");

		} catch (Exception e) {
			LOGGER.warn(null, e);
			throw e;
		}
	}
	
	/**
	 * @param watch
	 * @return
	 *
	@SuppressWarnings("unused")
	private final ApplicationContext createApplicationContext_(StopWatch watch) {
		watch.start("Setup context");
		final AnnotationConfigApplicationContext context = 
				new AnnotationConfigApplicationContext(RemoteClientConfiguration.class);
		logWatchStop(watch);
		return context;
	}
	*/
	
	/**
	 * @param watch
	 */
	private TaskInfo logWatchStop(StopWatch watch) {
		watch.stop();
		TaskInfo ti = watch.getLastTaskInfo();
		LOGGER.info("Task: " + ti.getTaskName() + ". Time: " + ti.getTimeMillis() +"ms");
		return ti;
	}

	/**
	 * @param watch
	 * @param sds
	 * @param lazy
	 * @return
	 */
	private List<Stats> getStatsList(StopWatch watch, RemoteStatsDatabaseService sds, boolean lazy) 
			throws RemoteException {
		watch.start("getStatsList(" + lazy + ")");
		List<Stats> statsList = sds.getStatsList(lazy);
		logWatchStop(watch);
		LOGGER.info("getStatsList(" + lazy + "): returns " + statsList.size() + " records.");
		for (Stats stat : statsList) {
			LOGGER.info(stat);
		}
		return statsList;
	}

}
