/**
 * 
 */
package uk.org.vacuumtube.dao.hibernate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author clivem
 *
 */
public class InPlay implements Serializable {

	private static final long serialVersionUID = 980867108751356967L;

	private String name;
	private String description;
	
	private final static Map<String, InPlay> INSTANCES_BY_NAME = new HashMap<String, InPlay>();
	
	public static final String _IN_PLAY = "IP";
	public static final String _PRE_EVENT = "PE";
	public static final String _NOT_IN_PLAY = "NI";
	
	public static final InPlay IN_PLAY = new InPlay(_IN_PLAY, "In-Play");
	public static final InPlay PRE_EVENT = new InPlay(_PRE_EVENT, "Pre-Event");
	public static final InPlay NOT_IN_PLAY = new InPlay(_NOT_IN_PLAY, "Event did not go in-play");
	
	static {
		addToMap(IN_PLAY);
		addToMap(PRE_EVENT);
		addToMap(NOT_IN_PLAY);
	}
	
	/**
	 * @param name
	 * @param description
	 */
	private InPlay(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
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
	public int hashCode() {
        return toString().hashCode();
    }
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
	    return name;
	}
	
	/**
	 * @return
	 */
	Object readResolve() {
		return getInstance(name);
	}

	/**
	 * @param id
	 * @return
	 */
	public final static InPlay getInstance(String name) {
		return INSTANCES_BY_NAME.get(name);
	}
	
	/**
	 * @param inPlay
	 */
	private final static void addToMap(InPlay inPlay) {
		INSTANCES_BY_NAME.put(inPlay.getName(), inPlay);
	}
}
