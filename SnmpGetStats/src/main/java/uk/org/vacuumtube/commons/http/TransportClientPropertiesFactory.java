package uk.org.vacuumtube.commons.http;

import java.util.HashMap;
import java.util.Map;

/**
 * @author clivem
 *
 */
public class TransportClientPropertiesFactory {

    private static Map<String, TransportClientProperties> TRANSPORT_CLIENT_PROPERTIES_MAP = 
    		new HashMap<String, TransportClientProperties>();
    
    public synchronized static final TransportClientProperties create(String protocol) {
        TransportClientProperties tcp = TRANSPORT_CLIENT_PROPERTIES_MAP.get(protocol);
        if (tcp == null) {
            String javaVers = System.getProperty("java.version");
            if (javaVers.startsWith("1.5"))  {
                if ("http".equals(protocol)) {
                    tcp = new Default15HTTPTransportClientProperties();
                } else if ("https".equals(protocol)) {
                    tcp = new Default15HTTPSTransportClientProperties();
                }
            } else {
                if ("http".equals(protocol)) {
                    tcp = new DefaultHTTPTransportClientProperties();
                } else if ("https".equals(protocol)) {
                    tcp = new DefaultHTTPSTransportClientProperties();
                }
            }
            if (tcp != null) {
                TRANSPORT_CLIENT_PROPERTIES_MAP.put(protocol, tcp);
            }
        }
        
        return tcp;
    }
}
