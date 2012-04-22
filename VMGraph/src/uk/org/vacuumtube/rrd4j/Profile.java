package uk.org.vacuumtube.rrd4j;
import java.util.HashMap;

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
    
    private int downConnectionSpeedMbps;
    private long limitDownMB;
    private int limitDownReductionPercentage;
    private int limitDownReductionHours;
    
    private int upConnectionSpeedMbps;
    private long limitUpMB;
    private int limitUpReductionPercentage;
    private int limitUpReductionHours;

    public enum Direction {BOTH, DOWN, UP};
    
    private static HashMap<String, HashMap<String, Profile>> profileMap;
    
    static {
    	profileMap = new HashMap<String, HashMap<String, Profile>>();

    	HashMap<String, Profile> serviceMap = new HashMap<String, Profile>();
		profileMap.put(XXL50_5, serviceMap);
    	serviceMap.put("NONE_BOTH_XXL50_5", 
    			new Profile(
    				XXL50_5, // serviceName
	    			50, // downConnectionSpeedMbps
	    			5, // upConnectionSpeedMbps
	    			Direction.BOTH, // direction
	    			-1, // limitDownMB
	    			0, // limitDownReductionPercentage
	    			0, // limitDownReductionHours
	    			-1, // limitUpMB
	    			0, // limitUpReductionPercentage
	    			0, // limitUpReductionHours
	    			0, // startHour
	    			24 // durationHours
    			));
    	serviceMap.put("P1_DOWN_XXL50_5", 
    			new Profile(
    					XXL50_5, 
    					50, 
    					5, 
    					Direction.DOWN, 
    					10000, 
    					50, 
    					5, 
    					-1, 
    					0, 
    					0, 
    					10, 
    					5
    					));
    	serviceMap.put("P2_DOWN_XXL50_5", 
    			new Profile(
    					XXL50_5, 
    					50, 
    					5, 
    					Direction.DOWN, 
    					5000, 
    					50, 
    					5, 
    					-1, 
    					0, 
    					0, 
    					16, 
    					5
    					));
    	serviceMap.put("P2_UP_XXL50_5", 
    			new Profile(
    					XXL50_5, 
    					50, 
    					5, 
    					Direction.UP, 
    					-1, 
    					0, 
    					0, 
    					6000, 
    					65, 
    					5, 
    					15, 
    					5
    					));

    	serviceMap = new HashMap<String, Profile>();
		profileMap.put(XL30_3, serviceMap);
    	serviceMap.put("NONE_BOTH_XL30_3", 
    			new Profile(
    				XL30_3, // serviceId
	    			30, // downConnectionSpeedMbps
	    			3, // upConnectionSpeed
	    			Direction.BOTH, // direction
	    			-1, // limitDownMB
	    			0, // limitDownReductionPercentage
	    			0, // limitDownReductionHours
	    			-1, // limitUpMB
	    			0, // limitUpReductionPercentage
	    			0, // limitUpReductionHours
	    			0, // startHour
	    			24 // durationHours
    			));
    	serviceMap.put("P1_DOWN_XL30_3", 
    			new Profile(
    				XL30_3, // serviceId
	    			30, // downConnectionSpeedMbps
	    			3, // upConnectionSpeedMbps
	    			Direction.DOWN, // direction
	    			7000, // limitDownMB
	    			50, // limitDownReductionPercentage
	    			5, // limitDownReductionHours
	    			-1, // limitUpMB
	    			0, // limitUpReductionPercentage
	    			0, // limitUpReductionHours
	    			10, // startHour
	    			5 // durationHours
    			));
    	serviceMap.put("P2_DOWN_XL30_3", 
    			new Profile(
    				XL30_3, // serviceId
	    			30, // connectionSpeedMbps
	    			3,
	    			Direction.DOWN, // direction
	    			3500, // limitDownMB
	    			50, // limitDownReductionPercentage
	    			5, // limitDownReductionHours
	    			-1, // limitUpMB
	    			0, // limitUpReductionPercentage
	    			0, // limitUpReductionHours
	    			16, // startHour
	    			5 // durationHours
    			));
    	serviceMap.put("P2_UP_XL30_3", 
    			new Profile(
    				XL30_3, // serviceId
	    			30, // connectionSpeedMbps
	    			3,
	    			Direction.UP, // direction
	    			-1, // limitDownMB
	    			0, // limitDownReductionPercentage
	    			0, // limitDownReductionHours
	    			4200, // limitUpMB
	    			75, // limitUpReductionPercentage
	    			5, // limitUpReductionHours
	    			15, // startHour
	    			5 // durationHours
    			));

    	serviceMap = new HashMap<String, Profile>();
		profileMap.put(XL60_6, serviceMap);
    	serviceMap.put("NONE_BOTH_XL60_6", 
    			new Profile(
    				XL60_6, // serviceId
	    			60, // connectionSpeedMbps
	    			6,
	    			Direction.BOTH, // direction
	    			-1, // limitDownMB
	    			0, // limitDownReductionPercentage
	    			0, // limitDownReductionHours
	    			-1, // limitUpMB
	    			0, // limitUpReductionPercentage
	    			0, // limitUpReductionHours
	    			0, // startHour
	    			24 // durationHours
    			));
    	serviceMap.put("P1_DOWN_XL60_6", 
    			new Profile(
    				XL60_6, // serviceId
	    			60, // connectionSpeedMbps
	    			6,
	    			Direction.DOWN, // direction
	    			10000, // limitDownMB
	    			50, // limitDownReductionPercentage
	    			5, // limitDownReductionHours
	    			-1, // limitUpMB
	    			0, // limitUpReductionPercentage
	    			0, // limitUpReductionHours
	    			10, // startHour
	    			5 // durationHours
    			));
    	serviceMap.put("P2_DOWN_XL60_6", 
    			new Profile(
    				XL60_6, // serviceId
	    			60, // connectionSpeedMbps
	    			6,
	    			Direction.DOWN, // direction
	    			5000, // limitDownMB
	    			50, // limitDownReductionPercentage
	    			5, // limitDownReductionHours
	    			-1, // limitUpMB
	    			0, // limitUpReductionPercentage
	    			0, // limitUpReductionHours
	    			16, // startHour
	    			5 // durationHours
    			));
    	serviceMap.put("P2_UP_XL60_6", 
    			new Profile(
    				XL60_6, // serviceId
	    			60, // connectionSpeedMbps
	    			6,
	    			Direction.UP, // direction
	    			-1, // limitDownMB
	    			0, // limitDownReductionPercentage
	    			0, // limitDownReductionHours
	    			7000, // limitUpMB
	    			75, // limitUpReductionPercentage
	    			5, // limitUpReductionHours
	    			15, // startHour
	    			5 // durationHours
    			));

    	serviceMap = new HashMap<String, Profile>();
		profileMap.put(XL100_5, serviceMap);
    	serviceMap.put("NONE_BOTH_XL100_5", 
    			new Profile(
    				XL100_5, // serviceId
	    			100, // connectionSpeedMbps
	    			5,
	    			Direction.BOTH, // direction
	    			-1, // limitDownMB
	    			0, // limitDownReductionPercentage
	    			0, // limitDownReductionHours
	    			-1, // limitUpMB
	    			0, // limitUpReductionPercentage
	    			0, // limitUpReductionHours
	    			0, // startHour
	    			24 // durationHours
    			));
    	serviceMap.put("P1_DOWN_XL100_5", 
    			new Profile(
    				XL100_5, // serviceId
	    			100, // connectionSpeedMbps
	    			5,
	    			Direction.DOWN, // direction
	    			20000, // limitDownMB
	    			50, // limitDownReductionPercentage
	    			5, // limitDownReductionHours
	    			-1, // limitUpMB
	    			0, // limitUpReductionPercentage
	    			0, // limitUpReductionHours
	    			10, // startHour
	    			5 // durationHours
    			));
    	serviceMap.put("P2_DOWN_XL100_5", 
    			new Profile(
    				XL100_5, // serviceId
	    			100, // connectionSpeedMbps
	    			5,
	    			Direction.DOWN, // direction
	    			10000, // limitDownMB
	    			50, // limitDownReductionPercentage
	    			5, // limitDownReductionHours
	    			-1, // limitUpMB
	    			0, // limitUpReductionPercentage
	    			0, // limitUpReductionHours
	    			16, // startHour
	    			5 // durationHours
    			));
    	serviceMap.put("P2_UP_XL100_5", 
    			new Profile(
    				XL100_5, // serviceId
	    			100, // connectionSpeedMbps
	    			5,
	    			Direction.UP, // direction
	    			-1, // limitDownMB
	    			0, // limitDownReductionPercentage
	    			0, // limitDownReductionHours
	    			6000, // limitUpMB
	    			65, // limitUpReductionPercentage
	    			5, // limitUpReductionHours
	    			15, // startHour
	    			5 // durationHours
    			));
    	
    	serviceMap = new HashMap<String, Profile>();
		profileMap.put(XL100_10, serviceMap);
    	serviceMap.put("NONE_BOTH_XL100_10", 
    			new Profile(
    				XL100_10, // serviceId
	    			100, // connectionSpeedMbps
	    			10,
	    			Direction.BOTH, // direction
	    			-1, // limitDownMB
	    			0, // limitDownReductionPercentage
	    			0, // limitDownReductionHours
	    			-1, // limitUpMB
	    			0, // limitUpReductionPercentage
	    			0, // limitUpReductionHours
	    			0, // startHour
	    			24 // durationHours
    			));
    	serviceMap.put("P1_DOWN_XL100_10", 
    			new Profile(
    				XL100_10, // serviceId
	    			100, // connectionSpeedMbps
	    			10,
	    			Direction.DOWN, // direction
	    			20000, // limitDownMB
	    			50, // limitDownReductionPercentage
	    			5, // limitDownReductionHours
	    			-1, // limitUpMB
	    			0, // limitUpReductionPercentage
	    			0, // limitUpReductionHours
	    			10, // startHour
	    			5 // durationHours
    			));
    	serviceMap.put("P2_DOWN_XL100_10", 
    			new Profile(
    				XL100_10, // serviceId
	    			100, // connectionSpeedMbps
	    			10,
	    			Direction.DOWN, // direction
	    			10000, // limitDownMB
	    			50, // limitDownReductionPercentage
	    			5, // limitDownReductionHours
	    			-1, // limitUpMB
	    			0, // limitUpReductionPercentage
	    			0, // limitUpReductionHours
	    			16, // startHour
	    			5 // durationHours
    			));
    	serviceMap.put("P2_UP_XL100_10", 
    			new Profile(
    				XL100_10, // serviceId
	    			100, // connectionSpeedMbps
	    			10,
	    			Direction.UP, // direction
	    			-1, // limitDownMB
	    			0, // limitDownReductionPercentage
	    			0, // limitDownReductionHours
	    			12000, // limitUpMB
	    			75, // limitUpReductionPercentage
	    			5, // limitUpReductionHours
	    			15, // startHour
	    			5 // durationHours
    			));
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
     * @param downConnectionSpeedMbps
     * @param upConnectionSpeedMbps
     * @param direction
     * @param limitDownMB
     * @param limitDownReductionPercentage
     * @param limitDownReductionHours
     * @param limitUpMB
     * @param limitUpReductionPercentage
     * @param limitUpReductionHours
     * @param startHour
     * @param durationHours
     */
    public Profile(String serviceName, int downConnectionSpeedMbps, int upConnectionSpeedMbps, Direction direction, 
    		long limitDownMB, int limitDownReductionPercentage, int limitDownReductionHours, 
			long limitUpMB, int limitUpReductionPercentage, int limitUpReductionHours, 
    		int startHour, int durationHours) {
    	this.serviceName = serviceName;
		this.downConnectionSpeedMbps = downConnectionSpeedMbps;
		this.upConnectionSpeedMbps = upConnectionSpeedMbps;
		this.direction = direction;
		this.limitDownMB = limitDownMB;
		this.limitDownReductionPercentage = limitDownReductionPercentage;
		this.limitDownReductionHours = limitDownReductionHours;
		this.limitUpMB = limitUpMB;
		this.limitUpReductionPercentage = limitUpReductionPercentage;
		this.limitUpReductionHours = limitUpReductionHours;
		this.startHour = startHour;
		this.durationHours = durationHours;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @return the downConnectionSpeedMbps
	 */
	public int getDownConnectionSpeedMbps() {
		return downConnectionSpeedMbps;
	}
	
	public long getDownConnectionSpeedbps() {
		return downConnectionSpeedMbps * KILO_MULT * KILO_MULT;
	}

	public long getDownConnectionSpeedbpsAfterSTM() {
		return (long) (getDownConnectionSpeedbps() *  ((100 - limitDownReductionPercentage) / 100d));
	}

	/**
	 * @return the upConnectionSpeedMbps
	 */
	public int getUpConnectionSpeedMbps() {
		return upConnectionSpeedMbps;
	}

	public long getUpConnectionSpeedbps() {
		return upConnectionSpeedMbps * KILO_MULT * KILO_MULT;
	}

	public long getUpConnectionSpeedbpsAfterSTM() {
		return (long) (getUpConnectionSpeedbps() *  ((100 - limitUpReductionPercentage) / 100d));
	}

	/**
	 * @return the direction
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * @return the limitDown
	 */
	public long getLimitDownMB() {
		return limitDownMB;
	}

	/**
	 * @return
	 */
	public long getLimitDownBytes() {
		return limitDownMB * KILO_MULT * KILO_MULT;
	}

	/**
	 * @return the limitUp
	 */
	public long getLimitUpMB() {
		return limitUpMB;
	}

	/**
	 * @return
	 */
	public long getLimitUpBytes() {
		return limitUpMB * KILO_MULT * KILO_MULT;
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

	public int getDurationHours() {
		return durationHours;
	}
	
	/**
	 * @return the limitDownReductionPercentage
	 */
	public int getLimitDownReductionPercentage() {
		return limitDownReductionPercentage;
	}

	/**
	 * @return the limitDownReductionHours
	 */
	public int getLimitDownReductionHours() {
		return limitDownReductionHours;
	}

	/**
	 * @return the limitUpReductionPercentage
	 */
	public int getLimitUpReductionPercentage() {
		return limitUpReductionPercentage;
	}

	/**
	 * @return the limitUpReductionHours
	 */
	public int getLimitUpReductionHours() {
		return limitUpReductionHours;
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
	    buf.append(", downConnectionSpeedMbps=");
	    buf.append(downConnectionSpeedMbps);
	    buf.append(", limitDownMB=");
	    buf.append(limitDownMB);
	    buf.append(", limitDownReductionPercentage");
	    buf.append(limitDownReductionPercentage);
	    buf.append(", limitDownReductionHours");
	    buf.append(limitDownReductionHours);
	    buf.append(", upConnectionSpeedMbps=");
	    buf.append(upConnectionSpeedMbps);
	    buf.append(", limitUpMB=");
	    buf.append(limitUpMB);
	    buf.append(", limitUpReductionPercentage");
	    buf.append(limitUpReductionPercentage);
	    buf.append(", limitUpReductionHours");
	    buf.append(limitUpReductionHours);
	    buf.append(", startHour=");
	    buf.append(startHour);
	    buf.append(", durationHours=");
	    buf.append(durationHours);
		buf.append("]");
		return buf.toString();
	}
}
