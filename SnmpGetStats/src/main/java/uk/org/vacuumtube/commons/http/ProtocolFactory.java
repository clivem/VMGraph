package uk.org.vacuumtube.commons.http;

import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

/**
 * @author clivem
 *
 */
public class ProtocolFactory {

    public final static Protocol EASY_HTTPS_PROTOCOL = 
        new Protocol("https", (ProtocolSocketFactory) new EasySSLProtocolSocketFactory(), 443);
    
    private ProtocolFactory() {
    }

    public final static void registerEasySSLProtocol() {
        Protocol protocol = Protocol.getProtocol("https");
        if (!protocol.equals(EASY_HTTPS_PROTOCOL)) {
            Protocol.registerProtocol("https", EASY_HTTPS_PROTOCOL);
        }
    }
}
