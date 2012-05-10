/**
 * 
 */
package uk.org.vacuumtube.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.remoting.rmi.RmiServiceExporter;

import uk.org.vacuumtube.service.RemoteStatsDatabaseService;
import uk.org.vacuumtube.service.RemoteStatsDatabaseServiceImpl;

/**
 * @author clivem
 *
 */
@Configuration
@Import(DatabaseConfiguration.class)
public class RemoteServerConfiguration {

	@Autowired
	private DatabaseConfiguration databaseConfiguration;
	
	@Bean(name = "remoteStatsDatabaseService")
	public RemoteStatsDatabaseService remoteStatsDatabaseService() {
		RemoteStatsDatabaseServiceImpl remoteStatsDatabaseServiceImpl = new RemoteStatsDatabaseServiceImpl();
		remoteStatsDatabaseServiceImpl.setStatsDao(databaseConfiguration.statsDao());
		return remoteStatsDatabaseServiceImpl;
	}
	
	@Bean(name = "statsRmiServiceExporter")
	public RmiServiceExporter statsRmiServiceExporter() {
		RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
		rmiServiceExporter.setServiceName("StatsDatabaseService");
		rmiServiceExporter.setService(remoteStatsDatabaseService());
		rmiServiceExporter.setServiceInterface(RemoteStatsDatabaseService.class);
		rmiServiceExporter.setRegistryPort(1199);
		return rmiServiceExporter;
	}	
}
