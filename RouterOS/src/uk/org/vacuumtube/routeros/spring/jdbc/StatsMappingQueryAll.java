/**
 * 
 */
package uk.org.vacuumtube.routeros.spring.jdbc;

import javax.sql.DataSource;

/**
 * @author clivem
 *
 */
public class StatsMappingQueryAll extends StatsMappingQuery {

	/**
	 * @param dataSource
	 */
	public StatsMappingQueryAll(DataSource dataSource) {
		super(dataSource, "select id, millis, rxbytes, txbytes, created from stats");
		compile();
	}
}
