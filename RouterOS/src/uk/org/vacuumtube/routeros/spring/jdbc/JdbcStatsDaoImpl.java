/**
 * 
 */
package uk.org.vacuumtube.routeros.spring.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import uk.org.vacuumtube.routeros.Stats;
import uk.org.vacuumtube.routeros.spring.dao.StatsDao;

/**
 * @author clivem
 *
 */
public class JdbcStatsDaoImpl implements StatsDao {
	
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insertStats;
	//private StatsMappingQueryAll statsMappingQueryAll;
	//private StatsMappingQueryById statsMappingQueryById;

	/**
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.insertStats = new SimpleJdbcInsert(dataSource)
			.withTableName("stats")
			.usingGeneratedKeyColumns("id");
		//this.statsMappingQueryAll = new StatsMappingQueryAll(dataSource);
		//this.statsMappingQueryById = new StatsMappingQueryById(dataSource);
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.StatsDao#add(uk.org.vacuumtube.routeros.spring.Stats)
	 */
	public Stats add(Stats stats) {
		Map<String, Object> parameters = new HashMap<String, Object>(4);
		parameters.put("millis", stats.getMillis());
		parameters.put("rxbytes", stats.getRxBytes());
		parameters.put("txbytes", stats.getTxBytes());
		parameters.put("created", new Timestamp(stats.getCreated().getTime()));
		Number newId = insertStats.executeAndReturnKey(parameters);
		stats.setId(newId.longValue());
		return stats;
	}
	
	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#delete(uk.org.vacuumtube.routeros.Stats)
	 */
	@Override
	public void delete(Stats stats) {
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.dao.StatsDao#update(uk.org.vacuumtube.routeros.Stats)
	 */
	@Override
	public void update(Stats stats) {
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.StatsDao#getStats(long)
	 */
	public Stats getStats(long id) {
		//return statsMappingQueryById.findObject(id);
		return this.jdbcTemplate.queryForObject("select * from stats where stats.id = ?", 
				new Object[]{id}, new StatsRowMapper());
	}
	
	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.routeros.spring.StatsDao#getCount()
	 */
	public int getCount() {
		return this.jdbcTemplate.queryForInt("select count(*) from stats");
	}
	
	/**
	 * @return
	 */
	public List<Stats> getStatsList() {
		//return statsMappingQueryAll.execute();
		return this.jdbcTemplate.query("select * from stats", new StatsRowMapper());
	}

	/**
	 * @author clivem
	 *
	 */
	private final static class StatsRowMapper implements RowMapper<Stats> {
		/* (non-Javadoc)
		 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
		 */
		@Override
		public Stats mapRow(ResultSet rs, int rowNum) throws SQLException {
			Stats stats = new Stats();
			stats.setId(rs.getLong("id"));
			stats.setMillis(rs.getLong("millis"));
			stats.setRxBytes(rs.getLong("rxbytes"));
			stats.setTxBytes(rs.getLong("txbytes"));
			stats.setCreated(new Date(rs.getTimestamp("created").getTime()));
			return stats;
		}
	}
	
	/**
	 * @param ctx
	 * @return
	 */
	public final static StatsDao getStatsDao(ApplicationContext ctx) {
		return ctx.getBean("jdbcStatsDao", JdbcStatsDaoImpl.class);
	}
}
