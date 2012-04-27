/**
 * 
 */
package uk.org.vacuumtube.routeros.spring.jdbc;

import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;

/**
 * @author clivem
 *
 */
public class StatsMappingQueryById extends StatsMappingQuery {
	
	/**
	 * @param ds
	 */
	public StatsMappingQueryById(DataSource ds) {
		super(ds, "select id, millis, rxbytes, txbytes, created from stats where id = ?");
		declareParameter(new SqlParameter("id", Types.NUMERIC));
		compile();
	}
}
