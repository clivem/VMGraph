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
	
	private boolean writtenFooter = false;
	
	/**
	 * @param title
	 * @param fileName
	 */
	public HtmlFile(String title, String fileName) {
		this.title = title;
		this.fileName = fileName;
		contentBuf = new ArrayList<String>();
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
		append("<title>" + title + "</title>");
		append("</head>");
		append("<body>");
		append("<center>");
		append("<h2>" + title + "</h2>");
		append("<br />");
	}
	
	/**
	 * 
	 */
	private void writeFooter() {
		if (!writtenFooter) {
			append("</center>");
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
