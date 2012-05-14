package uk.org.vacuumtube.serialization.test;

import java.io.IOException;

/**
 * @author clivem
 *
 */
public interface SerializationFactory {
	
	/**
	 * @param object
	 * @return the byte array containing the serialized object
	 * @throws IOException
	 */
	public byte[] serialize(Object object) throws IOException;
	
	/**
	 * @param bytes
	 * @return the de-serialized object
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException;
	
	/**
	 * @return the name of the implementation for logging purposes
	 */
	public String getName();
	
	public boolean isCompress();
	
	public void setCompress(boolean compress);
	
	public boolean isBuffer();
	
	public void setBuffer(boolean buffer);
}
