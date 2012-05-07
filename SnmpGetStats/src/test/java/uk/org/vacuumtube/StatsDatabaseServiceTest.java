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
import org.springframework.util.StopWatch;
import org.springframework.util.StopWatch.TaskInfo;

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
		LOGGER.info("\n\ntestStartupOfSpringInegrationContext()");
		try {
			StopWatch watch = new StopWatch();
			watch.start("Setup context");
	        @SuppressWarnings("unused")
			final ApplicationContext context = new ClassPathXmlApplicationContext(
					new String[] {"/META-INF/spring/app-context.xml", "/META-INF/spring/db-context.xml"}, 
					StatsDatabaseServiceTest.class);
			logWatchStop(watch);
			
			Thread.sleep(2000);
		} catch (Exception e) {
			LOGGER.warn(null, e);
			throw e;
		}
    }
	
	@Test
	public void testGetStatsEntityCount() throws Exception {
		LOGGER.info("\n\ntestGetStatsEntityCount()");
		try {
			StopWatch watch = new StopWatch();
			watch.start("Setup context");
			final ApplicationContext context = new ClassPathXmlApplicationContext(
					new String[] {"/META-INF/spring/app-context.xml", "/META-INF/spring/db-context.xml"}, 
					StatsDatabaseServiceTest.class);
			logWatchStop(watch);
			
			StatsDatabaseService sds = StatsDatabaseServiceImpl.getStatsDatabaseService(context);
			watch.start("sds.getCount()");
			int count = sds.getCount();
			logWatchStop(watch);
			LOGGER.info("sds.getCount(): " + count);
			Assert.assertTrue(count > -1);
		} catch (Exception e) {
			LOGGER.warn(null, e);
			throw e;
		}
	}

	@Test
	public void testGetStatsList() throws Exception {
		LOGGER.info("\n\ntestGetStatsList()");
		try {
			StopWatch watch = new StopWatch();
			watch.start("Loading context");
			final ApplicationContext context = new ClassPathXmlApplicationContext(
					new String[] {"/META-INF/spring/app-context.xml", "/META-INF/spring/db-context.xml"}, 
					StatsDatabaseServiceTest.class);
			logWatchStop(watch);
			
			StatsDatabaseService sds = StatsDatabaseServiceImpl.getStatsDatabaseService(context);
			watch.start("Load stats list");
			List<Stats> statsList = sds.getStatsList();
			logWatchStop(watch);
			Assert.assertNotNull(statsList);
			for (Stats stats : statsList) {
				LOGGER.debug("sds.getStatsList(): " + stats);
				watch.start("getStats(" + stats.getId() + ", " + false + ")");
				sds.getStatsById(stats.getId(), false); 
				logWatchStop(watch);
			}
		} catch (Exception e) {
			LOGGER.warn(null, e);
			throw e;
		}
	}

	@Test
	public void testCreateUpdateDeleteStats() throws Exception {
		LOGGER.info("\n\ntestCreateUpdateDeleteStats()");
		try {
			StopWatch watch = new StopWatch();
			
			watch.start("Setup context");
			final ApplicationContext context = new ClassPathXmlApplicationContext(
					new String[] {"/META-INF/spring/app-context.xml", "/META-INF/spring/db-context.xml"}, 
					StatsDatabaseServiceTest.class);
			logWatchStop(watch);
			
			StatsDatabaseService sds = StatsDatabaseServiceImpl.getStatsDatabaseService(context);
			
			// Create
			Stats stats = new Stats(System.currentTimeMillis(), 1L, 1L);
			watch.start("sds.add(" + stats + ")");
			Long id = sds.add(stats);
			logWatchStop(watch);
			LOGGER.info("After sds.add(): " + stats);
			Assert.assertNotNull("Save Stats object to db failed!", id);
			
			// Get
			watch.start("sds.getStatsById(" + id + ", " + false + ")");
			stats = sds.getStatsById(id, false);
			logWatchStop(watch);
			
			// Update
			stats.setRxBytes(1000L);
			watch.start("sds.update(" + stats + ")");
			sds.update(stats);
			logWatchStop(watch);
			LOGGER.info("After stats.setRxBytes(1000L) -> sds.update(): " + stats);
			
			// Get
			watch.start("sds.getStatsById(" + id + ", " + false + ")");
			stats = sds.getStatsById(id, false);
			logWatchStop(watch);
			LOGGER.info("sds.getStats(id=" + id + "): " + stats);
			Assert.assertEquals("RxBytes value not updated!", new Long(1000L), stats.getRxBytes());
			
			// Delete
			watch.start("sds.delete(" + stats + ")");
			sds.delete(stats);
			logWatchStop(watch);
			
			// Get
			watch.start("sds.getStatsById(" + id + ", " + false + ")");
			stats = sds.getStatsById(id, false);
			logWatchStop(watch);
			LOGGER.info("After stats.delete() -> sds.getStats(id=" + id + "): " + stats);
			Assert.assertNull("Delete stats object failed!", stats);
		} catch (Exception e) {
			LOGGER.warn(null, e);
			throw e;
		}
	}

	@Test
	public void testAddNoteToStats() throws Exception {
		LOGGER.info("\n\ntestAddNoteToStats()");
		try {
			StopWatch watch = new StopWatch();
			watch.start("Setup Context");
			final ApplicationContext context = new ClassPathXmlApplicationContext(
					new String[] {"/META-INF/spring/app-context.xml", "/META-INF/spring/db-context.xml"}, 
					StatsDatabaseServiceTest.class);
			logWatchStop(watch);

			StatsDatabaseService sds = StatsDatabaseServiceImpl.getStatsDatabaseService(context);
			
			// Add
			Stats stats = new Stats(System.currentTimeMillis(), 1L, 1L);
			watch.start("sds.add(" + stats + ")");
			Long id = sds.add(stats);
			logWatchStop(watch);
			LOGGER.info("After adding to db with sds.add(): " + stats);
			Assert.assertNotNull("Save Stats object to db failed!", id);
			
			int count = 10;
			// Add note and merge
			for (int i = 0; i < count; i++) {
				//stats.getNotes().add(new Notes(stats, "test" + i));
				sds.addNoteToStat(stats, "test_");
			}
			watch.start("Add " + count + " Notes to Stats and merge()");
			stats = sds.merge(stats);
			logWatchStop(watch);
			LOGGER.info("After adding note and sds.merge(stats): " + stats);
			Assert.assertNotNull(stats);

			// Delete
			watch.start("sds.delete(" + stats + ")");
			sds.delete(stats);
			logWatchStop(watch);
			
			watch.start("sds.getStatsById(" + id + ", " + false + ")");
			stats = sds.getStatsById(id, false);
			logWatchStop(watch);
			LOGGER.info("After stats.delete() -> sds.getStats(id=" + id + "): " + stats);
			Assert.assertNull("Delete stats object failed!", stats);
		} catch (Exception e) {
			LOGGER.warn(null, e);
			throw e;
		}
	}
	
	private void logWatchStop(StopWatch watch) {
		watch.stop();
		TaskInfo ti = watch.getLastTaskInfo();
		LOGGER.info("Task: " + ti.getTaskName() + ". Time: " + ti.getTimeMillis() +"ms");
	}
}
