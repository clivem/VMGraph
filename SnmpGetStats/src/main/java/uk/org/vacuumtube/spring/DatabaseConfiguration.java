/**
 * 
 */
package uk.org.vacuumtube.spring;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import uk.org.vacuumtube.dao.StatsDao;
import uk.org.vacuumtube.dao.hibernate.MySqlTimestampInterceptor;
import uk.org.vacuumtube.dao.hibernate.StatsDaoImpl;
import uk.org.vacuumtube.service.StatsDatabaseService;
import uk.org.vacuumtube.service.StatsDatabaseServiceImpl;

/**
 * @author clivem
 *
 */
@Configuration
@ImportResource("classpath:/META-INF/spring/db-context.xml")
public class DatabaseConfiguration {

	@Value("#{hibernateProperties}")
	private Properties hibernateProperties;

	@Value("#{jdbcProperties}")
	private Properties jdbcProperties;
	
	@Bean(name = "sessionFactory")
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
		bean.setPackagesToScan("uk.org.vacuumtube.dao");
		//bean.setMappingResources(new String[] {"uk/org/vacuumtube/dao/Stats.hbm.xml", "uk/org/vacuumtube/dao/Notes.hbm.xml"});
		bean.setHibernateProperties(hibernateProperties);
		bean.setDataSource(dataSource());
		bean.setEntityInterceptor(new MySqlTimestampInterceptor());
		return bean;
	}

	@Bean(name = "transactionManager")
	public HibernateTransactionManager transactionManager() {
		return new HibernateTransactionManager(sessionFactory().getObject());
	}

	@Bean(name = "hibernateStatsDao")
	public StatsDao statsDao() {
		StatsDaoImpl statsDaoImpl = new StatsDaoImpl();
		statsDaoImpl.setSessionFactory(sessionFactory().getObject());
		return statsDaoImpl;
	}
	
	@Bean(name = "statsDatabaseService")
	public StatsDatabaseService statsDatabaseService() {
		StatsDatabaseServiceImpl impl = new StatsDatabaseServiceImpl();
		impl.setStatsDao(statsDao());
		return impl;
	}

	@Bean(name = "dataSource", destroyMethod = "close")
	public DataSource dataSource() {
		String driverClassName = getProperty("jdbc.driverClassName", jdbcProperties);
		String url = getProperty("jdbc.url", jdbcProperties);
		String username = getProperty("jdbc.username", jdbcProperties);
		String password = getProperty("jdbc.password", jdbcProperties);
		
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driverClassName);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		return ds;
	}
	
	/**
	 * @param name
	 * @param properties
	 * @return the property value of the name
	 */
	private final static String getProperty(final String name, final Properties properties) {
		String value = properties.getProperty(name);
		if (value == null) {
			throw new BeanCreationException(name + " not set!");
		}
		return value;
	}
}
