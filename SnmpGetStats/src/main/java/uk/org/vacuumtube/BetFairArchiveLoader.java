/**
 * 
 */
package uk.org.vacuumtube;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.StopWatch;

import uk.org.vacuumtube.dao.History;
import uk.org.vacuumtube.dao.HistoryMapper;
import uk.org.vacuumtube.dao.Sport;
import uk.org.vacuumtube.service.HistoryService;
import uk.org.vacuumtube.service.ServiceLocator;
import uk.org.vacuumtube.spring.ApplicationConfiguration;

/**
 * @author clivem
 *
 */
public class BetFairArchiveLoader {

	public final static Logger LOGGER = Logger.getLogger(BetFairArchiveLoader.class);
	
	/*
	 * Archive file format
	 * 
	 * EVENT_ID
	 * COUNTRY (horseracing only)
	 * FULL_DESCRIPTION
	 * COURSE (horseracing only)
	 * SCHEDULED_OFF 
	 * EVENT
	 * ACTUAL_OFF
	 * SELECTION
	 * SETTLED_DATE
	 * ODDS
	 * LATEST_TAKEN (when these odds were last matched on the selection)
	 * FIRST_TAKEN (when these odds were first matched on the selection)
	 * IN_PLAY (IP - In-Play, PE - Pre-Event, NI - Event did not go in-play)
	 * NUMBER_BETS (number of individual bets placed)
	 * VOLUME_MATCHED (sums the stakes of both back and lay bets)
	 * SPORTS_ID
	 * SELECTION_ID
	 * WIN_FLAG (1 if the selection was paid out as a full or partial winner, 0 otherwise)
	 */
	
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
		
		LOGGER.info("Starting...");
		
		for (String fileName : args) {
			processArchive(fileName, ctx, false);
		}

		LOGGER.info("Finished");
	}
	
	private final static void processArchive(String fileName, ApplicationContext ctx, boolean validate) {
		
		HistoryService hs = ServiceLocator.getHistoryService(ctx);
		
		LOGGER.info("Loading: " + fileName);
		
		StopWatch watch = new StopWatch();
		watch.start("Loading: " + fileName);
		
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(fileName));

			Map<Long, Sport> tempSportMap = new HashMap<Long, Sport>(100);
			
			HistoryMapper mapper = new HistoryMapper();
			int count = 0;
			
			List<History> historyList = new ArrayList<History>(1000);
			
			String line = null;
			while ((line = in.readLine()) != null) {
				++count;
				if (count > 1) {
					try {
						History history = mapper.parseRecord(line);
						
						/*
						 * Mapper will have generated a dummy sport object. 
						 * Make sure it exists in the db and set it on the history object.
						 */
						long sportsId = history.getSport().getSportId();
						Sport sport = tempSportMap.get(sportsId);
						if (sport == null) {
							sport = hs.getSportById(sportsId);
							if (sport == null) {
								sport = new Sport(sportsId, "UNKNOWN", true);
								hs.createSport(sport);
							}
							tempSportMap.put(sportsId, sport);
						}
						history.setSport(sport);
						
						//Sports.getInstance(history.getSportsId());
						//LOGGER.info("Row(" + count + "): " + history);
						if (!validate) {
							historyList.add(history);
							if (count % 1000 == 0) {
								hs.createHistory(historyList);
								historyList.clear();
							}
						}
					} catch (Exception e) {
						LOGGER.warn("Row(" + count + "): " + line, e);
					}
				} else {
					LOGGER.info("Row(" + count + "): " + line);
					mapper.parseHeader(line);
				}
			}

			if (!validate && historyList.size() > 0) {
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
