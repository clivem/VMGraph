/**
 * 
 */
package uk.org.vacuumtube.spring;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Scope;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.httpinvoker.HttpInvokerRequestExecutor;

import uk.org.vacuumtube.commons.http.CommonsHttpClientProperties;
import uk.org.vacuumtube.commons.http.CustomCommonsHttpInvokerRequestExecutor;
import uk.org.vacuumtube.commons.http.Default15HTTPSTransportClientProperties;
import uk.org.vacuumtube.commons.http.Default15HTTPTransportClientProperties;
import uk.org.vacuumtube.commons.http.DefaultCommonsHttpClientProperties;
import uk.org.vacuumtube.commons.http.JBossCustomCommonsHttpInvokerRequestExecutor;
import uk.org.vacuumtube.commons.http.MultiThreadedHttpConnectionManagerFactory;
import uk.org.vacuumtube.commons.http.TransportClientProperties;
import uk.org.vacuumtube.service.ServiceLocator;
import uk.org.vacuumtube.service.StatsDatabaseService;

/**
 * @author clivem
 *
 */
@Configuration
@ImportResource("classpath:/META-INF/spring/client-context.xml")
public class WebClientConfiguration {

	@Value("${remoting.urlPrefix}")
	private String urlPrefix;
	
	@Value("${remoting.contextPath}")
	private String contextPath;
	
	@Bean(name = "httpTransportClientProperties")
	public Default15HTTPTransportClientProperties httpTransportClientProperties() {
		return new Default15HTTPTransportClientProperties();
	}
	
	@Bean(name = "httpsTransportClientProperties")
	public Default15HTTPSTransportClientProperties httpsTransportClientProperties() {
		return new Default15HTTPSTransportClientProperties();
	}
	
	@Bean(name = "transportClientPropertiesMap")
	public Map<String, TransportClientProperties> transportClientPropertiesMap() {
		Map<String, TransportClientProperties> map = new HashMap<String, TransportClientProperties>();
		map.put("http", httpTransportClientProperties());
		map.put("https", httpsTransportClientProperties());
		return map;
	}
	
	@Bean(name = "httpClientProperties")
	public CommonsHttpClientProperties httpClientProperties() {
		DefaultCommonsHttpClientProperties properties = 
				new DefaultCommonsHttpClientProperties();
		return properties;
	}

	@Bean(name = "connectionManager")
	public MultiThreadedHttpConnectionManager connectionManager() {
		return MultiThreadedHttpConnectionManagerFactory.create(httpClientProperties());
	}
	
	// Needs to be prototype not singleton
	@Bean(name = "customCommonsHttpInvokerRequestExecutor")
	@Scope(value = "prototype")
	public HttpInvokerRequestExecutor customCommonsHttpInvokerRequestExecutor() {
		return new CustomCommonsHttpInvokerRequestExecutor(connectionManager(), 
				transportClientPropertiesMap());
	}

	// Needs to be prototype not singleton
	@Bean(name = "jbossCustomCommonsHttpInvokerRequestExecutor")
	@Scope(value = "prototype")
	public HttpInvokerRequestExecutor jbossCustomCommonsHttpInvokerRequestExecutor() {
		return new JBossCustomCommonsHttpInvokerRequestExecutor(connectionManager(), 
				transportClientPropertiesMap());
	}

	//@Bean(name = "statsDatabaseServiceProxyFactory")
	@Bean(name = ServiceLocator.STATS_SERVICE_HTTP_SIMPLE_JAVA_BEAN_NAME)
	public HttpInvokerProxyFactoryBean statsDatabaseServiceProxyFactory() {
		HttpInvokerProxyFactoryBean invoker = new HttpInvokerProxyFactoryBean();
		invoker.setServiceUrl(urlPrefix + contextPath + ServiceLocator.STATS_SERVICE_HTTP_SIMPLE_JAVA_SERVICE_NAME);
		invoker.setServiceInterface(StatsDatabaseService.class);
		return invoker;
	}

	//@Bean(name = "customStatsDatabaseServiceProxyFactory")
	@Bean(name = ServiceLocator.STATS_SERVICE_HTTP_COMMONS_JAVA_BEAN_NAME)
	public HttpInvokerProxyFactoryBean customStatsDatabaseServiceProxyFactory() {
		HttpInvokerProxyFactoryBean invoker = new HttpInvokerProxyFactoryBean();
		invoker.setServiceUrl(urlPrefix + contextPath + ServiceLocator.STATS_SERVICE_HTTP_COMMONS_JAVA_SERVICE_NAME);
		invoker.setHttpInvokerRequestExecutor(customCommonsHttpInvokerRequestExecutor());
		invoker.setServiceInterface(StatsDatabaseService.class);
		return invoker;
	}

	//@Bean(name = "jbossStatsDatabaseServiceProxyFactory")
	@Bean(name = ServiceLocator.STATS_SERVICE_HTTP_COMMONS_JBOSS_BEAN_NAME)
	public HttpInvokerProxyFactoryBean jbossStatsDatabaseServiceProxyFactory() {
		HttpInvokerProxyFactoryBean invoker = new HttpInvokerProxyFactoryBean();
		invoker.setServiceUrl(urlPrefix + contextPath + ServiceLocator.STATS_SERVICE_HTTP_COMMONS_JBOSS_SERVICE_NAME);
		invoker.setHttpInvokerRequestExecutor(jbossCustomCommonsHttpInvokerRequestExecutor());
		invoker.setServiceInterface(StatsDatabaseService.class);
		return invoker;
	}

	/*
	@Bean(name = "statsBurlapProxyFactory")
	public BurlapProxyFactoryBean statsBurlapProxyFactory() {
		BurlapProxyFactoryBean invoker = new BurlapProxyFactoryBean();
		invoker.setServiceUrl("http://127.0.0.1:8080/remoting/BurlapStatsDatabaseService");
		//invoker.setHttpInvokerRequestExecutor(new CommonsHttpInvokerRequestExecutor());
		invoker.setServiceInterface(StatsDatabaseService.class);
		return invoker;
	}

	@Bean(name = "statsHessianProxyFactory")
	public HessianProxyFactoryBean statsHessianProxyFactory() {
		HessianProxyFactoryBean invoker = new HessianProxyFactoryBean();
		invoker.setServiceUrl("http://127.0.0.1:8080/remoting/HessianStatsDatabaseService");
		//invoker.setHttpInvokerRequestExecutor(new CommonsHttpInvokerRequestExecutor());
		invoker.setServiceInterface(StatsDatabaseService.class);
		return invoker;
	}
	*/
}
