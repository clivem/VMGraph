/**
 * 
 */
package uk.org.vacuumtube.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import uk.org.vacuumtube.util.DateFormatFactory;

/**
 * @author clivem
 *
 */
@Entity
@Table(name="history_note")
public class HistoryNote extends AbstractTimestampEntity implements PersistableEntity, Serializable {

	private static final long serialVersionUID = 8394854265499309836L;

	private Long historyNoteId;
	private History history;
	private String note;
	
	/**
	 * 
	 */
	public HistoryNote() {
		super();
	}
	
	/**
	 * @param note
	 */
	public HistoryNote(String note) {
		this();
		this.note = note;
	}

	/**
	 * @return the historyNoteId
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "history_note_id", unique = true, nullable = false)
	public Long getHistoryNoteId() {
		return historyNoteId;
	}

	/**
	 * @param historyNoteId the historyNoteId to set
	 */
	public void setHistoryNoteId(Long historyNoteId) {
		this.historyNoteId = historyNoteId;
	}

	/**
	 * @return the note
	 */
	@Column(name = "note", nullable = false)
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
	 * @return the history
	 */
	@ManyToOne()
	//@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "history_id", nullable = false)
	public History getHistory() {
		return history;
	}

	/**
	 * @param history the history to set
	 */
	public void setHistory(History history) {
		this.history = history;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		/*
		result = prime * result
				+ ((historyNoteId == null) ? 0 : historyNoteId.hashCode());
				*/
		result = prime * result + ((history == null) ? 0 : history.hashCode());
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
		if (!(obj instanceof HistoryNote)) {
			return false;
		}
		HistoryNote other = (HistoryNote) obj;
		if (history == null) {
			if (other.history != null) {
				return false;
			}
		} else if (!history.equals(other.history)) {
			return false;
		}
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
		buf.append("HistoryNote[historyNoteId=");
		buf.append(historyNoteId);
		buf.append(", note=");
		buf.append(note);
		buf.append(", created=");
		buf.append(DateFormatFactory.format(DateFormatFactory.DF_FULL, created));
		buf.append(", updated=");
		buf.append(DateFormatFactory.format(DateFormatFactory.DF_FULL, updated));
		buf.append("]");
		return buf.toString();
	}
}
