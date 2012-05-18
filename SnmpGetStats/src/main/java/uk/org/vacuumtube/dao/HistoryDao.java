/**
 * 
 */
package uk.org.vacuumtube.dao;

import java.util.List;

/**
 * @author clivem
 *
 */
public interface HistoryDao {

	public Long createHistory(History history);
	
	public History getHistoryById(Long id);
	
	public void deleteHistory(History history);
	
	public List<History> createHistory(List<History> historyList);
}
