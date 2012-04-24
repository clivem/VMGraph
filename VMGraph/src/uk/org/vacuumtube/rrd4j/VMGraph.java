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
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

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

import uk.org.vacuumtube.util.ByteFormat;
import uk.org.vacuumtube.util.OS;
import uk.org.vacuumtube.util.Version;

/**
 * @author clivem
 *
 */
public class VMGraph {
	
	protected final static Logger logger = Logger.getLogger(VMGraph.class);
	
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
    protected Profile[] profileList;
    protected String archiveInName;
    protected String archiveOutName;
    
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
    		Profile[] serviceList, String archiveInName, String archiveOutName) {
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
			Calendar calendar = Calendar.getInstance();
	        calendar.clear();
	        calendar.set(year, month, day, profileList[i].getStartHour(), 0);
	        
	        long startTs = Util.getTimestamp(calendar) * 1000;
	        calendar.add(Calendar.HOUR_OF_DAY, profileList[i].getDurationHours());
	        long endTs = Util.getTimestamp(calendar) * 1000;
	        
			graph(startTs, endTs, profileList[i]);
		}		
    }
    
    /**
     * @param startTs
     * @param endTs
     * @param profile
     * @throws IOException
     */
    public void graph(long startTs, long endTs, Profile profile) throws IOException {
    	//String path = rrdDb.getPath();
    	//RrdDb rrdDb = new RrdDb(rrdFilename, xmlFilename); 
		//RrdDb rrdDb = new RrdDb(rrdFilename, true);
        //println("File reopen in read-only mode");
    	long lastUpdateTime = rrdDb.getLastUpdateTime() * 1000;
        logger.info("DB Last update time was: " + DF_FULL.format(new Date(lastUpdateTime)));
        //logger.info("DB Last info was: " + rrdDb.getInfo());

        if (lastUpdateTime < startTs) {
        	logger.warn("DB LastUpdateTime < requested start time! Not graphing!");
        	return;
        }
        
        if (lastUpdateTime < endTs) {
        	endTs = lastUpdateTime;
        }
        
    	long downLimit = profile.getLimitBytes(Direction.DOWN);
    	long upLimit = profile.getLimitBytes(Direction.UP);
    	Direction direction = profile.getDirection();
		
        FetchRequest request = rrdDb.createFetchRequest(AVERAGE, startTs / 1000L, endTs / 1000L);
        //println(request.dump());
        FetchData fetchData = request.fetchData();
        logger.debug("DB Data fetched. " + fetchData.getRowCount() + " data points obtained.");
        //println(fetchData.exportXml());
        logger.debug("DB Fetch completed");

        logger.debug("DB ArcStep: " + fetchData.getArcStep() + ", ArcEndTime: " + 
        		DF_FULL.format(new Date(fetchData.getArcEndTime() * 1000)));
        
        long step = fetchData.getStep();
        long[] tstamp = fetchData.getTimestamps();
        double in[] = fetchData.getValues(archiveInName);
        double out[] = fetchData.getValues(archiveOutName);
        double inTotal = 0;
        double outTotal = 0;
        long upLimitBreachedTs = -1;
        long downLimitBreachedTs = -1;
        String upLimitBreached = null;
        String upLimitBreachedDetail = null;
        String downLimitBreached = null;
        String downLimitBreachedDetail = null;
        
        for (int i = 0; i < tstamp.length; i++) {
        	
        	long ts = tstamp[i] * 1000;
        	double temp_in = in[i] * step;
        	double temp_out = out[i] * step;
        	
        	if (logger.isTraceEnabled()) {
	        	switch (direction) {
	        		case DOWN:
	        			logger.trace(DF_FULL.format(new Date(ts)) + 
	                			", Down: " + ByteFormat.humanReadableByteCount((long)temp_in, true) +
	                			" (" + ByteFormat.humanReadableBitCount((long)(in[i] * 8)) + ")");
	        			break;
	        		case UP:
	        			logger.trace(DF_FULL.format(new Date(ts)) + 
	                			", Up: " + ByteFormat.humanReadableByteCount((long)temp_out, true) +
	                			" (" + ByteFormat.humanReadableBitCount((long)(out[i] * 8)) + ")");
	        			break;
	        		default:
	        			logger.trace(DF_FULL.format(new Date(ts)) + 
	                			", Down: " + ByteFormat.humanReadableByteCount((long)temp_in, true) +
	                			" (" + ByteFormat.humanReadableBitCount((long)(in[i] * 8)) + ")" + 
	                			" / Up: " + ByteFormat.humanReadableByteCount((long)temp_out, true) +
	                			" (" + ByteFormat.humanReadableBitCount((long)(out[i] * 8)) + ")");
	        			break;
	        	}
        	}
        	
        	if (i > 0) {
        		if (!Double.isNaN(temp_in)) {
            		inTotal += temp_in;
        		}
        		if (!Double.isNaN(temp_out)) {
        			outTotal += temp_out;
        		}
        		
        		if (logger.isTraceEnabled()) {
	        		switch (direction) {
	        			case DOWN:
	    	    	        logger.trace("Total Downloaded: " + ByteFormat.humanReadableByteCount((long)inTotal, true));
	        				break;
	        			case UP:
	    	    	        logger.trace("Total Uploaded: " + ByteFormat.humanReadableByteCount((long)outTotal, true));
	        				break;
	    				default:
	    	    	        logger.trace("Total Downloaded: " + ByteFormat.humanReadableByteCount((long)inTotal, true) +
		    	        	" / Total Uploaded: " + ByteFormat.humanReadableByteCount((long)outTotal, true));
	    					break;
	        		}
        		}

    	        if ((downLimit > 0) && downLimitBreached == null && inTotal >= downLimit) {
    	        	Date date = new Date(ts);
    	        	
    	        	Calendar cal = Calendar.getInstance();
    	        	cal.setTime(date);
    	        	cal.add(Calendar.HOUR, profile.getLimitReductionHours(Direction.DOWN));
    	        	
        			downLimitBreached = "VM STM download limit (of " + ByteFormat.humanReadableByteCount(downLimit, true) + 
        					") exceeded (by " + ByteFormat.humanReadableByteCount(((long)inTotal) - downLimit, true) + ") at " + 
        					DF_TIME.format(date) + "."; 
        			downLimitBreachedDetail = "Max download speed reduced by " + profile.getLimitReductionPercentage(Direction.DOWN) + "% (to " +  
        					ByteFormat.humanReadableBitCount(profile.getConnectionSpeedDownBpsAfterSTM()) + " for " + 
        					profile.getLimitReductionHours(Direction.DOWN) + " hours) until " +
        					DF_FULL.format(cal.getTime()) + ".";
        			logger.info(downLimitBreached + " " + downLimitBreachedDetail);
        			downLimitBreachedTs = ts;
        		}
        		
        		if ((upLimit > 0) && upLimitBreached == null && outTotal >= upLimit) {
    	        	Date date = new Date(ts);
    	        	
    	        	Calendar cal = Calendar.getInstance();
    	        	cal.setTime(date);
    	        	cal.add(Calendar.HOUR, profile.getLimitReductionHours(Direction.UP));
    	        	
        			upLimitBreached = "VM STM upload limit (of " + ByteFormat.humanReadableByteCount(upLimit, true) + 
        					") exceeded (by " + ByteFormat.humanReadableByteCount(((long)outTotal) - upLimit, true) + ") at " + 
        					DF_TIME.format(date) + ".";
        			upLimitBreachedDetail = "Max upload speed reduced by " + profile.getLimitReductionPercentage(Direction.UP) + "% (to " + 
        					ByteFormat.humanReadableBitCount(profile.getConnectionSpeedUpBpsAfterSTM()) + " for " + 
        					profile.getLimitReductionHours(Direction.UP) + " hours) until " +
        					DF_FULL.format(cal.getTime()) + ".";
        			logger.info(upLimitBreached + " " + upLimitBreachedDetail);
        			upLimitBreachedTs = ts;
        		}
        	}
        }
        
        //rrdDb.close();
        
        RrdGraphDef graphDef = new RrdGraphDef();
        //long endTime = Util.getTime();
        //long startTime = endTime - (24*60*60L);
        //graphDef.setTimeSpan(startTime, endTime);
        //long duration = fins - finishTs;
        //long dayD = 1000 * 60 * 60 * 24; 
        //(finishTs - startTs < 1000 * 60 * 60 * 24)
        //((service.getDurationHours() < 24)
        
        graphDef.setTitle("VM " + profile.getServiceName() + 
        		((direction == Direction.DOWN) ? " DOWN " : (direction == Direction.UP) ? " UP " : " ") +
        		DF_DATETIME.format(new Date(startTs)) + " - " + ((endTs - startTs < 1000 * 60 * 60 * 24) ? 
        				DF_TIME.format(new Date(endTs)) : DF_DATETIME.format(new Date(endTs))));
        graphDef.setVerticalLabel("bits per second");
        graphDef.setTimeSpan(startTs / 1000L, endTs / 1000L);
        graphDef.setHeight(120);
        graphDef.setWidth(500);
        
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
                break;
        	default:
            	graphDef.datasource("in", rrdDb.getPath(), archiveInName, ConsolFun.AVERAGE);
                graphDef.datasource("inbps", "in,8,*");
                graphDef.area("inbps", new Color(0, 0xFF, 0), "Download");
                graphDef.gprint("inbps", MAX, "Speed Max: %.2f %sbps");
                graphDef.gprint("inbps", AVERAGE, "Speed Avg: %.2f %sbps");
                graphDef.datasource("inTotal", "in,STEP,*");
                graphDef.gprint("inTotal", ConsolFun.TOTAL, "Bytes: %3.2f %sB\\c");
                //graphDef.comment("Down Bytes: " + ByteFormat.humanReadableByteCount((long)inTotal, true) + "\\c");
        		
                //graphDef.comment("\\c");
            	
                graphDef.datasource("out", rrdDb.getPath(), archiveOutName, ConsolFun.AVERAGE);
            	graphDef.datasource("outbps", "out,8,*");
                graphDef.line("outbps", new Color(0, 0, 0xFF), "Upload", 1);
                graphDef.gprint("outbps", MAX, "Speed Max: %.2f %sbps");
                graphDef.gprint("outbps", AVERAGE, "Speed Avg: %.2f %sbps");
                graphDef.datasource("outTotal", "out,STEP,*");
                graphDef.gprint("outTotal", ConsolFun.TOTAL, "Bytes: %3.2f %sB\\c");
                //graphDef.comment("Up Bytes: " + ByteFormat.humanReadableByteCount((long)outTotal, true) + "\\c");
                break;
        }

        //graphDef.setValueAxis(10000000d, 1);
        //graphDef.setMaxValue(55000000d);
        //graphDef.setRigid(true);
        graphDef.hrule(50000000, Color.DARK_GRAY, null);
        
        /*
        graphDef.setTimeAxis(RrdGraphConstants.MINUTE, 10,
                RrdGraphConstants.HOUR, 1,
                RrdGraphConstants.HOUR, 1,
                0, "%H:%M");
         */

        /*
        if (total != null) {
        	graphDef.comment("\\l");
        	graphDef.comment(total + "\\c");
        };
        */

        if (downLimitBreached != null) {
        	graphDef.comment("\\l");
        	graphDef.vrule(downLimitBreachedTs / 1000L, Color.BLACK, downLimitBreached + "\\c", 2.0F);
        	if (downLimitBreachedDetail != null) {
        		graphDef.comment(downLimitBreachedDetail + "\\c");
        	}
        	/*
        	String[] list = downLimitBreached.split("\\n");
        	for (int i = 0; i < list.length; i++) {
        		if (list[i] != null) {
        			if (i < 1) {
        	        	graphDef.vrule(downLimitBreachedTs / 1000L, Color.BLACK, list[i] + "\\c", 2.0F);
        			} else {
        				graphDef.comment(list[i] + "\\c");
        			}
        		}
        	}
        	*/
        };
        
        if (upLimitBreached != null) {
        	graphDef.comment("\\l");
        	graphDef.vrule(upLimitBreachedTs / 1000L, Color.RED, upLimitBreached + "\\c", 2.0F);
        	if (upLimitBreachedDetail != null) {
        		graphDef.comment(upLimitBreachedDetail + "\\c");
        	}
        	/*
        	String[] list = upLimitBreached.split("\\n");
        	for (int i = 0; i < list.length; i++) {
        		if (list[i] != null) {
        			if (i < 1) {
        				graphDef.vrule(upLimitBreachedTs / 1000L, Color.RED, list[i] + "\\c", 2.0F);
        			} else {
        				graphDef.comment(list[i] + "\\c");
        			}
        		}
        	}
        	*/
        }

        String graphName = "VM_" + profile.getServiceName() + "_" + 
        		DF_OUTNAME_DATETIME.format(new Date(startTs)) +
        		((direction == Direction.DOWN) ? "_DOWN" : (direction == Direction.UP) ? "_UP" : "");
        String fileName = outputPath + Util.getFileSeparator() + graphName + ".png";
        
        //graphDef.setFilename(fileName);
        graphDef.setFilename("-");
        graphDef.setImageFormat("PNG");
        graphDef.setImageInfo("<IMG SRC='%s' WIDTH='%d' HEIGHT='%d' ALT='" + graphName + "'>");
        //graphDef.setAltYMrtg(true);
        
        RrdGraph graph = new RrdGraph(graphDef);
        //BufferedImage bi = new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
        //graph.render(bi.getGraphics());

        File f = new File(fileName);
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
        
    	BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));
    	bos.write(graph.getRrdGraphInfo().getBytes());
    	bos.close();
        
        //String imgInfo = graph.getRrdGraphInfo().getImgInfo();
        //logger.info(imgInfo);
    }
    
    /**
	 * @param args
	 */
	public static void main(String[] args) {
		
		@SuppressWarnings("static-access")
		Option rrd_file_option = OptionBuilder.withArgName("name")
                .hasArg()
                .withDescription("use given name for rrd database file")
                .create("rrd_file");
		rrd_file_option.setRequired(true);
		
		@SuppressWarnings("static-access")
		Option out_dir_option = OptionBuilder.withArgName("name")
                .hasArg()
                .withDescription("use given name for out dir")
                .create("out_dir");
		
		@SuppressWarnings("static-access")
		Option rrdtool_file_option = OptionBuilder.withArgName("name")
                .hasArg()
                .withDescription("use given name for rrdtool src filename")
                .create("rrdtool_file");
		
		@SuppressWarnings("static-access")
		Option profile_option = OptionBuilder.withArgName("name")
                .hasArg()
                .withDescription("use given name for profile")
                .create("profile");
		profile_option.setRequired(true);
		
		@SuppressWarnings("static-access")
		Option year_option = OptionBuilder.withArgName("YYYY")
                .hasArg()
                .withDescription("use given YYYY for year")
                .create("year");
		year_option.setType(Integer.class);
		
		@SuppressWarnings("static-access")
		Option month_option = OptionBuilder.withArgName("MM")
                .hasArg()
                .withDescription("use given MM for month")
                .create("month");
		month_option.setType(Integer.class);
		
		@SuppressWarnings("static-access")
		Option day_option = OptionBuilder.withArgName("DD")
                .hasArg()
                .withDescription("use given DD for day")
                .create("day");
		day_option.setType(Integer.class);
		
		@SuppressWarnings("static-access")
		Option archive_in_name_option = OptionBuilder.withArgName("name")
                .hasArg()
                .withDescription("use given name for archive in/down name. default: traffic_in")
                .create("archive_in_name");
		
		@SuppressWarnings("static-access")
		Option archive_out_name_option = OptionBuilder.withArgName("name")
                .hasArg()
                .withDescription("use given name for archive out/up name. default: traffic_out")
                .create("archive_out_name");
		
		Option help_option = new Option( "help", "print this message" );

		Options options = new Options();
		options.addOption(rrd_file_option);
		options.addOption(out_dir_option);
		options.addOption(rrdtool_file_option);
		options.addOption(profile_option);
		options.addOption(year_option);
		options.addOption(month_option);
		options.addOption(day_option);
		options.addOption(archive_in_name_option);
		options.addOption(archive_out_name_option);
		options.addOption(help_option);
		
		CommandLineParser parser = new GnuParser();

		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException pe) {
			logger.error("Error parsing program arguments.", pe);
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
			buf.append(" -" + op.getOpt() + " " + op.getValue());
		}
		logger.info(buf);
		
		File rrdDbFile = new File(cmd.getOptionValue("rrd_file"));
		if (!rrdDbFile.exists()) {
			logger.debug("RrdDb file does not exist: " + rrdDbFile.getAbsolutePath());
		}
		
		File rrdDbToolFile = null;
		if (cmd.getOptionValue("rrdtool_file") != null) {
			rrdDbToolFile = new File(cmd.getOptionValue("rrdtool_file"));
			if (!rrdDbToolFile.exists()) {
				logger.error("rddtool input file does not exist: " + rrdDbToolFile.getAbsolutePath());
				System.exit(-1);
			}
		}
		
		File outDir = new File(cmd.getOptionValue("out_dir"));
		if (!outDir.exists()) {
			logger.error("Directory for output files must exist: " + outDir.getAbsolutePath());
			System.exit(-1);
		}
		
		Profile[] profileList = Profile.getProfileList(cmd.getOptionValue("profile"));
		if (profileList == null || profileList.length < 1) {
			String vpl = "";
			for (String p : Profile.SERVICE_NAME_LIST) {
				vpl += (" " + p);
			}
			logger.error("Invalid profile: " + cmd.getOptionValue("profile") + "! Valid profiles:" + vpl + ".");
			System.exit(-1);
		}
		
		int year = -1;
		int month = -1;
		int day = -1;

		if (cmd.hasOption("year") && cmd.hasOption("month") && cmd.hasOption("day")) {
			try {
				year = Integer.parseInt(cmd.getOptionValue("year"));
			} catch (NumberFormatException nfe) {
				logger.error("Invalid year: " + cmd.hasOption("year"), nfe);
				System.exit(-1);
			}
			
			try {
				month = Integer.parseInt(cmd.getOptionValue("month"));
				if (month < 1 || month > 12) {
					logger.error("Invalid month: " + month + ". Valid value: 1-12.");
					System.exit(-1);
				} else {
					month -= 1;
				}
			} catch (NumberFormatException nfe) {
				logger.error("Invalid month: " + cmd.getOptionValue("month"), nfe);
				System.exit(-1);
			}
			
			try {
				day = Integer.parseInt(cmd.getOptionValue("day"));
				if (day < 1 || day > 31) {
					logger.error("Invalid day: " + day + ". Valid value: 1-31.");
					System.exit(-1);
				}
			} catch (NumberFormatException nfe) {
				logger.error("Invalid day: " + cmd.getOptionValue("day"), nfe);
				System.exit(-1);
			}
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -1);
			if (logger.isDebugEnabled()) {
				logger.debug("YYYY/MM/DD not set. Using [" + DF_FULL.format(cal.getTime()) + "] for date.");
			}
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH);
			day = cal.get(Calendar.DATE);
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
			logger.error(null, ioe);
		} finally {
			if (rrdDb != null) {
				try {
					logger.info("Closing rrdDb...");
					rrdDb.close();
				} catch (IOException ioe) {}
			}
		}
		logger.info("Exiting...");
	}
}
