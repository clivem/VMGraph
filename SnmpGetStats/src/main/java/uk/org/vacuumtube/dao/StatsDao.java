/**
 * 
 */
package uk.org.vacuumtube.dao;

import java.util.List;

import uk.org.vacuumtube.exception.InfrastructureException;

/**
 * @author clivem
 *
 */
public interface StatsDao {

	/**
	 * @param stats
	 * @return
	 */
	public Long add(Stats stats) throws InfrastructureException;
	
	/**
	 * @param stats
	 * @return
	 */
	public void delete(Stats stats) throws InfrastructureException;

	/**
	 * @param stats
	 */
	public void update(Stats stats) throws InfrastructureException;
	
	/**
	 * @param id
	 * @return
	 */
	public Stats getStatsById(long id) throws InfrastructureException;
	
	public Stats getStatsById(long id, boolean lazy) throws InfrastructureException;

	/**
	 * @return
	 */
	public int getCount() throws InfrastructureException;
	
	/**
	 * @return
	 */
	public List<Stats> getStatsList() throws InfrastructureException;
	
	public List<Stats> getStatsList(boolean lazy) throws InfrastructureException;

	/**
	 * @param stats
	 * @param note
	 */
	public Notes addNoteToStat(Stats stats, String note) throws InfrastructureException;


	/**
	 * @param note
	 */
	//public void addNote(Notes note) throws InfrastructureException;
	
	/**
	 * @param stats
	 * @return
	 */
	public Stats merge(Stats stats) throws InfrastructureException;
}
