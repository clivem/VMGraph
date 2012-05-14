package uk.org.vacuumtube.commons.http;

import java.net.URL;

/**
 * @author clivem
 *
 */
public class Default15HTTPSTransportClientProperties extends DefaultHTTPSTransportClientProperties {

    /* (non-Javadoc)
     * @see uk.co.objectsoft.spring.DefaultHTTPTransportClientProperties#getProxyHost(java.net.URL)
     */
    public synchronized String getProxyHost(URL url) {
        String proxyHost = JDK15HTTPTransportClientProperties.getProxyHost(url);
        if (proxyHost != null) {
            return proxyHost;
        }
        
        return super.getProxyHost(url);
    }

    /* (non-Javadoc)
     * @see uk.co.objectsoft.spring.DefaultHTTPTransportClientProperties#getProxyPort(java.net.URL)
     */
    public synchronized String getProxyPort(URL url) {
        String proxyPort = JDK15HTTPTransportClientProperties.getProxyPort(url);
        if (proxyPort != null) {
            return proxyPort;
        }
        
        return super.getProxyPort(url);
    }    
}
