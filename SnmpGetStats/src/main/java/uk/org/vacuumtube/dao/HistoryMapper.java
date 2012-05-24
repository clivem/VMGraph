/**
 * 
 */
package uk.org.vacuumtube.dao;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import uk.org.vacuumtube.dao.hibernate.InPlay;
import uk.org.vacuumtube.util.DateFormatFactory;

/**
 * @author clivem
 *
 */
public class HistoryMapper {
	
	private final static Logger LOGGER = Logger.getLogger(HistoryMapper.class);
	
	private static Map<String, HeaderMapping> headerMap = new HashMap<String, HeaderMapping>();
	
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
	
	private Map<Integer, HeaderMapping> mappingMap = new HashMap<Integer, HeaderMapping>();

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
			mappingMap.get(count).process(line, next, history);			
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
			if (!SettledDateMapping.ID.equals(column) && !ActualOffMapping.ID.equals(column)) {
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
		 * @throws NumberFormatException
		 */
		public abstract void process(String line, String value, History history) throws ParseException, NumberFormatException;
	}
	
	private final static class SportsIdMapping extends HeaderMapping {

		public final static String ID = "SPORTS_ID";
		
		/**
		 * @param csvHeaderName
		 */
		public SportsIdMapping() {
			super(ID);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String line, String value, History history)
				throws ParseException, NumberFormatException {
			//history.setSportsId(Integer.parseInt(value));
			long id = Integer.parseInt(value);
			history.setSport(new Sport(id, "UNKNOWN", false));
		}
	}

	private final static class EventIdMapping extends HeaderMapping {

		public final static String ID = "EVENT_ID";

		/**
		 * 
		 */
		public EventIdMapping() {
			super(ID);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String line, String value, History history)
				throws ParseException, NumberFormatException {
			history.setEventId(Long.valueOf(value).longValue());
		}
	}

	private final static class SettledDateMapping extends HeaderMapping {

		public final static String ID = "SETTLED_DATE";

		/**
		 * 
		 */
		public SettledDateMapping() {
			super(ID);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String line, String value, History history)
				throws ParseException, NumberFormatException {
			history.setSettledDate(parseArchiveDate(csvHeaderName, value));
		}
	}

	private final static class FullDescriptionMapping extends HeaderMapping {

		public final static String ID = "FULL_DESCRIPTION";

		/**
		 * 
		 */
		public FullDescriptionMapping() {
			super(ID);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String line, String value, History history)
				throws ParseException, NumberFormatException {
			history.setFullDescription(value);
		}
	}

	private final static class ScheduledOffMapping extends HeaderMapping {

		public final static String ID = "SCHEDULED_OFF";

		/**
		 * 
		 */
		public ScheduledOffMapping() {
			super(ID);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String line, String value, History history)
				throws ParseException, NumberFormatException {
			history.setScheduledOffDate(parseArchiveDate(csvHeaderName, value));
		}
	}

	private final static class EventMapping extends HeaderMapping {

		public final static String ID = "EVENT";

		/**
		 * 
		 */
		public EventMapping() {
			super(ID);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String line, String value, History history)
				throws ParseException, NumberFormatException {
			history.setEvent(value);
		}
	}

	private final static class ActualOffMapping extends HeaderMapping {

		public final static String ID = "DT ACTUAL_OFF";

		/**
		 * 
		 */
		public ActualOffMapping() {
			super(ID);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String line, String value, History history)
				throws ParseException, NumberFormatException {
			history.setActualOffDate(parseArchiveDate(csvHeaderName, value));
		}
	}

	private final static class SelectionIdMapping extends HeaderMapping {

		public final static String ID = "SELECTION_ID";

		/**
		 * 
		 */
		public SelectionIdMapping() {
			super(ID);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String line, String value, History history)
				throws ParseException, NumberFormatException {
			history.setSelectionId(Long.valueOf(value).longValue());
		}
	}

	private final static class SelectionMapping extends HeaderMapping {

		public final static String ID = "SELECTION";

		/**
		 * 
		 */
		public SelectionMapping() {
			super(ID);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String line, String value, History history)
				throws ParseException, NumberFormatException {
			history.setSelection(value);
		}
	}

	private final static class OddsMapping extends HeaderMapping {

		public final static String ID = "ODDS";

		/**
		 * 
		 */
		public OddsMapping() {
			super(ID);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String line, String value, History history)
				throws ParseException, NumberFormatException {
			history.setOdds(Double.valueOf(value).doubleValue());
		}
	}

	private final static class NumberBetsMapping extends HeaderMapping {

		public final static String ID = "NUMBER_BETS";

		/**
		 * 
		 */
		public NumberBetsMapping() {
			super(ID);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String line, String value, History history)
				throws ParseException, NumberFormatException {
			history.setNumberBets(Long.valueOf(value).longValue());
		}
	}

	private final static class VolumeMatchedMapping extends HeaderMapping {

		public final static String ID = "VOLUME_MATCHED";

		/**
		 * 
		 */
		public VolumeMatchedMapping() {
			super(ID);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String line, String value, History history)
				throws ParseException, NumberFormatException {
			history.setVolumeMatched(Double.valueOf(value).doubleValue());
		}
	}

	private final static class LatestTakenMapping extends HeaderMapping {

		public final static String ID = "LATEST_TAKEN";

		/**
		 * 
		 */
		public LatestTakenMapping() {
			super(ID);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String line, String value, History history)
				throws ParseException, NumberFormatException {
			history.setLatestTaken(parseArchiveDate(csvHeaderName, value));
		}
	}

	private final static class FirstTakenMapping extends HeaderMapping {

		public final static String ID = "FIRST_TAKEN";

		/**
		 * 
		 */
		public FirstTakenMapping() {
			super(ID);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String line, String value, History history)
				throws ParseException, NumberFormatException {
			history.setFirstTaken(parseArchiveDate(csvHeaderName, value));
		}
	}

	private final static class WinFlagMapping extends HeaderMapping {

		public final static String ID = "WIN_FLAG";

		/**
		 * 
		 */
		public WinFlagMapping() {
			super(ID);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String line, String value, History history)
				throws ParseException, NumberFormatException {
			try {
				history.setWinFlag(Integer.parseInt(value) == 1);
			} catch (NumberFormatException ex) {
				LOGGER.warn(csvHeaderName + ": value='" + value + "'! setWinFlag(false): " + line);
				history.setWinFlag(false);
				history.addNote(new HistoryNote(csvHeaderName + ": value='" + value + "'! setWinFlag(false)"));
				//history.setWinFlag(-1);
				//throw ex;
			}
		}
	}

	private final static class InPlayMapping extends HeaderMapping {

		public final static String ID = "IN_PLAY";

		/**
		 * 
		 */
		public InPlayMapping() {
			super(ID);
		}

		/* (non-Javadoc)
		 * @see uk.org.vacuumtube.dao.HistoryMapper.HeaderMapping#process(java.lang.String, java.lang.String, uk.org.vacuumtube.dao.History)
		 */
		@Override
		public void process(String line, String value, History history)
				throws ParseException, NumberFormatException {
			//history.setInPlay(value);
			InPlay inPlay = InPlay.getInstance(value);
			if (inPlay != null) {
				history.setInPlay(inPlay);
			} else {
				LOGGER.warn(csvHeaderName + ": value='" + value + "'! setInPlay(null): " + line);
				history.setInPlay(null);
			}
		}
	}
	
	/**
	 * @param mapping
	 */
	private static final void put(HeaderMapping mapping) {
		headerMap.put(mapping.getCsvHeaderName(), mapping);
	}	
}
