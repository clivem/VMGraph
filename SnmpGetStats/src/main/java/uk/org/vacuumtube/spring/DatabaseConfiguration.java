/**
 * 
 */
package uk.org.vacuumtube.spring;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import uk.org.vacuumtube.dao.StatsDao;
import uk.org.vacuumtube.dao.hibernate.MySqlTimestampInterceptor;
import uk.org.vacuumtube.dao.hibernate.StatsDaoImpl;

/**
 * @author clivem
 *
 */
@Configuration
@ImportResource("classpath:/META-INF/spring/db-context.xml")
public class DatabaseConfiguration {

	@Value("#{hibernateProperties}")
	private Properties hibernateProperties;

	@Value("${jdbc.driverClassName}")
	private String jdbcDriverClassName;

	@Value("${jdbc.url}")
	private String jdbcUrl;
 
	@Value("${jdbc.username}")
	private String jdbcUsername;
 
	@Value("${jdbc.password}")
	private String jdbcPassword;

	@Bean(name = "sessionFactory")
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
		bean.setPackagesToScan("uk.org.vacuumtube.dao");
		/*
		bean.setMappingResources(new String[] {
			"uk/org/vacuumtube/dao/Stats.hbm.xml", 
			"uk/org/vacuumtube/dao/Notes.hbm.xml"});
		*/
		bean.setHibernateProperties(hibernateProperties);
		bean.setDataSource(dataSource());
		bean.setEntityInterceptor(new MySqlTimestampInterceptor());
		return bean;
	}
	
	@Bean(name = "persistenceExceptionTranslationPostProcessor")
	public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean(name = "transactionManager")
	public HibernateTransactionManager transactionManager() {
		return new HibernateTransactionManager(sessionFactory().getObject());
	}

	@Bean(name = "hibernateStatsDao")
	public StatsDao statsDao() {
		StatsDaoImpl statsDaoImpl = new StatsDaoImpl();
		//statsDaoImpl.setSessionFactory(sessionFactory().getObject());
		return statsDaoImpl;
	}
	
	@Bean(name = "dataSource", destroyMethod = "close")
	public DataSource dataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(jdbcDriverClassName);
		ds.setUrl(jdbcUrl);
		ds.setUsername(jdbcUsername);
		ds.setPassword(jdbcPassword);
		return ds;
	}	
}
