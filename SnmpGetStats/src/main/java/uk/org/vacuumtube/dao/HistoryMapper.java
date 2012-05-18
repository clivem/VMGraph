/**
 * 
 */
package uk.org.vacuumtube.dao;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import uk.org.vacuumtube.util.DateFormatFactory;

/**
 * @author clivem
 *
 */
public class HistoryMapper {
	
	private final static Logger LOGGER = Logger.getLogger(HistoryMapper.class);
	
	private static Map<String, HeaderMapping> headerMap = new HashMap<String, HeaderMapping>();
	
	private Map<Integer, HeaderMapping> mappingMap = new HashMap<Integer, HeaderMapping>();
	
	static {
		put(new SportsIdMapping("SPORTS_ID"));
		put(new EventIdMapping("EVENT_ID"));
		put(new SettledDateMapping("SETTLED_DATE"));
		put(new FullDescriptionMapping("FULL_DESCRIPTION"));
		put(new ScheduledOffMapping("SCHEDULED_OFF"));
		put(new EventMapping("EVENT"));
		put(new ActualOffMapping("DT ACTUAL_OFF"));
		put(new SelectionIdMapping("SELECTION_ID"));
		put(new SelectionMapping("SELECTION"));
		put(new OddsMapping("ODDS"));
		put(new NumberBetsMapping("NUMBER_BETS"));
		put(new VolumeMatchedMapping("VOLUME_MATCHED"));
		put(new LatestTakenMapping("LATEST_TAKEN"));
		put(new FirstTakenMapping("FIRST_TAKEN"));
		put(new WinFlagMapping("WIN_FLAG"));
		put(new InPlayMapping("IN_PLAY"));
	}
	
	/**
	 * @param mapping
	 */
	private static final void put(HeaderMapping mapping) {
		headerMap.put(mapping.getCsvHeaderName(), mapping);
	}
	
	/**
	 * 
	 */
	public HistoryMapper() {
		super();
	}
	
	/**
	 * @param line
	 * @throws Exception
	 */
	public void parseHeader(String line) throws Exception {
		StringTokenizer tok = new StringTokenizer(line, ",");
		int size = tok.countTokens();
		if (size != 16) {
			throw new Exception("Expecting 16 columns!");
		}

		int count = 0;
		while (tok.hasMoreTokens()) {
			String next = tok.nextToken();
			if (next.startsWith("\"") && next.endsWith("\"")) {
				next = next.substring(1, next.length() - 1);
			}
			
			HeaderMapping mapping = headerMap.get(next);
			if (mapping == null) {
				throw new Exception("No mapper for column: " + next);
			}
			mappingMap.put(count, mapping);
			count++;
		}
	}
	
	/**
	 * @param line
	 * @return
	 * @throws ParseException
	 */
	public History parse(String line) throws Exception {

		StringTokenizer tok = new StringTokenizer(line, ",");
		int size = tok.countTokens();
		if (size != 16) {
			throw new Exception("Expecting 16 columns!");
		}
		
		History history = new History();
		int count = 0;
		while (tok.hasMoreTokens()) {
			String next = tok.nextToken();
			if (next.startsWith("\"") && next.endsWith("\"")) {
				next = next.substring(1, next.length() - 1);
			}
			
			mappingMap.get(count).process(next, history);			
			
			count++;
			//System.out.println(next);
		}
		
		return history;
	}
	
	/**
	 * @param value
	 * @return
	 * @throws ParseException
	 */
	private static final Date parseArchiveDate(String column, String value) throws ParseException {
		if (value.length() == 0) {
			if (!"settledDate".equals(column) && !"dtActualOffDate".equals(column)) {
				LOGGER.warn("Column: " + column + ", Value: " + value);
			}
			return null;
		} else if (value.length() == 16) {
			return DateFormatFactory.parse(DateFormatFactory.BETFAIR_DATE_TIME_WOUT_SECS, value);
		} else {
			return DateFormatFactory.parse(DateFormatFactory.BETFAIR_DATE_TIME, value);
		}
	}
	
	private static abstract class HeaderMapping {
		
		protected String csvHeaderName;
		
		/**
		 * @param csvHeaderName
		 */
		public HeaderMapping(String csvHeaderName) {
			this.csvHeaderName = csvHeaderName;
		}

		/**
		 * @return the csvHeaderName
		 */
		public String getCsvHeaderName() {
			return csvHeaderName;
		}

		/**
		 * @param value
		 * @param history
		 * @throws ParseException
		 */
		public abstract void process(String value, History history) throws ParseException;
	}
	
	private static class SportsIdMapping extends HeaderMapping {

		/**
		 * @param csvHeaderName
		 */
		public SportsIdMapping(String csvHeaderName) {
			super(csvHeaderName);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String value, History history)
				throws ParseException {
			history.setSportsId(Integer.parseInt(value));
		}
	}

	private static class EventIdMapping extends HeaderMapping {

		/**
		 * @param csvHeaderName
		 */
		public EventIdMapping(String csvHeaderName) {
			super(csvHeaderName);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String value, History history)
				throws ParseException {
			history.setEventId(Long.valueOf(value).longValue());
		}
	}

	private static class SettledDateMapping extends HeaderMapping {

		/**
		 * @param csvHeaderName
		 */
		public SettledDateMapping(String csvHeaderName) {
			super(csvHeaderName);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String value, History history)
				throws ParseException {
			history.setSettledDate(parseArchiveDate("settledDate", value));
		}
	}

	private static class FullDescriptionMapping extends HeaderMapping {

		/**
		 * @param csvHeaderName
		 */
		public FullDescriptionMapping(String csvHeaderName) {
			super(csvHeaderName);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String value, History history)
				throws ParseException {
			history.setFullDescription(value);
		}
	}

	private static class ScheduledOffMapping extends HeaderMapping {

		/**
		 * @param csvHeaderName
		 */
		public ScheduledOffMapping(String csvHeaderName) {
			super(csvHeaderName);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String value, History history)
				throws ParseException {
			history.setScheduledOffDate(parseArchiveDate("scheduledOffDate", value));
		}
	}

	private static class EventMapping extends HeaderMapping {

		/**
		 * @param csvHeaderName
		 */
		public EventMapping(String csvHeaderName) {
			super(csvHeaderName);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String value, History history)
				throws ParseException {
			history.setEvent(value);
		}
	}

	private static class ActualOffMapping extends HeaderMapping {

		/**
		 * @param csvHeaderName
		 */
		public ActualOffMapping(String csvHeaderName) {
			super(csvHeaderName);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String value, History history)
				throws ParseException {
			history.setActualOffDate(parseArchiveDate("actualOffDate", value));
		}
	}

	private static class SelectionIdMapping extends HeaderMapping {

		/**
		 * @param csvHeaderName
		 */
		public SelectionIdMapping(String csvHeaderName) {
			super(csvHeaderName);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String value, History history)
				throws ParseException {
			history.setSelectionId(Long.valueOf(value).longValue());
		}
	}

	private static class SelectionMapping extends HeaderMapping {

		/**
		 * @param csvHeaderName
		 */
		public SelectionMapping(String csvHeaderName) {
			super(csvHeaderName);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String value, History history)
				throws ParseException {
			history.setSelection(value);
		}
	}

	private static class OddsMapping extends HeaderMapping {

		/**
		 * @param csvHeaderName
		 */
		public OddsMapping(String csvHeaderName) {
			super(csvHeaderName);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String value, History history)
				throws ParseException {
			history.setOdds(Double.valueOf(value).doubleValue());
		}
	}

	private static class NumberBetsMapping extends HeaderMapping {

		/**
		 * @param csvHeaderName
		 */
		public NumberBetsMapping(String csvHeaderName) {
			super(csvHeaderName);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String value, History history)
				throws ParseException {
			history.setNumberBets(Long.valueOf(value).longValue());
		}
	}

	private static class VolumeMatchedMapping extends HeaderMapping {

		/**
		 * @param csvHeaderName
		 */
		public VolumeMatchedMapping(String csvHeaderName) {
			super(csvHeaderName);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String value, History history)
				throws ParseException {
			history.setVolumeMatched(Double.valueOf(value).doubleValue());
		}
	}

	private static class LatestTakenMapping extends HeaderMapping {

		/**
		 * @param csvHeaderName
		 */
		public LatestTakenMapping(String csvHeaderName) {
			super(csvHeaderName);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String value, History history)
				throws ParseException {
			history.setLatestTaken(parseArchiveDate("latestTaken", value));
		}
	}

	private static class FirstTakenMapping extends HeaderMapping {

		/**
		 * @param csvHeaderName
		 */
		public FirstTakenMapping(String csvHeaderName) {
			super(csvHeaderName);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String value, History history)
				throws ParseException {
			history.setFirstTaken(parseArchiveDate("firstTaken", value));
		}
	}

	private static class WinFlagMapping extends HeaderMapping {

		/**
		 * @param csvHeaderName
		 */
		public WinFlagMapping(String csvHeaderName) {
			super(csvHeaderName);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String value, History history)
				throws ParseException {
			history.setWinFlag(Integer.parseInt(value));
		}
	}

	private static class InPlayMapping extends HeaderMapping {

		/**
		 * @param csvHeaderName
		 */
		public InPlayMapping(String csvHeaderName) {
			super(csvHeaderName);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String value, History history)
				throws ParseException {
			history.setInPlay(value);
		}
	}
}
