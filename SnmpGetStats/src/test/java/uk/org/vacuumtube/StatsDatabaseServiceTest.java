/**
 * 
 */
package uk.org.vacuumtube;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import uk.org.vacuumtube.service.StatsDatabaseService;
import uk.org.vacuumtube.spring.ApplicationConfiguration;

/**
 * @author clivem
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
  classes = ApplicationConfiguration.class, 
  loader = AnnotationConfigContextLoader.class)
public class StatsDatabaseServiceTest extends AbstractStatsDatabaseServiceTest {

	//private static final Logger LOGGER = Logger.getLogger(StatsDatabaseServiceTest.class);
	
	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.AbstractStatsDatabaseServiceTest#getStatsDatabaseService()
	 */
	public StatsDatabaseService getStatsDatabaseService() {
		return serviceLocator.getStatsDatabaseService();
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.AbstractStatsDatabaseServiceTest#getAltStatsDatabaseService()
	 */
	@Override
	public StatsDatabaseService getAltStatsDatabaseService() {
		return getStatsDatabaseService();
	}
}
