package uk.org.vacuumtube.commons.http;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;


/**
 * @author clivem
 *
 */
public class MultiThreadedHttpConnectionManagerFactory {

    /**
     * 
     */
    private MultiThreadedHttpConnectionManagerFactory() {
    }

    public final static synchronized MultiThreadedHttpConnectionManager create(
            CommonsHttpClientProperties clientProperties) {
    	MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(clientProperties.getMaximumConnectionsPerHost());
        connectionManager.getParams().setMaxTotalConnections(clientProperties.getMaximumTotalConnections());
        if (clientProperties.getDefaultConnectionTimeout() > 0) {
            connectionManager.getParams().setConnectionTimeout(clientProperties.getDefaultConnectionTimeout());
        }
        if (clientProperties.getDefaultSoTimeout() > 0) {
            connectionManager.getParams().setSoTimeout(clientProperties.getDefaultSoTimeout());
        }
        return connectionManager;
    }
}
