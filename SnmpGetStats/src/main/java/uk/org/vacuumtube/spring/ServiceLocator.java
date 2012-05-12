/**
 * 
 */
package uk.org.vacuumtube.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import uk.org.vacuumtube.service.RemoteStatsDatabaseService;
import uk.org.vacuumtube.service.StatsDatabaseService;

/**
 * @author clivem
 *
 */
public class ServiceLocator implements ApplicationContextAware {

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
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * @return the Spring ApplicationContext
	 */
	public final ApplicationContext getApplicationContext() {
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
