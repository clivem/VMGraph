/**
 * 
 */
package uk.org.vacuumtube.dao.hibernate;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Session.LockRequest;
import org.hibernate.SessionFactory;

import uk.org.vacuumtube.dao.Dao;
import uk.org.vacuumtube.dao.Entity;
import uk.org.vacuumtube.exception.DaoRuntimeException;

/**
 * @author clivem
 *
 */
public abstract class AbstractHibernateDaoImpl implements Dao {

    private SessionFactory sessionFactory;

	/**
     * 
     */
    protected AbstractHibernateDaoImpl() {
    }

    /**
     * @param sessionFactory
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * @return
     */
    public SessionFactory getSessionFactory() {
    	return sessionFactory;
    }

    /**
     * @return
     */
    protected final Session getSession() {
        return getSessionFactory().getCurrentSession();
    }

	/* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.Dao#entityToString(uk.org.vacuumtube.dao.Entity)
	 */
	public String entityToString(Entity entity) {
		try {
			/*
			 * Create a dummy non-locking lock request to re-attach object to the session
			 * so that lazy collections can be fetched without error.
			 */
			LockRequest lockReq = getSession().buildLockRequest(new LockOptions(LockMode.NONE));
			lockReq.lock(entity);
			return entity.toString();
		} catch (HibernateException ex) {
			throw new DaoRuntimeException(ex.getMessage(), ex);
		}
	}
	
    /* (non-Javadoc)
     * @see uk.org.vacuumtube.dao.Dao#save(uk.org.vacuumtube.dao.Entity)
     */
    public final Object save(Entity entityObject) {
        try {
            return getSession().save(entityObject);
        } catch (HibernateException ex) {
            throw new DaoRuntimeException(ex.getMessage(), ex);
        }
    }

    /* (non-Javadoc)
     * @see uk.org.vacuumtube.dao.Dao#update(uk.org.vacuumtube.dao.Entity)
     */
    public final void update(Entity entityObject) {
        try {
            getSession().saveOrUpdate(entityObject);
        } catch (HibernateException ex) {
            throw new DaoRuntimeException(ex.getMessage(), ex);
        }
    }

    /* (non-Javadoc)
     * @see uk.org.vacuumtube.dao.Dao#merge(uk.org.vacuumtube.dao.Entity)
     */
    public final Entity merge(Entity entityObject) {
        try {
            return (Entity) getSession().merge(entityObject);
        } catch (HibernateException ex) {
            throw new DaoRuntimeException(ex.getMessage(), ex);
        }
    }

    /* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.Dao#delete(uk.org.vacuumtube.dao.Entity)
	 */
	public void delete(Entity entityObject) {
		try {
			getSession().delete(entityObject);
		} catch (HibernateException ex) {
			throw new DaoRuntimeException(ex.getMessage(), ex);
		}
	}

    /* (non-Javadoc)
     * @see uk.org.vacuumtube.dao.Dao#get(java.lang.Class, java.io.Serializable)
     */
    public Entity get(Class<?> entityClass, Serializable primaryKey) {
        return get(entityClass, primaryKey, false);
    }

    /* (non-Javadoc)
     * @see uk.org.vacuumtube.dao.Dao#get(java.lang.Class, java.io.Serializable, boolean)
     */
    public Entity get(Class<?> entityClass, Serializable primaryKey, boolean lockRequired) {
        try {
            return (Entity) getSession().get(entityClass, primaryKey, 
            		new LockOptions((lockRequired) ? LockMode.PESSIMISTIC_WRITE : LockMode.NONE));
        } catch (HibernateException ex) {
            throw new DaoRuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * @param hql
     * @return
     */
    protected final Object getSingle(String hql) {
        try {
            Query query = getSession().createQuery(hql);
            query.setMaxResults(1);
            return query.uniqueResult();
        } catch (HibernateException ex) {
            throw new DaoRuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * @param hql
     * @param paramName
     * @param paramValue
     * @return
     */
    protected final Object getSingle(String hql, String paramName, Object paramValue) {
        try {
            Query query = getSession().createQuery(hql);
            query.setParameter(paramName, paramValue);
            query.setMaxResults(1);
            return query.uniqueResult();
        } catch (HibernateException ex) {
            throw new DaoRuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * @param hql
     * @param parameters
     * @return
     */
    protected final Object getSingle(String hql, Map<String, Object> parameters) {
        try {
            Query query = getSession().createQuery(hql);

            for (Iterator<Map.Entry<String, Object>> it = parameters.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Object> entry = it.next();
                query.setParameter(entry.getKey(), entry.getValue());
            }

            query.setMaxResults(1);
            return query.uniqueResult();
        } catch (HibernateException ex) {
            throw new DaoRuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * @param hql
     * @return
     */
    protected final List<?> getList(String hql) {
        try {
            Query query = getSession().createQuery(hql);
            return query.list();
        } catch (HibernateException ex) {
            throw new DaoRuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * @param hql
     * @param paramName
     * @param paramValue
     * @return
     */
    protected final List<?> getList(String hql, String paramName, Object paramValue) {
        try {
            Query query = getSession().createQuery(hql);
            query.setParameter(paramName, paramValue);
            return query.list();
        } catch (HibernateException ex) {
            throw new DaoRuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * @param hql
     * @param parameters
     * @return
     */
    protected final List<?> getList(String hql, Map<String, Object> parameters) {
        try {
            Query query = getSession().createQuery(hql);

            for (Iterator<Map.Entry<String, Object>> it = parameters.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Object> entry = it.next();
                query.setParameter(entry.getKey(), entry.getValue());
            }

            return query.list();
        } catch (HibernateException ex) {
            throw new DaoRuntimeException(ex.getMessage(), ex);
        }
    }
}
