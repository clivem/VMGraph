/**
 * 
 */
package uk.org.vacuumtube.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import uk.org.vacuumtube.service.ServiceLocator;
import uk.org.vacuumtube.service.StatsDatabaseService;
import uk.org.vacuumtube.service.StatsDatabaseServiceImpl;

/**
 * @author clivem
 *
 */
@Configuration
@ImportResource("classpath:/META-INF/spring/app-context.xml")
@Import(DatabaseConfiguration.class)
public class ApplicationConfiguration {

	@Autowired
	private DatabaseConfiguration databaseConfiguration;
	
	@Bean(name = ServiceLocator.STATS_SERVICE_BEAN_NAME)
	public StatsDatabaseService statsDatabaseService() {
		StatsDatabaseServiceImpl statsImpl = new StatsDatabaseServiceImpl();
		statsImpl.setStatsDao(databaseConfiguration.statsDao());
		return statsImpl;
	}
	
	/*
	@Bean
	public ServiceLocator serviceLocator() {
		ServiceLocator locator = new ServiceLocator();
		return locator;
	}
	*/
}
