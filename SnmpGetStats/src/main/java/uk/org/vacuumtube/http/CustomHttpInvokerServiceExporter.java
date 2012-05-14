/**
 * 
 */
package uk.org.vacuumtube.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;
import org.springframework.remoting.httpinvoker.SimpleHttpInvokerServiceExporter;
import org.springframework.remoting.support.RemoteInvocationResult;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

/**
 * @author clivem
 *
 */
public class CustomHttpInvokerServiceExporter extends SimpleHttpInvokerServiceExporter {

	private final static Logger LOGGER = Logger.getLogger(CustomHttpInvokerServiceExporter.class);
	
	/* (non-Javadoc)
	 * @see org.springframework.remoting.httpinvoker.SimpleHttpInvokerServiceExporter#decorateInputStream(com.sun.net.httpserver.HttpExchange, java.io.InputStream)
	 */
	@Override
	protected InputStream decorateInputStream(HttpExchange exchange,
			InputStream is) throws IOException {
		
		Headers headers = exchange.getRequestHeaders();
		List<String> encodingList = headers.get("Content-Encoding");
		if (encodingList != null) {
			for (String encoding : encodingList) {
				if ("gzip".equals(encoding)) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("HTTP request is gzip encoded. Decoding with GZIPInputStream.");
					}
					return new GZIPInputStream(is);
				}
			}
		}

		return is;
	}

	/**
	 * @param exchange
	 * @return
	 */
	protected boolean replyWithGzipEncoding(HttpExchange exchange) {
		
		Headers headers = exchange.getRequestHeaders();
		List<String> encodingList = headers.get("Accept-Encoding");
		if (encodingList != null) {
			for (String encoding : encodingList) {
				if ("gzip".equals(encoding)) {
					return true;
				}
			}		
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.remoting.httpinvoker.SimpleHttpInvokerServiceExporter#writeRemoteInvocationResult(com.sun.net.httpserver.HttpExchange, org.springframework.remoting.support.RemoteInvocationResult)
	 */
	@Override
	protected void writeRemoteInvocationResult(HttpExchange exchange,
			RemoteInvocationResult result) throws IOException {

		if (replyWithGzipEncoding(exchange)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Client accepts gzip encoding for HTTP response. Encoding with GZIPOutputStream.");
			}
			exchange.getResponseHeaders().set("Content-Encoding", "gzip");
		}
		super.writeRemoteInvocationResult(exchange, result);
	}

	/* (non-Javadoc)
	 * @see org.springframework.remoting.httpinvoker.SimpleHttpInvokerServiceExporter#writeRemoteInvocationResult(com.sun.net.httpserver.HttpExchange, org.springframework.remoting.support.RemoteInvocationResult, java.io.OutputStream)
	 */
	@Override
	protected void writeRemoteInvocationResult(HttpExchange exchange,
			RemoteInvocationResult result, OutputStream os) throws IOException {

		OutputStream wrappedStream = decorateOutputStream(exchange, os);
		ObjectOutputStream oos = createObjectOutputStream(wrappedStream);
		doWriteRemoteInvocationResult(result, oos);
		oos.flush();
		if (wrappedStream instanceof GZIPOutputStream) {
			((GZIPOutputStream) wrappedStream).finish();
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.remoting.httpinvoker.SimpleHttpInvokerServiceExporter#decorateOutputStream(com.sun.net.httpserver.HttpExchange, java.io.OutputStream)
	 */
	@Override
	protected OutputStream decorateOutputStream(HttpExchange exchange,
			OutputStream os) throws IOException {
		
		if (replyWithGzipEncoding(exchange)) {
			return new GZIPOutputStream(os);
		} else {
			return os;
		}
	}	
}
