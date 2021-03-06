/**
 * 
 */
package uk.org.vacuumtube.rrd4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author clivem
 *
 */
public class HtmlFile {
	
	private final static Logger LOGGER = Logger.getLogger(HtmlFile.class);

	private List<String> contentBuf = null;
	private String title = null;
	private String fileName = null;
	private Service service = null;
	
	private boolean writtenFooter = false;
	
	/**
	 * @param title
	 * @param fileName
	 */
	public HtmlFile(String title, String fileName, Service service) {
		this.title = title;
		this.fileName = fileName;
		contentBuf = new ArrayList<String>();
		this.service = service;
		writeHeader();
	}
	
	/**
	 * @param content
	 */
	public void append(String content) {
		//contentBuf.append(content);
		contentBuf.add(content);
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * 
	 */
	private void writeHeader() {
		append("<!DOCTYPE html>");
		append("<html>");
		append("<head>");
		append("<link rel='stylesheet' type='text/css' media='all' href='css/all.css' />");
		append("<title>" + title + "</title>");
		append("</head>");
		append("<body>");
		append("<div class='div_main'>");
		append("<h2>" + title + "</h2>");
		append("<h3>" + service.getServiceDescription() + "</h3>");
		//append("<p>&nbsp;</p>");
	}
	
	/**
	 * @param period
	 * @param graphTitle
	 * @param graphFileTitle
	 * @param width
	 * @param height
	 */
	public void writeGraph(ServicePeriod period, String graphTitle, String graphFileTitle, int width, int height) {
    	append("<div class='div_graph'>");
    	append("<h4>" + period.getServicePeriodDescription() + "</h4>");
    	append("<p><img src='" + graphFileTitle + ".png' alt='" + graphTitle + 
    			"' width='" + width + 
    			"' height='" + height + "' />");
    	for (StmProfile profile : period.getStmProfileMapValues()) {
    		if (profile.getLimitMB() > 0) {
    			append("<p class='strong'>" + profile.getStmProfileDescription() + "</p>");
    		}
    	}
    	append("</p></div><br/>");
	}
	
	/**
	 * 
	 */
	private void writeFooter() {
		if (!writtenFooter) {
			append("</div>");
			append("</body>");
			append("</html>");
			writtenFooter = true;
		}
	}

	/**
	 * 
	 */
	public void write() throws IOException {
		if (!writtenFooter) {
			writeFooter();
		}

		LOGGER.info("Writing html page: " + fileName);
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName);
			for (String content : contentBuf) {
				writer.println(content);
			}
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
}
