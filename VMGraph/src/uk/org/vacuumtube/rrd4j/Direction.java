/**
 * 
 */
package uk.org.vacuumtube.rrd4j;

/**
 * @author clivem
 *
 */
public enum Direction {
	
	DOWN("Down", "Download"), UP("Up", "Upload");

	private String description;
	private String shortDescription;
	
	/**
	 * @param shortDescription
	 * @param description
	 */
	private Direction(String shortDescription, String description) {
		this.shortDescription = shortDescription;
		this.description = description;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the short description
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return shortDescription.toUpperCase();
	}
};
