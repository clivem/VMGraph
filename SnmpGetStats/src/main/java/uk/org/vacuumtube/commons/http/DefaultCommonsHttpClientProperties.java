package uk.org.vacuumtube.commons.http;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author clivem
 *
 */
public class DefaultCommonsHttpClientProperties implements CommonsHttpClientProperties {

    private int connectionPoolTimeout = 0;
    private int defaultConnectionTimeout = 0;
    private int defaultSoTimeout = 0;
    private int maximumConnectionsPerHost = 20;
    private int maximumTotalConnections = 20;
    
    /**
     * 
     */
    public DefaultCommonsHttpClientProperties() {
        super();
    }

    /* (non-Javadoc)
     * @see uk.co.objectsoft.spring.CommonsHttpClientProperties#getConnectionPoolTimeout()
     */
    public int getConnectionPoolTimeout() {
        return connectionPoolTimeout;
    }

    /* (non-Javadoc)
     * @see uk.co.objectsoft.spring.CommonsHttpClientProperties#getDefaultConnectionTimeout()
     */
    public int getDefaultConnectionTimeout() {
        return defaultConnectionTimeout;
    }

    /* (non-Javadoc)
     * @see uk.co.objectsoft.spring.CommonsHttpClientProperties#getDefaultSoTimeout()
     */
    public int getDefaultSoTimeout() {
        return defaultSoTimeout;
    }

    /* (non-Javadoc)
     * @see uk.co.objectsoft.spring.CommonsHttpClientProperties#getMaximumConnectionsPerHost()
     */
    public int getMaximumConnectionsPerHost() {
        return maximumConnectionsPerHost;
    }

    /* (non-Javadoc)
     * @see uk.co.objectsoft.spring.CommonsHttpClientProperties#getMaximumTotalConnections()
     */
    public int getMaximumTotalConnections() {
        return maximumTotalConnections;
    }

    /**
     * @param connectionPoolTimeout The connectionPoolTimeout to set.
     */
    @Value("${http.properties.connectionPoolTimeout}")
    public void setConnectionPoolTimeout(int connectionPoolTimeout) {
        this.connectionPoolTimeout = connectionPoolTimeout;
    }

    /**
     * @param defaultConnectionTimeout The defaultConnectionTimeout to set.
     */
    @Value("${http.properties.defaultConnectionTimeout}")
    public void setDefaultConnectionTimeout(int defaultConnectionTimeout) {
        this.defaultConnectionTimeout = defaultConnectionTimeout;
    }

    /**
     * @param defaultSoTimeout The defaultSoTimeout to set.
     */
    @Value("${http.properties.defaultSoTimeout}")
    public void setDefaultSoTimeout(int defaultSoTimeout) {
        this.defaultSoTimeout = defaultSoTimeout;
    }

    /**
     * @param maximumConnectionsPerHost The maximumConnectionsPerHost to set.
     */
    @Value("${http.properties.maximumConnectionsPerHost}")
    public void setMaximumConnectionsPerHost(int maximumConnectionsPerHost) {
        this.maximumConnectionsPerHost = maximumConnectionsPerHost;
    }

    /**
     * @param maximumTotalConnections The maximumTotalConnections to set.
     */
    @Value("${http.properties.maximumTotalConnections}")
    public void setMaximumTotalConnections(int maximumTotalConnections) {
        this.maximumTotalConnections = maximumTotalConnections;
    }
}
