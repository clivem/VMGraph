package uk.org.vacuumtube.commons.http;

import java.net.URL;

/**
 * @author clivem
 *
 */
public class DefaultHTTPSTransportClientProperties extends DefaultHTTPTransportClientProperties {

    private final static String HTTPS_HOST_PROPERTY_NAME = "https.proxyHost";
    private final static String HTTPS_PORT_PROPERTY_NAME = "https.proxyPort";
    private final static String HTTPS_USER_PROPERTY_NAME = "https.proxyUser";
    private final static String HTTPS_PASSWORD_PROPERTY_NAME = "https.proxyPassword";
    private final static String HTTPS_NON_PROXY_HOSTS_PROPERTY_NAME = "https.nonProxyHosts";

    /* (non-Javadoc)
     * @see uk.co.objectsoft.spring.TransportClientProperties#getProxyHost(java.lang.String)
     */
    public String getProxyHost(URL url) {
        if (proxyHost == null) {
            proxyHost = System.getProperty(HTTPS_HOST_PROPERTY_NAME, null);
            super.getProxyHost(url);
        }
        return proxyHost;
    }

    /* (non-Javadoc)
     * @see uk.co.objectsoft.spring.TransportClientProperties#getNonProxyHosts()
     */
    public String getNonProxyHosts() {
        if (nonProxyHosts == null) {
            nonProxyHosts = System.getProperty(HTTPS_NON_PROXY_HOSTS_PROPERTY_NAME, null);
            super.getNonProxyHosts();
        }
        return nonProxyHosts;
    }

    /* (non-Javadoc)
     * @see uk.co.objectsoft.spring.TransportClientProperties#getProxyPort(java.lang.String)
     */
    public String getProxyPort(URL url) {
        if (proxyPort == null) {
            proxyPort = System.getProperty(HTTPS_PORT_PROPERTY_NAME, null);
            super.getProxyPort(url);
        }
        return proxyPort;
    }

    /* (non-Javadoc)
     * @see uk.co.objectsoft.spring.TransportClientProperties#getProxyUser()
     */
    public String getProxyUser() {
        if (proxyUser == null) {
            proxyUser = System.getProperty(HTTPS_USER_PROPERTY_NAME, null);
            super.getProxyUser();
        }
        return proxyUser;
    }

    /* (non-Javadoc)
     * @see uk.co.objectsoft.spring.TransportClientProperties#getProxyPassword()
     */
    public String getProxyPassword() {
        if (proxyPassword == null) {
            proxyPassword = System.getProperty(HTTPS_PASSWORD_PROPERTY_NAME, null);
            super.getProxyPassword();
        }
        return proxyPassword;
    }
}
