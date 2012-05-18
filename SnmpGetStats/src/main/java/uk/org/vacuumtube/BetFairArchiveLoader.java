/**
 * 
 */
package uk.org.vacuumtube;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.StopWatch;

import uk.org.vacuumtube.dao.History;
import uk.org.vacuumtube.dao.HistoryMapper;
import uk.org.vacuumtube.service.HistoryService;
import uk.org.vacuumtube.service.ServiceLocator;
import uk.org.vacuumtube.spring.ApplicationConfiguration;

/**
 * @author clivem
 *
 */
public class BetFairArchiveLoader {

	public final static Logger LOGGER = Logger.getLogger(BetFairArchiveLoader.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Usage: BetFairArchiveLoader <csvFileName>");
			System.exit(-1);
		}
		
		final AnnotationConfigApplicationContext ctx = 
				new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		ctx.registerShutdownHook();
		
		HistoryService hs = ServiceLocator.getHistoryService(ctx);
		
		for (String fileName : args) {
			StopWatch watch = new StopWatch();
			watch.start("Loading: " + args[0]);
			
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(fileName));
	
				HistoryMapper mapper = new HistoryMapper();
				int count = 0;
				
				List<History> historyList = new ArrayList<History>(1000);
				
				String line = null;
				while ((line = in.readLine()) != null) {
					++count;
					if (count > 1) {
						try {
							History history = mapper.parseRecord(line);
							//LOGGER.info("Row(" + count + "): " + history);
							//hs.createHistory(history);
							historyList.add(history);
							if (count % 1000 == 0) {
								hs.createHistory(historyList);
								historyList.clear();
							}
						} catch (Exception e) {
							LOGGER.warn("Row(" + count + "): " + line, e);
						}
					} else {
						LOGGER.info("Row(" + count + "): " + line);
						mapper.parseHeader(line);
					}
				}
	
				if (historyList.size() > 0) {
					try {
						hs.createHistory(historyList);
						historyList.clear();
					} catch (Exception ex) {
						LOGGER.warn(null, ex);
					}
				}
				
				in.close();
				watch.stop();
				LOGGER.info("Finished. Processed " + count + " rows in " + watch.getLastTaskTimeMillis() + "ms.");
			} catch (Exception e) {
				LOGGER.warn(null, e);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (Exception e) {}
				}
			}
		}
	}
}
