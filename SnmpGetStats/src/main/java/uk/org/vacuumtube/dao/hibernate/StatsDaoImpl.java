/**
 * 
 */
package uk.org.vacuumtube.dao.hibernate;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import uk.org.vacuumtube.dao.Notes;
import uk.org.vacuumtube.dao.Stats;
import uk.org.vacuumtube.dao.StatsDao;

/**
 * @author clivem
 *
 */
@Repository
public class StatsDaoImpl extends AbstractHibernateDaoImpl implements StatsDao {

	private final static Logger LOGGER = Logger.getLogger(StatsDaoImpl.class);
	
	/**
	 * 
	 */
	public StatsDaoImpl() {
		super();
		LOGGER.info("Created: StatsDaoImpl()");
	}
	
	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#statsToString(uk.org.vacuumtube.dao.Stats)
	 */
	@Override
	public String statsToString(Stats stats) {
		return super.entityToString(stats);
	}
	
	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#addNote(uk.org.vacuumtube.dao.Stats, uk.org.vacuumtube.dao.Notes)
	 */
	@Override
	public Notes createNote(Stats stats, String note) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("addNote(stats=" + stats.shortDescription() + ", note=" + note + ")");
		}

		Notes notes = stats.addNote(note);
		super.save(notes);
		return notes;
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#deleteNote(uk.org.vacuumtube.dao.Notes)
	 */
	@Override
	public void deleteNote(Notes note) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("deleteNote(note=" + note + ")");
		}
		
		Stats stats = note.getStats();
		if (stats != null) {
			stats.removeNote(note);
		}
		
		super.delete(note);
	}
	
	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#add(uk.org.vacuumtube.routeros.spring.dao.Stats)
	 */
	@Override
	public Long createStats(Stats stats) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("add(stats=" + stats.shortDescription() + ")");
		}

		return (Long) super.save(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#delete(uk.org.vacuumtube.routeros.Stats)
	 */
	@Override
	public void deleteStats(Stats stats) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("delete(stats=" + stats.shortDescription() + ")");
		}
		
		super.delete(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#update(uk.org.vacuumtube.routeros.Stats)
	 */
	@Override
	public void updateStats(Stats stats) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("update(stats=" + stats.shortDescription() + ")");
		}
		
		super.update(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#merge(uk.org.vacuumtube.dao.Stats)
	 */
	@Override
	public Stats mergeStats(Stats stats) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("merge(stats=" + stats.shortDescription() + ")");
		}
		
		return (Stats) super.merge(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#loadStatsById()
	 */
	@Override
	public Stats loadStatsById(long id) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("loadStatsById(id=" + id + ")");
		}
		
		Stats stats = (Stats) super.load(Stats.class, id); 
		if (stats != null) {
			if (!Hibernate.isInitialized(stats)) {
				try {
					Hibernate.initialize(stats);
				} catch (ObjectNotFoundException ex) {
					return null;
				}
			}
		}
		return stats;
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#getStats(long)
	 */
	@Override
	public Stats getStatsById(long id) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("getStats(id=" + id + ")");
		}
		
		return getStatsById(id, false);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#getStatsById(long, boolean)
	 */
	@Override
	public Stats getStatsById(long id, boolean lazy) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("getStats(id=" + id + ", lazy=" + lazy + ")");
		}

		Stats stats = null;
		/*
		if (!lazy) {
			getSession().enableFetchProfile("stats-with-notes");
		}
		stats = (Stats) getSession().get(Stats.class, id);
		*/
		
		if (!lazy) {
			Criteria criteria = getSession().createCriteria(Stats.class)
					.add(Restrictions.idEq(id));				
			criteria = criteria.setFetchMode("notes", FetchMode.JOIN);
			stats = (Stats) criteria.uniqueResult();
		} else {
			stats = (Stats) super.get(Stats.class, id);
		}
		return stats;
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#getCount()
	 */
	@Override
	public int getStatsCount() {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("getCount()");
		}
		
		/*
		Long count = (Long) getSession()
				.createQuery("select count(*) from Stats")
				.uniqueResult();
		return count.intValue();
		*/
		
		return ((Long) super.getSingle("select count(*) from Stats")).intValue();
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#getStatsList()
	 */
	@Override
	public List<Stats> getStatsList() {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("getStatsList()");
		}

		return getStatsList(false);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.StatsDao#getStatsList(boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Stats> getStatsList(boolean lazy) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("getStatsList(lazy=" + lazy + ")");
		}
		
		List<Stats> statsList = null;
		/*
		Criteria criteria = getSession().createCriteria(Stats.class)
				.addOrder(Order.asc("id"));
		
		if (!lazy) {
			criteria = criteria.setFetchMode("notes", FetchMode.JOIN)
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		}

		statsList = criteria.list();
		*/
		String hql = null;
		if (!lazy) {
			hql = "select DISTINCT stats from Stats stats left join fetch stats.notes ORDER BY stats.id";
		} else {
			hql = "select stats from Stats stats ORDER BY stats.id";
		}
		statsList = (List<Stats>) super.getList(hql);		

		return statsList;
	}

	/**
	 * @param stats
	 */
	private void eagerLoadNotesCollection(Stats stats) {
		if (stats != null) {
			Hibernate.initialize(stats.getNotes());
		}
	}
	
	/**
	 * @param statsList
	 */
	@SuppressWarnings("unused")
	private void eagerLoadNotesCollection(Collection<Stats> statsList) {
		for (Stats stats : statsList) {
			eagerLoadNotesCollection(stats);
		}
	}
}
