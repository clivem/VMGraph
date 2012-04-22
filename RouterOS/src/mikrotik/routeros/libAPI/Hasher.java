package mikrotik.routeros.libAPI;

/*
 * Helper.java
 *
 * Created on 08 June 2007, 11:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * 
 * @author janisk
 */
public class Hasher {

	private final static Logger logger = Logger.getLogger(Hasher.class);
	
	/**
	 * makes MD5 hash of string for use with RouterOS API
	 * 
	 * @param s
	 *            - variable to make hacsh from
	 * @return
	 */
	static public String hashMD5(String s) {
		//String md5val = "";
		MessageDigest algorithm = null;
		try {
			algorithm = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsae) {
			//System.out.println("Cannot find digest algorithm");
			logger.log(Level.ERROR, "Cannot find digest algorithm!", nsae);
			System.exit(1);
		}
		byte[] defaultBytes = new byte[s.length()];
		for (int i = 0; i < s.length(); i++) {
			defaultBytes[i] = (byte) (0xFF & s.charAt(i));
		}
		algorithm.reset();
		algorithm.update(defaultBytes);
		byte messageDigest[] = algorithm.digest();
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < messageDigest.length; i++) {
			String hex = Integer.toHexString(0xFF & messageDigest[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	/**
	 * converts hex value string to normal strint for use with RouterOS API
	 * 
	 * @param s
	 *            - hex string to convert to
	 * @return - converted string.
	 */
	static public String hexStrToStr(String s) {
		String ret = "";
		for (int i = 0; i < s.length(); i += 2) {
			ret += (char) Integer.parseInt(s.substring(i, i + 2), 16);
		}
		return ret;
	}
}
