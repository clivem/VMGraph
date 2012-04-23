package uk.org.vacuumtube.rrd4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author clivem
 *
 */
public class Profile {

	public final static int KILO_MULT = 1000;

	public final static String XL30_3 = "XL_30_3";
	public final static String XXL50_5 = "XXL_50_5";
	public final static String XL60_6 = "XL_60_6";
	public final static String XL100_5 = "XL_100_5";
	public final static String XL100_10 = "XL_100_10";

	public final static String[] SERVICE_NAME_LIST = {XL30_3, XXL50_5, XL60_6, XL100_5, XL100_10};

	private String serviceName;

	private Direction direction;
	private int startHour;
	private int durationHours;

	private int connectionSpeedDownMbps;
	//private long limitDownMB;
	//private int limitDownReductionPercentage;
	//private int limitDownReductionHours;

	private int connectionSpeedUpMbps;
	//private long limitUpMB;
	//private int limitUpReductionPercentage;
	//private int limitUpReductionHours;

	public enum Direction {BOTH, DOWN, UP};
	
	private ArrayList<StmProfile> stmProfileList = new ArrayList<StmProfile>();

	private static HashMap<String, HashMap<String, Profile>> profileMap;

    static {
    	profileMap = new HashMap<String, HashMap<String, Profile>>();

		/*
		 * XL_30_3
		 */
    	HashMap<String, Profile> serviceMap = new HashMap<String, Profile>();
    	serviceMap.put("DAY_BOTH_XL30_3", 
    			new Profile(XL30_3, 30, 3, Direction.BOTH, 0, 24,
    					new StmProfile[]{new StmProfile(Direction.BOTH, -1, 0, 0)}));
    	serviceMap.put("1000-1500_DOWN_XL30_3", 
    			new Profile(XL30_3, 30, 3, Direction.DOWN, 10, 5,
    					new StmProfile[]{new StmProfile(Direction.DOWN, 7000, 50, 5)}));
    	serviceMap.put("1600-2100_DOWN_XL30_3", 
    			new Profile(XL30_3, 30, 3, Direction.DOWN, 16, 5,
    					new StmProfile[]{new StmProfile(Direction.DOWN, 3500, 50, 5)}));
    	serviceMap.put("1500-2000_UP_XL30_3", 
    			new Profile(XL30_3, 30, 3, Direction.UP, 15, 5,
    					new StmProfile[]{new StmProfile(Direction.UP, 4200, 75, 5)}));
		profileMap.put(XL30_3, serviceMap);

    	/*
    	 * XXL50_5
    	 */
    	serviceMap = new HashMap<String, Profile>();
    	serviceMap.put("DAY_BOTH_XXL50_5", 
    			new Profile(XXL50_5, 50, 5, Direction.BOTH, 0, 24,
    					new StmProfile[]{new StmProfile(Direction.BOTH, -1, 0, 0)}));
    	serviceMap.put("1000-1500_DOWN_XXL50_5", 
    			new Profile(XXL50_5, 50, 5, Direction.DOWN, 10, 5,
    					new StmProfile[]{new StmProfile(Direction.DOWN, 10000, 50, 5)}));
    	serviceMap.put("1600-2100_DOWN_XXL50_5", 
    			new Profile(XXL50_5, 50, 5, Direction.DOWN, 16, 5,
    					new StmProfile[]{new StmProfile(Direction.DOWN, 5000, 50, 5)}));
    	serviceMap.put("1500-2000_UP_XXL50_5", 
    			new Profile(XXL50_5, 50, 5, Direction.UP, 15, 5,
    					new StmProfile[]{new StmProfile(Direction.UP, 6000, 65, 5)}));
		profileMap.put(XXL50_5, serviceMap);

		/*
		 * XL60_6
		 */
    	serviceMap = new HashMap<String, Profile>();
    	serviceMap.put("DAY_BOTH_XL60_6", 
    			new Profile(XL60_6, 60, 6, Direction.BOTH, 0, 24,
    					new StmProfile[]{new StmProfile(Direction.BOTH, -1, 0, 0)}));
    	serviceMap.put("1000-1500_DOWN_XL60_6", 
    			new Profile(XL60_6, 60, 6, Direction.DOWN, 10, 5,
    					new StmProfile[]{new StmProfile(Direction.DOWN, 10000, 50, 5)}));
    	serviceMap.put("1600-2100_DOWN_XL60_6", 
    			new Profile(XL60_6, 60, 6, Direction.DOWN, 16, 5,
    					new StmProfile[]{new StmProfile(Direction.DOWN, 5000, 50, 5)}));
    	serviceMap.put("1500-2000_UP_XL60_6", 
    			new Profile(XL60_6, 60, 6, Direction.UP, 15, 5,
    					new StmProfile[]{new StmProfile(Direction.UP, 7000, 75, 5)}));
		profileMap.put(XL60_6, serviceMap);

		/*
		 * XL100_5
		 */
    	serviceMap = new HashMap<String, Profile>();
    	serviceMap.put("DAY_BOTH_XL100_5", 
    			new Profile(XL100_5, 100, 5, Direction.BOTH, 0, 24,
    					new StmProfile[]{new StmProfile(Direction.BOTH, -1, 0, 0)}));
    	serviceMap.put("1000-1500_DOWN_XL100_5", 
    			new Profile(XL100_5, 100, 5, Direction.DOWN, 10, 5,
    					new StmProfile[]{new StmProfile(Direction.DOWN, 20000, 50, 5)}));
    	serviceMap.put("1600-2100_DOWN_XL100_5", 
    			new Profile(XL100_5, 100, 5, Direction.DOWN, 16, 5,
    					new StmProfile[]{new StmProfile(Direction.DOWN, 10000, 50, 5)}));
    	serviceMap.put("1500-2000_UP_XL100_5", 
    			new Profile(XL100_5, 100, 5, Direction.UP, 15, 5,
    					new StmProfile[]{new StmProfile(Direction.UP, 6000, 65, 5)}));
		profileMap.put(XL100_5, serviceMap);

		/*
		 * XL100_10
		 */
    	serviceMap = new HashMap<String, Profile>();
    	serviceMap.put("DAY_BOTH_XL100_10", 
    			new Profile(XL100_10, 100, 10, Direction.BOTH, 0, 24,
    					new StmProfile[]{new StmProfile(Direction.BOTH, -1, 0, 0)}));
    	serviceMap.put("1000-1500_DOWN_XL100_10", 
    			new Profile(XL100_10, 100, 10, Direction.DOWN, 10, 5,
    					new StmProfile[]{new StmProfile(Direction.DOWN, 20000, 50, 5)}));
    	serviceMap.put("1600-2100_DOWN_XL100_10", 
    			new Profile(XL100_10, 100, 10, Direction.DOWN, 16, 5,
    					new StmProfile[]{new StmProfile(Direction.DOWN, 10000, 50, 5)}));
    	serviceMap.put("1500-2000_UP_XL100_10", 
    			new Profile(XL100_10, 100, 10, Direction.UP, 15, 5,
    					new StmProfile[]{new StmProfile(Direction.UP, 12000, 75, 5)}));
		profileMap.put(XL100_10, serviceMap);
    }
    
    /**
     * @param connectionSpeed
     * @return
     */
    public static Profile[] getProfileList(String serviceName) {
		HashMap<String, Profile> serviceMap = profileMap.get(serviceName);
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
    public Profile(String serviceName, int connectionSpeedDownMbps, int connectionSpeedUpMbps, 
    		Direction direction, int startHour, int durationHours, StmProfile[] smtProfileList) {
    	this.serviceName = serviceName;
    	this.connectionSpeedDownMbps = connectionSpeedDownMbps;
    	this.connectionSpeedUpMbps = connectionSpeedUpMbps;
		this.direction = direction;
		this.startHour = startHour;
		this.durationHours = durationHours;
		stmProfileList.addAll(stmProfileList);
	}

    /**
     * @param dir
     * @return
     */
    public StmProfile getStmProfile(Direction dir) {
    	Iterator<StmProfile> stmIterator = stmProfileList.iterator();
    	while (stmIterator.hasNext()) {
    		StmProfile stmProfile = stmIterator.next();
    		if (stmProfile.getDirection().equals(dir)) {
    			return stmProfile;
    		}
    	}
    	return null;
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
	 * @return
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
	    buf.append("serviceName=");
	    buf.append(serviceName);
	    buf.append(", direction=");
	    buf.append(direction);
	    buf.append(", connectionSpeedDownMbps=");
	    buf.append(connectionSpeedDownMbps);
	    buf.append(", connectionSpeedUpMbps=");
	    buf.append(connectionSpeedUpMbps);
	    buf.append(", limitUpMB=");
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
