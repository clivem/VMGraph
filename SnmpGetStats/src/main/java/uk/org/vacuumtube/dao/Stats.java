/**
 * 
 */
package uk.org.vacuumtube.dao;

import java.util.HashSet;
import java.util.Set;

import uk.org.vacuumtube.hibernate.Persistable;
import uk.org.vacuumtube.util.Format;

/**
 * @author clivem
 *
 */
public class Stats extends Persistable {

    //protected final static DateFormat DF_FULL = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss,SSS z");

	//protected Long id = null;
	protected Long millis = null;
	protected Long rxBytes = null;
	protected Long txBytes = null;
	//protected Date created = null;
	//protected Date updated = null;
	
	protected Set<Notes> notes = new HashSet<Notes>();

	/**
	 * @param millis
	 * @param rxBytes
	 * @param txBytes
	 */
	public Stats(Long millis, Long rxBytes, Long txBytes) {
		super(millis);
		this.millis = millis;
		this.rxBytes = rxBytes;
		this.txBytes = txBytes;
	}
	
	/**
	 * 
	 */
	public Stats() {
	}

	/**
	 * @return the id
	 *
	public Long getId() {
		return id;
	}
	*/

	/**
	 * @param id the id to set
	 *
	public void setId(Long id) {
		this.id = id;
	}
	*/

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
	 * @return the created
	 *
	public Date getCreated() {
		return created;
	}
	*/

	/**
	 * @param created the created to set
	 *
	public void setCreated(Date created) {
		this.created = created;
	}
	*/

	/**
	 * @return the updated
	 *
	public Date getUpdated() {
		return updated;
	}
	*/

	/**
	 * @param updated the updated to set
	 *
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	*/

	/**
	 * @return the notes
	 */
	public Set<Notes> getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(Set<Notes> notes) {
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
		buf.append("Stats[id=");
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
		buf.append(notes);
		buf.append("]");
		return buf.toString();
	}	
}
