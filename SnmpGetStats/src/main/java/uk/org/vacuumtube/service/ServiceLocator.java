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
	 * @return an instance of a RMI proxied RemoteStatsDatabaseService
	 */
	public final RemoteStatsDatabaseService getRemoteStatsDatabaseService() {
		return getRemoteStatsDatabaseService(applicationContext);
	}
	
	/**
	 * @return an instance of a http proxied StatsDatabaseService
	 */
	public final StatsDatabaseService getSimpleStatsDatabaseService() {
		return getSimpleStatsDatabaseService(applicationContext);
	}
	
	/**
	 * @return an instance of a http proxied StatsDatabaseService
	 */
	public final StatsDatabaseService getCustomStatsDatabaseService() {
		return getCustomStatsDatabaseService(applicationContext);
	}
	
	/**
	 * @return an instance of a http proxied StatsDatabaseService
	 */
	public final StatsDatabaseService getJBossStatsDatabaseService() {
		return getJBossStatsDatabaseService(applicationContext);
	}
	
	/**
	 * @return an instance of a burlap proxied StatsDatabaseService
	 *
	public final StatsDatabaseService getBurlapStatsDatabaseService() {
		return getBurlapStatsDatabaseService(applicationContext);
	}
	*/
	
	/**
	 * @return an instance of a hessian proxied StatsDatabaseService
	 *
	public final StatsDatabaseService getHessianStatsDatabaseService() {
		return getHessianStatsDatabaseService(applicationContext);
	}
	*/
	
	/**
	 * @param context
	 * @return an instance of a StatsDatabaseService 
	 */
	public static final StatsDatabaseService getStatsDatabaseService(ApplicationContext context) {
		return context.getBean("statsDatabaseService", StatsDatabaseService.class);
	}
	
	/**
	 * @param context
	 * @return an instance of a RMI proxied RemoteStatsDatabaseService 
	 */
	public static final RemoteStatsDatabaseService getRemoteStatsDatabaseService(ApplicationContext context) { 
		return context.getBean("statsRmiProxyFactory", RemoteStatsDatabaseService.class);
	}

	/**
	 * @param context
	 * @return an instance of a http proxied StatsDatabaseService 
	 */
	public static final StatsDatabaseService getSimpleStatsDatabaseService(ApplicationContext context) { 
		return context.getBean("statsDatabaseServiceProxyFactory", StatsDatabaseService.class);
	}
	
	/**
	 * @param context
	 * @return an instance of a http proxied StatsDatabaseService 
	 */
	public static final StatsDatabaseService getCustomStatsDatabaseService(ApplicationContext context) { 
		return context.getBean("customStatsDatabaseServiceProxyFactory", StatsDatabaseService.class);
	}
	
	/**
	 * @param context
	 * @return an instance of a http proxied StatsDatabaseService 
	 */
	public static final StatsDatabaseService getJBossStatsDatabaseService(ApplicationContext context) { 
		return context.getBean("jbossStatsDatabaseServiceProxyFactory", StatsDatabaseService.class);
	}
	
	/**
	 * @param context
	 * @return an instance of a burlap proxied StatsDatabaseService 
	 *
	public static final StatsDatabaseService getBurlapStatsDatabaseService(ApplicationContext context) { 
		return context.getBean("statsBurlapProxyFactory", StatsDatabaseService.class);
	}
	*/

	/**
	 * @param context
	 * @return an instance of a hessian proxied StatsDatabaseService 
	 *
	public static final StatsDatabaseService getHessianStatsDatabaseService(ApplicationContext context) { 
		return context.getBean("statsHessianProxyFactory", StatsDatabaseService.class);
	}
	*/
}
