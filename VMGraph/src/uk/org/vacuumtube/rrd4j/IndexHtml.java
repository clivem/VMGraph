/**
 * 
 */
package uk.org.vacuumtube.rrd4j;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author clivem
 *
 */
public class IndexHtml {

	private final static Logger LOGGER = Logger.getLogger(IndexHtml.class);
	
	private final static String INDEX_HTML = "index.html";
	
	private List<String> contentList;
	private File directory;
	
	/**
	 * @param directory
	 */
	public IndexHtml(File directory) {
		this.directory = directory;
		this.contentList = new ArrayList<String>();
	}
	
	/**
	 * 
	 */
	public void createIndexHtml() {
		if (!directory.exists() || !directory.isDirectory()) {
			LOGGER.warn("Directory [" + directory.getAbsolutePath() + "] does not exist or is not a directory!");
			return;
		}
		
		String[] dailyHtmlFileNames = directory.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (dir.getAbsolutePath().equals(directory.getAbsolutePath()) && 
						(name.startsWith("VM") && name.endsWith(".html"))) {
					return true;
				}
				return false;
			}
		});
		
		writeHeader();
		for (String fileName : dailyHtmlFileNames) {
			//LOGGER.info(fileName);
			append("<p><a href='" + fileName + "' title='" + fileName + "'>" + fileName + "</a></p>");
		}
		writeFooter();
		
		try {
			write();
		} catch (IOException ioe) {
			LOGGER.warn("Error writing html file!" , ioe);
		}
	}
	
	/**
	 * 
	 */
	public void write() throws IOException {
		String fileName = directory.getAbsolutePath() + File.separator + INDEX_HTML;
		
		LOGGER.info("Writing html page: " + fileName);
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName);
			for (String content : contentList) {
				writer.println(content);
			}
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * 
	 */
	private void writeHeader() {
		append("<!DOCTYPE html>");
		append("<html>");
		append("<head>");
		append("<link rel='stylesheet' type='text/css' media='all' href='css/all.css' />");
		append("<title>" + "Virgin Media Daily Traffic Graphs" + "</title>");
		append("</head>");
		append("<body>");
		//append("<div class='div_main'>");
		append("<h2>" + "Virgin Media Daily Traffic Graphs" + "</h2>");
	}
	
	/**
	 * 
	 */
	private void writeFooter() {
		//append("</div>");
		append("</body>");
		append("</html>");
	}
	
	/**
	 * @param content
	 */
	private void append(String content) {
		contentList.add(content);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			LOGGER.warn("Usage: IndexHtml <dirName>");
			System.exit(-1);
		}
		IndexHtml indexHtml = new IndexHtml(new File(args[0]));
		indexHtml.createIndexHtml();
	}
}
