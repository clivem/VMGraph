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
	 * @param entity
	 * @return a string representation of the entity
	 */
	public String entityToString(Entity entity);

	/**
     * @param entityObject
     * @return
     */
    public Object save(Entity entityObject);
    
    /**
     * @param entityObject
     */
    public void update(Entity entityObject);

    /**
     * @param entityObject
     * @return
     */
    public Entity merge(Entity entityObject);
    
    /**
     * @param entityObject
     */
    public void delete(Entity entityObject);
    
    /**
     * @param entityClass
     * @param primaryKey
     * @return
     */
    public Entity get(Class<?> entityClass, Serializable primaryKey);
    
    /**
     * @param entityClass
     * @param primaryKey
     * @param lockRequired
     * @return
     */
    public Entity get(Class<?> entityClass, Serializable primaryKey, boolean lockRequired);
}

