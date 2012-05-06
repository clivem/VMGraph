package uk.org.vacuumtube.hibernate;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

@Configuration
public class HibernateConfiguration {

	@Value("#{dataSource}")
	private DataSource dataSource;

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		Properties props = new Properties();
		//props.put("hibernate.dialect", org.hibernate.dialect.MySQLDialect.class.getName());
		props.put("hibernate.dialect", org.hibernate.dialect.MySQL5InnoDBDialect.class.getName());
		props.put("hibernate.show_sql", "false");

		LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
		bean.setPackagesToScan("uk.org.vacuumtube.dao");
		bean.setHibernateProperties(props);
		bean.setDataSource(this.dataSource);
		bean.setEntityInterceptor(new MySqlFixInterceptor());
		return bean;
	}

	@Bean
	public HibernateTransactionManager transactionManager() {
		return new HibernateTransactionManager(sessionFactory().getObject());
	}
}
