/**
 * 
 */
package uk.org.vacuumtube;

import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;
import org.springframework.util.StopWatch;
import org.springframework.util.StopWatch.TaskInfo;

import uk.org.vacuumtube.dao.Notes;
import uk.org.vacuumtube.dao.Stats;
import uk.org.vacuumtube.serialization.test.TestObject;
import uk.org.vacuumtube.service.ServiceLocator;
import uk.org.vacuumtube.service.StatsDatabaseService;

/**
 * @author clivem
 *
 */
public abstract class AbstractStatsDatabaseServiceTest implements ApplicationContextAware {

	protected ServiceLocator serviceLocator = null;
	
	private static final Logger LOGGER = Logger.getLogger(AbstractStatsDatabaseServiceTest.class);
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		serviceLocator = new ServiceLocator(applicationContext);
	}
	
	public abstract StatsDatabaseService getStatsDatabaseService();
	public abstract StatsDatabaseService getAltStatsDatabaseService();

	@Test
    public void testGetTestObject() throws Exception {
		LOGGER.info("\n\ntestGetTestObject()");
		try {
			StopWatch watch = new StopWatch();
			watch.start("getTestObject()");
			getStatsDatabaseService().getTestObject();
			logWatchStop(watch);
		} catch (Exception e) {
			LOGGER.warn(null, e);
			throw e;
		}
    }
	
	@Test
    public void testGetTestObjectList() throws Exception {
		LOGGER.info("\n\ntestGetTestObjectList()");
		try {
			int count = 10;
			for (int i = 0; i < count; i++) {
				StatsDatabaseService service = null;
				if (i % 2 == 0) {
					service = getStatsDatabaseService();
				} else {
					service = getAltStatsDatabaseService();
				}
						
				StopWatch watch = new StopWatch();
				int size = 50000;
				watch.start("getTestObjectList(" + size + ")");
				TestObject[] objects = service.getTestObjectList(size);
				logWatchStop(watch);
				LOGGER.info("getTestObjectList(" + size + "): returns " + objects.length + " objects.");
			}
		} catch (Exception e) {
			LOGGER.warn(null, e);
			throw e;
		}
    }
	
	@Test
	public void testLoadStatsById() throws Exception {
		LOGGER.info("\n\ntestLoadStatsById()");
		try {
			StopWatch watch = new StopWatch();

			StatsDatabaseService sds = getStatsDatabaseService();
			
			watch.start("sds.loadStatsById(-1)");
			Stats stats = null;
			try {
				stats = sds.loadStatsById(-1);
			} catch (DataAccessException dae) {
				LOGGER.info("DataAccessException Expected! Message: " + 
						dae.getMessage());
			}

			logWatchStop(watch);
			if (stats != null) {
				LOGGER.info("sds.loadStatsById(-1) returns: " + stats);
			}
			Assert.assertNull("Stats object with id == -1 should not exist in db!", stats);
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
			StatsDatabaseService sds = getStatsDatabaseService();

			watch.start("sds.getCount()");
			int count = sds.getStatsCount();
			logWatchStop(watch);
			LOGGER.info("sds.getCount() returns: " + count);
			Assert.assertTrue(count > -1);
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
			StatsDatabaseService sds = getStatsDatabaseService();
			
			// Create
			Stats stats = new Stats(System.currentTimeMillis(), 1L, 1L);
			
			watch.start("sds.add(" + stats.shortDescription() + ")");
			Long id = sds.createStats(stats);
			logWatchStop(watch);
			
			Assert.assertNotNull("Save Stats object to db failed!", id);
			
			// Get
			boolean lazy = true;
			stats = getStatsById(watch, sds, id, lazy);

			lazy = false;
			stats = getStatsById(watch, sds, id, lazy);
			
			Long updateBytes = new Long(1000L);
			// Update
			stats.setRxBytes(updateBytes);
			
			watch.start("sds.update(" + stats.shortDescription() + ")");
			sds.updateStats(stats);
			logWatchStop(watch);
			
			// Get
			stats = getStatsById(watch, sds, id, lazy);
			Assert.assertEquals("RxBytes value not updated!", updateBytes, stats.getRxBytes());
			
			// Delete
			watch.start("sds.delete(" + stats.shortDescription() + ")");
			sds.deleteStats(stats);
			logWatchStop(watch);
			
			// Get
			stats = getStatsById(watch, sds, id, lazy);
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
			StatsDatabaseService sds = getStatsDatabaseService();
			
			// Add
			Stats stats = new Stats(System.currentTimeMillis(), 1L, 1L);
			watch.start("sds.add(" + stats + ")");
			Long id = sds.createStats(stats);
			logWatchStop(watch);
			LOGGER.info("After adding to db with sds.add(): " + stats);
			Assert.assertNotNull("Save Stats object to db failed!", id);
			
			stats = getStatsById(watch, sds, id, false);

			int count = 5;
			watch.start("Add " + count + " Notes to: " + stats.shortDescription());
			// Add note(s) and merge
			for (int i = 0; i < count; i++) {
				//sds.addNoteToStat(stats, "test_" + i);
				stats.addNote("test_" + i);
			}
			logWatchStop(watch);

			watch.start("updateStats(" + stats.shortDescription() + ")");
			sds.updateStats(stats);
			//sds.update(stats);
			logWatchStop(watch);
			
			stats = getStatsById(watch, sds, id, false);
			//LOGGER.info("After adding " + count + " note(s) and sds.update(stats): " + stats);
			Assert.assertEquals("Number of notes added != number of notes after updateStats()!", 
					count, stats.getNotes().size());
			
			// Delete Notes
			boolean deleteNotes = false;
			if (deleteNotes) {
				Notes[] notes = stats.getNotes().toArray(new Notes[0]);
				watch.start("Deleting " + notes.length + " notes");
				for (Notes note : notes) {
					sds.deleteNote(note);
				}
				logWatchStop(watch);

				stats = getStatsById(watch, sds, id, false);
				Assert.assertTrue("stats.notes.size() != 0 after delete all!", stats.getNotes().size() == 0);
			}

			// Delete
			boolean deleteStats = false;
			if (deleteStats) {
				watch.start("sds.delete(" + stats.shortDescription() + ")");
				sds.deleteStats(stats);
				logWatchStop(watch);
				
				stats = getStatsById(watch, sds, id, true);
				Assert.assertNull("Delete stats object failed!", stats);
			}
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
			StatsDatabaseService sds = getStatsDatabaseService();

			int count = 2;
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
	 * @param sds
	 * @param id
	 * @param lazy
	 * @return
	 */
	private Stats getStatsById(StopWatch watch, StatsDatabaseService sds, Long id, boolean lazy) {
		watch.start("sds.getStatsById(" + id + ", " + lazy + ")");
		Stats stats = sds.getStatsById(id, lazy);
		logWatchStop(watch);
		LOGGER.info("sds.getStatsById(" + id + ", " + lazy + ") returns: " + stats);
		return stats;
	}
	
	/**
	 * @param watch
	 * @param sds
	 * @param lazy
	 * @return
	 */
	private List<Stats> getStatsList(StopWatch watch, StatsDatabaseService sds, boolean lazy) {
		watch.start("getStatsList(" + lazy + ")");
		List<Stats> statsList = sds.getStatsList(lazy);
		logWatchStop(watch);
		LOGGER.info("getStatsList(" + lazy + "): returns " + statsList.size() + " records.");
		for (Stats stat : statsList) {
			LOGGER.info(stat);
		}
		return statsList;
	}
	
	/**
	 * @param watch
	 * @return
	 *
	@SuppressWarnings("unused")
	private final ApplicationContext createApplicationContext_(StopWatch watch) {
		watch.start("Setup context");
		final AnnotationConfigApplicationContext context = 
				new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
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
}
