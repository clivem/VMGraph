/**
 * 
 */
package uk.org.vacuumtube.snmp;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

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
import org.rrd4j.DsType;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.snmp4j.PDU;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;

import uk.org.vacuumtube.util.ByteFormat;

/**
 * @author clivem
 *
 */
public class SnmpGetStats {
	
	protected static final Logger logger = Logger.getLogger(SnmpGetStats.class);

    protected static final DateFormat DF_FULL = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS z");

    public final static String DEFAULT_ARCHIVE_IN_NAME = "traffic_in";
    public final static String DEFAULT_ARCHIVE_OUT_NAME = "traffic_out";

	protected String snmpAddress = null;
	protected String snmpInOid = null;
	protected String snmpOutOid = null;
	protected String rrdDbFileName = null;
	
	protected RrdDb rrdDb = null;		

	protected long rxBytesPrev = -1;
	protected long txBytesPrev = -1;
	protected long timestampPrev = -1;
	
    protected String archiveInName;
    protected String archiveOutName;
	
	protected Timer timer = null;
	
	/**
	 * @param snmpAddress
	 * @param snmpInOid
	 * @param snmpOutOid
	 * @param rrdDbFileName
	 * @param archiveInName
	 * @param archiveOutName
	 */
	public SnmpGetStats(String snmpAddress, String snmpInOid, String snmpOutOid, String rrdDbFileName,
			String archiveInName, String archiveOutName) {
		if (logger.isTraceEnabled()) {
			logger.trace("SnmpGetStats(snmpAddress=" + snmpAddress + ", snmpInOid=" + snmpInOid + 
					", snmpOutOid=" + snmpOutOid + ", rrdDbFileName=" + rrdDbFileName + 
					", archiveInName=" + archiveInName + ", archiveOutName=" + archiveOutName + ")");
		}
		
		this.snmpAddress = snmpAddress;
		this.snmpInOid = snmpInOid;
		this.snmpOutOid = snmpOutOid;
		this.rrdDbFileName = rrdDbFileName;
		this.archiveInName = archiveInName;
		this.archiveOutName = archiveOutName;
	}
	
	/**
	 * @param snmpAddress
	 * @param snmpInOid
	 * @param snmpOutOid
	 * @param rrdDbFileName
	 */
	public SnmpGetStats(String snmpAddress, String snmpInOid, String snmpOutOid, String rrdDbFileName) {
		this(snmpAddress, snmpInOid, snmpOutOid, rrdDbFileName, 
				DEFAULT_ARCHIVE_IN_NAME, DEFAULT_ARCHIVE_OUT_NAME);
	}
	
	/**
	 * @param interval
	 * @throws IOException
	 */
	public void execute(int interval) throws IOException {
		if (logger.isTraceEnabled()) {
			logger.trace("SnmpGetStats.execute(interval=" + interval + ")");
		}
		
		if (rrdDb == null) {
			openRrdDb();
		}
		
		if (interval < 1) {
			logger.info("Polling once.");
			GetSnmpInterfaceStatisticsTask task = 
					new GetSnmpInterfaceStatisticsTask(snmpAddress, snmpInOid, snmpOutOid);
			task.run();
		} else {
			if (timer == null) {
				timer = new Timer("SnmpTimer", true);
			} else {
				logger.warn("Timer already created! Returning...");
				return;
			}

			long start = System.currentTimeMillis() + 60000;
			long mod = start % 60000;
			Date date = new Date(start - mod);
			
			logger.info("Service using timer interval: " + interval + "m. Starting at: " + DF_FULL.format(date));
			
			timer.scheduleAtFixedRate(new GetSnmpInterfaceStatisticsTask(snmpAddress, snmpInOid, snmpOutOid), date, interval * 60000);
		}
	}
	
	/**
	 * 
	 */
	public void close() {
		if (logger.isTraceEnabled()) {
			logger.trace("SnmpGetStats.close()");
		}
		
		if (timer != null) {
			timer.cancel();			
		}

		if (rrdDb != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Closing RRDB: " + rrdDbFileName);
			}
			try {
				rrdDb.close();
			} catch(IOException ioe) {}
		}
	}
	
	/**
	 * @return
	 * @throws IOException
	 */
	public RrdDb openRrdDb() throws IOException {
		if (logger.isTraceEnabled()) {
			logger.trace("SnmpGetStats.createRrdDb()");
		}
		
		File f = new File(rrdDbFileName);
		if (!f.exists()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Creating new RRDB: " + rrdDbFileName);
			}
			RrdDef rrdDef = new RrdDef(rrdDbFileName);
			rrdDef.setStep(300 / 5);
			rrdDef.addDatasource(archiveInName, DsType.COUNTER, 600 / 5, Double.NaN, Double.NaN);
			rrdDef.addDatasource(archiveOutName, DsType.COUNTER, 600 / 5, Double.NaN, Double.NaN);
			rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 1, 600 * 5);
			rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 6 * 5, 700);
			rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 24 * 5, 775);
			rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 288 * 5, 797);
			rrdDef.addArchive(ConsolFun.MAX, 0.5, 1, 600 * 5);
			rrdDef.addArchive(ConsolFun.MAX, 0.5, 6 * 5, 700);
			rrdDef.addArchive(ConsolFun.MAX, 0.5, 24 * 5, 775);
			rrdDef.addArchive(ConsolFun.MAX, 0.5, 288 * 5, 797);
			rrdDb = new RrdDb(rrdDef);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Opening existing RRDB: " + rrdDbFileName);
			}
			rrdDb = new RrdDb(rrdDbFileName);
		}
		
		return rrdDb;
	}
	
	/**
	 * @return the rxBytesPrev
	 */
	public long getRxBytesPrev() {
		return rxBytesPrev;
	}

	/**
	 * @param rxBytesPrev the rxBytesPrev to set
	 */
	public void setRxBytesPrev(long rxBytesPrev) {
		this.rxBytesPrev = rxBytesPrev;
	}

	/**
	 * @return the txBytesPrev
	 */
	public long getTxBytesPrev() {
		return txBytesPrev;
	}

	/**
	 * @param txBytesPrev the txBytesPrev to set
	 */
	public void setTxBytesPrev(long txBytesPrev) {
		this.txBytesPrev = txBytesPrev;
	}

	/**
	 * @return the timestampPrev
	 */
	public long getTimestampPrev() {
		return timestampPrev;
	}

	/**
	 * @param timestampPrev the timestampPrev to set
	 */
	public void setTimestampPrev(long timestampPrev) {
		this.timestampPrev = timestampPrev;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

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
		Option service_option = OptionBuilder.withArgName("interval")
                .hasArg()
                .withDescription("use given polling interval (minutes)")
                .create("service");
		service_option.setType(Integer.class);
		options.addOption(service_option);
		
		@SuppressWarnings("static-access")
		Option snmp_addr_option = OptionBuilder.withArgName("address")
                .hasArg()
                .withDescription("use given snmp address. eg. udp:192.168.0.1/161")
                .create("snmp_addr");
		options.addOption(snmp_addr_option);
		
		@SuppressWarnings("static-access")
		Option in_oid_option = OptionBuilder.withArgName("oid")
                .hasArg()
                .withDescription("use given oid for in/down. eg. .1.3.6.1.2.1.2.2.1.10.1")
                .create("in_oid");
		in_oid_option.setRequired(true);
		options.addOption(in_oid_option);
		
		@SuppressWarnings("static-access")
		Option out_oid_option = OptionBuilder.withArgName("oid")
                .hasArg()
                .withDescription("use given oid for out/up. eg. .1.3.6.1.2.1.2.2.1.16.1")
                .create("out_oid");
		out_oid_option.setRequired(true);
		options.addOption(out_oid_option);

		@SuppressWarnings("static-access")
		Option rrd_file_option = OptionBuilder.withArgName("filename")
                .hasArg()
                .withDescription("use given filename for rrd database file")
                .create("rrd_file");
		rrd_file_option.setRequired(true);
		options.addOption(rrd_file_option);
		
		@SuppressWarnings("static-access")
		Option archive_in_name_option = OptionBuilder.withArgName("name")
                .hasArg()
                .withDescription("use given name for archive in/down name. default: traffic_in")
                .create("archive_in_name");
		options.addOption(archive_in_name_option);
		
		@SuppressWarnings("static-access")
		Option archive_out_name_option = OptionBuilder.withArgName("name")
                .hasArg()
                .withDescription("use given name for archive out/up name. default: traffic_out")
                .create("archive_out_name");
		options.addOption(archive_out_name_option);
		
		Option help_option = new Option( "help", "print this message" );
		options.addOption(help_option);

		Option nostdin_option = new Option( "nostdin", "run as a service with stdin" );
		options.addOption(nostdin_option);

		CommandLineParser parser = new GnuParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException pe) {
			logger.error("Error parsing program arguments.", pe);
			System.exit(-1);
		}		
		
		if (cmd.hasOption("help")) {
			new HelpFormatter().printHelp("SnmpGetStats", options);
			System.exit(0);
		}

		StringBuffer buf = new StringBuffer();
		buf.append("Starting: SnmpGetStats");
		@SuppressWarnings("unchecked")
		Iterator<Option> it = cmd.iterator();
		while (it.hasNext()) {
			Option op = it.next();
			buf.append(" -" + op.getOpt() + ((op.getValue() != null) ? " " + op.getValue() : ""));
		}
		logger.info(buf);
		
		SnmpGetStats service = null;
		try {
			service = new SnmpGetStats(
					cmd.getOptionValue("snmp_addr"),
					cmd.getOptionValue("in_oid"),
					cmd.getOptionValue("out_oid"),
					cmd.getOptionValue("rrd_file"),
					cmd.hasOption("archive_in_name") ? cmd.getOptionValue("archive_in_name") : DEFAULT_ARCHIVE_IN_NAME, 
					cmd.hasOption("archive_out_name") ? cmd.getOptionValue("archive_out_name") : DEFAULT_ARCHIVE_OUT_NAME
					);
						
			if (!cmd.hasOption("service")) {
				service.execute(0);
			} else {
				int minutes = -1;
				try {
					minutes = Integer.parseInt(cmd.getOptionValue("service"));
					service.execute(minutes);
					if (cmd.hasOption("nostdin")) {
						try {
							while (true) {
								// sleep for an hour
								Thread.sleep(1000 * 60 * 60);
							}
						} catch (InterruptedException ie) {
							logger.warn(null, ie);
						}
					} else { // interactive
						Scanner scanner = new Scanner(System.in);
						while (true) {
							String input = scanner.nextLine();
				        	if(input == null || "quit".equals(input.trim())) {
				        		break;
				        	}
						}
						/*
						try {
							BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
							while (true) {
								String cmdLn = br.readLine();
								if (cmdLn.equals("quit")) {
									break;
								}
							}
						} catch (IOException ioe) {
							logger.warn(null, ioe);
						}
						*/
					}
				} catch (NumberFormatException nfe) {
					logger.error("Invalid number given for service polling interval: " + cmd.getOptionValue("service"), nfe);
				}
			}			
		} catch (IOException ioe) {
			logger.error(null, ioe);
		} finally {
			if (service != null) {
				service.close();
			}
		}
		logger.info("Exiting...");
	}

	/**
	 * @author clivem
	 *
	 */
	class GetSnmpInterfaceStatisticsTask extends TimerTask {

		//final Logger logger = Logger.getLogger(GetSnmpInterfaceStatisticsTask.class);
		
		private String address;
		private OID bytes_in_oid;
		private OID bytes_out_oid;
		
		/**
		 * @param address
		 * @param bytes_in_oid
		 * @param bytes_out_oid
		 */
		GetSnmpInterfaceStatisticsTask(String address, String bytes_in_oid, String bytes_out_oid) {
			this.address = address;
			this.bytes_in_oid = new OID(bytes_in_oid);
			this.bytes_out_oid = new OID(bytes_out_oid);

			if (logger.isTraceEnabled()) {
				logger.trace("GetSnmpInterfaceStatisticsTask(address=" + address + ", bytes_in_oid=" + bytes_in_oid + 
						", bytes_out_oid=" + bytes_out_oid + ")");
			}
		}
		
		/* (non-Javadoc)
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run() {
			if (logger.isTraceEnabled()) {
				logger.trace("GetSnmpInterfaceStatisticsTask.run(): START");
			}
			
			long timestamp = -1;
			long rxBytes = -1;
			long txBytes = -1;
			SnmpClient snmpClient = null;
			
			try {
				snmpClient = new SnmpClient(address);
				ResponseEvent event = snmpClient.get(new OID[] {bytes_in_oid, bytes_out_oid});
				if (event != null) {
					PDU response = event.getResponse();
					if (response != null) {
						timestamp = System.currentTimeMillis();
						int size = response.size();
						if (size > 0) {
							String logDebug = "Timestamp: " + DF_FULL.format(new Date(timestamp));
							for (int i = 0; i < size; i++) {
								OID oid = response.get(i).getOid();
								Variable var = response.get(i).getVariable();
								if (oid.equals(bytes_in_oid)) {
									rxBytes = var.toLong();
									logDebug += ", Bytes In: " + rxBytes;
								} else if (oid.equals(bytes_out_oid)) {
									txBytes = var.toLong();
									logDebug += ", Bytes Out: " + txBytes;
								} else {
									logger.debug(oid + " " + var);
									logDebug += ", oid: " + oid + " var: " + var;
								}
							}
							
							if (logger.isDebugEnabled()) {
								logger.debug(logDebug);
							}
							
							if (txBytes > -1 && rxBytes > -1) {
								if (rrdDb != null) {
									try {
										String update = Math.round(timestamp / 1000.0) + ":" + rxBytes + ":" + txBytes;
										if (logger.isTraceEnabled()) {
											logger.trace("RRD Update: " + update);
										}
										rrdDb.createSample().setAndUpdate(update);
									} catch (Exception ioe) {
										logger.warn("Error updating RrdDb!", ioe);
									}
								}
								
								if (logger.isTraceEnabled()) {
									long updateTs = rrdDb.getLastUpdateTime();
									logger.trace("RRD Last Update Time: (" + updateTs + ") " + 
											DF_FULL.format(updateTs * 1000L));
								}
								
								if (logger.isTraceEnabled()) {
									logger.trace("Rx: " + ByteFormat.humanReadableByteCount(rxBytes, true) +
											" (" + ByteFormat.humanReadableByteCount(rxBytes, false) + ")" +
											", Tx: " + ByteFormat.humanReadableByteCount(txBytes, true) +
											" (" + ByteFormat.humanReadableByteCount(txBytes, false) + ")");
								}
								
								if (rxBytesPrev > -1 && txBytesPrev > -1) {
									long time = timestamp - timestampPrev;
									long seconds = Math.round((time / 1000.0));
									if (seconds > 0) {
										long rxbps = ((rxBytes - rxBytesPrev) * 8) / seconds;
										long txbps = ((txBytes - txBytesPrev) * 8) / seconds;

										if (logger.isDebugEnabled()) {
											logger.debug("Delta: " + time + "ms (" + seconds + "s)" + 
													", Rx: " + ByteFormat.humanReadableByteCount(rxBytes - rxBytesPrev, true) +
													" (" + ByteFormat.humanReadableByteCount(rxBytes - rxBytesPrev, false) + ") " +
													ByteFormat.humanReadableBitCount(rxbps) +
													", Tx: " + ByteFormat.humanReadableByteCount(txBytes - txBytesPrev, true) +
													" (" + ByteFormat.humanReadableByteCount(txBytes - txBytesPrev, false) + ") " +
													ByteFormat.humanReadableBitCount(txbps));
										}
									}
								}

								rxBytesPrev = rxBytes;
								txBytesPrev = txBytes;
								timestampPrev = timestamp;
							}
						}
					}
				} else {
					logger.warn("Received null ResponseEvent!");
				}
			} catch (IOException ioe) {
				logger.warn(null, ioe);
			} finally {
				if (snmpClient != null) {
					try {
						snmpClient.stop();
					} catch (IOException ioe) {}
				}
			}
			if (logger.isTraceEnabled()) {
				logger.trace("GetSnmpInterfaceStatisticsTask.run(): END");
			}
		}
	}
}
