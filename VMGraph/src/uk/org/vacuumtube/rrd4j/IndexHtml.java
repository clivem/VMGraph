/**
 * 
 */
package uk.org.vacuumtube.rrd4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

/**
 * @author clivem
 *
 */
public class IndexHtml {

	private final static Logger LOGGER = Logger.getLogger(IndexHtml.class);
	
	private final static String INDEX_HTML_FILENAME = "index.html";
	private final static String TITLE_START_TAG = "<title>";
	private final static String TITLE_END_TAG = "</title>";
	
	private List<String> contentList;
	private File directory;
	
	/**
	 * @param directory
	 */
	public IndexHtml(File directory) {
		this.directory = directory;
		this.contentList = new ArrayList<String>();
	}
	
	private static class FileDateComparator implements Comparator<String> {
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(String o1, String o2) {
			if (o1.endsWith(".html") && o2.endsWith(".html")) {
				try {
					String s1 = o1.substring(0, o1.length() - 5);
					int date1 = Integer.parseInt(s1.substring(s1.length() - 8));
					String s2 = o2.substring(0, o2.length() - 5);
					int date2 = Integer.parseInt(s2.substring(s2.length() - 8));
					if (date1 > date2) {
						return 1;
					} else if (date1 < date2) {
						return -1;
					} else {
						return 0;
					}
				} catch (Exception e) {
					LOGGER.warn(null, e);
				}
			}
			
			return o1.compareTo(o2);
		}
		
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
		//Arrays.sort(dailyHtmlFileNames);

		SortedSet<String> set = new TreeSet<String>(new FileDateComparator());
		for (String fileName : dailyHtmlFileNames) {
			set.add(fileName);
		}
		set.toArray(dailyHtmlFileNames);
				
		writeHeader();
		//for (String fileName : dailyHtmlFileNames) {
		for (int i = dailyHtmlFileNames.length - 1; i >= 0; i--) {
			//LOGGER.info(fileName);
			String fileName = dailyHtmlFileNames[i];
			
			String title = fileName;
			BufferedReader br = null;
			try {
				File f = new File(directory.getAbsolutePath() + File.separator + fileName);
				br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
				while (true) {
					String line = br.readLine();
					if (line == null) {
						break;
					}
					if (line.startsWith(TITLE_START_TAG) && line.endsWith(TITLE_END_TAG)) {
						title = line.substring(TITLE_START_TAG.length(), line.length() - TITLE_END_TAG.length());
						break;
					}
				}
			} catch (FileNotFoundException fnfe) {
				LOGGER.warn(null, fnfe);
			} catch (IOException ioe) {
				LOGGER.warn(null, ioe);
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (Exception e) {}
				}
			}

			append("<p><a href='" + fileName + "' title='" + title + "'>" + title + "</a></p>");
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
		String fileName = directory.getAbsolutePath() + File.separator + INDEX_HTML_FILENAME;
		
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
		String title = "Virgin Media Broadband Daily Traffic Graphs";
		append("<!DOCTYPE html>");
		append("<html>");
		append("<head>");
		append("<link rel='stylesheet' type='text/css' media='all' href='css/all.css' />");
		append("<title>" + title + "</title>");
		append("</head>");
		append("<body>");
		//append("<div class='div_main'>");
		append("<h2>" + title + "</h2>");
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
