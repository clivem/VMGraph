/**
 * 
 */
package uk.org.vacuumtube.dao;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import uk.org.vacuumtube.util.Format;

/**
 * @author clivem
 *
 */
@Entity
@Table(name="STATS")
public class Stats extends AbstractTimestampEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "STATSID")
	protected Long id = null;

	@Basic
	@Column(name = "MILLIS", nullable = false)
	protected Long millis = null;
	
	@Basic
	@Column(name = "RXBYTES", nullable = false)
	protected Long rxBytes = null;
	
	@Basic
	@Column(name = "TXBYTES", nullable = false)
	protected Long txBytes = null;
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER, mappedBy = "stats")
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@Fetch(FetchMode.JOIN)
	protected Collection<Notes> notes = null;

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
		this.notes = new LinkedHashSet<Notes>();
	}
	
	/**
	 * 
	 */
	public Stats() {
	}

	/**
	 * @return the statsId
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long statsId) {
		this.id = id;
	}

	/**
	 * @return the millis
	 */
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
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Stats[statsId=");
		buf.append(id);
		buf.append(", millis=");
		buf.append(millis);
		buf.append(", rxBytes=");
		buf.append(rxBytes);
		buf.append(", txBytes=");
		buf.append(txBytes);
		buf.append(", created=");
		buf.append(Format.formatDateFull(created));
		buf.append(", updated=");
		buf.append(Format.formatDateFull(updated));
		buf.append(", notes=");
		if (notes != null) {
			//buf.append(notes);
			buf.append("Notes[" + notes.size() + "]");
		} else {
			buf.append("null");
		}
		buf.append("]");
		return buf.toString();
	}	
}
