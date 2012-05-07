/**
 * 
 */
package uk.org.vacuumtube.hibernate;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.context.ApplicationContext;

import uk.org.vacuumtube.dao.Notes;
import uk.org.vacuumtube.dao.Stats;
import uk.org.vacuumtube.dao.StatsDao;
import uk.org.vacuumtube.exception.InfrastructureException;

/**
 * @author clivem
 *
 */
public class StatsDaoImpl extends HibernateDaoImpl implements StatsDao {

	private final static Logger LOGGER = Logger.getLogger(StatsDaoImpl.class);
	
	/**
	 * 
	 */
	public StatsDaoImpl() throws InfrastructureException {
	}
	
	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#addNote(uk.org.vacuumtube.dao.Notes)
	 */
	@Override
	public void addNote(Notes note) throws InfrastructureException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("addNote(note=" + note + ")");
		}
		super.makePersistent(note);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#addNote(uk.org.vacuumtube.dao.Stats, uk.org.vacuumtube.dao.Notes)
	 */
	@Override
	public void addNote(Stats stats, Notes note) throws InfrastructureException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("addNote(stats=" + stats + ", note=" + note + ")");
		}

		stats.getNotes().add(note);
		super.makePersistent(note);
		//super.makePersistent(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#add(uk.org.vacuumtube.routeros.spring.dao.Stats)
	 */
	@Override
	public Long add(Stats stats) throws InfrastructureException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("add(stats=" + stats + ")");
		}
		
		try {
			Long id = (Long) getSession().save(stats);
			return id;
		} catch (HibernateException he) {
			throw new InfrastructureException(he);
		}
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#delete(uk.org.vacuumtube.routeros.Stats)
	 */
	@Override
	public void delete(Stats stats) throws InfrastructureException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("delete(stats=" + stats + ")");
		}
		
		super.makeTransient(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#update(uk.org.vacuumtube.routeros.Stats)
	 */
	@Override
	public void update(Stats stats) throws InfrastructureException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("update(stats=" + stats + ")");
		}
		
		super.update(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#merge(uk.org.vacuumtube.dao.Stats)
	 */
	@Override
	public Stats merge(Stats stats) throws InfrastructureException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("merge(stats=" + stats + ")");
		}
		
		return (Stats) super.merge(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#getStats(long)
	 */
	@Override
	public Stats getStats(long id) throws InfrastructureException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("getStats(id=" + id + ")");
		}
		
		try {
			Stats stats = (Stats) getSession().get(Stats.class, id);
			//loadNotes(stats);
			return stats;
		} catch (HibernateException he) {
			throw new InfrastructureException(he);
		}
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#getCount()
	 */
	@Override
	public int getCount() throws InfrastructureException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("getCount()");
		}
		
		try {
			Long count = (Long) getSession()
					.createQuery("select count(*) from Stats").uniqueResult();
			return count.intValue();
		} catch (HibernateException he) {
			throw new InfrastructureException(he);
		}
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#getStatsList()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Stats> getStatsList() throws InfrastructureException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("getStatsList()");
		}

		try {
			List<Stats> statsList = getSession()
					.createQuery("from Stats stats ORDER BY stats.id")
					//.createQuery("from Stats stats")
					.list();
			/*
			for (Stats stat : statsList) {
				loadNotes(stat);
			}
			*/
			return statsList;
		} catch (HibernateException he) {
			throw new InfrastructureException(he);
		}
	}

	/**
	 * @param stats
	 */
	@SuppressWarnings("unused")
	private void loadNotes(Stats stats) {
		if (stats != null) {
			Collection<Notes> notesList = stats.getNotes();
			if (notesList != null) {
				Iterator<Notes> it = notesList.iterator();
				while (it.hasNext()) {
					LOGGER.debug(it.next());
				}
			}
		}
	}
	
	/**
	 * @param ctx
	 * @return
	 */
	public final static StatsDao getStatsDao(ApplicationContext ctx) {
		return ctx.getBean("hibernateStatsDao", StatsDaoImpl.class);
	}
}
