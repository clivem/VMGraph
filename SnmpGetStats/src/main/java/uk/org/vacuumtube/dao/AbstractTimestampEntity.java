/**
 * 
 */
package uk.org.vacuumtube.dao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.Source;
import org.hibernate.annotations.SourceType;

/**
 * @author clivem
 *
 */
@MappedSuperclass
public abstract class AbstractTimestampEntity implements Serializable {

	private static final long serialVersionUID = -4375702600461553419L;

	protected Date created = null;
	protected Date updated = null;
	
	/**
	 * 
	 */
	protected AbstractTimestampEntity() {
		super();
		// MySQL "fix" (truncate the millis) which are not stored by their timestamp
		this.created = new Date((System.currentTimeMillis() / 1000L) * 1000L);
	}
	
	/**
	 * @return the created date
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", nullable = false, length = 19)
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the updated date
	 */
	@Version
	@Source(SourceType.VM)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated", nullable = false, length = 19)
	public Date getUpdated() {
		return updated;
	}

	/**
	 * @param updated
	 */
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
}
