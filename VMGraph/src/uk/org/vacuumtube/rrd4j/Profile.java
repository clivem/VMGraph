/**
 * 
 */
package uk.org.vacuumtube.rrd4j;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author clivem
 *
 */
public class Profile {
	
	public final static long KILO_MULT = 1000;
	
	public final static String XL30_3 = "XL_30_3";
	public final static String XXL50_5 = "XXL_50_5";
	public final static String XL60_6 = "XL_60_6";
	public final static String XL100_5 = "XL_100_5";
	public final static String XL100_10 = "XL_100_10";
	public final static String[] SERVICE_NAME_LIST = {XL30_3, XXL50_5, XL60_6, XL100_5, XL100_10};

	private static Map<String, Map<String, Profile>> PROFILE_MAP = new TreeMap<String, Map<String, Profile>>();

	private String id;
	private String serviceName;
	private int connectionSpeedDownMbps;
	private int connectionSpeedUpMbps;
	private Direction direction;
	private int startHour;
	private int durationHours;
	private List<StmProfile> stmProfileList;

    static {
		/*
		 * XL_30_3
		 */
    	Map<String, Profile> serviceMap = new TreeMap<String, Profile>();
    	put(serviceMap, new Profile(XL30_3 + "_BOTH_24HR", XL30_3, 30, 3, Direction.BOTH, 0, 24,
    			Arrays.asList(new StmProfile(Direction.BOTH, -1, 0, 0))));
    	put(serviceMap, new Profile(XL30_3 + "_DOWN_1000-1500", XL30_3, 30, 3, Direction.DOWN, 10, 5,
    			Arrays.asList(new StmProfile(Direction.DOWN, 7000, 50, 5))));
    	put(serviceMap, new Profile(XL30_3 + "_DOWN_1600-2100", XL30_3, 30, 3, Direction.DOWN, 16, 5,
    			Arrays.asList(new StmProfile(Direction.DOWN, 3500, 50, 5))));
    	put(serviceMap, new Profile(XL30_3 + "_UP_1500-2000", XL30_3, 30, 3, Direction.UP, 15, 5,
    			Arrays.asList(new StmProfile(Direction.UP, 4200, 75, 5))));
		PROFILE_MAP.put(XL30_3, serviceMap);

    	/*
    	 * XXL50_5
    	 */
    	serviceMap = new TreeMap<String, Profile>();
    	put(serviceMap, new Profile(XXL50_5 + "_BOTH_24HR", XXL50_5, 50, 5, Direction.BOTH, 0, 24,
    			Arrays.asList(new StmProfile(Direction.BOTH, -1, 0, 0))));
    	put(serviceMap, new Profile(XXL50_5 + "_DOWN_1000-1500", XXL50_5, 50, 5, Direction.DOWN, 10, 5,
    			Arrays.asList(new StmProfile(Direction.DOWN, 10000, 50, 5))));
    	put(serviceMap, new Profile(XXL50_5 + "_DOWN_1600-2100", XXL50_5, 50, 5, Direction.DOWN, 16, 5,
    			Arrays.asList(new StmProfile(Direction.DOWN, 5000, 50, 5))));
    	put(serviceMap, new Profile(XXL50_5 + "_UP_1500-2000", XXL50_5, 50, 5, Direction.UP, 15, 5,
    			Arrays.asList(new StmProfile(Direction.UP, 6000, 65, 5))));
		PROFILE_MAP.put(XXL50_5, serviceMap);

		/*
		 * XL60_6
		 */
    	serviceMap = new TreeMap<String, Profile>();
    	put(serviceMap, new Profile(XL60_6 + "_BOTH_24HR", XL60_6, 60, 6, Direction.BOTH, 0, 24,
    			Arrays.asList(new StmProfile(Direction.BOTH, -1, 0, 0))));
    	put(serviceMap, new Profile(XL60_6 + "_DOWN_1000-1500", XL60_6, 60, 6, Direction.DOWN, 10, 5,
    			Arrays.asList(new StmProfile(Direction.DOWN, 10000, 50, 5))));
    	put(serviceMap, new Profile(XL60_6 + "_DOWN_1600-2100", XL60_6, 60, 6, Direction.DOWN, 16, 5,
    			Arrays.asList(new StmProfile(Direction.DOWN, 5000, 50, 5))));
    	put(serviceMap, new Profile(XL60_6 + "_UP_1500-2000", XL60_6, 60, 6, Direction.UP, 15, 5,
    			Arrays.asList(new StmProfile(Direction.UP, 7000, 75, 5))));
		PROFILE_MAP.put(XL60_6, serviceMap);

		/*
		 * XL100_5
		 */
    	serviceMap = new TreeMap<String, Profile>();
    	put(serviceMap, new Profile(XL100_5 + "_BOTH_24HR", XL100_5, 100, 5, Direction.BOTH, 0, 24,
    			Arrays.asList(new StmProfile(Direction.BOTH, -1, 0, 0))));
    	put(serviceMap, new Profile(XL100_5 + "_DOWN_1000-1500", XL100_5, 100, 5, Direction.DOWN, 10, 5,
    			Arrays.asList(new StmProfile(Direction.DOWN, 20000, 50, 5))));
    	put(serviceMap, new Profile(XL100_5 + "_DOWN_1600-2100", XL100_5, 100, 5, Direction.DOWN, 16, 5,
    			Arrays.asList(new StmProfile(Direction.DOWN, 10000, 50, 5))));
    	put(serviceMap, new Profile(XL100_5 + "_UP_1500-2000", XL100_5, 100, 5, Direction.UP, 15, 5,
    			Arrays.asList(new StmProfile(Direction.UP, 6000, 65, 5))));
		PROFILE_MAP.put(XL100_5, serviceMap);

		/*
		 * XL100_10
		 */
    	serviceMap = new TreeMap<String, Profile>();
    	put(serviceMap, new Profile(XL100_10 + "_BOTH_24HR", XL100_10, 100, 10, Direction.BOTH, 0, 24,
    			Arrays.asList(new StmProfile(Direction.BOTH, -1, 0, 0))));
    	put(serviceMap, new Profile(XL100_10 + "_DOWN_1000-1500", XL100_10, 100, 10, Direction.DOWN, 10, 5,
    			Arrays.asList(new StmProfile(Direction.DOWN, 20000, 50, 5))));
    	put(serviceMap, new Profile(XL100_10 + "_DOWN_1600-2100", XL100_10, 100, 10, Direction.DOWN, 16, 5,
    			Arrays.asList(new StmProfile(Direction.DOWN, 10000, 50, 5))));
    	put(serviceMap, new Profile(XL100_10 + "_UP_1500-2000", XL100_10, 100, 10, Direction.UP, 15, 5,
    			Arrays.asList(new StmProfile(Direction.UP, 12000, 75, 5))));
		PROFILE_MAP.put(XL100_10, serviceMap);
    }
    
	/**
	 * @param map
	 * @param profile
	 */
	public final static void put(Map<String, Profile> map, Profile profile) {
		map.put(profile.getId(), profile);
	}
	
    /**
     * @param serviceName
     * @return
     */
    public final static Profile[] getProfileList(String serviceName) {
		Map<String, Profile> serviceMap = PROFILE_MAP.get(serviceName);
		if (serviceMap != null) {
			return serviceMap.values().toArray(new Profile[serviceMap.size()]);
		}
    	return new Profile[0];
    }
    
    /**
     * @param serviceName
     * @param direction
     * @param startHour
     * @param durationHours
     * @param smtProfileList
     */
    public Profile(String id, String serviceName, int connectionSpeedDownMbps, int connectionSpeedUpMbps, 
    		Direction direction, int startHour, int durationHours, List<StmProfile> smtProfileList) {
    	this.id = id;
    	this.serviceName = serviceName;
    	this.connectionSpeedDownMbps = connectionSpeedDownMbps;
    	this.connectionSpeedUpMbps = connectionSpeedUpMbps;
		this.direction = direction;
		this.startHour = startHour;
		this.durationHours = durationHours;
		this.stmProfileList = smtProfileList;
	}

    /**
     * @param dir
     * @return
     */
    public StmProfile getStmProfile(Direction dir) {
    	StmProfile both = null;
    	Iterator<StmProfile> stmIterator = stmProfileList.iterator();
    	while (stmIterator.hasNext()) {
    		StmProfile stmProfile = stmIterator.next();
    		if (stmProfile.getDirection() == dir) {
    			return stmProfile;
    		}
    		if (both == null && stmProfile.getDirection() == Direction.BOTH) {
    			both = stmProfile;
    		}
    	}
    	return both;
    }
    
	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}	

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @return
	 */
	public int getConnectionSpeedDownMbps() {
		return connectionSpeedDownMbps;
	}

	/**
	 * @return
	 */
	public long getConnectionSpeedDownBps() {
		return connectionSpeedDownMbps * KILO_MULT * KILO_MULT;
	}

	/**
	 * @return
	 */
	public long getConnectionSpeedDownBpsAfterSTM() {
		return (long) (getConnectionSpeedDownBps() *  ((100 - getLimitReductionPercentage(Direction.DOWN)) / 100d));
	}

	/**
	 * @return 
	 */
	public int getConnectionSpeedUpMbps() {
		return connectionSpeedUpMbps;
	}

	/**
	 * @return
	 */
	public long getConnectionSpeedUpBps() {
		return connectionSpeedUpMbps * KILO_MULT * KILO_MULT;
	}

	/**
	 * @return
	 */
	public long getConnectionSpeedUpBpsAfterSTM() {
		return (long) (getConnectionSpeedUpBps() *  ((100 - getLimitReductionPercentage(Direction.UP)) / 100d));
	}

	/**
	 * @return the direction
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * @return the startHour
	 */
	public int getStartHour() {
		return startHour;
	}

	/**
	 * @return the endHour
	 */
	public int getEndHour() {
		return startHour + durationHours;
	}

	/**
	 * @return the durationHours
	 */
	public int getDurationHours() {
		return durationHours;
	}
	
	/**
	 * @param dir
	 * @return
	 */
	public long getLimitMBytes(Direction dir) {
		StmProfile p = getStmProfile(dir);
		if (p != null) {
			return p.getLimitMB();
		}
		return -1;
	}
	
	/**
	 * @param dir
	 * @return
	 */
	public long getLimitBytes(Direction dir) {
		StmProfile p = getStmProfile(dir);
		if (p != null) {
			return p.getLimitMB() * KILO_MULT * KILO_MULT; 
		}
		return -1;
	}
	
	/**
	 * @param dir
	 * @return
	 */
	public int getLimitReductionPercentage(Direction dir) {
		StmProfile p = getStmProfile(dir);
		if (p != null) {
			return p.getLimitReductionPercentage();
		}
		return 0;
	}
	
	/**
	 * @param dir
	 * @return
	 */
	public int getLimitReductionHours(Direction dir) {
		StmProfile p = getStmProfile(dir);
		if (p != null) {
			return p.getLimitReductionHours();
		}
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Service[");
	    buf.append("id=");
	    buf.append(id);
	    buf.append(", serviceName=");
	    buf.append(serviceName);
	    buf.append(", direction=");
	    buf.append(direction);
	    buf.append(", connectionSpeedDownMbps=");
	    buf.append(connectionSpeedDownMbps);
	    buf.append(", connectionSpeedUpMbps=");
	    buf.append(connectionSpeedUpMbps);
	    buf.append(", startHour=");
	    buf.append(startHour);
	    buf.append(", durationHours=");
	    buf.append(durationHours);
	    buf.append(", stmProfileList=");
	    buf.append(stmProfileList);
		buf.append("]");
		return buf.toString();
	}
}
