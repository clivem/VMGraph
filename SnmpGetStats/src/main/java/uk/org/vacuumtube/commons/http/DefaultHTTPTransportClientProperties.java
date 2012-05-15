package uk.org.vacuumtube.commons.http;

import java.net.URL;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author clivem
 *
 */
public class DefaultHTTPTransportClientProperties implements TransportClientProperties {

	//private final static Logger LOGGER = Logger.getLogger(DefaultHTTPTransportClientProperties.class);

	@Value("#{clientProperties}")
	private Properties properties;
	
    private static final String EMPTY_STRING = "";
    
    private final static String HTTP_HOST_PROPERTY_NAME = "http.proxyHost";
    private final static String HTTP_PORT_PROPERTY_NAME = "http.proxyPort";
    private final static String HTTP_USER_PROPERTY_NAME = "http.proxyUser";
    private final static String HTTP_PASSWORD_PROPERTY_NAME = "http.proxyPassword";
    private final static String HTTP_NON_PROXY_HOSTS_PROPERTY_NAME = "http.nonProxyHosts";
    private final static String HTTP_AUTH_USERNAME_PROPERTY_NAME = "http.authUsername";
    private final static String HTTP_AUTH_PASSWORD_PROPERTY_NAME = "http.authPassword";

    protected String proxyHost = null;
    protected String nonProxyHosts = null;
    protected String proxyPort = null;
    protected String proxyUser = null;
    protected String proxyPassword = null;
    protected String authUsername = null;
    protected String authPassword = null;

    /* (non-Javadoc)
     * @see uk.co.objectsoft.spring.TransportClientProperties#getProxyHost(java.lang.String)
     */
    public String getProxyHost(URL url) {
        if (proxyHost == null) {
            proxyHost = getProperty(HTTP_HOST_PROPERTY_NAME);
            if (proxyHost == null) {
                proxyHost = EMPTY_STRING;
            }
        }
        return proxyHost;
    }

    /* (non-Javadoc)
     * @see uk.co.objectsoft.spring.TransportClientProperties#getNonProxyHosts()
     */
    public String getNonProxyHosts() {
        if (nonProxyHosts == null) {
            nonProxyHosts = getProperty(HTTP_NON_PROXY_HOSTS_PROPERTY_NAME);
            if (nonProxyHosts == null) {
                nonProxyHosts = EMPTY_STRING;
            }
        }
        return nonProxyHosts;
    }

    /* (non-Javadoc)
     * @see uk.co.objectsoft.spring.TransportClientProperties#getProxyPort(java.lang.String)
     */
    public String getProxyPort(URL url) {
        if (proxyPort == null) {
            proxyPort = getProperty(HTTP_PORT_PROPERTY_NAME);
            if (proxyPort == null) {
                proxyPort = EMPTY_STRING;
            }
        }
        return proxyPort;
    }

    /* (non-Javadoc)
     * @see uk.co.objectsoft.spring.TransportClientProperties#getProxyUser()
     */
    public String getProxyUser() {
        if (proxyUser == null) {
            proxyUser = getProperty(HTTP_USER_PROPERTY_NAME);
            if (proxyUser == null) {
                proxyUser = EMPTY_STRING;
            }
        }
        return proxyUser;
    }

    /* (non-Javadoc)
     * @see uk.co.objectsoft.spring.TransportClientProperties#getProxyPassword()
     */
    public String getProxyPassword() {
        if (proxyPassword == null) {
            proxyPassword = getProperty(HTTP_PASSWORD_PROPERTY_NAME);
            if (proxyPassword == null) {
                proxyPassword = EMPTY_STRING;
            }
        }
        return proxyPassword;
    }

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.commons.http.TransportClientProperties#getAuthUsername()
	 */
	@Override
	public String getAuthUsername() {
        if (authUsername == null) {
            authUsername = getProperty(HTTP_AUTH_USERNAME_PROPERTY_NAME);
            if (authUsername == null) {
                authUsername = EMPTY_STRING;
            }
        }
        return authUsername;
	}

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.commons.http.TransportClientProperties#getAuthPassword()
	 */
	@Override
	public String getAuthPassword() {
        if (authPassword == null) {
            authPassword = getProperty(HTTP_AUTH_PASSWORD_PROPERTY_NAME);
            if (authPassword == null) {
                authPassword = EMPTY_STRING;
            }
        }
        return authPassword;
	}
	
	/**
	 * @param key
	 * @return
	 */
	private String getProperty(String key) {
		String value = null;
		if (properties != null) {
			value = properties.getProperty(key);
		}
		
		if (value == null) {
			value = System.getProperty(key);
		}
		
		return value;
	}
}
