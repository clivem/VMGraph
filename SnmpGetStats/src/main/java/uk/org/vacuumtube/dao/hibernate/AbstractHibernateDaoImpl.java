/**
 * 
 */
package uk.org.vacuumtube.dao.hibernate;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Session.LockRequest;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.org.vacuumtube.dao.Dao;
import uk.org.vacuumtube.dao.PersistableEntity;

/**
 * @author clivem
 *
 */
@Repository
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
    @Autowired
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
	 * @see uk.org.vacuumtube.dao.Dao#entityToString(uk.org.vacuumtube.dao.PersistableEntity)
	 */
	public String entityToString(PersistableEntity persistableEntity) {
		/*
		 * Create a dummy non-locking lock request to re-attach object to the session
		 * so that lazy collections can be fetched without error.
		 */
		LockRequest lockReq = getSession().buildLockRequest(new LockOptions(LockMode.NONE));
		lockReq.lock(persistableEntity);
		return persistableEntity.toString();
	}
	
    /* (non-Javadoc)
     * @see uk.org.vacuumtube.dao.Dao#save(uk.org.vacuumtube.dao.PersistableEntity)
     */
    public final Object save(PersistableEntity entityObject) {
    	return getSession().save(entityObject);
    }

    /* (non-Javadoc)
     * @see uk.org.vacuumtube.dao.Dao#update(uk.org.vacuumtube.dao.PersistableEntity)
     */
    public final void update(PersistableEntity entityObject) {
    	getSession().saveOrUpdate(entityObject);
    }

    /* (non-Javadoc)
     * @see uk.org.vacuumtube.dao.Dao#merge(uk.org.vacuumtube.dao.PersistableEntity)
     */
    public final Object merge(PersistableEntity entityObject) {
    	return getSession().merge(entityObject);
    }

    /* (non-Javadoc)
	 * @see uk.org.vacuumtube.dao.Dao#delete(uk.org.vacuumtube.dao.PersistableEntity)
	 */
	public void delete(PersistableEntity entityObject) {
		getSession().delete(entityObject);
	}

    /* (non-Javadoc)
     * @see uk.org.vacuumtube.dao.Dao#get(java.lang.Class, java.io.Serializable)
     */
    public Object get(Class<?> entityClass, Serializable primaryKey) {
        return get(entityClass, primaryKey, false);
    }

    /* (non-Javadoc)
     * @see uk.org.vacuumtube.dao.Dao#get(java.lang.Class, java.io.Serializable, boolean)
     */
    public Object get(Class<?> entityClass, Serializable primaryKey, boolean lockRequired) {
    	return getSession().get(entityClass, primaryKey, 
            		new LockOptions((lockRequired) ? LockMode.PESSIMISTIC_WRITE : LockMode.NONE));
    }

    /* (non-Javadoc)
     * @see uk.org.vacuumtube.dao.Dao#load(java.lang.Class, java.io.Serializable)
     */
    public Object load(Class<?> name, Serializable id) {
    	try {
    		return getSession().load(name, id);
    	} catch (ObjectNotFoundException ex) {
    		return null;
    	}
    }
    
    /**
     * @param hql
     * @return
     */
    protected final Object getSingle(String hql) {
        Query query = getSession().createQuery(hql);
        query.setMaxResults(1);
        return query.uniqueResult();
    }

    /**
     * @param hql
     * @param paramName
     * @param paramValue
     * @return
     */
    protected final Object getSingle(String hql, String paramName, Object paramValue) {
        Query query = getSession().createQuery(hql);
        query.setParameter(paramName, paramValue);
        query.setMaxResults(1);
        return query.uniqueResult();
    }

    /**
     * @param hql
     * @param parameters
     * @return
     */
    protected final Object getSingle(String hql, Map<String, Object> parameters) {
        Query query = getSession().createQuery(hql);

        for (Iterator<Map.Entry<String, Object>> it = parameters.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> entry = it.next();
            query.setParameter(entry.getKey(), entry.getValue());
        }

        query.setMaxResults(1);
        return query.uniqueResult();
    }

    /**
     * @param hql
     * @return
     */
	protected final List<?> getList(String hql) {
	    Query query = getSession().createQuery(hql);
	    return query.list();
    }

    /**
     * @param hql
     * @param paramName
     * @param paramValue
     * @return
     */
	protected final List<?> getList(String hql, String paramName, Object paramValue) {
        Query query = getSession().createQuery(hql);
        query.setParameter(paramName, paramValue);
        return query.list();
    }

    /**
     * @param hql
     * @param parameters
     * @return
     */
	protected final List<?> getList(String hql, Map<String, Object> parameters) {
        Query query = getSession().createQuery(hql);

        for (Iterator<Map.Entry<String, Object>> it = parameters.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> entry = it.next();
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.list();
    }
}
