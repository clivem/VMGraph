/**
 * 
 */
package uk.org.vacuumtube.service;

import uk.org.vacuumtube.dao.StatsDao;
import uk.org.vacuumtube.serialization.test.TestObject;

/**
 * @author clivem
 *
 */
public interface StatsDatabaseService extends StatsDao {
	
	public TestObject getTestObject();
	
	public TestObject[] getTestObjectList(int size);
}
