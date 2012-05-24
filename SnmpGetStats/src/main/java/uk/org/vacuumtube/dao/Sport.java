/**
 * 
 */
package uk.org.vacuumtube.dao;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;

import uk.org.vacuumtube.util.DateFormatFactory;

/**
 * @author clivem
 *
 */
@Entity
@Table(name="sport")
public class Sport extends AbstractTimestampEntity implements PersistableEntity, Serializable {

	private static final long serialVersionUID = -8105500570315088346L;

	private Long sportId;
	private String description;
	private boolean valid;
	private Collection<History> histories;
	
	/**
	 * 
	 */
	public Sport() {
		super();
	}
	
	/**
	 * @param sportId
	 * @param description
	 * @param valid
	 */
	public Sport(Long sportId, String description, boolean valid) {
		this();
		this.sportId = sportId;
		this.description = description;
		this.valid = valid;
	}

	/**
	 * @return the sportId
	 */
	@Id
	@Column(name = "sport_id", unique = true, nullable = false)
	public Long getSportId() {
		return sportId;
	}

	/**
	 * @param sportId the sportId to set
	 */
	public void setSportId(Long sportId) {
		this.sportId = sportId;
	}

	/**
	 * @return the description
	 */
	@Column(name = "description", nullable = false)
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the valid
	 */
	@Column(name = "is_valid", nullable = false)
	@org.hibernate.annotations.Type(type = "numeric_boolean")
	public boolean isValid() {
		return valid;
	}

	/**
	 * @param isValid the isValid to set
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	/**
	 * @return the histories
	 */
	//@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY, mappedBy = "sport")
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY, mappedBy = "sport")
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	//@Fetch(FetchMode.JOIN)
	public Collection<History> getHistories() {
		return histories;
	}

	/**
	 * @param histories the histories to set
	 */
	public void setHistories(Collection<History> histories) {
		this.histories = histories;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + (valid ? 1231 : 1237);
		result = prime * result + ((sportId == null) ? 0 : sportId.hashCode());
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
		if (!(obj instanceof Sport)) {
			return false;
		}
		Sport other = (Sport) obj;
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (valid != other.valid) {
			return false;
		}
		if (sportId == null) {
			if (other.sportId != null) {
				return false;
			}
		} else if (!sportId.equals(other.sportId)) {
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
		buf.append("Sport[sportId=");
		buf.append(sportId);
		buf.append(", description=");
		buf.append(description);
		buf.append(", valid=");
		buf.append(valid);
		buf.append(", created=");
		buf.append(DateFormatFactory.format(DateFormatFactory.DF_FULL, created));
		buf.append(", updated=");
		buf.append(DateFormatFactory.format(DateFormatFactory.DF_FULL, updated));
		buf.append(", notes=");
		if (histories != null) {
			//buf.append(histories);
			if (Hibernate.isInitialized(histories)) {
				buf.append("History[" + histories.size() + "]");
			} else {
				buf.append("History[PROXY NOT INITIALISED]");
			}
		} else {
			buf.append("null");
		}
		buf.append("]");
		return buf.toString();
	}
}
