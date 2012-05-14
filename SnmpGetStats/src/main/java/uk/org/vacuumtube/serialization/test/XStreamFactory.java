package uk.org.vacuumtube.serialization.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.thoughtworks.xstream.XStream;

/**
 * @author clivem
 *
 */
public class XStreamFactory extends AbstractSerializationFactory {

	private static XStream xstream = new XStream();
	
	public XStreamFactory() {
		super();
	}

	public XStreamFactory(boolean compress, boolean buffer) {
		super(compress, buffer);
	}
	
	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.serialization.test.SerializationFactory#deserialize(byte[])
	 */
	public Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		GZIPInputStream gis = null;
		BufferedInputStream bis = null;
		try {
			if (compress) {
				gis = new GZIPInputStream(bais);
				if (buffer) {
					bis = new BufferedInputStream(gis);
					return xstream.fromXML(bis);
				} else {
					return xstream.fromXML(gis);
				}
			} else {
				if (buffer) {
					bis = new BufferedInputStream(bais);
					return xstream.fromXML(bis);
				} else {
					return xstream.fromXML(bais);
				}
			}
		} finally {
			if (bis != null) {
				try { 
					bis.close(); 
				} catch (Exception e) {}
			} else if (gis != null) {
				try { 
					gis.close(); 
				} catch (Exception e) {}
			} else {
				try { 
					bais.close(); 
				} catch (Exception e) {}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.serialization.test.SerializationFactory#serialize(java.lang.Object)
	 */
	public byte[] serialize(Object object) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedOutputStream bos = null;
		GZIPOutputStream gos = null;
		try {
			if (compress) {
				gos = new GZIPOutputStream(baos, true);
				if (buffer) {
					bos = new BufferedOutputStream(gos);
					xstream.toXML(object, bos);
				} else {
					xstream.toXML(object, gos);
				}
			} else {
				if (buffer) {
					bos = new BufferedOutputStream(baos);
					xstream.toXML(object, bos);
				} else {
					xstream.toXML(object, baos);
				}
			}
			if (bos != null) {
				bos.flush();
			} else if (gos != null) {
				gos.flush();
			}
			if (gos != null) {
				gos.finish();
			}
			return baos.toByteArray();
		} finally {
			if (bos != null) {
				try { 
					bos.close(); 
				} catch (Exception e) {}
			} else if (gos != null) {
				try { 
					gos.close(); 
				} catch (Exception e) {}
			} else {
				try { 
					baos.close(); 
				} catch (Exception e) {}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.serialization.test.SerializationFactory#getName()
	 */
	public String getName() {
		return "XStream Serialization [compress=" + compress + ", buffer=" + buffer + "]";
	}	
}
