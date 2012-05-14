/**
 * 
 */
package uk.org.vacuumtube.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

/**
 * @author clivem
 * 
 */
public class DateFormatFactory {

	public final static String DATE_TIME = "yyyy/MM/dd HH:mm:ss";
	public final static String DATE = "yyyy/MM/dd";
	public final static String TIME = "HH:mm:ss";
	public final static String DATE_TIME_MILLIS = "yy/MM/dd HH:mm:ss,SSS";
	public final static String DF_FULL = "yyyy/MM/dd HH:mm:ss,SSS z";

	private final static String[] LIST = new String[] { 
		DATE_TIME, DATE, TIME, DATE_TIME_MILLIS, DF_FULL };

	private static Hashtable<String, DateFormat> map;

	static {
		map = new Hashtable<String, DateFormat>(LIST.length, 1);
		for (int i = 0; i < LIST.length; i++) {
			map.put(LIST[i], new SimpleDateFormat(LIST[i]));
		}
	}
	
	/**
	 * 
	 */
	private DateFormatFactory() {
	}

	/**
	 * @param format
	 * @param value
	 * @return a date representation of the string specified by format
	 * @throws ParseException
	 */
	public final static Date parse(String format, String value)
			throws ParseException {
		DateFormat date_format = map.get(format);
		if (date_format != null) {
			synchronized (date_format) {
				return date_format.parse(value);
			}
		}

		date_format = new SimpleDateFormat(format);
		return date_format.parse(value);
	}

	/**
	 * @param format
	 * @param value
	 * @return a string representation of the date in the specified format
	 */
	public final static String format(String format, Date value) {
		if (value != null) {
			DateFormat date_format = map.get(format);
			if (date_format != null) {
				synchronized (date_format) {
					return date_format.format(value);
				}
			}
	
			date_format = new SimpleDateFormat(format);
			return date_format.format(value);
		} else {
			return "NULL";
		}
	}
}