/**
 * 
 */
package uk.org.vacuumtube.spring;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.remoting.httpinvoker.SimpleHttpInvokerServiceExporter;
import org.springframework.remoting.support.SimpleHttpServerFactoryBean;

import uk.org.vacuumtube.service.StatsDatabaseService;

import com.sun.net.httpserver.HttpHandler;

/**
 * @author clivem
 *
 */
@Configuration
@Import(ApplicationConfiguration.class)
public class WebServerConfiguration {

	@Autowired
	private ApplicationConfiguration applicationConfiguration;
	
	@Bean(name = "statsDatabaseServiceHttpExporter")
	public SimpleHttpInvokerServiceExporter statsDatabaseServiceHttpExporter() {
		SimpleHttpInvokerServiceExporter exporter = new SimpleHttpInvokerServiceExporter();
		exporter.setService(applicationConfiguration.statsDatabaseService());
		exporter.setServiceInterface(StatsDatabaseService.class);
		return exporter;
	}
	/*
	@Bean(name = "statsDatabaseServiceBurlapExporter")
	public SimpleBurlapServiceExporter statsDatabaseServiceBurlapExporter() {
		SimpleBurlapServiceExporter exporter = new SimpleBurlapServiceExporter();
		exporter.setService(applicationConfiguration.statsDatabaseService());
		exporter.setServiceInterface(StatsDatabaseService.class);
		return exporter;
	}
	
	@Bean(name = "statsDatabaseServiceHessianExporter")
	public SimpleHessianServiceExporter statsDatabaseServiceHessianExporter() {
		SimpleHessianServiceExporter exporter = new SimpleHessianServiceExporter();
		exporter.setService(applicationConfiguration.statsDatabaseService());
		exporter.setServiceInterface(StatsDatabaseService.class);
		return exporter;
	}
	*/
	@Bean(name = "httpServerFactory")
	public SimpleHttpServerFactoryBean simpleHttpServerFactory() {
		Map<String, HttpHandler> map = new HashMap<String, HttpHandler>();
		map.put("/remoting/HttpStatsDatabaseService", statsDatabaseServiceHttpExporter());
		//map.put("/remoting/BurlapStatsDatabaseService", statsDatabaseServiceBurlapExporter());
		//map.put("/remoting/HessianStatsDatabaseService", statsDatabaseServiceHessianExporter());
		
		SimpleHttpServerFactoryBean httpServerFactory = new SimpleHttpServerFactoryBean();		
		httpServerFactory.setContexts(map);
		httpServerFactory.setHostname("127.0.0.1");
		httpServerFactory.setPort(8080);
		return httpServerFactory;
	}
}
