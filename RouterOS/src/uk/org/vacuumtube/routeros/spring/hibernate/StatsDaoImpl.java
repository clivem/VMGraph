/**
 * 
 */
package uk.org.vacuumtube.routeros.spring.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import uk.org.vacuumtube.routeros.Stats;
import uk.org.vacuumtube.routeros.spring.dao.StatsDao;

/**
 * @author clivem
 *
 */
@Repository
public class StatsDaoImpl implements StatsDao {

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
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#add(uk.org.vacuumtube.routeros.spring.dao.Stats)
	 */
	@Override
	public Stats add(Stats stats) {
		sessionFactory.getCurrentSession().save(stats);
		return stats;
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#delete(uk.org.vacuumtube.routeros.Stats)
	 */
	@Override
	public void delete(Stats stats) {
		sessionFactory.getCurrentSession().delete(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#update(uk.org.vacuumtube.routeros.Stats)
	 */
	@Override
	public void update(Stats stats) {
		sessionFactory.getCurrentSession().saveOrUpdate(stats);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#getStats(long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Stats getStats(long id) {
		List<Stats> list = sessionFactory.getCurrentSession()
				.createQuery("from Stats stats where stats.id = :id")
				.setLong("id", id)
				.list();
		
		if (list.size() > 0) {
			return list.get(0);
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#getCount()
	 */
	@Override
	public int getCount() {
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
