/**
 * 
 */
package uk.org.vacuumtube.dao.hibernate;

import java.util.Date;
import java.util.Iterator;

import org.hibernate.EmptyInterceptor;

import uk.org.vacuumtube.dao.AbstractTimestampEntity;

/**
 * @author clivem
 *
 */
public class MySqlTimestampInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 6119618691845929405L;

	@Override
	public void postFlush(@SuppressWarnings("rawtypes") Iterator entities) {
		while (entities.hasNext()) {
			Object entity = entities.next();
			if(entity instanceof AbstractTimestampEntity && ((AbstractTimestampEntity) entity).getUpdated() != null) { 
				((AbstractTimestampEntity) entity).setUpdated(
						(new Date((((AbstractTimestampEntity) entity).getUpdated().getTime() / 1000) * 1000))); 
			}
			if(entity instanceof AbstractTimestampEntity && ((AbstractTimestampEntity) entity).getCreated() != null) { 
				((AbstractTimestampEntity) entity).setCreated(
						(new Date((((AbstractTimestampEntity) entity).getCreated().getTime() / 1000) * 1000))); 
			}
		}
	}
}
	