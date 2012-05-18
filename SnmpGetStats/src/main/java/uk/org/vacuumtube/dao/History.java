/**
 * 
 */
package uk.org.vacuumtube.dao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import uk.org.vacuumtube.util.DateFormatFactory;

/**
 * @author clivem
 *
 */
@Entity
@Table(name="history")
public class History extends AbstractTimestampEntity implements PersistableEntity, Serializable {

	private static final long serialVersionUID = -3789672959688552347L;
	
	protected Long historyId = null;
	protected int sportsId;
	protected long eventId;
	protected Date settledDate;
	protected String fullDescription;
	protected Date scheduledOffDate;
	protected String event;
	protected Date actualOffDate;
	protected long selectionId;
	protected String selection;
	protected double odds;
	protected long numberBets;
	protected double volumeMatched;
	protected Date latestTaken;
	protected Date firstTaken;
	protected int winFlag;
	protected String inPlay;

	/**
	 * 
	 */
	public History() {
		super();
	}
	
	/**
	 * @param sportsId
	 * @param eventId
	 * @param settledDate
	 * @param fullDescription
	 * @param scheduledOffDate
	 * @param event
	 * @param actualOffDate
	 * @param selectionId
	 * @param selection
	 * @param odds
	 * @param numberBets
	 * @param volumeMatched
	 * @param latestTaken
	 * @param firstTaken
	 * @param winFlag
	 * @param inPlay
	 */
	public History(int sportsId, long eventId,
			Date settledDate, String fullDescription, Date scheduledOffDate,
			String event, Date actualOffDate, long selectionId,
			String selection, double odds, long numberBets,
			double volumeMatched, Date latestTaken, Date firstTaken,
			int winFlag, String inPlay) {
		this();
		this.sportsId = sportsId;
		this.eventId = eventId;
		this.settledDate = settledDate;
		this.fullDescription = fullDescription;
		this.scheduledOffDate = scheduledOffDate;
		this.event = event;
		this.actualOffDate = actualOffDate;
		this.selectionId = selectionId;
		this.selection = selection;
		this.odds = odds;
		this.numberBets = numberBets;
		this.volumeMatched = volumeMatched;
		this.latestTaken = latestTaken;
		this.firstTaken = firstTaken;
		this.winFlag = winFlag;
		this.inPlay = inPlay;
	}

	/**
	 * @return the historyId
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "history_id", unique = true, nullable = false)
	public Long getHistoryId() {
		return historyId;
	}

	/**
	 * @param historyId the historyId to set
	 */
	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
	}

	/**
	 * @return the sportsId
	 */
	@Column(name = "sports_id", nullable = false)
	public int getSportsId() {
		return sportsId;
	}

	/**
	 * @param sportsId the sportsId to set
	 */
	public void setSportsId(int sportsId) {
		this.sportsId = sportsId;
	}

	/**
	 * @return the eventId
	 */
	@Column(name = "event_id", nullable = false)
	public long getEventId() {
		return eventId;
	}

	/**
	 * @param eventId the eventId to set
	 */
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	/**
	 * @return the settledDate
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "settled_date", nullable = true, length = 19)
	public Date getSettledDate() {
		return settledDate;
	}

	/**
	 * @param settledDate the settledDate to set
	 */
	public void setSettledDate(Date settledDate) {
		this.settledDate = settledDate;
	}

	/**
	 * @return the fullDescription
	 */
	@Column(name = "full_description", nullable = false)
	public String getFullDescription() {
		return fullDescription;
	}

	/**
	 * @param fullDescription the fullDescription to set
	 */
	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	/**
	 * @return the scheduledOffDate
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "scheduled_off_date", nullable = false, length = 19)
	public Date getScheduledOffDate() {
		return scheduledOffDate;
	}

	/**
	 * @param scheduledOffDate the scheduledOffDate to set
	 */
	public void setScheduledOffDate(Date scheduledOffDate) {
		this.scheduledOffDate = scheduledOffDate;
	}

	/**
	 * @return the event
	 */
	@Column(name = "event", nullable = false)
	public String getEvent() {
		return event;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(String event) {
		this.event = event;
	}

	/**
	 * @return the actualOffDate
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "actual_off_date", nullable = true, length = 19)
	public Date getActualOffDate() {
		return actualOffDate;
	}

	/**
	 * @param actualOffDate the actualOffDate to set
	 */
	public void setActualOffDate(Date actualOffDate) {
		this.actualOffDate = actualOffDate;
	}

	/**
	 * @return the selectionId
	 */
	@Column(name = "selection_id", nullable = false)
	public long getSelectionId() {
		return selectionId;
	}

	/**
	 * @param selectionId the selectionId to set
	 */
	public void setSelectionId(long selectionId) {
		this.selectionId = selectionId;
	}

	/**
	 * @return the selection
	 */
	@Column(name = "selection", nullable = false)
	public String getSelection() {
		return selection;
	}

	/**
	 * @param selection the selection to set
	 */
	public void setSelection(String selection) {
		this.selection = selection;
	}

	/**
	 * @return the odds
	 */
	@Column(name = "odds", nullable = false)
	public double getOdds() {
		return odds;
	}

	/**
	 * @param odds the odds to set
	 */
	public void setOdds(double odds) {
		this.odds = odds;
	}

	/**
	 * @return the numberBets
	 */
	@Column(name = "number_bets", nullable = false)
	public long getNumberBets() {
		return numberBets;
	}

	/**
	 * @param numberBets the numberBets to set
	 */
	public void setNumberBets(long numberBets) {
		this.numberBets = numberBets;
	}

	/**
	 * @return the volumeMatched
	 */
	@Column(name = "volume_matched", nullable = false)
	public double getVolumeMatched() {
		return volumeMatched;
	}

	/**
	 * @param volumeMatched the volumeMatched to set
	 */
	public void setVolumeMatched(double volumeMatched) {
		this.volumeMatched = volumeMatched;
	}

	/**
	 * @return the latestTaken
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "latest_taken", nullable = false, length = 19)
	public Date getLatestTaken() {
		return latestTaken;
	}

	/**
	 * @param latestTaken the latestTaken to set
	 */
	public void setLatestTaken(Date latestTaken) {
		this.latestTaken = latestTaken;
	}

	/**
	 * @return the firstTaken
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "first_taken", nullable = false, length = 19)
	public Date getFirstTaken() {
		return firstTaken;
	}

	/**
	 * @param firstTaken the firstTaken to set
	 */
	public void setFirstTaken(Date firstTaken) {
		this.firstTaken = firstTaken;
	}

	/**
	 * @return the winFlag
	 */
	@Column(name = "win_flag", nullable = false)
	public int getWinFlag() {
		return winFlag;
	}

	/**
	 * @param winFlag the winFlag to set
	 */
	public void setWinFlag(int winFlag) {
		this.winFlag = winFlag;
	}

	/**
	 * @return the inPlay
	 */
	@Column(name = "in_play", nullable = false)
	public String getInPlay() {
		return inPlay;
	}

	/**
	 * @param inPlay the inPlay to set
	 */
	public void setInPlay(String inPlay) {
		this.inPlay = inPlay;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actualOffDate == null) ? 0 : actualOffDate.hashCode());
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result + (int) (eventId ^ (eventId >>> 32));
		result = prime * result
				+ ((firstTaken == null) ? 0 : firstTaken.hashCode());
		result = prime * result
				+ ((fullDescription == null) ? 0 : fullDescription.hashCode());
		/*
		result = prime * result
				+ ((historyId == null) ? 0 : historyId.hashCode());
		*/
		result = prime * result + ((inPlay == null) ? 0 : inPlay.hashCode());
		result = prime * result
				+ ((latestTaken == null) ? 0 : latestTaken.hashCode());
		result = prime * result + (int) (numberBets ^ (numberBets >>> 32));
		long temp;
		temp = Double.doubleToLongBits(odds);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((scheduledOffDate == null) ? 0 : scheduledOffDate.hashCode());
		result = prime * result
				+ ((selection == null) ? 0 : selection.hashCode());
		result = prime * result + (int) (selectionId ^ (selectionId >>> 32));
		result = prime * result
				+ ((settledDate == null) ? 0 : settledDate.hashCode());
		result = prime * result + sportsId;
		temp = Double.doubleToLongBits(volumeMatched);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + winFlag;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof History)) {
			return false;
		}
		History other = (History) obj;
		if (actualOffDate == null) {
			if (other.actualOffDate != null) {
				return false;
			}
		} else if (!actualOffDate.equals(other.actualOffDate)) {
			return false;
		}
		if (event == null) {
			if (other.event != null) {
				return false;
			}
		} else if (!event.equals(other.event)) {
			return false;
		}
		if (eventId != other.eventId) {
			return false;
		}
		if (firstTaken == null) {
			if (other.firstTaken != null) {
				return false;
			}
		} else if (!firstTaken.equals(other.firstTaken)) {
			return false;
		}
		if (fullDescription == null) {
			if (other.fullDescription != null) {
				return false;
			}
		} else if (!fullDescription.equals(other.fullDescription)) {
			return false;
		}
		/*
		if (historyId == null) {
			if (other.historyId != null) {
				return false;
			}
		} else if (!historyId.equals(other.historyId)) {
			return false;
		}
		*/
		if (inPlay == null) {
			if (other.inPlay != null) {
				return false;
			}
		} else if (!inPlay.equals(other.inPlay)) {
			return false;
		}
		if (latestTaken == null) {
			if (other.latestTaken != null) {
				return false;
			}
		} else if (!latestTaken.equals(other.latestTaken)) {
			return false;
		}
		if (numberBets != other.numberBets) {
			return false;
		}
		if (Double.doubleToLongBits(odds) != Double
				.doubleToLongBits(other.odds)) {
			return false;
		}
		if (scheduledOffDate == null) {
			if (other.scheduledOffDate != null) {
				return false;
			}
		} else if (!scheduledOffDate.equals(other.scheduledOffDate)) {
			return false;
		}
		if (selection == null) {
			if (other.selection != null) {
				return false;
			}
		} else if (!selection.equals(other.selection)) {
			return false;
		}
		if (selectionId != other.selectionId) {
			return false;
		}
		if (settledDate == null) {
			if (other.settledDate != null) {
				return false;
			}
		} else if (!settledDate.equals(other.settledDate)) {
			return false;
		}
		if (sportsId != other.sportsId) {
			return false;
		}
		if (Double.doubleToLongBits(volumeMatched) != Double
				.doubleToLongBits(other.volumeMatched)) {
			return false;
		}
		if (winFlag != other.winFlag) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("History[historyId=");
		buf.append(historyId);
		buf.append(", sportsId=");
		buf.append(sportsId);
		buf.append(", eventId=");
		buf.append(eventId);
		buf.append(", settledDate=");
		buf.append(settledDate);
		buf.append(", fullDescription=");
		buf.append(fullDescription);
		buf.append(", scheduledOffDate=");
		buf.append(scheduledOffDate);
		buf.append(", event=");
		buf.append(event);
		buf.append(", actualOffDate=");
		buf.append(actualOffDate);
		buf.append(", selectionId=");
		buf.append(selectionId);
		buf.append(", selection=");
		buf.append(selection);
		buf.append(", odds=");
		buf.append(odds);
		buf.append(", numberBets=");
		buf.append(numberBets);
		buf.append(", volumeMatched=");
		buf.append(volumeMatched);
		buf.append(", latestTaken=");
		buf.append(latestTaken);
		buf.append(", firstTaken=");
		buf.append(firstTaken);
		buf.append(", winFlag=");
		buf.append(winFlag);
		buf.append(", inPlay=");
		buf.append(inPlay);
		buf.append(", created=");
		buf.append(DateFormatFactory.format(DateFormatFactory.DF_FULL, created));
		buf.append(", updated=");
		buf.append(DateFormatFactory.format(DateFormatFactory.DF_FULL, updated));
		buf.append("]");
		return buf.toString();
	}
}
