/**
 * 
 */
package uk.org.vacuumtube.service;



/**
 * @author clivem
 *
 */
public class RemoteStatsDatabaseServiceImpl 
		extends StatsDatabaseServiceImpl 
		implements RemoteStatsDatabaseService {
	
	public RemoteStatsDatabaseServiceImpl() {
		super();
		LOGGER.info("Created: RemoteStatsDatabaseServiceImpl()");
	}
}
