/**
 * 
 */
package uk.org.vacuumtube.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.remoting.rmi.RmiServiceExporter;

import uk.org.vacuumtube.service.ServiceLocator;
import uk.org.vacuumtube.service.StatsDatabaseService;

/**
 * @author clivem
 *
 */
@Configuration
//@Import(DatabaseConfiguration.class)
@Import(ApplicationConfiguration.class)
public class RemoteServerConfiguration {

	@Autowired
	private ApplicationConfiguration applicationConfiguration;
	/*
	@Autowired
	private DatabaseConfiguration databaseConfiguration;
	
	@Bean(name = "remoteStatsDatabaseService")
	public RemoteStatsDatabaseService remoteStatsDatabaseService() {
		RemoteStatsDatabaseServiceImpl remoteStatsImpl = 
				new RemoteStatsDatabaseServiceImpl();
		remoteStatsImpl.setStatsDao(databaseConfiguration.statsDao());
		return remoteStatsImpl;
	}

	@Bean(name = "statsRmiServiceExporter")
	public RmiServiceExporter statsRmiServiceExporter() {
		RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
		rmiServiceExporter.setServiceName(ServiceLocator.STATS_SERVICE_RMI_SERVICE_NAME);
		rmiServiceExporter.setService(remoteStatsDatabaseService());
		rmiServiceExporter.setServiceInterface(RemoteStatsDatabaseService.class);
		rmiServiceExporter.setRegistryPort(ServiceLocator.RMI_REGISTRY_PORT);
		return rmiServiceExporter;
	}	
	*/
	@Bean
	public RmiServiceExporter statsRmiServiceExporter() {
		RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
		rmiServiceExporter.setServiceName(ServiceLocator.STATS_SERVICE_RMI_SERVICE_NAME);
		rmiServiceExporter.setService(applicationConfiguration.statsDatabaseService());
		rmiServiceExporter.setServiceInterface(StatsDatabaseService.class);
		rmiServiceExporter.setServicePort(ServiceLocator.STATS_SERVICE_RMI_SERVICE_PORT);
		rmiServiceExporter.setRegistryPort(ServiceLocator.RMI_REGISTRY_PORT);
		return rmiServiceExporter;
	}	
}
