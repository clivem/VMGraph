/**
 * 
 */
package uk.org.vacuumtube.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author clivem
 *
 */
public class Format {

	private final static DateFormat DF_FULL = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss,SSS z");
	
	/**
	 * 
	 */
	private Format() {}
	
	public final static String formatDateFull(final Date date) {
		if (date == null) {
			return "null";
		}
		synchronized (DF_FULL) {
			return DF_FULL.format(date);
		}
	}
}
