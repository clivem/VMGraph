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

	/*
	 * WEB
	 */
	public final static String WEB_CONTEXT_PATH = "/remoting/";
	
	/*
	 * RMI
	 */
	public final static int RMI_REGISTRY_PORT = 58500;
	
	/*
     * LOCAL
     */
    public final static String STATS_SERVICE_BEAN_NAME = 
    		"statsDatabaseService";

    /*
     * RMI
     */
    public final static String STATS_SERVICE_RMI_BEAN_NAME = 
    		"statsRmiProxyFactory";
    public final static String STATS_SERVICE_RMI_SERVICE_NAME = 
    		"StatsDatabaseService";
    public final static int STATS_SERVICE_RMI_SERVICE_PORT = 
    		58501;
    
    /*
     * HTTP (Spring Default http transport / Java Object serialisation)
     */
    public final static String STATS_SERVICE_HTTP_SIMPLE_JAVA_BEAN_NAME = 
    		"statsDatabaseServiceProxyFactory";
    public final static String STATS_SERVICE_HTTP_SIMPLE_JAVA_SERVICE_NAME = 
    		"StatsDatabaseService";

    /*
     * HTTP (commons-http transport / Java Object serialisation)
     */
    public final static String STATS_SERVICE_HTTP_COMMONS_JAVA_BEAN_NAME = 
    		"customStatsDatabaseServiceProxyFactory";
    public final static String STATS_SERVICE_HTTP_COMMONS_JAVA_SERVICE_NAME = 
    		"CustomStatsDatabaseService";

    /*
     * HTTP (commons-http transport / JBoss Object serialisation)
     */
    public final static String STATS_SERVICE_HTTP_COMMONS_JBOSS_BEAN_NAME = 
    		"jbossStatsDatabaseServiceProxyFactory";
    public final static String STATS_SERVICE_HTTP_COMMONS_JBOSS_SERVICE_NAME = 
    		"JBossStatsDatabaseService";
	
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
	 * @return a local instance of a StatsDatabaseService 
	 */
	public final StatsDatabaseService getStatsDatabaseService() {
		return getStatsDatabaseService(applicationContext);
	}
	
	/**
	 * @param context
	 * @return a local instance of a StatsDatabaseService 
	 */
	public static final StatsDatabaseService getStatsDatabaseService(ApplicationContext context) {
		return context.getBean(STATS_SERVICE_BEAN_NAME, StatsDatabaseService.class);
	}
	
	/**
	 * @return an instance of a RMI proxied RemoteStatsDatabaseService for remote invocation 
	 */
	public final StatsDatabaseService getRemoteStatsDatabaseService() {
		return getRemoteStatsDatabaseService(applicationContext);
	}
	
	/**
	 * @param context
	 * @return an instance of a RMI proxied RemoteStatsDatabaseService for remote invocation 
	 */
	public static final StatsDatabaseService getRemoteStatsDatabaseService(ApplicationContext context) { 
		return context.getBean(STATS_SERVICE_RMI_BEAN_NAME, StatsDatabaseService.class);
	}

	/**
	 * @return an instance of a http proxied StatsDatabaseService using the default Spring http connection
	 * management and Java object serialization
	 */
	public final StatsDatabaseService getSimpleStatsDatabaseService() {
		return getSimpleStatsDatabaseService(applicationContext);
	}
	
	/**
	 * @param context
	 * @return an instance of a http proxied StatsDatabaseService using the default Spring http connection
	 * management and Java object serialization
	 */
	public static final StatsDatabaseService getSimpleStatsDatabaseService(ApplicationContext context) { 
		return context.getBean(STATS_SERVICE_HTTP_SIMPLE_JAVA_BEAN_NAME, StatsDatabaseService.class);
	}
	
	/**
	 * @return an instance of a http proxied StatsDatabaseService using commons-http for connection and 
	 * Java object serialization
	 */
	public final StatsDatabaseService getCustomStatsDatabaseService() {
		return getCustomStatsDatabaseService(applicationContext);
	}
	
	/**
	 * @param context
	 * @return an instance of a http proxied StatsDatabaseService using commons-http for connection and 
	 * Java object serialization
	 */
	public static final StatsDatabaseService getCustomStatsDatabaseService(ApplicationContext context) { 
		return context.getBean(STATS_SERVICE_HTTP_COMMONS_JAVA_BEAN_NAME, StatsDatabaseService.class);
	}
	
	/**
	 * @return an instance of a http proxied StatsDatabaseService using commons-http for connection and 
	 * JBoss object serialization
	 */
	public final StatsDatabaseService getJBossStatsDatabaseService() {
		return getJBossStatsDatabaseService(applicationContext);
	}

	/**
	 * @param context
	 * @return an instance of a http proxied StatsDatabaseService using commons-http for connection and 
	 * JBoss object serialization
	 */
	public static final StatsDatabaseService getJBossStatsDatabaseService(ApplicationContext context) { 
		return context.getBean(STATS_SERVICE_HTTP_COMMONS_JBOSS_BEAN_NAME, StatsDatabaseService.class);
	}
	
	/**
	 * @return an instance of a burlap proxied StatsDatabaseService
	 *
	public final StatsDatabaseService getBurlapStatsDatabaseService() {
		return getBurlapStatsDatabaseService(applicationContext);
	}
	*/
	
	/**
	 * @param context
	 * @return an instance of a burlap proxied StatsDatabaseService 
	 *
	public static final StatsDatabaseService getBurlapStatsDatabaseService(ApplicationContext context) { 
		return context.getBean("statsBurlapProxyFactory", StatsDatabaseService.class);
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
	 * @return an instance of a hessian proxied StatsDatabaseService 
	 *
	public static final StatsDatabaseService getHessianStatsDatabaseService(ApplicationContext context) { 
		return context.getBean("statsHessianProxyFactory", StatsDatabaseService.class);
	}
	*/
}
