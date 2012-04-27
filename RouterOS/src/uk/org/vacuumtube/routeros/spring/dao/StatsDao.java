/**
 * 
 */
package uk.org.vacuumtube.routeros.spring.dao;

import java.util.List;

import uk.org.vacuumtube.routeros.Stats;


/**
 * @author clivem
 *
 */
public interface StatsDao {

	/**
	 * @param stats
	 * @return
	 */
	public Stats add(Stats stats);
	
	/**
	 * @param stats
	 * @return
	 */
	public void delete(Stats stats);

	/**
	 * @param stats
	 */
	public void update(Stats stats);
	
	/**
	 * @param id
	 * @return
	 */
	public Stats getStats(long id);
	
	/**
	 * @return
	 */
	public int getCount();
	
	/**
	 * @return
	 */
	public List<Stats> getStatsList();
}
