/**
 * 
 */
package uk.org.vacuumtube.dao.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import uk.org.vacuumtube.dao.History;
import uk.org.vacuumtube.dao.HistoryDao;
import uk.org.vacuumtube.dao.Sport;

/**
 * @author clivem
 *
 */
@Repository
public class HistoryDaoImpl extends AbstractHibernateDaoImpl implements HistoryDao {
	
	private final static Logger LOGGER = Logger.getLogger(HistoryDaoImpl.class);
	
	public HistoryDaoImpl() {	
		super();
		LOGGER.info("Created: HistoryDaoImpl()");
	}
	
	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.HistoryDao#createHistory(uk.org.vacuumtube.dao.History)
	 */
	@Override
	public Long createHistory(History history) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("createHistory(history=" + history + ")");
		}

		return (Long) super.save(history);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.HistoryDao#getHistoryById(java.lang.Long)
	 */
	@Override
	public History getHistoryById(Long id) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("getHistoryById(id=" + id + ")");
		}

		return (History) super.get(History.class, id);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.HistoryDao#deleteHistory(uk.org.vacuumtube.dao.History)
	 */
	@Override
	public void deleteHistory(History history) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("deleteHistory(history=" + history + ")");
		}
		
		super.delete(history);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.HistoryDao#createHistory(java.util.List)
	 */
	@Override
	public List<History> createHistory(List<History> historyList) {
		int count = 0;
		for (History history : historyList) {
			super.save(history);
			if (++count % 20 == 0) {
				getSession().flush();
				getSession().clear();
			}
		}
		
		return historyList;
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.HistoryDao#createSport(uk.org.vacuumtube.dao.Sport)
	 */
	@Override
	public Long createSport(Sport sport) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("createSport(sport=" + sport + ")");
		}

		return (Long) super.save(sport);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.HistoryDao#getSportById(java.lang.Long)
	 */
	@Override
	public Sport getSportById(Long id) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("getSportById(id=" + id + ")");
		}

		return (Sport) super.get(Sport.class, id);
	}
}
