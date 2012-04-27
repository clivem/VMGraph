/**
 * 
 */
package uk.org.vacuumtube.routeros.spring.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.jdbc.object.MappingSqlQuery;

import uk.org.vacuumtube.routeros.Stats;

/**
 * @author clivem
 *
 */
public abstract class StatsMappingQuery extends MappingSqlQuery<Stats>  {

	/**
	 * @param dataSource
	 * @param sql
	 */
	public StatsMappingQuery(DataSource dataSource, String sql) {
		super(dataSource, sql);
	}

	/* (non-Javadoc)
	 * @see org.springframework.jdbc.object.MappingSqlQuery#mapRow(java.sql.ResultSet, int)
	 */
	@Override
	protected Stats mapRow(ResultSet rs, int rowNumber) throws SQLException {
		Stats stats = new Stats();
		stats.setId(rs.getLong("id"));
		stats.setMillis(rs.getLong("millis"));
		stats.setRxBytes(rs.getLong("rxbytes"));
		stats.setTxBytes(rs.getLong("txbytes"));
		stats.setCreated(new Date(rs.getTimestamp("created").getTime()));
		return stats;
	}
}
