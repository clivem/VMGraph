/**
 * 
 */
package uk.org.vacuumtube.hibernate;

import java.util.Date;

/**
 * @author clivem
 *
 */
public abstract class Persistable {

	protected Long id = null;
	protected Date updated = null;
	protected Date created = null;
	
	/**
	 * 
	 */
	public Persistable() {
	}
	
	/**
	 * @param createdMillis
	 */
	public Persistable(long createdMillis) {
		// MySQL "fix" (truncate the millis) which are not stored by their timestamp
		this.created = new Date((createdMillis / 1000L) * 1000L);
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the updated
	 */
	public Date getUpdated() {
		return updated;
	}

	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
}
