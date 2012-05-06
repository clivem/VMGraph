/**
 * 
 */
package uk.org.vacuumtube.dao;

import java.util.Date;

import javax.persistence.Basic;
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
public abstract class AbstractTimestampEntity implements Persistable {

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED", nullable = false)
	protected Date created;

	@Version
	@Source(SourceType.VM)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED", nullable = false)
	protected Date updated;

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
}
