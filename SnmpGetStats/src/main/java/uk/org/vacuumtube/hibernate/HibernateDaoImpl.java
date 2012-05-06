/**
 * 
 */
package uk.org.vacuumtube.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import uk.org.vacuumtube.dao.HibernateDao;
import uk.org.vacuumtube.exception.InfrastructureException;

/**
 * @author clivem
 *
 */
public class HibernateDaoImpl implements HibernateDao {

	private SessionFactory sessionFactory;
	
    /**
     * 
     */
    public HibernateDaoImpl(){
    }

	/**
	 * @return Returns the sessionFactory.
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param sessionFactory The sessionFactory to set.
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * @return Returns the current session associated with the thread or transaction
	 */
	public Session getSession() {
		return getSessionFactory().getCurrentSession();
	}

	/**
	 * @param object
	 * @throws InfrastructureException
	 */
	public void save(Object object) throws InfrastructureException {
        try {
            getSession().save(object);
        } catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

	/**
	 * @param object
	 * @throws InfrastructureException
	 */
	public void update(Object object) throws InfrastructureException {
        try {
            getSession().update(object);
        } catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    /**
     * @param object
     * @return
     * @throws InfrastructureException
     */
    public Object merge(Object object) throws InfrastructureException {
        try {
            return getSession().merge(object);
        } catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

	/**
	 * @param object
	 * @throws InfrastructureException
	 */
	public void makePersistent(Object object) throws InfrastructureException {
        try {
            getSession().saveOrUpdate(object);
        } catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    /**
     * @param object
     * @throws InfrastructureException
     */
    public void makeTransient(Object object) throws InfrastructureException {
        try {
            getSession().delete(object);
        } catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }
    
    /**
     * @param queryString
     * @param parameters
     * @param alias
     * @param lockMode
     * @return
     */
    public List<?> findByNamedParam(String queryString, Map<String, Object> parameters, String alias, LockMode lockMode) 
    		throws InfrastructureException {
    	try {
	    	Query query = getSession().createQuery(queryString);
	    	
	    	for (Iterator<Entry<String, Object>> it = parameters.entrySet().iterator(); it.hasNext();) {
				Entry<String, Object> entry = it.next();
				String name = entry.getKey();
				Object value = entry.getValue();
				
				if (value == null) {
					query.setParameter(name, value);
				} else if (value instanceof Collection) {
					query.setParameterList(name, (Collection<?>)value);
				} else if (value.getClass().isArray()) {
					query.setParameterList(name, (Object[])value);
				} else {
					query.setParameter(name, value);
				}
			}
	    	
	    	if (alias != null) {    	
	    		query.setLockMode(alias, lockMode);
	    	}
	    	
	    	return query.list();
    	} catch (HibernateException he) {
    		throw new InfrastructureException(he);
    	}
    }

    /**
     * @param c
     * @return
     */
    private final List<Object> getUniqueList(Collection<?> c) {
    	List<Object> l = new ArrayList<Object>();
    	Object prev = null;
    	for( Iterator<?> it = c.iterator(); it.hasNext(); ) {
    		Object o = it.next();
    		if(o != prev) {
    			l.add(o);
    			prev = o;
    		}
    	}
    	return l;
    } 
    
    /**
     * @param hql - HQL query string. Must contain order by ID clause.
     * @return a list of distinct objects.
     */
    protected List<Object> getList(String hql) {
		try {
	        Query query = getSession().createQuery(hql);	        	        
	        return getUniqueList(query.list());
	    } catch (HibernateException e) {
	        throw new InfrastructureException(e);
	    }
	}
    
    /** 
     * @param hql - HQL query string. Must contain order by ID clause.
     * @param paramName
     * @param paramValue
     * @return a list of distinct objects.
     */
	protected List<Object> getList(String hql, String paramName, Object paramValue) {
		try {
	        Query query = getSession().createQuery(hql);
	        query.setParameter(paramName, paramValue);	        	        
	        return getUniqueList(query.list());	        
	    } catch (HibernateException e) {
	        throw new InfrastructureException(e);
	    }
	}
	
	/**
	 * @param hql
	 * @param paramName
	 * @param paramValue
	 * @return
	 */
	public Object getSingle(String hql, String paramName, Object paramValue) {
		try {
            Query query = getSession().createQuery(hql);
            query.setParameter(paramName, paramValue);
            query.setMaxResults(1);   
            return query.uniqueResult();
        } catch (HibernateException e) {
            throw new InfrastructureException(e);
        }
	}
}
