/**
 * 
 */
package uk.org.vacuumtube.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author clivem
 *
 */
@Service
public class ServiceLocator {

	private ApplicationContext applicationContext = null;
	
	/**
	 * 
	 */
	public ServiceLocator() {
	}
	
	/**
	 * @param applicationContext
	 */
	public ServiceLocator(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	/**
	 * @param applicationContext
	 */
	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * @return the Spring ApplicationContext
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	/**
	 * @return an instance of a StatsDatabaseService
	 */
	public final StatsDatabaseService getStatsDatabaseService() {
		return getStatsDatabaseService(applicationContext);
	}
	
	/**
	 * @return an instance of a RemoteStatsDatabaseService
	 */
	public final RemoteStatsDatabaseService getRemoteStatsDatabaseService() {
		return getRemoteStatsDatabaseService(applicationContext);
	}
	
	/**
	 * @param context
	 * @return an instance of a StatsDatabaseService 
	 */
	public static final StatsDatabaseService getStatsDatabaseService(ApplicationContext context) {
		return context.getBean("statsDatabaseService", StatsDatabaseService.class);
	}
	
	/**
	 * @param context
	 * @return an instance of a RemoteStatsDatabaseService 
	 */
	public static final RemoteStatsDatabaseService getRemoteStatsDatabaseService(ApplicationContext context) { 
		return context.getBean("remoteStatsDatabaseService", RemoteStatsDatabaseService.class);
	}
}
