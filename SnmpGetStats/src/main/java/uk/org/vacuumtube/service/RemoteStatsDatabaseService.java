/**
 * 
 */
package uk.org.vacuumtube.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import uk.org.vacuumtube.dao.Stats;

/**
 * @author clivem
 *
 */
public interface RemoteStatsDatabaseService extends Remote {

	/**
	 * @return a Collection of the Stats entities
	 */
	public List<Stats> getStatsList() throws RemoteException;
	
	/**
	 * @param lazy
	 * @return a Collection of the Stats entities
	 */
	public List<Stats> getStatsList(boolean lazy) throws RemoteException;

}
