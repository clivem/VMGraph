/**
 * 
 */
package uk.org.vacuumtube.spring;

import java.util.Properties;

import org.quartz.JobDataMap;
import org.quartz.Trigger;
import org.quartz.impl.JobDetailImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import uk.org.vacuumtube.schedule.GetSnmpInterfaceStatisticsJob;

/**
 * @author clivem
 *
 */
@Configuration
@ImportResource("classpath:/META-INF/spring/schedule-context.xml")
public class ScheduleConfiguration {

	@Value("#{snmpProperties}")
	private Properties snmpProperties;

	@Bean(name = "snmpStatsJobDetail")
	public JobDetailImpl snmpStatsJobDetail() {
		JobDetailImpl jobDetailImpl = new JobDetailImpl();
		jobDetailImpl.setName("GetSnmpInterfaceStatisticsJob");
		jobDetailImpl.setJobClass(GetSnmpInterfaceStatisticsJob.class);
		jobDetailImpl.setJobDataMap(new JobDataMap(snmpProperties));
		return jobDetailImpl;
	}
	
	@Bean(name = "snmpStatsJobCronTrigger")
	public CronTriggerFactoryBean snmpStatsJobCronTrigger() {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(snmpStatsJobDetail());
		cronTriggerFactoryBean.setCronExpression("0 0/1 * * * ?");
		return cronTriggerFactoryBean;
	}

	/*
	@Bean(name = "snmpStatsJobSimpleTrigger")
	public SimpleTriggerFactoryBean snmpStatsJobSimpleTrigger() {
		SimpleTriggerFactoryBean simpleTriggerFactoryBean = new SimpleTriggerFactoryBean();
		simpleTriggerFactoryBean.setJobDetail(snmpStatsJobDetail());
		simpleTriggerFactoryBean.setStartDelay(10000L);
		simpleTriggerFactoryBean.setRepeatInterval(60000L);
		return simpleTriggerFactoryBean;
	}
	*/
	
	@Bean(name = "scheduler")
	public SchedulerFactoryBean scheduler() {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		schedulerFactoryBean.setTriggers(new Trigger[] {snmpStatsJobCronTrigger().getObject()});
		//schedulerFactoryBean.setTriggers(new Trigger[] {snmpStatsJobSimpleTrigger().getObject()});
		return schedulerFactoryBean;
	}
}
