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

import uk.org.vacuumtube.dao.Notes;
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
		final ApplicationContext context = new ClassPathXmlApplicationContext(
				"/META-INF/spring/db-context.xml", StatsDatabaseServiceTest.class);
        
        Thread.sleep(2000);
    }
	
	@Test
	public void testGetStatsEntityCount() throws Exception {
		final ApplicationContext context = new ClassPathXmlApplicationContext(
				"/META-INF/spring/db-context.xml", StatsDatabaseServiceTest.class);
		
		StatsDatabaseService sds = StatsDatabaseServiceImpl.getStatsDatabaseService(context);
		int count = sds.getCount();
		LOGGER.info("sds.getCount(): " + count);
		Assert.assertTrue(count > -1);
	}

	@Test
	public void testGetStatsList() throws Exception {
		final ApplicationContext context = new ClassPathXmlApplicationContext(
				"/META-INF/spring/db-context.xml", StatsDatabaseServiceTest.class);
		
		StatsDatabaseService sds = StatsDatabaseServiceImpl.getStatsDatabaseService(context);
		List<Stats> statsList = sds.getStatsList();
		Assert.assertNotNull(statsList);
		for (Stats stats : statsList) {
			LOGGER.info("sds.getStatsList(): " + stats);
		}
	}

	@Test
	public void testCreateUpdateDeleteStats() throws Exception {
		final ApplicationContext context = new ClassPathXmlApplicationContext(
				"/META-INF/spring/db-context.xml", StatsDatabaseServiceTest.class);
		
		StatsDatabaseService sds = StatsDatabaseServiceImpl.getStatsDatabaseService(context);
		Stats stats = new Stats(System.currentTimeMillis(), 1L, 1L);
		Long id = sds.add(stats);
		LOGGER.info("After sds.add(): " + stats);
		Assert.assertNotNull("Save Stats object to db failed!", id);
		stats.setRxBytes(1000L);
		sds.update(stats);
		LOGGER.info("After stats.setRxBytes(1000L) -> sds.update(): " + stats);
		stats = sds.getStats(id);
		LOGGER.info("sds.getStats(id=" + id + "): " + stats);
		Assert.assertEquals("RxBytes value not updated!", new Long(1000L), stats.getRxBytes());
		sds.delete(stats);
		stats = sds.getStats(id);
		LOGGER.info("After stats.delete() -> sds.getStats(id=" + id + "): " + stats);
		Assert.assertNull("Delete stats object failed!", stats);
	}

	@Test
	public void testAddNoteToStats() throws Exception {
		final ApplicationContext context = new ClassPathXmlApplicationContext(
				"/META-INF/spring/db-context.xml", StatsDatabaseServiceTest.class);
		
		StatsDatabaseService sds = StatsDatabaseServiceImpl.getStatsDatabaseService(context);
		Stats stats = new Stats(System.currentTimeMillis(), 1L, 1L);
		Long id = sds.add(stats);
		LOGGER.info("After sds.add(): " + stats);
		Assert.assertNotNull("Save Stats object to db failed!", id);
		Notes note = new Notes(stats, "test_1");
		stats.getNotes().add(note);
		sds.update(stats);
		//stats = sds.mergeStats(stats);
		LOGGER.info("After sds.addNote(): " + stats);
		stats = sds.getStats(id);
		LOGGER.info("sds.getStats(id=" + id + "): " + stats);
		Assert.assertNotNull(stats);
		sds.delete(stats);
		stats = sds.getStats(id);
		LOGGER.info("After stats.delete() -> sds.getStats(id=" + id + "): " + stats);
		Assert.assertNull("Delete stats object failed!", stats);
	}
}
