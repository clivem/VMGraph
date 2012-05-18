/**
 * 
 */
package uk.org.vacuumtube.dao;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
		put(new SportsIdMapping());
		put(new EventIdMapping());
		put(new SettledDateMapping());
		put(new FullDescriptionMapping());
		put(new ScheduledOffMapping());
		put(new EventMapping());
		put(new ActualOffMapping());
		put(new SelectionIdMapping());
		put(new SelectionMapping());
		put(new OddsMapping());
		put(new NumberBetsMapping());
		put(new VolumeMatchedMapping());
		put(new LatestTakenMapping());
		put(new FirstTakenMapping());
		put(new WinFlagMapping());
		put(new InPlayMapping());
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
		
		String[] tokens = parse(line);
		int count = 0;
		for (String token : tokens) {
			String next = strip(token);
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
	public History parseRecord(String line) throws Exception {

		String[] tokens = parse(line);
		History history = new History();
		int count = 0;
		for (String token : tokens) {
			String next = strip(token);
			mappingMap.get(count).process(next, history);			
			count++;
		}
		
		return history;
	}
	
	/**
	 * @param line
	 * @return
	 * @throws Exception
	 */
	private String[] parse(String line) throws Exception {
		
		String[] tokens = line.split(",\"");
		int size = tokens.length;
		if (size != headerMap.size()) {
			int count = 0;
			for (String token : tokens) {
				LOGGER.warn(count++ + ": [" + strip(token) + "]");
			}
			throw new Exception("Expecting " + headerMap.size() + " columns. Received " + size + "!");
		}
		return tokens;
	}
	
	/**
	 * @param value
	 * @return
	 */
	private String strip(String value) {
		if (value.startsWith("\"")) {
			value = value.substring(1);
		}
		if (value.endsWith("\"")) {
			value = value.substring(0, value.length() - 1);
		}
		return value;
	}
	
	/**
	 * @param value
	 * @return
	 * @throws ParseException
	 */
	private static final Date parseArchiveDate(String column, String value) throws ParseException {
		if (value.length() == 0) {
			if (!"settledDate".equals(column) && !"actualOffDate".equals(column)) {
				LOGGER.warn("Column: " + column + ", Value: " + value);
			}
			return null;
		} else if (value.length() == DateFormatFactory.BETFAIR_DATE_TIME_WOUT_SECS.length()) {
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

		public final static String ID = "SPORTS_ID";
		
		/**
		 * @param csvHeaderName
		 */
		public SportsIdMapping() {
			super(ID);
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

		public final static String ID = "EVENT_ID";

		/**
		 * @param csvHeaderName
		 */
		public EventIdMapping() {
			super(ID);
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

		public final static String ID = "SETTLED_DATE";

		/**
		 * @param csvHeaderName
		 */
		public SettledDateMapping() {
			super(ID);
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

		public final static String ID = "FULL_DESCRIPTION";

		/**
		 * @param csvHeaderName
		 */
		public FullDescriptionMapping() {
			super(ID);
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

		public final static String ID = "SCHEDULED_OFF";

		/**
		 * @param csvHeaderName
		 */
		public ScheduledOffMapping() {
			super(ID);
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

		public final static String ID = "EVENT";

		/**
		 * @param csvHeaderName
		 */
		public EventMapping() {
			super(ID);
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

		public final static String ID = "DT ACTUAL_OFF";

		/**
		 * @param csvHeaderName
		 */
		public ActualOffMapping() {
			super(ID);
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

		public final static String ID = "SELECTION_ID";

		/**
		 * @param csvHeaderName
		 */
		public SelectionIdMapping() {
			super(ID);
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

		public final static String ID = "SELECTION";

		/**
		 * @param csvHeaderName
		 */
		public SelectionMapping() {
			super(ID);
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

		public final static String ID = "ODDS";

		/**
		 * @param csvHeaderName
		 */
		public OddsMapping() {
			super(ID);
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

		public final static String ID = "NUMBER_BETS";

		/**
		 * @param csvHeaderName
		 */
		public NumberBetsMapping() {
			super(ID);
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

		public final static String ID = "VOLUME_MATCHED";

		/**
		 * @param csvHeaderName
		 */
		public VolumeMatchedMapping() {
			super(ID);
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

		public final static String ID = "LATEST_TAKEN";

		/**
		 * @param csvHeaderName
		 */
		public LatestTakenMapping() {
			super(ID);
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

		public final static String ID = "FIRST_TAKEN";

		/**
		 * @param csvHeaderName
		 */
		public FirstTakenMapping() {
			super(ID);
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

		public final static String ID = "WIN_FLAG";

		/**
		 * @param csvHeaderName
		 */
		public WinFlagMapping() {
			super(ID);
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

		public final static String ID = "IN_PLAY";

		/**
		 * @param csvHeaderName
		 */
		public InPlayMapping() {
			super(ID);
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
