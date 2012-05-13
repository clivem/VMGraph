/**
 * 
 */
package uk.org.vacuumtube.dao;

import java.io.Serializable;


/**
 * @author clivem
 *
 */
public interface Dao {
    
	/**
	 * @param persistableEntity
	 * @return a string representation of the entity
	 */
	public String entityToString(PersistableEntity persistableEntity);

	/**
     * @param entityObject
     * @return
     */
    public Object save(PersistableEntity entityObject);
    
    /**
     * @param entityObject
     */
    public void update(PersistableEntity entityObject);

    /**
     * @param entityObject
     * @return
     */
    public Object merge(PersistableEntity entityObject);
    
    /**
     * @param entityObject
     */
    public void delete(PersistableEntity entityObject);
    
    /**
     * @param entityClass
     * @param primaryKey
     * @return
     */
    public Object get(Class<?> entityClass, Serializable primaryKey);
    
    /**
     * @param entityClass
     * @param primaryKey
     * @param lockRequired
     * @return
     */
    public Object get(Class<?> entityClass, Serializable primaryKey, boolean lockRequired);
    
    /**
     * @param name
     * @param id
     * @return
     */
    public Object load(Class<?> name, Serializable id);
}

