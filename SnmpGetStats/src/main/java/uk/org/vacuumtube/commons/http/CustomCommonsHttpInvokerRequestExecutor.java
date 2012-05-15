package uk.org.vacuumtube.commons.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.remoting.httpinvoker.AbstractHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration;
import org.springframework.remoting.support.RemoteInvocationResult;

/**
 * @author clivem
 *
 */
public class CustomCommonsHttpInvokerRequestExecutor extends AbstractHttpInvokerRequestExecutor {

    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
    public static final String COMPRESSION_GZIP = "gzip";

    static {
        // Allow self-signed certs 
        ProtocolFactory.registerEasySSLProtocol();
    }

    private boolean httpChunkStream = true;
    private boolean requestGzipResponse = true;
    private boolean gzipRequest = false;
    
    private boolean useExpectContinue = false;
    
    private HttpConnectionManager connectionManager;
    
    private Map<String, TransportClientProperties> transportClientPropertiesMap;
    
    /**
     * @param httpConnectionManager
     */
    public CustomCommonsHttpInvokerRequestExecutor(HttpConnectionManager httpConnectionManager, 
    		Map<String, TransportClientProperties> transportClientPropertiesMap) {
        this.connectionManager = httpConnectionManager;
        this.transportClientPropertiesMap = transportClientPropertiesMap;
    }
    
    /**
     * @return Returns the requestGzipResponse.
     */
    public boolean isRequestGzipResponse() {
        return requestGzipResponse;
    }

    /**
     * @param requestGzipResponse The requestGzipResponse to set.
     */
    @Value("${http.properties.requestGzipResponse}")
    public void setRequestGzipResponse(boolean requestGzipResponse) {
        this.requestGzipResponse = requestGzipResponse;
    }

    /**
     * @return Returns the gzipRequest.
     */
    public boolean isGzipRequest() {
        return gzipRequest;
    }

    /**
     * @param gzipRequest The gzipRequest to set.
     */
    @Value("${http.properties.gzipRequest}")
    public void setGzipRequest(boolean gzipRequest) {
        this.gzipRequest = gzipRequest;
    }

    /**
     * @return Returns the httpChunkStream.
     */
    public boolean isHttpChunkStream() {
        return httpChunkStream;
    }

    /**
     * @param httpChunkStream The httpChunkStream to set.
     */
    @Value("${http.properties.httpChunkStream}")
    public void setHttpChunkStream(boolean httpChunkStream) {
        this.httpChunkStream = httpChunkStream;
    }
    
    /**
     * Execute the given request through Commons HttpClient.
     * <p>This method implements the basic processing workflow:
     * The actual work happens in this class's template methods.
     * @see #createPostMethod
     * @see #setRequestBody
     * @see #executePostMethod
     * @see #validateResponse
     * @see #getResponseBody
     */
    protected RemoteInvocationResult doExecuteRequest(
            HttpInvokerClientConfiguration config, ByteArrayOutputStream baos)
            throws IOException, ClassNotFoundException {

        HttpClient httpClient = new HttpClient(connectionManager);
        PostMethod postMethod = createPostMethod(config);
        // postMethod.getParams().setVersion(HttpVersion.HTTP_1_1);
        try {
            URL targetURL = new URL(config.getServiceUrl());
            HostConfiguration hostConfiguration = getHostConfiguration(httpClient, targetURL); 
            setRequestBody(config, postMethod, baos);
            executePostMethod(hostConfiguration, httpClient, postMethod);
            validateResponse(config, postMethod);
            InputStream responseBody = getResponseBody(config, postMethod);
            return readRemoteInvocationResult(responseBody, config.getCodebaseUrl());
        }
        finally {
            // Need to explicitly release because it might be pooled.
            postMethod.releaseConnection();
        }
    }

    /**
     * Execute the given PostMethod instance.
     * @param config the HTTP host configuration that specifies the target service
     * @param httpClient the HttpClient to execute on
     * @param postMethod the PostMethod to execute
     * @throws IOException if thrown by I/O methods
     */
    protected void executePostMethod(HostConfiguration config, HttpClient httpClient, 
            PostMethod postMethod) throws IOException {
        httpClient.executeMethod(config, postMethod, null);
    }
    
    /**
     * Validate the given response as contained in the PostMethod object,
     * throwing an exception if it does not correspond to a successful HTTP response.
     * <p>Default implementation rejects any HTTP status code beyond 2xx, to avoid
     * parsing the response body and trying to deserialize from a corrupted stream.
     * @param config the HTTP invoker configuration that specifies the target service
     * @param postMethod the executed PostMethod to validate
     * @throws IOException if validation failed
     * @see org.apache.commons.httpclient.methods.PostMethod#getStatusCode()
     * @see org.apache.commons.httpclient.HttpException
     */
    protected void validateResponse(HttpInvokerClientConfiguration config, PostMethod postMethod)
            throws IOException {
        if (postMethod.getStatusCode() >= 300) {
            throw new HttpException(
                    "Did not receive successful HTTP response: status code = " + postMethod.getStatusCode() +
                    ", status message = [" + postMethod.getStatusText() + "]");
        }
    }

    /**
     * Set the given serialized remote invocation as request body.
     * <p>The default implementation simply sets the serialized invocation
     * as the PostMethod's request body. This can be overridden, for example,
     * to write a specific encoding and potentially set appropriate HTTP
     * request headers.
     * @param config the HTTP invoker configuration that specifies the target service
     * @param postMethod the PostMethod to set the request body on
     * @param baos the ByteArrayOutputStream that contains the serialized
     * RemoteInvocation object
     * @throws IOException if thrown by I/O methods
     * @see org.apache.commons.httpclient.methods.PostMethod#setRequestEntity
     */
    protected void setRequestBody(HttpInvokerClientConfiguration config, PostMethod postMethod, ByteArrayOutputStream baos) throws IOException {
        if (gzipRequest) {
            postMethod.setRequestEntity(new GzipMessageRequestEntity(postMethod, baos, httpChunkStream));
        } else {
            postMethod.setRequestEntity(new MessageRequestEntity(postMethod, baos, httpChunkStream));
        }
    }

    /**
     * Create a PostMethod for the given configuration.
     * <p>The default implementation creates a standard PostMethod with
     * "application/x-java-serialized-object" as "Content-Type" header.
     * @param config the HTTP invoker configuration that specifies the
     * target service
     * @return the PostMethod instance
     * @throws IOException if thrown by I/O methods
     */
    protected PostMethod createPostMethod(HttpInvokerClientConfiguration config) throws IOException {
        PostMethod postMethod = new PostMethod(config.getServiceUrl());
        postMethod.setRequestHeader(HTTP_HEADER_CONTENT_TYPE, CONTENT_TYPE_SERIALIZED_OBJECT);
        postMethod.setRequestHeader(HEADER_USER_AGENT, "Custom Commons-HttpClient/3.0");
        if (gzipRequest) {
            postMethod.setRequestHeader(HEADER_CONTENT_ENCODING, COMPRESSION_GZIP);
        }
        if (requestGzipResponse) {
            postMethod.setRequestHeader(HEADER_ACCEPT_ENCODING, COMPRESSION_GZIP);
        }
        if (useExpectContinue) {
            postMethod.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, useExpectContinue);
        }
        return postMethod;
    }

    /**
     * Extract the response body from the given executed remote invocation
     * request.
     * <p>The default implementation simply fetches the PostMethod's response
     * body stream. This can be overridden, for example, to check for GZIP
     * response encoding and wrap the returned InputStream in a GZIPInputStream.
     * @param config the HTTP invoker configuration that specifies the target service
     * @param postMethod the PostMethod to read the response body from
     * @return an InputStream for the response body
     * @throws IOException if thrown by I/O methods
     * @see org.apache.commons.httpclient.methods.PostMethod#getResponseBodyAsStream()
     * @see org.apache.commons.httpclient.methods.PostMethod#getResponseHeader(String)
     */
    protected InputStream getResponseBody(HttpInvokerClientConfiguration config, PostMethod postMethod) throws IOException {
        Header contentEncoding = postMethod.getResponseHeader(HEADER_CONTENT_ENCODING);
        if (contentEncoding != null) {
            if (contentEncoding.getValue().equalsIgnoreCase(COMPRESSION_GZIP)) {
                return new GZIPInputStream(postMethod.getResponseBodyAsStream());
            } else {
                throw new IOException("Unsupported Content-Encoding: " + contentEncoding.getValue());
            }
        }
        return postMethod.getResponseBodyAsStream();
    }
    
    protected HostConfiguration getHostConfiguration(HttpClient client, URL targetURL) throws IOException {
        HostConfiguration config = new HostConfiguration();

        int port = targetURL.getPort();
        if (port == -1) {
            if (targetURL.getProtocol().equalsIgnoreCase("https")) {
                port = 443; // default port for https being 443
            } else { // it must be http
                port = 80; // default port for http being 80
            }
        }
        
    	/*
        TransportClientProperties properties = 
        		TransportClientPropertiesFactory.create(targetURL.getProtocol()); // http or https
        */
    	TransportClientProperties properties = 
    			transportClientPropertiesMap.get(targetURL.getProtocol());
        if (properties == null) {
        	config.setHost(targetURL.getHost(), port, targetURL.getProtocol());
        	return config;
        }

        boolean hostInNonProxyList = isHostInNonProxyList(targetURL.getHost(), properties.getNonProxyHosts());
        if (hostInNonProxyList) {
            config.setHost(targetURL.getHost(), port, targetURL.getProtocol());
        } else {
            String proxyHost = properties.getProxyHost(targetURL);
            String proxyPort = properties.getProxyPort(targetURL);
            if (proxyHost.length() == 0 || proxyPort.length() == 0) {
                config.setHost(targetURL.getHost(), port, targetURL.getProtocol());
            } else {
                if (properties.getProxyUser().length() != 0) {
                    Credentials proxyCred = new UsernamePasswordCredentials(properties.getProxyUser(), 
                            properties.getProxyPassword());
                    // if the username is in the form "user\domain"
                    // then use NTCredentials instead.
                    int domainIndex = properties.getProxyUser().indexOf("\\");
                    if (domainIndex > 0) {
                        String domain = properties.getProxyUser().substring(0, domainIndex);
                        if (properties.getProxyUser().length() > domainIndex + 1) {
                            String user = properties.getProxyUser().substring(domainIndex + 1);
                            proxyCred = new NTCredentials(user, properties.getProxyPassword(), 
                                    properties.getProxyHost(targetURL), domain);
                        }
                    }
                    client.getState().setProxyCredentials(AuthScope.ANY, proxyCred);
                }
                config.setProxy(proxyHost, new Integer(proxyPort).intValue());
            }
        }

        if (properties.getAuthUsername().length() > 0) {
	        //client.getParams().setAuthenticationPreemptive(true);
	        client.getState().setCredentials(new AuthScope(targetURL.getHost(), targetURL.getPort()), 
	        		new UsernamePasswordCredentials(properties.getAuthUsername(), properties.getAuthPassword()));
        }
        		
        return config;
    }

    /**
     * Check if the specified host is in the list of non proxy hosts.
     *
     * @param host host name
     * @param nonProxyHosts string containing the list of non proxy hosts
     *
     * @return true/false
     */
    protected boolean isHostInNonProxyList(String host, String nonProxyHosts) {
        
        if ((nonProxyHosts == null) || (host == null)) {
            return false;
        }
        
        /*
         * The http.nonProxyHosts system property is a list enclosed in
         * double quotes with items separated by a vertical bar.
         */
        StringTokenizer tokenizer = new StringTokenizer(nonProxyHosts, "|\"");
        
        while (tokenizer.hasMoreTokens()) {
            String pattern = tokenizer.nextToken();
            if (match(pattern, host, false)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Matches a string against a pattern. The pattern contains two special
     * characters:
     * '*' which means zero or more characters,
     *
     * @param pattern the (non-null) pattern to match against
     * @param str     the (non-null) string that must be matched against the
     *                pattern
     * @param isCaseSensitive
     *
     * @return <code>true</code> when the string matches against the pattern,
     *         <code>false</code> otherwise.
     */
    protected static boolean match(String pattern, String str,
                                   boolean isCaseSensitive) {
        
        char[] patArr = pattern.toCharArray();
        char[] strArr = str.toCharArray();
        int patIdxStart = 0;
        int patIdxEnd = patArr.length - 1;
        int strIdxStart = 0;
        int strIdxEnd = strArr.length - 1;
        char ch;
        boolean containsStar = false;
        
        for (int i = 0; i < patArr.length; i++) {
            if (patArr[i] == '*') {
                containsStar = true;
                break;
            }
        }
        if (!containsStar) {
            
            // No '*'s, so we make a shortcut
            if (patIdxEnd != strIdxEnd) {
                return false;        // Pattern and string do not have the same size
            }
            for (int i = 0; i <= patIdxEnd; i++) {
                ch = patArr[i];
                if (isCaseSensitive && (ch != strArr[i])) {
                    return false;    // Character mismatch
                }
                if (!isCaseSensitive
                && (Character.toUpperCase(ch)
                != Character.toUpperCase(strArr[i]))) {
                    return false;    // Character mismatch
                }
            }
            return true;             // String matches against pattern
        }
        if (patIdxEnd == 0) {
            return true;    // Pattern contains only '*', which matches anything
        }
        
        // Process characters before first star
        while ((ch = patArr[patIdxStart]) != '*'
        && (strIdxStart <= strIdxEnd)) {
            if (isCaseSensitive && (ch != strArr[strIdxStart])) {
                return false;    // Character mismatch
            }
            if (!isCaseSensitive
            && (Character.toUpperCase(ch)
            != Character.toUpperCase(strArr[strIdxStart]))) {
                return false;    // Character mismatch
            }
            patIdxStart++;
            strIdxStart++;
        }
        if (strIdxStart > strIdxEnd) {
            
            // All characters in the string are used. Check if only '*'s are
            // left in the pattern. If so, we succeeded. Otherwise failure.
            for (int i = patIdxStart; i <= patIdxEnd; i++) {
                if (patArr[i] != '*') {
                    return false;
                }
            }
            return true;
        }
        
        // Process characters after last star
        while ((ch = patArr[patIdxEnd]) != '*' && (strIdxStart <= strIdxEnd)) {
            if (isCaseSensitive && (ch != strArr[strIdxEnd])) {
                return false;    // Character mismatch
            }
            if (!isCaseSensitive
            && (Character.toUpperCase(ch)
            != Character.toUpperCase(strArr[strIdxEnd]))) {
                return false;    // Character mismatch
            }
            patIdxEnd--;
            strIdxEnd--;
        }
        if (strIdxStart > strIdxEnd) {
            
            // All characters in the string are used. Check if only '*'s are
            // left in the pattern. If so, we succeeded. Otherwise failure.
            for (int i = patIdxStart; i <= patIdxEnd; i++) {
                if (patArr[i] != '*') {
                    return false;
                }
            }
            return true;
        }
        
        // process pattern between stars. padIdxStart and patIdxEnd point
        // always to a '*'.
        while ((patIdxStart != patIdxEnd) && (strIdxStart <= strIdxEnd)) {
            int patIdxTmp = -1;
            
            for (int i = patIdxStart + 1; i <= patIdxEnd; i++) {
                if (patArr[i] == '*') {
                    patIdxTmp = i;
                    break;
                }
            }
            if (patIdxTmp == patIdxStart + 1) {
                
                // Two stars next to each other, skip the first one.
                patIdxStart++;
                continue;
            }
            
            // Find the pattern between padIdxStart & padIdxTmp in str between
            // strIdxStart & strIdxEnd
            int patLength = (patIdxTmp - patIdxStart - 1);
            int strLength = (strIdxEnd - strIdxStart + 1);
            int foundIdx = -1;
            
            strLoop:
                for (int i = 0; i <= strLength - patLength; i++) {
                    for (int j = 0; j < patLength; j++) {
                        ch = patArr[patIdxStart + j + 1];
                        if (isCaseSensitive
                        && (ch != strArr[strIdxStart + i + j])) {
                            continue strLoop;
                        }
                        if (!isCaseSensitive && (Character
                        .toUpperCase(ch) != Character
                        .toUpperCase(strArr[strIdxStart + i + j]))) {
                            continue strLoop;
                        }
                    }
                    foundIdx = strIdxStart + i;
                    break;
                }
                if (foundIdx == -1) {
                    return false;
                }
                patIdxStart = patIdxTmp;
                strIdxStart = foundIdx + patLength;
        }
        
        // All characters in the string are used. Check if only '*'s are left
        // in the pattern. If so, we succeeded. Otherwise failure.
        for (int i = patIdxStart; i <= patIdxEnd; i++) {
            if (patArr[i] != '*') {
                return false;
            }
        }
        return true;
    }

    private static class MessageRequestEntity implements RequestEntity {
        
        private HttpMethodBase method;
        protected ByteArrayOutputStream message;
        boolean httpChunkStream = true; //Use HTTP chunking or not.

        public MessageRequestEntity(HttpMethodBase method, ByteArrayOutputStream message) {
            this.message = message;
            this.method = method;
        }

        public MessageRequestEntity(HttpMethodBase method, ByteArrayOutputStream message, boolean httpChunkStream) {
            this.message = message;
            this.method = method;
            this.httpChunkStream = httpChunkStream;
        }

        public boolean isRepeatable() {
            return true;
        }

        public void writeRequest(OutputStream out) throws IOException {
            message.writeTo(out);
        }

        protected boolean isContentLengthNeeded() {
            return this.method.getParams().getVersion() == HttpVersion.HTTP_1_0 || !httpChunkStream;
        }
        
        public long getContentLength() {
            if (isContentLengthNeeded()) {
                try {
                    return message.size();
                } catch (Exception e) {
                }
            } 
            return -1; /* -1 for chunked */
        }

        public String getContentType() {
            return null; // a separate header is added
        }
        
    }
    
    private static class GzipMessageRequestEntity extends MessageRequestEntity {
        
        public GzipMessageRequestEntity(HttpMethodBase method, ByteArrayOutputStream message) {
            super(method, message);
        }

        public GzipMessageRequestEntity(HttpMethodBase method, ByteArrayOutputStream message, boolean httpChunkStream) {
            super(method, message, httpChunkStream);
        }
        
        public void writeRequest(OutputStream out) throws IOException {
            if (cachedStream != null) {
                cachedStream.writeTo(out);
            } else {
                GZIPOutputStream gzStream = new GZIPOutputStream(out);
                super.writeRequest(gzStream);
                gzStream.finish();
            }
        }
        
        public long getContentLength() {
            if(isContentLengthNeeded()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    writeRequest(baos);
                    cachedStream = baos;
                    return baos.size();
                } catch (IOException e) {
                    // fall through to doing chunked.
                }
            }
            return -1; // do chunked 
        }
        
        private ByteArrayOutputStream cachedStream;
    }
}
