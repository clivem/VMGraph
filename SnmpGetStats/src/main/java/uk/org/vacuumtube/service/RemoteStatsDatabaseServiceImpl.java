/**
 * 
 */
package uk.org.vacuumtube.service;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import uk.org.vacuumtube.dao.Stats;
import uk.org.vacuumtube.dao.StatsDao;

/**
 * @author clivem
 *
 */
public class RemoteStatsDatabaseServiceImpl implements RemoteStatsDatabaseService {

	private final static Logger LOGGER = Logger.getLogger(RemoteStatsDatabaseServiceImpl.class);
	
	private StatsDao statsDao;
	
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
	 * @see uk.org.vacuumtube.service.RemoteStatsDatabaseService#getStatsList()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Stats> getStatsList() throws RemoteException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("getStatsList()");
		}
		
		return statsDao.getStatsList();
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.service.RemoteStatsDatabaseService#getStatsList(boolean)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Stats> getStatsList(boolean lazy) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("getStatsList(lazy=" + lazy + ")");
		}
		
		return statsDao.getStatsList(lazy);
	}
}
