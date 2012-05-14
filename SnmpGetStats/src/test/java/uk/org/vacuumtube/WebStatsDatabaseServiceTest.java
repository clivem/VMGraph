/**
 * 
 */
package uk.org.vacuumtube;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import uk.org.vacuumtube.service.StatsDatabaseService;
import uk.org.vacuumtube.spring.WebClientConfiguration;

/**
 * @author clivem
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
  classes = WebClientConfiguration.class, 
  loader = AnnotationConfigContextLoader.class)
public class WebStatsDatabaseServiceTest extends AbstractStatsDatabaseServiceTest {

	//private final static Logger LOGGER = Logger.getLogger(WebStatsDatabaseServiceTest.class);
	
	public WebStatsDatabaseServiceTest() {
	}
	
	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.AbstractStatsDatabaseServiceTest#getStatsDatabaseService()
	 */
	public StatsDatabaseService getStatsDatabaseService() {
		//return serviceLocator.getSimpleStatsDatabaseService();
		//return serviceLocator.getCustomStatsDatabaseService();
		return serviceLocator.getJBossStatsDatabaseService();
	}
}
