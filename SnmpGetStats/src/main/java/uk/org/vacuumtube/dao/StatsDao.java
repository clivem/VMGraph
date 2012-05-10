/**
 * 
 */
package uk.org.vacuumtube.dao;

import java.util.List;

/**
 * @author clivem
 *
 */
public interface StatsDao {

	/**
	 * @param entity
	 * @return a string representation of the entity
	 */
	public String statsToString(Stats stats);
	
	/**
	 * @param stats
	 * @return the id of the Stats entity
	 */
	public Long createStats(Stats stats);
	
	/**
	 * @param stats
	 */
	public void deleteStats(Stats stats);

	/**
	 * @param stats
	 */
	public void updateStats(Stats stats);
	
	/**
	 * @param stats
	 * @return the Stats object with the current DB record
	 */
	public Stats mergeStats(Stats stats);

	/**
	 * @return
	 */
	public Stats loadStatsById(long id);
	
	/**
	 * @param id
	 * @return the Stats object identified by the id
	 */
	public Stats getStatsById(long id);
	
	/**
	 * @param id
	 * @param lazy
	 * @return the Stats object identified by the id
	 */
	public Stats getStatsById(long id, boolean lazy);

	/**
	 * @return a Collection of the Stats entities
	 */
	public List<Stats> getStatsList();
	
	/**
	 * @param lazy
	 * @return a Collection of the Stats entities
	 */
	public List<Stats> getStatsList(boolean lazy);

	/**
	 * @return the number of persistent Stats entities
	 */
	public int getStatsCount();
	
	/**
	 * @param stats
	 * @param note
	 * @return the created Notes object
	 */
	public Notes createNote(Stats stats, String note);

	/**
	 * @param note
	 */
	public void deleteNote(Notes note);
	
}
