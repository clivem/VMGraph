/**
 * 
 */
package uk.org.vacuumtube.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import uk.org.vacuumtube.dao.Notes;
import uk.org.vacuumtube.dao.Stats;
import uk.org.vacuumtube.dao.StatsDao;
import uk.org.vacuumtube.serialization.test.TestObject;

/**
 * @author clivem
 *
 */
public class StatsDatabaseServiceImpl implements StatsDatabaseService {

	protected static final Logger LOGGER = Logger.getLogger(StatsDatabaseServiceImpl.class);
	
	private StatsDao statsDao;
	
	/**
	 * 
	 */
	public StatsDatabaseServiceImpl() {
		super();
		LOGGER.info("Created: StatsDatabaseServiceImpl()");
	}

	/**
	 * @return the statsDao
	 */
	public StatsDao getStatsDao() {
		return statsDao;
	}

	/**
	 * @param statsDao the statsDao to set
	 */
	public void setStatsDao(StatsDao statsDao) {
		this.statsDao = statsDao;
	}
	
	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#statsToString(uk.org.vacuumtube.dao.Stats)
	 */
	@Override
	@Transactional(readOnly = true)
	public String statsToString(Stats stats) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("statsToString(stats=" + stats + ")");
		}
		return statsDao.statsToString(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.service.RouterStatsService#getStatsList()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Stats> getStatsList() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getStatsList()");
		}
		return statsDao.getStatsList();
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#getStatsList(boolean)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Stats> getStatsList(boolean lazy) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getStatsList(lazy=" + lazy + ")");
		}
		return statsDao.getStatsList(lazy);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#addNote(uk.org.vacuumtube.dao.Stats, uk.org.vacuumtube.dao.Notes)
	 */
	@Override
	@Transactional
	public Notes createNote(Stats stats, String note) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("createNote(stats=" + stats + ", note=" + note + ")");
		}
		return statsDao.createNote(stats, note);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#add(uk.org.vacuumtube.dao.Stats)
	 */
	@Override
	@Transactional
	public Long createStats(Stats stats) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("createStats(stats=" + stats + ")");
		}
		return statsDao.createStats(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#deleteNote(uk.org.vacuumtube.dao.Notes)
	 */
	@Override
	@Transactional
	public void deleteNote(Notes note) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("deleteNote(note=" + note + ")");
		}
		statsDao.deleteNote(note);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#delete(uk.org.vacuumtube.routeros.Stats)
	 */
	@Override
	@Transactional
	public void deleteStats(Stats stats) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("deleteStats(stats=" + stats + ")");
		}
		statsDao.deleteStats(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#update(uk.org.vacuumtube.routeros.Stats)
	 */
	@Override
	@Transactional
	public void updateStats(Stats stats) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("updateStats(stats=" + stats + ")");
		}
		statsDao.updateStats(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#mergeStats(uk.org.vacuumtube.dao.Stats)
	 */
	@Override
	@Transactional
	public Stats mergeStats(Stats stats) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("mergeStats(stats=" + stats + ")");
		}
		return statsDao.mergeStats(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#loadStatsById(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Stats loadStatsById(long id) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("loadStatsById(id=" + id + ")");
		}
		return statsDao.loadStatsById(id);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#getStats(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Stats getStatsById(long id) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getStatsById(id=" + id + ")");
		}
		return statsDao.getStatsById(id);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#getStatsById(long, boolean)
	 */
	@Override
	@Transactional(readOnly = true)
	public Stats getStatsById(long id, boolean lazy) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getStatsById(id=" + id + ", lazy=" + lazy + ")");
		}
		return statsDao.getStatsById(id, lazy);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#getCount()
	 */
	@Override
	@Transactional(readOnly = true)
	public int getStatsCount() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getStatsCount()");
		}
		return statsDao.getStatsCount();
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.service.StatsDatabaseService#getTestObject()
	 */
	@Override
	public TestObject getTestObject() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getTestObject()");
		}
		return TestObject.create();
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.service.StatsDatabaseService#getTestObjectList(int)
	 */
	@Override
	public TestObject[] getTestObjectList(int size) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getTestObjectList(size=" + size + ")");
		}
		return TestObject.createArray(size);
	}
}
