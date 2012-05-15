/**
 * 
 */
package uk.org.vacuumtube.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import uk.org.vacuumtube.service.ServiceLocator;
import uk.org.vacuumtube.service.StatsDatabaseService;

/**
 * @author clivem
 *
 */
@Configuration
@ImportResource("classpath:/META-INF/spring/client-context.xml")
public class RemoteClientConfiguration {

	@Value("${remoting.rmiHost}")
	private String rmiHost;
	
	@Value("${remoting.rmiPort}")
	private int rmiPort;
	
	//@Bean(name = "statsRmiProxyFactory")
	@Bean(name = ServiceLocator.STATS_SERVICE_RMI_BEAN_NAME)
	public RmiProxyFactoryBean statsRmiProxyFactory() {
		RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
		rmiProxyFactoryBean.setServiceUrl("rmi://" + rmiHost + ":" + rmiPort + "/" + 
				ServiceLocator.STATS_SERVICE_RMI_SERVICE_NAME);
		rmiProxyFactoryBean.setServiceInterface(StatsDatabaseService.class);
		return rmiProxyFactoryBean;
	}
}
