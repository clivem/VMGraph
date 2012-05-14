package uk.org.vacuumtube.commons.http;

import java.net.URL;

/**
 * @author clivem
 *
 */
public interface TransportClientProperties {

    /**
     * Returns a valid String, may be empty ("").
     */
    public String getProxyHost(URL url);

    /**
     * Returns a valid String, may be empty ("").
     */
    public String getNonProxyHosts();

    /**
     * Returns a valid String, may be empty ("").
     */
    public String getProxyPort(URL url);

    /**
     * Returns a valid String, may be empty ("").
     */
    public String getProxyUser();

    /**
     * Returns a valid String, may be empty ("").
     */
    public String getProxyPassword();
}
