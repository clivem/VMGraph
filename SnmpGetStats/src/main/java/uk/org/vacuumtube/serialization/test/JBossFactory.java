package uk.org.vacuumtube.serialization.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.jboss.serial.io.JBossObjectInputStream;
import org.jboss.serial.io.JBossObjectOutputStream;

/**
 * @author clivem
 *
 */
public class JBossFactory extends AbstractSerializationFactory {

	public JBossFactory() {
		super();
	}
	
	public JBossFactory(boolean compress, boolean buffer) {
		super(compress, buffer);
	}
	
	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.serialization.test.SerializationFactory#deserialize(byte[])
	 */
	public Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		GZIPInputStream gis = null;
		BufferedInputStream bis = null;
		JBossObjectInputStream ois = null;
		try {
			if (compress) {
				gis = new GZIPInputStream(bais);
				if (buffer) {
					bis = new BufferedInputStream(gis);
					ois = new JBossObjectInputStream(bis);
				} else {
					ois = new JBossObjectInputStream(gis);
				}
			} else {
				if (buffer) {
					bis = new BufferedInputStream(bais);
					ois = new JBossObjectInputStream(bis);
				} else {
					ois = new JBossObjectInputStream(bais);
				}
			}
			return ois.readObject();
		} finally {
			if (ois != null) {
				try { 
					ois.close(); 
				} catch (Exception e) {}
			}
			
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
		JBossObjectOutputStream mvos = null;
		BufferedOutputStream bos = null;
		GZIPOutputStream gos = null;
		try {
			if (compress) {
				gos = new GZIPOutputStream(baos, true);
				if (buffer) {
					bos = new BufferedOutputStream(gos);
					mvos = new JBossObjectOutputStream(bos);
				} else {
					mvos = new JBossObjectOutputStream(gos);
				}
			} else {
				if (buffer) {
					bos = new BufferedOutputStream(baos);
					mvos = new JBossObjectOutputStream(bos);
				} else {
					mvos = new JBossObjectOutputStream(baos);
				}
			}
			mvos.writeObject(object);
			mvos.flush();
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
			if (mvos != null) {
				try { 
					mvos.close(); 
				} catch (Exception e) {}
			}
			
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
		return "JBoss Serialization [compress=" + compress + ", buffer=" + buffer + "]";
	}
}
