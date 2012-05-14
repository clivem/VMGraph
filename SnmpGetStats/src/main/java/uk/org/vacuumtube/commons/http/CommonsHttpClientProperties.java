package uk.org.vacuumtube.commons.http;

/**
 * @author clivem
 *
 */
public interface CommonsHttpClientProperties {

    /**
     * Used to set the maximum number of connections that the pool can open
     * for all hosts.  Since connections imply sockets and sockets imply
     * file descriptors, the setting you use must not exceed any limits
     * your system imposes on the number of open file descriptors a
     * single process may have.
     *
     * @return an integer > 1
     */
    public int getMaximumTotalConnections();

    /**
     * Used to set the maximum number of connections that will be pooled
     * for a given host.  This setting is also constrained by 
     * the one returned from getMaximumTotalConnections.
     *
     * @return an integer > 1
     */
    public int getMaximumConnectionsPerHost();    

    /**
     * Used to set the amount of time, in milliseconds, spent waiting
     * for an available connection from the pool.  An exception is raised
     * if the timeout is triggered.
     *
     * @return an integer > 1 OR 0 for infinite timeout
     */
    public int getConnectionPoolTimeout();

    /**
     * Used to set the default amount of time, in milliseconds, spent waiting
     * for a connection. 
     *   
     * @return an integer >= 0
     */
    public int getDefaultConnectionTimeout();

    /**
     * Used to set the default amount of time, in milliseconds, spent waiting
     * for a reponse. 
     *
     * @return an integer >= 0
     */
    public int getDefaultSoTimeout();
}
