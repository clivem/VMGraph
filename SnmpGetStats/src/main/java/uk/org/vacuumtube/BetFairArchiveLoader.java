/**
 * 
 */
package uk.org.vacuumtube;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;

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
		
		StopWatch watch = new StopWatch();
		watch.start("Loading: " + args[0]);
		
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(args[0]));

			HistoryMapper mapper = new HistoryMapper();
			int count = 0;
			
			String line = null;
			while ((line = in.readLine()) != null) {
				++count;
				if (count > 1) {
					try {
						History history = mapper.parse(line);
						//LOGGER.info("Row(" + count + "): " + history);
						hs.createHistory(history);
					} catch (ParseException ex) {
						LOGGER.warn("Row(" + count + "): " + line, ex);
					}
				} else {
					LOGGER.info("Row(" + count + "): " + line);
					mapper.parseHeader(line);
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
