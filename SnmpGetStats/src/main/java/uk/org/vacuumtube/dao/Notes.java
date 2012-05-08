/**
 * 
 */
package uk.org.vacuumtube.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import uk.org.vacuumtube.util.Format;

/**
 * @author clivem
 *
 */
@Entity
@Table(name="NOTES")
public class Notes extends AbstractTimestampEntity {

	private static final long serialVersionUID = 7877156363139225872L;
	
	protected Long notesId = null;
	protected String note = null;	
	protected Stats stats = null;
	
	/**
	 * 
	 */
	public Notes() {
	}

	/**
	 * @param note
	 */
	public Notes(Stats stats, String note) {
		this.note = note;
		this.stats = stats;
		// MySQL "fix" (truncate the millis) which are not stored by their timestamp
		this.created = new Date((System.currentTimeMillis() / 1000L) * 1000L);
	}

	/**
	 * @return the notesId
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="NOTESID", unique = true, nullable = false)
	public Long getNotesId() {
		return notesId;
	}

	/**
	 * @param notesId the notesId to set
	 */
	public void setNotesId(Long notesId) {
		this.notesId = notesId;
	}

	/**
	 * @return the note
	 */
	@Column(name = "NOTE", nullable = false)
	public String getNote() {
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the stats
	 */
	@ManyToOne()
	//@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATSID", nullable = false)
	public Stats getStats() {
		return stats;
	}

	/**
	 * @param stats the stats to set
	 */
	public void setStats(Stats stats) {
		this.stats = stats;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((updated == null) ? 0 : updated.hashCode());
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
		if (!(obj instanceof Notes)) {
			return false;
		}
		Notes other = (Notes) obj;
		if (note == null) {
			if (other.note != null) {
				return false;
			}
		} else if (!note.equals(other.note)) {
			return false;
		}
		if (created == null) {
			if (other.created != null) {
				return false;
			}
		} else if (!created.equals(other.created)) {
			return false;
		}
		if (updated == null) {
			if (other.updated != null) {
				return false;
			}
		} else if (!updated.equals(other.updated)) {
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
		buf.append("Notes[notesId=");
		buf.append(notesId);
		buf.append(", note=");
		buf.append(note);
		buf.append(", created=");
		buf.append(Format.formatDateFull(created));
		buf.append(", updated=");
		buf.append(Format.formatDateFull(updated));
		buf.append(", stats=");
		buf.append(((stats != null) ? ("Stats[statsId=" + stats.getStatsId() + "]") : "null"));
		buf.append("]");
		return buf.toString();
	}	

	/**
	 * @return
	 */
	public String shortDescription() {
		return "Notes[id=" + notesId + "]";
	}
}
