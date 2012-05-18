/**
 * 
 */
package uk.org.vacuumtube.dao;

/**
 * @author clivem
 *
 */
public interface HistoryDao {

	public Long createHistory(History history);
	
	public History getHistoryById(Long id);
	
	public void deleteHistory(History history);
}
