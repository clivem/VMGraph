/**
 * 
 */
package uk.org.vacuumtube.spring;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import uk.org.vacuumtube.dao.StatsDao;
import uk.org.vacuumtube.hibernate.MySqlFixInterceptor;
import uk.org.vacuumtube.hibernate.StatsDaoImpl;
import uk.org.vacuumtube.service.StatsDatabaseService;
import uk.org.vacuumtube.service.StatsDatabaseServiceImpl;

/**
 * @author clivem
 *
 */
@Configuration
public class AppConfiguration {

	@Value("#{dataSource}")
	private DataSource dataSource;

	@Bean(name = "sessionFactory")
	public LocalSessionFactoryBean sessionFactory() {
		Properties props = new Properties();
		// props.put("hibernate.dialect",
		// org.hibernate.dialect.MySQLDialect.class.getName());
		props.put("hibernate.dialect",
				org.hibernate.dialect.MySQL5InnoDBDialect.class.getName());
		props.put("hibernate.show_sql", "true");

		LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
		bean.setPackagesToScan("uk.org.vacuumtube.dao");
		//bean.setMappingResources(new String[] {"META-INF/stats.hbm.xml", "META-INF/notes.hbm.xml"});
		bean.setHibernateProperties(props);
		bean.setDataSource(this.dataSource);
		bean.setEntityInterceptor(new MySqlFixInterceptor());
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
	
	/*
	 * Hibernate: select stats0_.STATSID as STATSID0_1_, stats0_.UPDATED as UPDATED0_1_, stats0_.MILLIS as MILLIS0_1_, 
	 * stats0_.RXBYTES as RXBYTES0_1_, stats0_.TXBYTES as TXBYTES0_1_, stats0_.CREATED as CREATED0_1_, notes1_.STATSID 
	 * as STATSID0_3_, notes1_.NOTESID as NOTESID3_, notes1_.NOTESID as NOTESID1_0_, notes1_.UPDATED as UPDATED1_0_, 
	 * notes1_.CREATED as CREATED1_0_, notes1_.NOTE as NOTE1_0_, notes1_.STATSID as STATSID1_0_ from STATS stats0_ 
	 * left outer join NOTES notes1_ on stats0_.STATSID=notes1_.STATSID where stats0_.STATSID=?
	 */
	
	/*
	 * Hibernate: select stats0_.STATSID as STATSID1_2_, stats0_.CREATED as CREATED1_2_, stats0_.UPDATED as UPDATED1_2_, 
	 * stats0_.MILLIS as MILLIS1_2_, stats0_.RXBYTES as RXBYTES1_2_, stats0_.TXBYTES as TXBYTES1_2_, notes1_.NOTESID 
	 * as NOTESID1_4_, notes1_.NOTESID as NOTESID4_, notes1_.NOTESID as NOTESID0_0_, notes1_.CREATED as CREATED0_0_, 
	 * notes1_.UPDATED as UPDATED0_0_, notes1_.NOTE as NOTE0_0_, notes1_.STATSID as STATSID0_0_, stats2_.STATSID as 
	 * STATSID1_1_, stats2_.CREATED as CREATED1_1_, stats2_.UPDATED as UPDATED1_1_, stats2_.MILLIS as MILLIS1_1_, 
	 * stats2_.RXBYTES as RXBYTES1_1_, stats2_.TXBYTES as TXBYTES1_1_ from STATS stats0_ left outer join NOTES notes1_ 
	 * on stats0_.STATSID=notes1_.NOTESID left outer join STATS stats2_ on notes1_.STATSID=stats2_.STATSID 
	 * where stats0_.STATSID=?
	 */

	@Bean(name = "statsDatabaseService")
	public StatsDatabaseService statsDatabaseService() {
		StatsDatabaseServiceImpl impl = new StatsDatabaseServiceImpl();
		impl.setStatsDao(statsDao());
		return impl;
	}
}
