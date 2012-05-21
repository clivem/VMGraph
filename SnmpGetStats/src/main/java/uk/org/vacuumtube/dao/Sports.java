/**
 * 
 */
package uk.org.vacuumtube.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author clivem
 *
 */
public class Sports {
	
	private static final Logger LOGGER = Logger.getLogger(Sports.class);  

	private static final Map<Integer, Sports> INSTANCES_BY_ID = new HashMap<Integer, Sports>();
	
	public final static Sports SOCCER = new Sports(1, "Soccer");
	public final static Sports TENNIS = new Sports(2, "Tennis");
	public final static Sports GOLF = new Sports(3, "Golf");
	public final static Sports CRICKET = new Sports(4, "Cricket");
	public final static Sports RUGBY_UNION = new Sports(5, "Rugby Union");
	public final static Sports BOXING = new Sports(6, "Boxing");
	public final static Sports HORSE_RACING = new Sports(7, "Horse Racing");
	public final static Sports MOTOR_RACING = new Sports(8, "Motor Sport");
	public final static Sports SPECIAL_BETS = new Sports(10, "Special Bets");
	public final static Sports CYCLING = new Sports(11, "Cycling");
	public final static Sports ROWING = new Sports(12, "Rowing");
	public final static Sports HORSE_RACING_TODAYS_CARD = new Sports(13, "Horse Racing - Todays Card");
	public final static Sports SOCCER_FIXTURES = new Sports(14, "Soccer - Fixtures");
	public final static Sports GREYHOUND_TODAYS_CARD = new Sports(15, "Greyhound - Todays Card");
	public final static Sports RUGBY_LEAGUE = new Sports(1477, "Rugby League");
	public final static Sports DARTS = new Sports(3503, "Darts");
	public final static Sports ATHLETICS = new Sports(3988, "Athletics");
	public final static Sports GREYHOUND_RACING = new Sports(4339, "Greyhound Racing");
	public final static Sports FINANCIAL_BETS = new Sports(6231, "Financial Bets");
	public final static Sports SNOOKER = new Sports(6422, "Snooker");
	public final static Sports AMERICAN_FOOTBALL = new Sports(6423, "American Football");
	public final static Sports BASEBALL = new Sports(7511, "Baseball");
	public final static Sports BASKETBALL = new Sports(7522, "Basketball");
	public final static Sports HOCKEY = new Sports(7523, "Hockey");
	public final static Sports ICE_HOCKEY = new Sports(7524, "Ice Hockey");
	public final static Sports SUMO_WRESTLING = new Sports(7525, "Sumo Wrestling");
	public final static Sports AUSTRALIAN_RULES = new Sports(61420, "Australian Rules");
	public final static Sports GAELIC_FOOTBALL = new Sports(66598, "Gaelic Football");
	public final static Sports HURLING = new Sports(66599, "Hurling");
	public final static Sports POOL = new Sports(72382, "Pool");
	public final static Sports CHESS = new Sports(136332, "Chess");
	public final static Sports TROTTING = new Sports(256284, "Trotting");
	public final static Sports COMMONWEALTH_GAMES = new Sports(300000, "Commonwealth Games");
	public final static Sports POKER = new Sports(315220, "Poker");
	public final static Sports WINTER_SPORTS = new Sports(451485, "Winter Sports");
	public final static Sports HANDBALL = new Sports(468328, "Handball");
	public final static Sports BADMINTON = new Sports(627555, "Badminton");
	public final static Sports INTERNATIONAL_RULES = new Sports(678378, "International Rules");
	public final static Sports BRIDGE = new Sports(982477, "Bridge");
	public final static Sports VOLLEYBALL = new Sports(998917, "Volleyball");
	public final static Sports BOWLS = new Sports(998919, "Bowls");
	public final static Sports FLOORBALL = new Sports(998920, "Floorball");
	public final static Sports NETBALL = new Sports(606611, "Netball");
	public final static Sports YACHTING = new Sports(998916, "Yachting");
	public final static Sports SWIMMING = new Sports(620576, "Swimming");
	public final static Sports EXCHANGE_POKER = new Sports(1444073, "Exchange Poker");
	public final static Sports BACKGAMMON = new Sports(1938544, "Backgammon");
	public final static Sports GAA_SPORTS = new Sports(2030972, "GAA Sports");
	public final static Sports GAELIC_GAMES = new Sports(2152880, "Gaelic Games");
	public final static Sports INTERNATIONAL_MARKETS = new Sports(2264869, "International Markets");
	public final static Sports POLITICS = new Sports(2378961, "Politics");

	static {
		addToMap(SOCCER);
		addToMap(TENNIS);
		addToMap(GOLF);
		addToMap(CRICKET);
		addToMap(RUGBY_UNION);
		addToMap(BOXING);
		addToMap(HORSE_RACING);
		addToMap(MOTOR_RACING);
		addToMap(SPECIAL_BETS);
		addToMap(CYCLING);
		addToMap(ROWING);
		addToMap(HORSE_RACING_TODAYS_CARD);
		addToMap(SOCCER_FIXTURES);
		addToMap(GREYHOUND_TODAYS_CARD);
		addToMap(RUGBY_LEAGUE);
		addToMap(DARTS);
		addToMap(ATHLETICS);
		addToMap(GREYHOUND_RACING);
		addToMap(FINANCIAL_BETS);
		addToMap(SNOOKER);
		addToMap(AMERICAN_FOOTBALL);
		addToMap(BASEBALL);
		addToMap(BASKETBALL);
		addToMap(HOCKEY);
		addToMap(ICE_HOCKEY);
		addToMap(SUMO_WRESTLING);
		addToMap(AUSTRALIAN_RULES);
		addToMap(GAELIC_FOOTBALL);
		addToMap(HURLING);
		addToMap(POOL);
		addToMap(CHESS);
		addToMap(TROTTING);
		addToMap(COMMONWEALTH_GAMES);
		addToMap(POKER);
		addToMap(WINTER_SPORTS);
		addToMap(HANDBALL);
		addToMap(BADMINTON);
		addToMap(INTERNATIONAL_RULES);
		addToMap(BRIDGE);
		addToMap(VOLLEYBALL);
		addToMap(BOWLS);
		addToMap(FLOORBALL);
		addToMap(NETBALL);
		addToMap(YACHTING);
		addToMap(SWIMMING);
		addToMap(EXCHANGE_POKER);
		addToMap(BACKGAMMON);
		addToMap(GAA_SPORTS);
		addToMap(GAELIC_GAMES);
		addToMap(INTERNATIONAL_MARKETS);
		addToMap(POLITICS);
	}
	
	private int sportsId;
	private String description;
	
	/**
	 * @param sportsId
	 * @param description
	 */
	private Sports(int sportsId, String description) {
		this.sportsId = sportsId;
		this.description = description;
	}

	/**
	 * @return the sportsId
	 */
	public int getSportsId() {
		return sportsId;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new Integer(sportsId).hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return obj == this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Sports[sportsId=" + sportsId + ", description=" + description
				+ "]";
	}	
	
	/**
	 * @param sports
	 */
	private final static void addToMap(Sports sports) {
		INSTANCES_BY_ID.put(sports.getSportsId(), sports);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public final static Sports getInstance(int id) {
		Sports sports = INSTANCES_BY_ID.get(id);
		if (sports == null) {
			LOGGER.warn("Unknown Sport with ID: " + id + ". Creating record...");
			sports = new Sports(id, "UNKNOWN");
			addToMap(sports);
		}
		
		return sports;
	}
}
