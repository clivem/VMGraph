/**
 * 
 */
package uk.org.vacuumtube.http;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
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
		List<String> encodingList = headers.get("Content-encoding");
		for (String encoding : encodingList) {
			if ("gzip".equals(encoding)) {
				LOGGER.info("Request is gzip encoded. Wrapping with GZIPInputStream.");
				return new GZIPInputStream(is);
			}
		}

		return super.decorateInputStream(exchange, is);
	}

	/**
	 * Write the given RemoteInvocationResult to the given HTTP response.
	 * @param exchange current HTTP request/response
	 * @param result the RemoteInvocationResult object
	 * @throws java.io.IOException in case of I/O failure
	 */
	protected void writeRemoteInvocationResult(HttpExchange exchange, RemoteInvocationResult result)
			throws IOException {

		boolean gzip = false;
		Headers headers = exchange.getRequestHeaders();
		List<String> encodingList = headers.get("Accept-encoding");
		for (String encoding : encodingList) {
			if ("gzip".equals(encoding)) {
				LOGGER.info("Client requests gzip encoding for response. Wrapping with GZIPOutputStream.");
				gzip = true;
				break;
			}
		}

		if (gzip) {
			exchange.getResponseHeaders().set("Content-Encoding", "gzip");
		}
		exchange.getResponseHeaders().set("Content-Type", getContentType());
		exchange.sendResponseHeaders(200, 0);
		write(exchange, result, gzip);
	}
	
	protected void write(HttpExchange exchange, RemoteInvocationResult result, boolean gzip) 
			throws IOException {
		ObjectOutputStream oos = null;
		GZIPOutputStream gos = null;
		if(gzip) {
			gos = new GZIPOutputStream(exchange.getResponseBody());
			oos = new ObjectOutputStream(new BufferedOutputStream(gos));
		} else {
			oos = new ObjectOutputStream(exchange.getResponseBody());
		}
		oos.writeObject(result);
		oos.flush();
		if (gos != null) {
			gos.finish();
		}
	}
}
