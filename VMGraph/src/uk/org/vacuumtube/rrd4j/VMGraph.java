/**
 * 
 */
package uk.org.vacuumtube.rrd4j;

import static org.rrd4j.ConsolFun.AVERAGE;
import static org.rrd4j.ConsolFun.MAX;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.rrd4j.ConsolFun;
import org.rrd4j.core.FetchData;
import org.rrd4j.core.FetchRequest;
import org.rrd4j.core.RrdBackendFactory;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.Util;
import org.rrd4j.graph.RrdGraph;
import org.rrd4j.graph.RrdGraphDef;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import uk.org.vacuumtube.util.ByteFormat;

/**
 * @author clivem
 *
 */
public class VMGraph {
	
	public static final Logger LOGGER = Logger.getLogger(VMGraph.class);
	//public final static Logger LOGGER = LoggerFactory.getLogger(VMGraph.class);
	
    protected final static DateFormat DF_FULL = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z");
    protected final static DateFormat DF_DATE = new SimpleDateFormat("yyyy/MM/dd");
    protected final static DateFormat DF_TIME = new SimpleDateFormat("HH:mm");
    protected final static DateFormat DF_DATETIME = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    protected final static DateFormat DF_OUTNAME_DATETIME = new SimpleDateFormat("yyyyMMdd_HHmm");
    protected final static DateFormat DF_OUTNAME_DATE = new SimpleDateFormat("yyyyMMdd");

    public final static String DEFAULT_ARCHIVE_IN_NAME = "traffic_in";
    public final static String DEFAULT_ARCHIVE_OUT_NAME = "traffic_out";
    
    protected RrdDb rrdDb = null;
    protected String outputPath = null;
    protected int year;
    protected int month;
    protected int day;
    protected Service[] profileList;
    protected String archiveInName;
    protected String archiveOutName;
    
    protected HtmlFile htmlFile = null;
    
    /**
     * @param rrdDb
     * @param outputPath
     * @param year
     * @param month
     * @param day
     * @param serviceList
     * @param archiveInName
     * @param archiveOutName
     */
    public VMGraph(RrdDb rrdDb, String outputPath, int year, int month, int day, 
    		Service[] serviceList, String archiveInName, String archiveOutName) {
    	this.rrdDb = rrdDb;
    	this.outputPath = outputPath;
    	this.year = year;
    	this.month = month;
    	this.day = day;
    	this.profileList = serviceList;
    	this.archiveInName = archiveInName;
    	this.archiveOutName = archiveOutName;
    }
    
    /**
     * @throws IOException
     */
    public void graph() throws IOException {
		for (int i = 0; i < profileList.length; i++) {
			graph(profileList[i]);
		}
		
		if (htmlFile != null) {
			htmlFile.write();
		}
    }
    
    /**
     * @param startTs
     * @param endTs
     * @param service
     * @throws IOException
     */
    public void graph(Service service) throws IOException {
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.debug("graph(profile=" + service + ")");
    	}

		Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, day, service.getStartHour(), 0);
        
        long startTs = Util.getTimestamp(calendar) * 1000;
        calendar.add(Calendar.HOUR_OF_DAY, service.getDurationHours());
        long endTs = Util.getTimestamp(calendar) * 1000;

    	LOGGER.info("Graphing Profile: " + service.getServiceId() + 
    			", Start: " + startTs + " [" + DF_FULL.format(new Date(startTs)) + 
    			"], End: " + endTs + " [" + DF_FULL.format(new Date(endTs)) + "]");
    	
    	for (StmProfile profile : service.getStmProfileMapValues()) {
    		if (profile.getLimitMB() > 0) {
    			LOGGER.info(profile.getLimitDescription());
    		}
    	}
    	
    	long lastUpdateTime = rrdDb.getLastUpdateTime() * 1000;
        LOGGER.info("RRD last update time: " + lastUpdateTime + " [" + DF_FULL.format(new Date(lastUpdateTime)) + "]");

        if (lastUpdateTime < startTs) {
        	LOGGER.info("RRD last update time < requested start time. Will not create graph for this profile!");
        	return;
        }
        
        if (lastUpdateTime < endTs) {
        	endTs = lastUpdateTime;
        	LOGGER.info("RRD last update time < requested end time. Graphing until last update time.");
        }
        
    	if (htmlFile == null) {
    		String title = "Virgin Media Broadband Traffic " + DF_DATE.format(new Date(startTs));
    		String fileName = outputPath + Util.getFileSeparator() + "VM_" + service.getServiceName() + "_" + 
            		DF_OUTNAME_DATE.format(new Date(startTs)) + ".html";
    		htmlFile = new HtmlFile(title, fileName);
    	}

    	/*
    	 * Fetch the data
    	 */
        FetchRequest request = rrdDb.createFetchRequest(AVERAGE, startTs / 1000L, endTs / 1000L);
        //logger.debug(request.dump());
        FetchData fetchData = request.fetchData();
        LOGGER.info("RRD data fetched: " + fetchData.getRowCount() + " data points obtained. Fetch ArcStep: " + 
        		fetchData.getArcStep() + "s. Fetch ArcEndTime: " + DF_FULL.format(new Date(fetchData.getArcEndTime() * 1000)));
        //logger.debug(fetchData.exportXml());

        long step = fetchData.getStep();
        long[] tstamp = fetchData.getTimestamps();
        double in[] = fetchData.getValues(archiveInName);
        double out[] = fetchData.getValues(archiveOutName);

    	long upLimitBytes = service.getLimitBytes(Direction.UP);        
    	double upTotalBytes = 0;
        long upLimitExceededTs = -1;
        String upLimitExceeded = null;
        String upLimitExceededDetail = null;

    	long downLimitBytes = service.getLimitBytes(Direction.DOWN);
        double downTotalBytes = 0;
        long downLimitExceededTs = -1;
        String downLimitExceeded = null;
        String downLimitExceededDetail = null;
        
        for (int i = 0; i < tstamp.length; i++) {
        	
        	long stepTs = tstamp[i] * 1000;
        	double stepDownBytes = in[i] * step;
        	double stepUpBytes = out[i] * step;
        	
        	String stepTsDate = stepTs + " [" + DF_FULL.format(new Date(stepTs)) + "] ";
        	   		
        	if (LOGGER.isDebugEnabled()) {
	        	for (Direction direction : service.getStmProfileMapKeySet()) {
		        	switch (direction) {
		        		case DOWN:
		        			LOGGER.debug(stepTsDate + direction + ": " + 
		        					ByteFormat.humanReadableByteCount((long)stepDownBytes, true) +
		                			" (" + ByteFormat.humanReadableBitCount((long)(in[i] * 8)) + ")");
		        			break;
		        		case UP:
		        			LOGGER.debug(stepTsDate + direction + ": " + 
		        					ByteFormat.humanReadableByteCount((long)stepUpBytes, true) +
		                			" (" + ByteFormat.humanReadableBitCount((long)(out[i] * 8)) + ")");
		        			break;
			        }
		        }
        	}
        	
        	if (i > 0) {
        		if (!Double.isNaN(stepDownBytes)) {
            		downTotalBytes += stepDownBytes;
        		}
        		if (!Double.isNaN(stepUpBytes)) {
        			upTotalBytes += stepUpBytes;
        		}
        		
            	for (Direction direction : service.getStmProfileMapKeySet()) {
	        		switch (direction) {
	        			case DOWN:
	                		if (LOGGER.isDebugEnabled() && i < tstamp.length - 1) {
	                			LOGGER.debug(stepTsDate + "Cumulative " + direction + ": " + ByteFormat.humanReadableByteCount((long)downTotalBytes, true));
	                    	}
	            	        if (i == tstamp.length - 1) {
	            	    		LOGGER.info(stepTsDate + "Total " + direction + ": " + ByteFormat.humanReadableByteCount((long)downTotalBytes, true));
	            	        }
	                		
	            	        if ((downLimitBytes > 0) && downLimitExceeded == null && downTotalBytes >= downLimitBytes) {
	            	        	Date date = new Date(stepTs);
	            	        	
	            	        	Calendar cal = Calendar.getInstance();
	            	        	cal.setTime(date);
	            	        	cal.add(Calendar.HOUR, service.getLimitReductionHours(Direction.DOWN));
	            	        	
	                			downLimitExceeded = "VM STM download limit (of " + ByteFormat.humanReadableByteCount(downLimitBytes, true) + 
	                					") exceeded (by " + ByteFormat.humanReadableByteCount(((long)downTotalBytes) - downLimitBytes, true) + ") at " + 
	                					DF_TIME.format(date) + "."; 
	                			downLimitExceededDetail = "Max download speed reduced by " + service.getLimitReductionPercentage(Direction.DOWN) + "% (to " +  
	                					ByteFormat.humanReadableBitCount(service.getConnectionSpeedDownBpsAfterSTM()) + " for " + 
	                					service.getLimitReductionHours(Direction.DOWN) + " hours) until " +
	                					DF_FULL.format(cal.getTime()) + ".";
	                			LOGGER.info(downLimitExceeded + " " + downLimitExceededDetail);
	                			downLimitExceededTs = stepTs;
	                		}	                		
	            	        
	        				break;
	        			case UP:
	                		if (LOGGER.isDebugEnabled() && i < tstamp.length - 1) {
	                			LOGGER.debug(stepTsDate + "Cumulative " + direction + ": " + 
	                					ByteFormat.humanReadableByteCount((long)upTotalBytes, true));
	                    	}
	                		if (i == tstamp.length - 1) {
	                			LOGGER.info(stepTsDate + "Total " + direction + ": " + 
	                					ByteFormat.humanReadableByteCount((long)upTotalBytes, true));
	                		}
	                		
	                		if ((upLimitBytes > 0) && upLimitExceeded == null && upTotalBytes >= upLimitBytes) {
	            	        	Date date = new Date(stepTs);
	            	        	
	            	        	Calendar cal = Calendar.getInstance();
	            	        	cal.setTime(date);
	            	        	cal.add(Calendar.HOUR, service.getLimitReductionHours(Direction.UP));
	            	        	
	                			upLimitExceeded = "VM STM upload limit (of " + ByteFormat.humanReadableByteCount(upLimitBytes, true) + 
	                					") exceeded (by " + ByteFormat.humanReadableByteCount(((long)upTotalBytes) - upLimitBytes, true) + ") at " + 
	                					DF_TIME.format(date) + ".";
	                			upLimitExceededDetail = "Max upload speed reduced by " + service.getLimitReductionPercentage(Direction.UP) + "% (to " + 
	                					ByteFormat.humanReadableBitCount(service.getConnectionSpeedUpBpsAfterSTM()) + " for " + 
	                					service.getLimitReductionHours(Direction.UP) + " hours) until " +
	                					DF_FULL.format(cal.getTime()) + ".";
	                			LOGGER.info(upLimitExceeded + " " + upLimitExceededDetail);
	                			upLimitExceededTs = stepTs;
	                		}
	                		break;
	        		}
	        	}            	
        	}
        }

        String graphTitle = "VM " + service.getServiceName() + 
        		(service.stmProfileListHasSingleProfile(Direction.DOWN) ? " DOWN " : 
        			service.stmProfileListHasSingleProfile(Direction.UP) ? " UP " : " ") +
        		DF_DATETIME.format(new Date(startTs)) + " - " + ((endTs - startTs < 1000 * 60 * 60 * 24) ? 
        				DF_TIME.format(new Date(endTs)) : DF_DATETIME.format(new Date(endTs)));
        
		RrdGraphDef graphDef = new RrdGraphDef();
        graphDef.setTitle(graphTitle);
        graphDef.setVerticalLabel("bits per second");
        graphDef.setTimeSpan(startTs / 1000L, endTs / 1000L);
        graphDef.setHeight(120);
        graphDef.setWidth(500);
        
        
    	for (Direction direction : service.getStmProfileMapKeySet()) {
	        switch(direction) {
	        	case DOWN:
	            	graphDef.datasource("in", rrdDb.getPath(), archiveInName, ConsolFun.AVERAGE);
	                graphDef.datasource("inbps", "in,8,*");
	                graphDef.area("inbps", new Color(0, 0xFF, 0), "Download");
	                graphDef.gprint("inbps", MAX, "Speed Max: %.2f %sbps");
	                graphDef.gprint("inbps", AVERAGE, "Speed Avg: %.2f %sbps");
	                graphDef.datasource("inTotal", "in,STEP,*");
	                graphDef.gprint("inTotal", ConsolFun.TOTAL, "Bytes: %3.2f %sB\\c");
	                //graphDef.comment("Down Bytes: " + ByteFormat.humanReadableByteCount((long)inTotal, true) + "\\c");	
	                if (downLimitExceeded != null) {
	                	graphDef.comment("\\l");
	                	graphDef.vrule(downLimitExceededTs / 1000L, Color.BLACK, downLimitExceeded + "\\c", 2.0F);
	                	if (downLimitExceededDetail != null) {
	                		graphDef.comment(downLimitExceededDetail + "\\c");
	                	}
	                };	                
	                break;
	        	case UP:
	            	graphDef.datasource("out", rrdDb.getPath(), archiveOutName, ConsolFun.AVERAGE);
	            	graphDef.datasource("outbps", "out,8,*");
	                graphDef.area("outbps", new Color(0, 0, 0xFF), "Upload");
	                graphDef.gprint("outbps", MAX, "Speed Max: %.2f %sbps");
	                graphDef.gprint("outbps", AVERAGE, "Speed Avg: %.2f %sbps");            
	                graphDef.datasource("outTotal", "out,STEP,*");
	                graphDef.gprint("outTotal", ConsolFun.TOTAL, "Bytes: %3.2f %sB\\c");
	                //graphDef.comment("Up Bytes: " + ByteFormat.humanReadableByteCount((long)outTotal, true) + "\\c");        		
	                if (upLimitExceeded != null) {
	                	graphDef.comment("\\l");
	                	graphDef.vrule(upLimitExceededTs / 1000L, Color.RED, upLimitExceeded + "\\c", 2.0F);
	                	if (upLimitExceededDetail != null) {
	                		graphDef.comment(upLimitExceededDetail + "\\c");
	                	}
	                }	                
	                break;
	        }
    	}
    	
    	for (StmProfile profile : service.getStmProfileMapValues()) {
    		if (profile.getLimitMB() > 0) {
    			if ((profile.getDirection() == Direction.DOWN && downLimitExceeded == null) ||
    					(profile.getDirection() == Direction.UP && upLimitExceeded == null)) {
	    			graphDef.comment("\\c");
	    			graphDef.comment(profile.getLimitDescription() + "\\c");
    			}
    		}
    	}
    	
        graphDef.hrule(50000000, Color.DARK_GRAY, null);
        
        String graphFileTitle = "VM_" + service.getServiceName() + "_" + 
        		DF_OUTNAME_DATETIME.format(new Date(startTs)) +
        		(service.stmProfileListHasSingleProfile(Direction.DOWN) ? "_DOWN" : 
        			service.stmProfileListHasSingleProfile(Direction.UP) ? "_UP" : "");
        String graphFileName = outputPath + Util.getFileSeparator() + graphFileTitle + ".png";
        
        //graphDef.setFilename(fileName);
        graphDef.setFilename("-");
        graphDef.setImageFormat("PNG");
        //graphDef.setImageInfo("<IMG SRC='%s' WIDTH='%d' HEIGHT='%d' ALT='" + graphName + "'>");
        //graphDef.setAltYMrtg(true);
        
        RrdGraph graph = new RrdGraph(graphDef);
        //BufferedImage bi = new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
        //graph.render(bi.getGraphics());

        File f = new File(graphFileName);
        /*
         * Set posix group write permissions
         * 
        if (Version.isGreater(1.65f) && OS.isUnix() && !f.exists()) {
            String attrList = "rw-rw-r--";
	        try {
    			Set<PosixFilePermission> perms = PosixFilePermissions.fromString(attrList);
    			FileAttribute<Set<PosixFilePermission>> at =
    				    PosixFilePermissions.asFileAttribute(perms);
    			Files.createFile(f.toPath(), at);
	        } catch (IOException ioe) {
	        	logger.warn("Error creating file with attributes [" + attrList + "]: " + fileName, ioe);
	        } catch (UnsupportedOperationException uoe) {
	        	logger.warn("Error creating file with attributes [" + attrList + "]: " + fileName, uoe);
	        }
        }
        */
        
        LOGGER.info("Writing graph: " + f.getAbsolutePath());
    	BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));
    	bos.write(graph.getRrdGraphInfo().getBytes());
    	bos.close();
    	
    	htmlFile.append("<p><img src='" + graphFileTitle + ".png' alt='" + graphTitle + 
    			"' width='" + graph.getRrdGraphInfo().getWidth() + 
    			"' height='" + graph.getRrdGraphInfo().getHeight() + "' /></p>");
    	
        //String imgInfo = graph.getRrdGraphInfo().getImgInfo();
        //logger.info(imgInfo);
    }
    
    /**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// assume SLF4J is bound to logback in the current environment
	    //LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
	    // print logback's internal status
	    //StatusPrinter.print(lc);

		Options options = new Options();

		/*
		Option property = OptionBuilder.withArgName("property=value")
                .hasArgs(2)
                .withValueSeparator()
                .withDescription("use value for given property")
                .create("D");
		options.addOption(property);
        */
		
		@SuppressWarnings("static-access")
		Option rrd_file_option = OptionBuilder.withArgName("name")
                .hasArg()
                .withDescription("use given name for rrd database file")
                .create("rrd_file");
		rrd_file_option.setRequired(true);
		options.addOption(rrd_file_option);
		
		@SuppressWarnings("static-access")
		Option out_dir_option = OptionBuilder.withArgName("name")
                .hasArg()
                .withDescription("use given name for out dir")
                .create("out_dir");
		options.addOption(out_dir_option);
		
		@SuppressWarnings("static-access")
		Option rrdtool_file_option = OptionBuilder.withArgName("name")
                .hasArg()
                .withDescription("use given name for rrdtool src filename")
                .create("rrdtool_file");
		options.addOption(rrdtool_file_option);
		
		@SuppressWarnings("static-access")
		Option profile_option = OptionBuilder.withArgName("name")
                .hasArg()
                .withDescription("use given name for profile")
                .create("profile");
		profile_option.setRequired(true);
		options.addOption(profile_option);
		
		@SuppressWarnings("static-access")
		Option year_option = OptionBuilder.withArgName("YYYY")
                .hasArg()
                .withDescription("use given YYYY for year")
                .create("year");
		year_option.setType(Integer.class);
		options.addOption(year_option);
		
		@SuppressWarnings("static-access")
		Option month_option = OptionBuilder.withArgName("MM")
                .hasArg()
                .withDescription("use given MM for month")
                .create("month");
		month_option.setType(Integer.class);
		options.addOption(month_option);
		
		@SuppressWarnings("static-access")
		Option day_option = OptionBuilder.withArgName("DD")
                .hasArg()
                .withDescription("use given DD for day")
                .create("day");
		day_option.setType(Integer.class);
		options.addOption(day_option);
		
		@SuppressWarnings("static-access")
		Option archive_in_name_option = OptionBuilder.withArgName("name")
                .hasArg()
                .withDescription("use given name for DOWN/IN stats archive in rrd file. default: traffic_in")
                .create("archive_in_name");
		options.addOption(archive_in_name_option);
		
		@SuppressWarnings("static-access")
		Option archive_out_name_option = OptionBuilder.withArgName("name")
                .hasArg()
                .withDescription("use given name for UP/OUT stats archive in rrd file. default: traffic_out")
                .create("archive_out_name");
		options.addOption(archive_out_name_option);
		
		Option help_option = new Option( "help", "print this message" );
		options.addOption(help_option);
		
		CommandLineParser parser = new GnuParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException pe) {
			LOGGER.error("Error parsing program arguments.", pe);
			System.exit(-1);
		}		

		if (cmd.hasOption("help")) {
			new HelpFormatter().printHelp("VMGraph", options);
			System.exit(0);
		}

		StringBuffer buf = new StringBuffer();
		buf.append("Starting: VMGraph");
		@SuppressWarnings("unchecked")
		Iterator<Option> it = cmd.iterator();
		while (it.hasNext()) {
			Option op = it.next();
			buf.append(" -" + op.getOpt() + ((op.getValue() != null) ? " " + op.getValue() : ""));
		}
		LOGGER.info(buf.toString());
		
		File rrdDbFile = new File(cmd.getOptionValue("rrd_file"));
		if (!rrdDbFile.exists()) {
			LOGGER.debug("RrdDb file does not exist: " + rrdDbFile.getAbsolutePath());
		}
		
		File rrdDbToolFile = null;
		if (cmd.getOptionValue("rrdtool_file") != null) {
			rrdDbToolFile = new File(cmd.getOptionValue("rrdtool_file"));
			if (!rrdDbToolFile.exists()) {
				LOGGER.error("rddtool input file does not exist: " + rrdDbToolFile.getAbsolutePath());
				System.exit(-1);
			}
		}
		
		File outDir = new File(cmd.getOptionValue("out_dir"));
		if (!outDir.exists()) {
			LOGGER.error("Directory for output files must exist: " + outDir.getAbsolutePath());
			System.exit(-1);
		}
		
		Service[] profileList = Service.getProfileList(cmd.getOptionValue("profile"));
		if (profileList == null || profileList.length < 1) {
			String vpl = "";
			for (String p : Service.SERVICE_NAME_LIST) {
				vpl += (" " + p);
			}
			LOGGER.error("Invalid profile: " + cmd.getOptionValue("profile") + "! Valid profiles:" + vpl + ".");
			System.exit(-1);
		}
		
		int year = -1;
		int month = -1;
		int day = -1;

		if (cmd.hasOption("year") && cmd.hasOption("month") && cmd.hasOption("day")) {
			try {
				year = Integer.parseInt(cmd.getOptionValue("year"));
			} catch (NumberFormatException nfe) {
				LOGGER.error("Invalid -year: " + cmd.getOptionValue("year"), nfe);
				System.exit(-1);
			}
			
			try {
				month = Integer.parseInt(cmd.getOptionValue("month"));
				if (month < 1 || month > 12) {
					LOGGER.error("Invalid -month: " + month + ". Valid value: 1-12.");
					System.exit(-1);
				} else {
					month -= 1;
				}
			} catch (NumberFormatException nfe) {
				LOGGER.error("Invalid -month: " + cmd.getOptionValue("month"), nfe);
				System.exit(-1);
			}
			
			try {
				day = Integer.parseInt(cmd.getOptionValue("day"));
				if (day < 1 || day > 31) {
					LOGGER.error("Invalid -day: " + day + ". Valid value: 1-31.");
					System.exit(-1);
				}
			} catch (NumberFormatException nfe) {
				LOGGER.error("Invalid -day: " + cmd.getOptionValue("day"), nfe);
				System.exit(-1);
			}
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -1);
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH);
			day = cal.get(Calendar.DATE);
			LOGGER.info("-year|-month|-day not set on cmd line. Graphing yesterday: [" + 
					DF_FULL.format(cal.getTime()) + "]");
		}
				
		RrdDb rrdDb = null;
		try {
			if (rrdDbToolFile != null) {
				// Open a RRDTool created database file
				rrdDb = new RrdDb(rrdDbFile.getAbsolutePath(), 
						"rrdtool:/" + rrdDbToolFile.getAbsolutePath(), 
						RrdBackendFactory.getFactory("FILE"));
			} else {
				rrdDb = new RrdDb(rrdDbFile.getAbsolutePath(), true, 
						RrdBackendFactory.getFactory("FILE"));
			}
			
			VMGraph graph = new VMGraph(rrdDb, outDir.getAbsolutePath(), year, month, day, profileList, 
					cmd.hasOption("archive_in_name") ? cmd.getOptionValue("archive_in_name") : DEFAULT_ARCHIVE_IN_NAME, 
					cmd.hasOption("archive_out_name") ? cmd.getOptionValue("archive_out_name") : DEFAULT_ARCHIVE_OUT_NAME);
			graph.graph();
		} catch (IOException ioe) {
			LOGGER.error(null, ioe);
		} finally {
			if (rrdDb != null) {
				try {
					LOGGER.info("Closing rrdDb...");
					rrdDb.close();
				} catch (IOException ioe) {}
			}
		}
		LOGGER.info("Exiting...");
	}
}
