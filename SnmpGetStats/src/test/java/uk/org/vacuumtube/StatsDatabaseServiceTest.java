/**
 * 
 */
package uk.org.vacuumtube;

import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import uk.org.vacuumtube.dao.Stats;
import uk.org.vacuumtube.service.StatsDatabaseService;
import uk.org.vacuumtube.service.StatsDatabaseServiceImpl;

/**
 * @author clivem
 *
 */
public class StatsDatabaseServiceTest {

	private static final Logger LOGGER = Logger.getLogger(StatsDatabaseServiceTest.class);
	
	@Test
    public void testStartupOfSpringInegrationContext() throws Exception {
        @SuppressWarnings("unused")
		final ApplicationContext context
            = new ClassPathXmlApplicationContext("/META-INF/spring/db-context.xml",
                                                  StatsDatabaseServiceTest.class);
        Thread.sleep(2000);
    }
	
	@Test
	public void testGetStatsList() throws Exception {
		final ApplicationContext context
        	= new ClassPathXmlApplicationContext("/META-INF/spring/db-context.xml", StatsDatabaseServiceTest.class);
		
		StatsDatabaseService sds = StatsDatabaseServiceImpl.getStatsDatabaseService(context);
		List<Stats> statsList = sds.getStatsList();
		for (Stats stats : statsList) {
			LOGGER.info(stats);
		}
	}

	@Test
	public void testCreateStats() throws Exception {
		final ApplicationContext context
        	= new ClassPathXmlApplicationContext("/META-INF/spring/db-context.xml", StatsDatabaseServiceTest.class);
		
		StatsDatabaseService sds = StatsDatabaseServiceImpl.getStatsDatabaseService(context);
		Stats stats = new Stats(System.currentTimeMillis(), 1L, 1L);
		sds.add(stats);
		Assert.assertFalse(stats.getId() == -1L);
	}
}
