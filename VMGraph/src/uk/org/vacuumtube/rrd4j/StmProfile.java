package uk.org.vacuumtube.rrd4j;

import uk.org.vacuumtube.rrd4j.Profile.Direction;

/**
 * @author clivem
 *
 */
public class StmProfile {

	private Direction direction;
	private int limitMB;
	private int limitReductionPercentage;
	private int limitReductionHours;
	
	/**
	 * @param direction
	 * @param limitMB
	 * @param limitReductionPercentage
	 * @param limitReductionHours
	 */
	public StmProfile(Direction direction, int limitMB, int limitReductionPercentage, int limitReductionHours) {
		this.direction = direction;
		//this.startHour = startHour;
		//this.durationHours = durationHours;
		this.limitMB = limitMB;
		this.limitReductionPercentage = limitReductionPercentage;
		this.limitReductionHours = limitReductionHours;
	}
	
	/**
	 * @return the direction
	 */
	public Direction getDirection() {
		return direction;
	}
	
	/**
	 * @param direction the direction to set
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	/**
	 * @return the limitMB
	 */
	public int getLimitMB() {
		return limitMB;
	}
	
	/**
	 * @param limitMB the limitMB to set
	 */
	public void setLimitMB(int limitMB) {
		this.limitMB = limitMB;
	}
	
	/**
	 * @return the limitReductionPercentage
	 */
	public int getLimitReductionPercentage() {
		return limitReductionPercentage;
	}
	
	/**
	 * @param limitReductionPercentage the limitReductionPercentage to set
	 */
	public void setLimitReductionPercentage(int limitReductionPercentage) {
		this.limitReductionPercentage = limitReductionPercentage;
	}
	
	/**
	 * @return the limitReductionHours
	 */
	public int getLimitReductionHours() {
		return limitReductionHours;
	}
	
	/**
	 * @param limitReductionHours the limitReductionHours to set
	 */
	public void setLimitReductionHours(int limitReductionHours) {
		this.limitReductionHours = limitReductionHours;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("StmProfile[");
	    buf.append("direction=");
	    buf.append(direction);
	    buf.append(", limitMB=");
	    buf.append(limitMB);
	    buf.append(", limitReductionPercentage=");
	    buf.append(limitReductionPercentage);
	    buf.append(", limitReductionHours=");
	    buf.append(limitReductionHours);
		buf.append("]");
		return buf.toString();
	}
}
