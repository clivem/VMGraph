/**
 * 
 */
package uk.org.vacuumtube.service;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import uk.org.vacuumtube.dao.Notes;
import uk.org.vacuumtube.dao.Stats;
import uk.org.vacuumtube.dao.StatsDao;

/**
 * @author clivem
 *
 */
public class StatsDatabaseServiceImpl implements StatsDatabaseService {

	private StatsDao statsDao;
	
	/**
	 * 
	 */
	public StatsDatabaseServiceImpl() {
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
		return statsDao.statsToString(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.service.RouterStatsService#getStatsList()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Stats> getStatsList() {
		return statsDao.getStatsList();
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#getStatsList(boolean)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Stats> getStatsList(boolean lazy) {
		return statsDao.getStatsList(lazy);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#addNote(uk.org.vacuumtube.dao.Stats, uk.org.vacuumtube.dao.Notes)
	 */
	@Override
	@Transactional
	public Notes createNote(Stats stats, String note) {
		return statsDao.createNote(stats, note);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#add(uk.org.vacuumtube.dao.Stats)
	 */
	@Override
	@Transactional
	public Long createStats(Stats stats) {
		return statsDao.createStats(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#deleteNote(uk.org.vacuumtube.dao.Notes)
	 */
	@Override
	@Transactional
	public void deleteNote(Notes note) {
		statsDao.deleteNote(note);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#delete(uk.org.vacuumtube.routeros.Stats)
	 */
	@Override
	@Transactional
	public void deleteStats(Stats stats) {
		statsDao.deleteStats(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#update(uk.org.vacuumtube.routeros.Stats)
	 */
	@Override
	@Transactional
	public void updateStats(Stats stats) {
		statsDao.updateStats(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#mergeStats(uk.org.vacuumtube.dao.Stats)
	 */
	@Override
	@Transactional
	public Stats mergeStats(Stats stats) {
		return statsDao.mergeStats(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#getStats(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Stats getStatsById(long id) {
		return statsDao.getStatsById(id);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#getStatsById(long, boolean)
	 */
	@Override
	@Transactional(readOnly = true)
	public Stats getStatsById(long id, boolean lazy) {
		return statsDao.getStatsById(id, lazy);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#getCount()
	 */
	@Override
	@Transactional(readOnly = true)
	public int getStatsCount() {
		return statsDao.getStatsCount();
	}

	/**
	 * @param ctx
	 * @return
	 */
	public final static StatsDatabaseService getStatsDatabaseService(ApplicationContext ctx) {
		return ctx.getBean("statsDatabaseService", StatsDatabaseService.class);
	}
}
