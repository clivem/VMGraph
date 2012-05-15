/**
 * 
 */
package uk.org.vacuumtube;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import uk.org.vacuumtube.service.StatsDatabaseService;
import uk.org.vacuumtube.spring.RemoteClientConfiguration;

/**
 * @author clivem
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
  classes = RemoteClientConfiguration.class, 
  loader = AnnotationConfigContextLoader.class)
public class RemoteStatsDatabaseServiceTest extends AbstractStatsDatabaseServiceTest {

	public RemoteStatsDatabaseServiceTest() {
	}
	
	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.AbstractStatsDatabaseServiceTest#getStatsDatabaseService()
	 */
	@Override
	public StatsDatabaseService getStatsDatabaseService() {
		return serviceLocator.getRemoteStatsDatabaseService();
	}
}