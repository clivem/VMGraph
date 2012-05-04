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
	 * @see uk.org.vacuumtube.routeros.service.RouterStatsService#getStatsList()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Stats> getStatsList() {
		return statsDao.getStatsList();
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#addNote(uk.org.vacuumtube.dao.Stats, uk.org.vacuumtube.dao.Notes)
	 */
	@Override
	@Transactional
	public void addNote(Stats stats, Notes note) {
		statsDao.addNote(stats, note);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#addNote(uk.org.vacuumtube.dao.Notes)
	 */
	@Override
	@Transactional
	public void addNote(Notes note) {
		statsDao.addNote(note);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#add(uk.org.vacuumtube.dao.Stats)
	 */
	@Override
	@Transactional
	public Long add(Stats stats) {
		return statsDao.add(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#delete(uk.org.vacuumtube.routeros.Stats)
	 */
	@Override
	@Transactional
	public void delete(Stats stats) {
		statsDao.delete(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#update(uk.org.vacuumtube.routeros.Stats)
	 */
	@Override
	@Transactional
	public void update(Stats stats) {
		statsDao.update(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#mergeStats(uk.org.vacuumtube.dao.Stats)
	 */
	@Override
	@Transactional
	public Stats merge(Stats stats) {
		return statsDao.merge(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#getStats(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Stats getStats(long id) {
		return statsDao.getStats(id);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#getCount()
	 */
	@Override
	@Transactional(readOnly = true)
	public int getCount() {
		return statsDao.getCount();
	}

	/**
	 * @param ctx
	 * @return
	 */
	public final static StatsDatabaseService getStatsDatabaseService(ApplicationContext ctx) {
		return ctx.getBean("statsDatabaseService", StatsDatabaseService.class);
	}
}
