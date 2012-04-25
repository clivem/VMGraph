/**
 * 
 */
package uk.org.vacuumtube.rrd4j;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author clivem
 *
 */
public class Service {
	
	public final static long KILO_MULT = 1000;
	
	public final static String XL30_3 = "XL_30_3";
	public final static String XXL50_5 = "XXL_50_5";
	public final static String XL60_6 = "XL_60_6";
	public final static String XL100_5 = "XL_100_5";
	public final static String XL100_10 = "XL_100_10";
	public final static String[] SERVICE_NAME_LIST = {XL30_3, XXL50_5, XL60_6, XL100_5, XL100_10};

	private static Map<String, Map<String, Service>> PROFILE_MAP = new TreeMap<String, Map<String, Service>>();

	private String serviceId;
	private String serviceName;
	private int connectionSpeedDownMbps;
	private int connectionSpeedUpMbps;
	private int startHour;
	private int durationHours;
	private Map<Direction, StmProfile> stmProfileMap;

    static {
		/*
		 * XL_30_3
		 */
    	Map<String, Service> serviceMap = new TreeMap<String, Service>();
    	put(serviceMap, new Service(XL30_3 + "_BOTH_24HR", XL30_3, 30, 3, /*Direction.BOTH,*/ 0, 24,
    			StmProfile.createMap(new StmProfile[]{
    					new StmProfile(Direction.DOWN, -1, 0, 0), 
    					new StmProfile(Direction.UP, -1, 0, 0)})));
    	put(serviceMap, new Service(XL30_3 + "_DOWN_1000-1500", XL30_3, 30, 3, /*Direction.DOWN,*/ 10, 5,
    			StmProfile.createMap(new StmProfile[]{new StmProfile(Direction.DOWN, 7000, 50, 5)})));
    	put(serviceMap, new Service(XL30_3 + "_DOWN_1600-2100", XL30_3, 30, 3, /*Direction.DOWN,*/ 16, 5,
    			StmProfile.createMap(new StmProfile[]{new StmProfile(Direction.DOWN, 3500, 50, 5)})));
    	put(serviceMap, new Service(XL30_3 + "_UP_1500-2000", XL30_3, 30, 3, /*Direction.UP,*/ 15, 5,
    			StmProfile.createMap(new StmProfile[]{new StmProfile(Direction.UP, 4200, 75, 5)})));
		PROFILE_MAP.put(XL30_3, serviceMap);

    	/*
    	 * XXL50_5
    	 */
    	serviceMap = new TreeMap<String, Service>();
    	put(serviceMap, new Service(XXL50_5 + "_BOTH_24HR", XXL50_5, 50, 5, /*Direction.BOTH,*/ 0, 24,
    			StmProfile.createMap(new StmProfile[]{
    					new StmProfile(Direction.DOWN, -1, 0, 0), 
    					new StmProfile(Direction.UP, -1, 0, 0)})));
    	put(serviceMap, new Service(XXL50_5 + "_DOWN_1000-1500", XXL50_5, 50, 5, /*Direction.DOWN,*/ 10, 5,
    			StmProfile.createMap(new StmProfile[]{new StmProfile(Direction.DOWN, 10000, 50, 5)})));
    	put(serviceMap, new Service(XXL50_5 + "_DOWN_1600-2100", XXL50_5, 50, 5, /*Direction.DOWN,*/ 16, 5,
    			StmProfile.createMap(new StmProfile[]{new StmProfile(Direction.DOWN, 5000, 50, 5)})));
    	put(serviceMap, new Service(XXL50_5 + "_UP_1500-2000", XXL50_5, 50, 5, /*Direction.UP,*/ 15, 5,
    			StmProfile.createMap(new StmProfile[]{new StmProfile(Direction.UP, 6000, 65, 5)})));
		PROFILE_MAP.put(XXL50_5, serviceMap);

		/*
		 * XL60_6
		 */
    	serviceMap = new TreeMap<String, Service>();
    	put(serviceMap, new Service(XL60_6 + "_BOTH_24HR", XL60_6, 60, 6, /*Direction.BOTH,*/ 0, 24,
    			StmProfile.createMap(new StmProfile[]{
    					new StmProfile(Direction.DOWN, -1, 0, 0), 
    					new StmProfile(Direction.UP, -1, 0, 0)})));
    	put(serviceMap, new Service(XL60_6 + "_DOWN_1000-1500", XL60_6, 60, 6, /*Direction.DOWN,*/ 10, 5,
    			StmProfile.createMap(new StmProfile[]{new StmProfile(Direction.DOWN, 10000, 50, 5)})));
    	put(serviceMap, new Service(XL60_6 + "_DOWN_1600-2100", XL60_6, 60, 6, /*Direction.DOWN,*/ 16, 5,
    			StmProfile.createMap(new StmProfile[]{new StmProfile(Direction.DOWN, 5000, 50, 5)})));
    	put(serviceMap, new Service(XL60_6 + "_UP_1500-2000", XL60_6, 60, 6, /*Direction.UP,*/ 15, 5,
    			StmProfile.createMap(new StmProfile[]{new StmProfile(Direction.UP, 7000, 75, 5)})));
		PROFILE_MAP.put(XL60_6, serviceMap);

		/*
		 * XL100_5
		 */
    	serviceMap = new TreeMap<String, Service>();
    	put(serviceMap, new Service(XL100_5 + "_BOTH_24HR", XL100_5, 100, 5, /*Direction.BOTH,*/ 0, 24,
    			StmProfile.createMap(new StmProfile[]{
    					new StmProfile(Direction.DOWN, -1, 0, 0), 
    					new StmProfile(Direction.UP, -1, 0, 0)})));
    	put(serviceMap, new Service(XL100_5 + "_DOWN_1000-1500", XL100_5, 100, 5, /*Direction.DOWN,*/ 10, 5,
    			StmProfile.createMap(new StmProfile[]{new StmProfile(Direction.DOWN, 20000, 50, 5)})));
    	put(serviceMap, new Service(XL100_5 + "_DOWN_1600-2100", XL100_5, 100, 5, /*Direction.DOWN,*/ 16, 5,
    			StmProfile.createMap(new StmProfile[]{new StmProfile(Direction.DOWN, 10000, 50, 5)})));
    	put(serviceMap, new Service(XL100_5 + "_UP_1500-2000", XL100_5, 100, 5, /*Direction.UP,*/ 15, 5,
    			StmProfile.createMap(new StmProfile[]{new StmProfile(Direction.UP, 6000, 65, 5)})));
		PROFILE_MAP.put(XL100_5, serviceMap);

		/*
		 * XL100_10
		 */
    	serviceMap = new TreeMap<String, Service>();
    	put(serviceMap, new Service(XL100_10 + "_BOTH_24HR", XL100_10, 100, 10, /*Direction.BOTH,*/ 0, 24,
    			StmProfile.createMap(new StmProfile[]{
    					new StmProfile(Direction.DOWN, -1, 0, 0), 
    					new StmProfile(Direction.UP, -1, 0, 0)})));
    	put(serviceMap, new Service(XL100_10 + "_DOWN_1000-1500", XL100_10, 100, 10, /*Direction.DOWN,*/ 10, 5,
    			StmProfile.createMap(new StmProfile[]{new StmProfile(Direction.DOWN, 20000, 50, 5)})));
    	put(serviceMap, new Service(XL100_10 + "_DOWN_1600-2100", XL100_10, 100, 10, /*Direction.DOWN,*/ 16, 5,
    			StmProfile.createMap(new StmProfile[]{new StmProfile(Direction.DOWN, 10000, 50, 5)})));
    	put(serviceMap, new Service(XL100_10 + "_UP_1500-2000", XL100_10, 100, 10, /*Direction.UP,*/ 15, 5,
    			StmProfile.createMap(new StmProfile[]{new StmProfile(Direction.UP, 12000, 75, 5)})));
		PROFILE_MAP.put(XL100_10, serviceMap);
    }
    
	/**
	 * @param map
	 * @param profile
	 */
	private final static void put(Map<String, Service> map, Service profile) {
		map.put(profile.getServiceId(), profile);
	}
	
    /**
     * @param serviceName
     * @return a list of STM profiles for the given serviceName
     */
    public final static Service[] getProfileList(String serviceName) {
		Map<String, Service> serviceMap = PROFILE_MAP.get(serviceName);
		if (serviceMap != null) {
			return serviceMap.values().toArray(new Service[serviceMap.size()]);
		}
    	return new Service[0];
    }
    
    /**
     * @param id
     * @param serviceName
     * @param connectionSpeedDownMbps
     * @param connectionSpeedUpMbps
     * @param startHour
     * @param durationHours
     * @param stmProfileMap
     */
    public Service(String id, String serviceName, int connectionSpeedDownMbps, int connectionSpeedUpMbps, 
    		/*Direction direction, */int startHour, int durationHours, /*List<StmProfile> smtProfileList*/
    		Map<Direction, StmProfile> stmProfileMap) {
    	this.serviceId = id;
    	this.serviceName = serviceName;
    	this.connectionSpeedDownMbps = connectionSpeedDownMbps;
    	this.connectionSpeedUpMbps = connectionSpeedUpMbps;
		this.startHour = startHour;
		this.durationHours = durationHours;
		this.stmProfileMap = stmProfileMap;
	}

    /**
     * @param direction
     * @return the STM profile for the given direction
     */
    public StmProfile getStmProfile(Direction direction) {
    	return stmProfileMap.get(direction);
    }
    
	/**
	 * @return the unique serviceId for this service STM period
	 */
	public String getServiceId() {
		return serviceId;
	}	

	/**
	 * @return the service profile name
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @return the DOWN connection speed in Mbps
	 */
	public int getConnectionSpeedDownMbps() {
		return connectionSpeedDownMbps;
	}

	/**
	 * @return the the DOWN connection speed in bps
	 */
	public long getConnectionSpeedDownBps() {
		return connectionSpeedDownMbps * KILO_MULT * KILO_MULT;
	}

	/**
	 * @return the DOWN connection speed after STM is applied
	 */
	public long getConnectionSpeedDownBpsAfterSTM() {
		return (long) (getConnectionSpeedDownBps() *  ((100 - getLimitReductionPercentage(Direction.DOWN)) / 100d));
	}

	/**
	 * @return the UP connection speed in Mbps
	 */
	public int getConnectionSpeedUpMbps() {
		return connectionSpeedUpMbps;
	}

	/**
	 * @return the UP connection speed in bps 
	 */
	public long getConnectionSpeedUpBps() {
		return connectionSpeedUpMbps * KILO_MULT * KILO_MULT;
	}

	/**
	 * @return the UP connection speed after STM is applied
	 */
	public long getConnectionSpeedUpBpsAfterSTM() {
		return (long) (getConnectionSpeedUpBps() *  ((100 - getLimitReductionPercentage(Direction.UP)) / 100d));
	}

	/**
	 * @return the startHour of the period
	 */
	public int getStartHour() {
		return startHour;
	}

	/**
	 * @return the endHour of the period
	 */
	public int getEndHour() {
		return startHour + durationHours;
	}

	/**
	 * @return the durationHours of the period
	 */
	public int getDurationHours() {
		return durationHours;
	}
	
	/**
	 * @param dir
	 * @return the data limit in MBytes
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
	 * @return the data limit in bytes
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
	 * @return the percentage speed reduction after exceeding the 
	 * period data limit
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
	 * @return the number of hours the speed reduction applies 
	 * after exceeding the period data limit
	 */
	public int getLimitReductionHours(Direction dir) {
		StmProfile p = getStmProfile(dir);
		if (p != null) {
			return p.getLimitReductionHours();
		}
		return 0;
	}
	
	/**
	 * @return a Set containing the stmProfileMap keys
	 */
	public Set<Direction> getStmProfileMapKeySet() {
		return stmProfileMap.keySet();
	}
		
	/**
	 * @return a Collection containing the stmProfileMap values
	 */
	public Collection<StmProfile> getStmProfileMapValues() {
		return stmProfileMap.values();
	}
		
	/**
	 * @param direction
	 * @return true if the stmProfileList contains a profile for the direction
	 */
	public boolean stmProfileListHasDirection(Direction direction) {
		return (stmProfileMap.get(direction) != null);
	}

	/**
	 * @param direction
	 * @return true if the stmProfileList only contains one direction profile and it matches direction
	 */
	public boolean stmProfileListHasSingleProfile(Direction direction) {
		return (stmProfileMap.size() == 1 && stmProfileMap.containsKey(direction));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Service[");
	    buf.append("serviceId=");
	    buf.append(serviceId);
	    buf.append(", serviceName=");
	    buf.append(serviceName);
	    buf.append(", connectionSpeedDownMbps=");
	    buf.append(connectionSpeedDownMbps);
	    buf.append(", connectionSpeedUpMbps=");
	    buf.append(connectionSpeedUpMbps);
	    buf.append(", startHour=");
	    buf.append(startHour);
	    buf.append(", durationHours=");
	    buf.append(durationHours);
	    buf.append(", stmProfileMap=");
	    buf.append(stmProfileMap);
		buf.append("]");
		return buf.toString();
	}
}
