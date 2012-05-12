/**
 * 
 */
package uk.org.vacuumtube.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import uk.org.vacuumtube.service.RemoteStatsDatabaseService;

/**
 * @author clivem
 *
 */
@Configuration
public class RemoteClientConfiguration {

	@Bean(name = "statsRmiProxyFactory")
	public RmiProxyFactoryBean statsRmiProxyFactory() {
		RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
		rmiProxyFactoryBean.setServiceUrl("rmi://192.168.0.31:1199/StatsDatabaseService");
		rmiProxyFactoryBean.setServiceInterface(RemoteStatsDatabaseService.class);
		return rmiProxyFactoryBean;
	}
	
	@Bean(name = "remoteStatsDatabaseService")
	public RemoteStatsDatabaseService statsDatabaseService() {
		return (RemoteStatsDatabaseService) statsRmiProxyFactory().getObject();
	}
}
