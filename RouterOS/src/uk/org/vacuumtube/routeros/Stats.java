/**
 * 
 */
package uk.org.vacuumtube.routeros;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author clivem
 *
 */
public class Stats {

    protected final static DateFormat DF_FULL = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss,SSS z");

	protected Long id = null;
	protected Long millis;
	protected Long rxBytes;
	protected Long txBytes;
	protected Date created;
	
	/**
	 * @param id
	 * @param millis
	 * @param rxBytes
	 * @param txBytes
	 */
	public Stats(Long id, Long millis, Long rxBytes, Long txBytes) {
		this.id = id;
		this.millis = millis;
		this.rxBytes = rxBytes;
		this.txBytes = txBytes;
		this.created = new Date(millis);
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
		this.created = new Date(millis);
	}
	
	/**
	 * 
	 */
	public Stats() {
		this.id = null;
		this.millis = null;
		this.rxBytes = null;
		this.txBytes = null;
		this.created = null;
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
		synchronized (DF_FULL) {
			buf.append(DF_FULL.format(created));
		}
		buf.append("]");
		return buf.toString();
	}
}
