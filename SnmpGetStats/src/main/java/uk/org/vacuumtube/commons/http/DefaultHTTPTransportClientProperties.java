package uk.org.vacuumtube.commons.http;

import java.net.URL;

/**
 * @author clivem
 *
 */
public class DefaultHTTPTransportClientProperties implements TransportClientProperties {

    private static final String EMPTY_STRING = "";
    
    private final static String HTTP_HOST_PROPERTY_NAME = "http.proxyHost";
    private final static String HTTP_PORT_PROPERTY_NAME = "http.proxyPort";
    private final static String HTTP_USER_PROPERTY_NAME = "http.proxyUser";
    private final static String HTTP_PASSWORD_PROPERTY_NAME = "http.proxyPassword";
    private final static String HTTP_NON_PROXY_HOSTS_PROPERTY_NAME = "http.nonProxyHosts";

    protected String proxyHost = null;
    protected String nonProxyHosts = null;
    protected String proxyPort = null;
    protected String proxyUser = null;
    protected String proxyPassword = null;

    /* (non-Javadoc)
     * @see uk.co.objectsoft.spring.TransportClientProperties#getProxyHost(java.lang.String)
     */
    public String getProxyHost(URL url) {
        if (proxyHost == null) {
            proxyHost = System.getProperty(HTTP_HOST_PROPERTY_NAME, null);
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
            nonProxyHosts = System.getProperty(HTTP_NON_PROXY_HOSTS_PROPERTY_NAME, null);
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
            proxyPort = System.getProperty(HTTP_PORT_PROPERTY_NAME, null);
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
            proxyUser = System.getProperty(HTTP_USER_PROPERTY_NAME, null);
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
            proxyPassword = System.getProperty(HTTP_PASSWORD_PROPERTY_NAME, null);
            if (proxyPassword == null) {
                proxyPassword = EMPTY_STRING;
            }
        }
        return proxyPassword;
    }
}
