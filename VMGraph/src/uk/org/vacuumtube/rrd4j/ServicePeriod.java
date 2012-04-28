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
public class ServicePeriod {

	private String servicePeriodName;
	private int startHour;
	private int durationHours;
	
	private Map<Direction, StmProfile> stmProfileMap;

	/**
	 * @param servicePeriodName
	 * @param startHour
	 * @param durationHours
	 * @param stmProfileMap
	 */
	public ServicePeriod(String servicePeriodName, int startHour,
			int durationHours, Map<Direction, StmProfile> stmProfileMap) {
		this.servicePeriodName = servicePeriodName;
		this.startHour = startHour;
		this.durationHours = durationHours;
		this.stmProfileMap = stmProfileMap;
	}

	/**
	 * @return the servicePeriodName
	 */
	public String getServicePeriodName() {
		return servicePeriodName;
	}

	/**
	 * @return the startHour
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
	 * @return the durationHours
	 */
	public int getDurationHours() {
		return durationHours;
	}

	/**
	 * @return the stmProfileMap
	 */
	public Map<Direction, StmProfile> getStmProfileMap() {
		return stmProfileMap;
	}	
	
    /**
     * @param direction
     * @return the STM profile for the given direction
     */
    public StmProfile getStmProfile(Direction direction) {
    	return stmProfileMap.get(direction);
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
			return p.getLimitMB() * Service.KILO_MULT * Service.KILO_MULT; 
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
	 * @param direction
	 * @param connectionSpeedBps
	 * @return the connection speed after STM is applied
	 */
	public long getConnectionSpeedBpsAfterSTM(Direction direction, long connectionSpeedBps) {
		return (long) (connectionSpeedBps *  ((100 - getLimitReductionPercentage(direction)) / 100d));
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

	/**
	 * @param serviceStmPeriods
	 * @return a Map containing the ServicePeriod[] keyed by servicePeriodName.
	 */
	public static final Map<String, ServicePeriod> createMap(ServicePeriod[] serviceStmPeriods) {
		TreeMap<String, ServicePeriod> map = new TreeMap<String, ServicePeriod>();
		for (ServicePeriod period : serviceStmPeriods) {
			map.put(period.getServicePeriodName(), period);
		}
		return map;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("ServicePeriod[servicePeriodName=");
		buf.append(servicePeriodName);
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
