/**
 * 
 */
package uk.org.vacuumtube.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;

import uk.org.vacuumtube.util.DateFormatFactory;

/**
 * @author clivem
 *
 */
@Entity
@Table(name="stats")
@FetchProfile(name = "stats-with-notes", fetchOverrides = {
	@FetchProfile.FetchOverride(entity = Stats.class, association = "notes", mode = FetchMode.JOIN)
})
public class Stats extends AbstractTimestampEntity implements uk.org.vacuumtube.dao.PersistableEntity, Serializable {

	private static final long serialVersionUID = 2247407172964283263L;

	protected Long statsId = null;
	protected Long millis = null;
	protected Long rxBytes = null;
	protected Long txBytes = null;
	protected Collection<Notes> notes = null;

	/**
	 * 
	 */
	public Stats() {
	}

	/**
	 * @param millis
	 * @param rxBytes
	 * @param txBytes
	 */
	public Stats(Long millis, Long rxBytes, Long txBytes) {
		this.millis = millis;
		this.rxBytes = rxBytes;
		this.txBytes = txBytes;
		// MySQL "fix" (truncate the millis) which are not stored by their timestamp
		this.created = new Date((millis / 1000L) * 1000L);
		//this.notes = new LinkedHashSet<Notes>();
	}
	
	/**
	 * @return the statsId
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stats_id", unique = true, nullable = false)
	public Long getStatsId() {
		return statsId;
	}

	/**
	 * @param statsId the statsId to set
	 */
	public void setStatsId(Long statsId) {
		this.statsId = statsId;
	}

	/**
	 * @return the millis
	 */
	@Column(name = "millis", nullable = false)
	public Long getMillis() {
		return millis;
	}

	/**
	 * @param millis the millis to set
	 */
	public void setMillis(Long millis) {
		this.millis = millis;
	}

	/**
	 * @return the rxBytes
	 */
	@Column(name = "rx_bytes", nullable = false)
	public Long getRxBytes() {
		return rxBytes;
	}

	/**
	 * @param rxBytes the rxBytes to set
	 */
	public void setRxBytes(Long rxBytes) {
		this.rxBytes = rxBytes;
	}

	/**
	 * @return the txBytes
	 */
	@Column(name = "tx_bytes", nullable = false)
	public Long getTxBytes() {
		return txBytes;
	}

	/**
	 * @param txBytes the txBytes to set
	 */
	public void setTxBytes(Long txBytes) {
		this.txBytes = txBytes;
	}

	/**
	 * @return the notes
	 */
	//@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY, mappedBy = "stats")
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY, mappedBy = "stats")
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	//@Fetch(FetchMode.JOIN)
	public Collection<Notes> getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(Collection<Notes> notes) {
		this.notes = notes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((millis == null) ? 0 : millis.hashCode());
		result = prime * result + ((rxBytes == null) ? 0 : rxBytes.hashCode());
		result = prime * result + ((txBytes == null) ? 0 : txBytes.hashCode());
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
		if (!(obj instanceof Stats)) {
			return false;
		}
		Stats other = (Stats) obj;
		if (millis == null) {
			if (other.millis != null) {
				return false;
			}
		} else if (!millis.equals(other.millis)) {
			return false;
		}
		if (rxBytes == null) {
			if (other.rxBytes != null) {
				return false;
			}
		} else if (!rxBytes.equals(other.rxBytes)) {
			return false;
		}
		if (txBytes == null) {
			if (other.txBytes != null) {
				return false;
			}
		} else if (!txBytes.equals(other.txBytes)) {
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
		buf.append("Stats[statsId=");
		buf.append(statsId);
		buf.append(", millis=");
		buf.append(millis);
		buf.append(", rxBytes=");
		buf.append(rxBytes);
		buf.append(", txBytes=");
		buf.append(txBytes);
		buf.append(", created=");
		buf.append(DateFormatFactory.format(DateFormatFactory.DF_FULL, created));
		buf.append(", updated=");
		buf.append(DateFormatFactory.format(DateFormatFactory.DF_FULL, updated));
		buf.append(", notes=");
		if (notes != null) {
			//buf.append(notes);
			if (Hibernate.isInitialized(notes)) {
				buf.append("Notes[" + notes.size() + "]");
			} else {
				buf.append("Notes[PROXY NOT INITIALISED]");
			}
		} else {
			buf.append("null");
		}
		buf.append("]");
		return buf.toString();
	}	
	
	/**
	 * @return
	 */
	public String shortDescription() {
		return "Stats[id=" + statsId + "]";
	}
	
	/**
	 * @param note
	 */
	public Notes addNote(String note) {
		if (notes == null) {
			notes = new LinkedHashSet<Notes>();
		}
		Notes n = new Notes(this, note);
		notes.add(n);
		return n;
	}
	
	/**
	 * @param note
	 */
	public void removeNote(Notes note) {
		if (notes != null) {
			notes.remove(note);
		}
		note.setStats(null);
	}
}
