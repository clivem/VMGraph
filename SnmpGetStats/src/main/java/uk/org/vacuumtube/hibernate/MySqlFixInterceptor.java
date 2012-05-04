/**
 * 
 */
package uk.org.vacuumtube.hibernate;

import java.util.Date;
import java.util.Iterator;

import org.hibernate.EmptyInterceptor;

import uk.org.vacuumtube.dao.Persistable;

/**
 * @author clivem
 *
 */
public class MySqlFixInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 6119618691845929405L;

	@Override
	public void postFlush(@SuppressWarnings("rawtypes") Iterator entities) {
		while (entities.hasNext()) {
			Object entity = entities.next();
			if(entity instanceof Persistable && ((Persistable) entity).getUpdated() != null) { 
				((Persistable) entity).setUpdated(
						(new Date((((Persistable) entity).getUpdated().getTime() / 1000) * 1000))); 
			}
			if(entity instanceof Persistable && ((Persistable) entity).getCreated() != null) { 
				((Persistable) entity).setCreated(
						(new Date((((Persistable) entity).getCreated().getTime() / 1000) * 1000))); 
			}
		}
	}
}
	