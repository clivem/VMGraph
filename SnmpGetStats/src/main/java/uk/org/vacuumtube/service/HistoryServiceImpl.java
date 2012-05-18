/**
 * 
 */
package uk.org.vacuumtube.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import uk.org.vacuumtube.dao.History;
import uk.org.vacuumtube.dao.HistoryDao;

/**
 * @author clivem
 *
 */
public class HistoryServiceImpl implements HistoryService {

	private static final Logger LOGGER = Logger.getLogger(HistoryServiceImpl.class);
	
	private HistoryDao historyDao;
	
	public HistoryServiceImpl() {
		super();
		LOGGER.info("Created: HistoryServiceImpl()");
	}
	
	/**
	 * @return the historyDao
	 */
	public HistoryDao getHistoryDao() {
		return historyDao;
	}

	/**
	 * @param historyDao the historyDao to set
	 */
	public void setHistoryDao(HistoryDao historyDao) {
		this.historyDao = historyDao;
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.HistoryDao#createHistory(uk.org.vacuumtube.dao.History)
	 */
	@Override
	@Transactional
	public Long createHistory(History history) {

		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("createHistory(history=" + history + ")");
		}
		return historyDao.createHistory(history);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.HistoryDao#getHistoryById(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public History getHistoryById(Long id) {
		
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("getHistoryById(id=" + id + ")");
		}
		return historyDao.getHistoryById(id);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.HistoryDao#deleteHistory(uk.org.vacuumtube.dao.History)
	 */
	@Override
	@Transactional
	public void deleteHistory(History history) {
		
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("deleteHistory(history=" + history + ")");
		}
		historyDao.deleteHistory(history);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.HistoryDao#createHistory(java.util.List)
	 */
	@Override
	@Transactional
	public List<History> createHistory(List<History> historyList) {

		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("createHistory(historyList.size=" + historyList.size() + ")");
		}
		return historyDao.createHistory(historyList);
	}
}
