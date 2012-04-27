/**
 * 
 */
package uk.org.vacuumtube.routeros;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import uk.org.vacuumtube.routeros.spring.dao.StatsDao;
import uk.org.vacuumtube.routeros.spring.jdbc.JdbcStatsDaoImpl;
import uk.org.vacuumtube.routeros.spring.service.RouterStatsService;

/**
 * @author clivem
 *
 */
public class RouterStatsTest {

	private final static Logger LOGGER = Logger.getLogger(RouterStatsTest.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx  = new ClassPathXmlApplicationContext("classpath:db.xml");
		ctx.registerShutdownHook();
		
		//RouterStatsService service = ctx.getBean("routerStatsService", RouterStatsService.class);
		
		StatsDao service = JdbcStatsDaoImpl.getStatsDao(ctx);

		int count = service.getCount();
		LOGGER.info("Number of records in stats table: " + count);
				
		Stats stats = service.getStats(1L);
		LOGGER.info(stats);
		
		List<Stats> statsList = service.getStatsList();
		for (Stats s : statsList) {
			LOGGER.info(s);
		}
		
		stats = service.add(new Stats(System.currentTimeMillis(), 1L, 1L));		
		LOGGER.info(stats);

		stats.setRxBytes(100L);
		service.update(stats);		
		LOGGER.info(stats);
		
		service.delete(stats);		
		LOGGER.info(stats);

		/*
		StatsDao dao = JdbcStatsDaoImpl.getStatsDao(ctx);
		
		int count = dao.getCount();
		LOGGER.info("Number of records in stats table: " + count);
				
		Stats stats = dao.getStats(1);
		LOGGER.info(stats);
		
		stats = dao.add(new Stats(System.currentTimeMillis(), 1L, 1L));		
		LOGGER.info(stats);
		
		List<Stats> statsList = dao.getStatsList();
		for (Stats s : statsList) {
			LOGGER.info(s);
		}
		*/
	}
}
