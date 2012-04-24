/**
 * 
 */
package uk.org.vacuumtube.util;

/**
 * @author clivem
 *
 */
public class Version {

	/**
	 * 
	 */
	private Version() {
	}
	
	/**
	 * @return
	 */
	public final static boolean isGreater(float version) {
		String sVersion = System.getProperty("java.version");
		sVersion = sVersion.substring(0, 3);
		Float f = Float.valueOf(sVersion);
		if (f.floatValue() > version) {
			return true;
		}
		return false;
	}
	
}
