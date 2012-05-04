/**
 * 
 */
package uk.org.vacuumtube.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import uk.org.vacuumtube.dao.Notes;
import uk.org.vacuumtube.dao.Stats;
import uk.org.vacuumtube.dao.StatsDao;

/**
 * @author clivem
 *
 */
@Repository
public class StatsDaoImpl implements StatsDao {

	private final static Logger LOGGER = Logger.getLogger(StatsDaoImpl.class);
	
	private SessionFactory sessionFactory;
	
	/**
	 * 
	 */
	public StatsDaoImpl() {
	}
	
	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}	
	
	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#addNote(uk.org.vacuumtube.dao.Notes)
	 */
	@Override
	public void addNote(Notes note) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("addNote(note=" + note + ")");
		}
		sessionFactory.getCurrentSession().saveOrUpdate(note);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#addNote(uk.org.vacuumtube.dao.Stats, uk.org.vacuumtube.dao.Notes)
	 */
	@Override
	public void addNote(Stats stats, Notes note) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("addNote(stats=" + stats + ", note=" + note + ")");
		}
		stats.getNotes().add(note);
		//sessionFactory.getCurrentSession().save(note);
		sessionFactory.getCurrentSession().saveOrUpdate(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#add(uk.org.vacuumtube.routeros.spring.dao.Stats)
	 */
	@Override
	public Long add(Stats stats) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("add(stats=" + stats + ")");
		}
		Long id = (Long) sessionFactory.getCurrentSession().save(stats);
		return id;
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#delete(uk.org.vacuumtube.routeros.Stats)
	 */
	@Override
	public void delete(Stats stats) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("delete(stats=" + stats + ")");
		}
		sessionFactory.getCurrentSession().delete(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#update(uk.org.vacuumtube.routeros.Stats)
	 */
	@Override
	public void update(Stats stats) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("update(stats=" + stats + ")");
		}
		sessionFactory.getCurrentSession().update(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#merge(uk.org.vacuumtube.dao.Stats)
	 */
	@Override
	public Stats merge(Stats stats) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("merge(stats=" + stats + ")");
		}
		return (Stats) sessionFactory.getCurrentSession().merge(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#getStats(long)
	 */
	@Override
	public Stats getStats(long id) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("getStats(id=" + id + ")");
		}
		Stats stats = (Stats) sessionFactory.getCurrentSession()
				.get(Stats.class, id);
		//stats.getNotes();
		return stats;
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#getCount()
	 */
	@Override
	public int getCount() {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("getCount()");
		}
		Long count = (Long) sessionFactory.getCurrentSession()
				.createQuery("select count(*) from Stats").uniqueResult();
		return count.intValue();
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#getStatsList()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Stats> getStatsList() {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("getStatsList()");
		}
		return sessionFactory.getCurrentSession()
				.createQuery("from Stats stats order by stats.id")
				.list();
	}
	
	/**
	 * @param ctx
	 * @return
	 */
	public final static StatsDao getStatsDao(ApplicationContext ctx) {
		return ctx.getBean("hibernateStatsDao", StatsDaoImpl.class);
	}
}
