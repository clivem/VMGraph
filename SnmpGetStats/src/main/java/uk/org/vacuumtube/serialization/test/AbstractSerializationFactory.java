/**
 * 
 */
package uk.org.vacuumtube.serialization.test;

/**
 * @author clivem
 *
 */
public abstract class AbstractSerializationFactory implements SerializationFactory {

	protected boolean compress = false;
	protected boolean buffer = false;
	
	public AbstractSerializationFactory() {
		this(false, false);
	}

	public AbstractSerializationFactory(boolean compress, boolean buffer) {
		this.compress = compress;
		this.buffer = buffer;
	}

	/**
	 * @return the compress
	 */
	public boolean isCompress() {
		return compress;
	}

	/**
	 * @param compress the compress to set
	 */
	public void setCompress(boolean compress) {
		this.compress = compress;
	}

	/**
	 * @return the buffer
	 */
	public boolean isBuffer() {
		return buffer;
	}

	/**
	 * @param buffer the buffer to set
	 */
	public void setBuffer(boolean buffer) {
		this.buffer = buffer;
	}
}
