/**
 * 
 */
package uk.org.vacuumtube.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import uk.org.vacuumtube.dao.Notes;
import uk.org.vacuumtube.dao.Stats;

/**
 * @author clivem
 *
 */
public interface RemoteStatsDatabaseService extends Remote {

	/**
	 * @param entity
	 * @return a string representation of the entity
	 */
	public String statsToString(Stats stats) throws RemoteException;
	
	/**
	 * @param stats
	 * @return the id of the Stats entity
	 */
	public Long createStats(Stats stats) throws RemoteException;
	
	/**
	 * @param stats
	 */
	public void deleteStats(Stats stats) throws RemoteException;

	/**
	 * @param stats
	 */
	public void updateStats(Stats stats) throws RemoteException;
	
	/**
	 * @param stats
	 * @return the Stats object with the current DB record
	 */
	public Stats mergeStats(Stats stats) throws RemoteException;

	/**
	 * @return
	 */
	public Stats loadStatsById(long id) throws RemoteException;
	
	/**
	 * @param id
	 * @return the Stats object identified by the id
	 */
	public Stats getStatsById(long id) throws RemoteException;
	
	/**
	 * @param id
	 * @param lazy
	 * @return the Stats object identified by the id
	 */
	public Stats getStatsById(long id, boolean lazy) throws RemoteException;

	/**
	 * @return a Collection of the Stats entities
	 */
	public List<Stats> getStatsList() throws RemoteException;
	
	/**
	 * @param lazy
	 * @return a Collection of the Stats entities
	 */
	public List<Stats> getStatsList(boolean lazy) throws RemoteException;

	/**
	 * @return the number of persistent Stats entities
	 */
	public int getStatsCount() throws RemoteException;
	
	/**
	 * @param stats
	 * @param note
	 * @return the created Notes object
	 */
	public Notes createNote(Stats stats, String note) throws RemoteException;

	/**
	 * @param note
	 */
	public void deleteNote(Notes note) throws RemoteException;
	
}
