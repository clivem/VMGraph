/**
 * 
 */
package uk.org.vacuumtube.http;

import org.apache.log4j.Logger;

import com.sun.net.httpserver.BasicAuthenticator;

/**
 * @author clivem
 *
 */
public class RealmAuthenticator extends BasicAuthenticator {

	private final static Logger LOGGER = Logger.getLogger(RealmAuthenticator.class);
	
	public RealmAuthenticator(String realm) {
		super(realm);
	}
	
	/* (non-Javadoc)
	 * @see com.sun.net.httpserver.BasicAuthenticator#checkCredentials(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkCredentials(String username, String password) {
		LOGGER.info("checkCredentials(username=" + username + ", password=" + password + ")");
		return true;
	}
}
