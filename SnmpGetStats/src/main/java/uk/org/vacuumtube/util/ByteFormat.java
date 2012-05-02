/**
 * 
 */
package uk.org.vacuumtube.util;

import java.text.DecimalFormat;

/**
 * @author clivem
 *
 */
public class ByteFormat {

	private static final long K = 1024;
	private static final long M = K * K;
	private static final long G = M * K;
	private static final long T = G * K;
	
	/**
	 * 
	 */
	private ByteFormat() {}

	/**
	 * @param value
	 * @return
	 */
	public static String convertToStringRepresentation(final long value){
	    final long[] dividers = new long[] { T, G, M, K, 1 };
	    final String[] units = new String[] { "TB", "GB", "MB", "KB", "B" };
	    if(value < 1)
	        throw new IllegalArgumentException("Invalid file size: " + value);
	    String result = null;
	    for(int i = 0; i < dividers.length; i++){
	        final long divider = dividers[i];
	        if(value >= divider){
	            result = format(value, divider, units[i]);
	            break;
	        }
	    }
	    return result;
	}

	/**
	 * @param value
	 * @param divider
	 * @param unit
	 * @return
	 */
	private static String format(final long value, final long divider, final String unit){
	    final double result =
	        divider > 1 ? (double) value / (double) divider : (double) value;
	    return new DecimalFormat("#,##0.#").format(result) + " " + unit;
	}
	
	/**
	 * @param bytes
	 * @param si
	 * @return
	 */
	public static String humanReadableByteCount(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.2f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
	/**
	 * @param bits
	 * @return
	 */
	public static String humanReadableBitCount(long bits) {
	    int unit = 1000;
	    if (bits < unit) return bits + " bps";
	    int exp = (int) (Math.log(bits) / Math.log(unit));
	    String pre = "kMGTPE".charAt(exp - 1) + "";
	    return String.format("%.2f %sbps", bits / Math.pow(unit, exp), pre);
	}
}
