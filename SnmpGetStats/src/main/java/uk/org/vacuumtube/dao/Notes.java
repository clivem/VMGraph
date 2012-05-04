/**
 * 
 */
package uk.org.vacuumtube.dao;

import uk.org.vacuumtube.util.Format;

/**
 * @author clivem
 *
 */
public class Notes extends Persistable {

	protected String note = null;
	protected Stats stats = null;
	
	/**
	 * @param note
	 */
	public Notes(Stats stats, String note) {
		super(System.currentTimeMillis());
		this.note = note;
		this.stats = stats;
	}

	/**
	 * 
	 */
	public Notes() {
		super();
	}

	/**
	 * @return the note
	 */
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
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
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
		if (created == null) {
			if (other.created != null) {
				return false;
			}
		} else if (!created.equals(other.created)) {
			return false;
		}
		if (note == null) {
			if (other.note != null) {
				return false;
			}
		} else if (!note.equals(other.note)) {
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
		buf.append("Notes[id=");
		buf.append(id);
		buf.append(", note=");
		buf.append(note);
		buf.append(", created=");
		buf.append(Format.formatDateFull(created));
		buf.append(", updated=");
		buf.append(Format.formatDateFull(updated));
		buf.append(", stats=");
		buf.append(((stats != null) ? "Stats[id=" + stats.getId() + "]" : "null"));
		buf.append("]");
		return buf.toString();
	}	
}
