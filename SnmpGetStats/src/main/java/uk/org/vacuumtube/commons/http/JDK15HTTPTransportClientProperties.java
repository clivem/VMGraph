package uk.org.vacuumtube.commons.http;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author clivem
 *
 */
public class JDK15HTTPTransportClientProperties {

    private final static Logger logger = Logger.getLogger(Default15HTTPTransportClientProperties.class);
    
    private final static Map<URI, Proxy> proxyMap = new HashMap<URI, Proxy>();

    private JDK15HTTPTransportClientProperties() {
    }
    
    public final static synchronized String getProxyHost(URL url) {
        Proxy proxy = null;
        try {
            proxy = getProxy(url.toURI());
            if (proxy != null) {
                InetSocketAddress sa = (InetSocketAddress) proxy.address();
                return sa.getHostName();
            }
        } catch (URISyntaxException e) {}
        
        return null;
    }

    public final static synchronized String getProxyPort(URL url) {
        Proxy proxy = null;
        try {
            proxy = getProxy(url.toURI());
            if (proxy != null) {
                InetSocketAddress sa = (InetSocketAddress) proxy.address();
                return String.valueOf(sa.getPort());
            }
        } catch (URISyntaxException e) {}
        
        return null;
    }
    
    /**
     * @param uri The uri requiring proxy
     * @return The http proxy 
     */
    private static Proxy getProxy(URI uri) {
        Proxy proxy = (Proxy) proxyMap.get(uri);
        if (proxy != null) {
            return proxy;
        }
        
        ProxySelector proxySelector = ProxySelector.getDefault();
        if (proxySelector != null) {
            List<Proxy> list = proxySelector.select(uri);
            Iterator<Proxy> it = list.iterator();
            while (it.hasNext()) {
                proxy = it.next();
                if (proxy.type() == Proxy.Type.HTTP) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Caching proxy [" + proxy + "] for [" + uri + "]");
                    }
                    proxyMap.put(uri, proxy);
                    return proxy;
                }
            }
        }
        
        return null;
    }
}
