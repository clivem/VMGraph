/**
 * 
 */
package uk.org.vacuumtube.spring;

import java.util.Properties;

import org.quartz.JobDataMap;
import org.quartz.Trigger;
import org.quartz.impl.JobDetailImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import uk.org.vacuumtube.schedule.GetSnmpInterfaceStatisticsQuartzJob;
import uk.org.vacuumtube.service.ServiceLocator;

/**
 * @author clivem
 *
 */
@Configuration
@ImportResource("classpath:/META-INF/spring/schedule-context.xml")
@Import(ApplicationConfiguration.class)
public class ScheduleConfiguration {

	@Autowired
	private ApplicationConfiguration appConfiguration;
	
	@Value("#{snmpProperties}")
	private Properties snmpProperties;
	
	@Bean(name = "snmpStatsJobDetail")
	@DependsOn(value = ServiceLocator.STATS_SERVICE_BEAN_NAME)
	public JobDetailImpl snmpStatsJobDetail() {
		/*
		 * Put the snmp.properties, reference to the statsDatabaseService and
		 * starting defaults for the prev params into the jobDataMap.
		 */
		JobDataMap map = new JobDataMap(snmpProperties);
		map.put("statsDatabaseService", appConfiguration.statsDatabaseService());
		map.put("prevTimestamp", new Long(-1));
		map.put("prevRxBytes", new Long(-1));
		map.put("prevTxBytes", new Long(-1));
		// Override the RRD filename from the snmpProperties if set as a system property
		String rrdDbFilename = System.getProperty(GetSnmpInterfaceStatisticsQuartzJob.SNMP_RRDDB_FILENAME);
		if (rrdDbFilename != null) {
			map.put(GetSnmpInterfaceStatisticsQuartzJob.SNMP_RRDDB_FILENAME, rrdDbFilename);
		}

		JobDetailImpl jobDetailImpl = new JobDetailImpl();
		jobDetailImpl.setName("GetSnmpInterfaceStatisticsQuartzJob");
		jobDetailImpl.setJobClass(GetSnmpInterfaceStatisticsQuartzJob.class);
		jobDetailImpl.setJobDataMap(map);
		//jobDetailImpl.getJobDataMap().putAll(map);
		return jobDetailImpl;
	}
	
	/*
	@Bean(name = "snmpStatsJobCronTriggerFactory")
	public CronTriggerFactoryBean snmpStatsJobCronTriggerFactory() {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(snmpStatsJobDetail());
		cronTriggerFactoryBean.setCronExpression("0 0/1 * * * ?");
		return cronTriggerFactoryBean;
	}
	*/

	@Bean(name = "snmpStatsJobSimpleTriggerFactory")
	public SimpleTriggerFactoryBean snmpStatsJobSimpleTriggerFactory() {
		SimpleTriggerFactoryBean simpleTriggerFactoryBean = new SimpleTriggerFactoryBean();
		simpleTriggerFactoryBean.setJobDetail(snmpStatsJobDetail());
		// Start on minute boundary
		simpleTriggerFactoryBean.setStartDelay(60000 - (System.currentTimeMillis() % 60000));
		simpleTriggerFactoryBean.setRepeatInterval(60000);
		return simpleTriggerFactoryBean;
	}
	
	@Bean(name = "quartzScheduler")
	public SchedulerFactoryBean quartzScheduler() {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		
		// Use a SpringBeanJobFactory for dependency injection
		schedulerFactoryBean.setJobFactory(new SpringBeanJobFactory());

		//schedulerFactoryBean.setTriggers(new Trigger[] {snmpStatsJobCronTriggerFactory().getObject()});
		schedulerFactoryBean.setTriggers(new Trigger[] {snmpStatsJobSimpleTriggerFactory().getObject()});
		
		return schedulerFactoryBean;
	}

	/*
	@Bean(name = "testJob") 
	public TestJob testJob() {
		TestJob job = new TestJob();
		return job;
	}
	*/
}
