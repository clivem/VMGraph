/**
 * 
 */
package uk.org.vacuumtube.dao;

import java.io.Serializable;

import uk.org.vacuumtube.exception.InfrastructureException;

/**
 * @author clivem
 *
 */
public interface HibernateDao {

	/**
	 * @param object
	 * @throws InfrastructureException
	 */
	public Serializable save(Object object) throws InfrastructureException;

	/**
	 * @param object
	 * @throws InfrastructureException
	 */
	public void update(Object object) throws InfrastructureException;

	/**
     * @param object
     * @return
     * @throws InfrastructureException
     */
    public Object merge(Object object) throws InfrastructureException;
    
	/**
	 * @param object
	 * @throws InfrastructureException
	 */
	public void makePersistent(Object object) throws InfrastructureException;

    /**
     * @param object
     * @throws InfrastructureException
     */
    public void makeTransient(Object object) throws InfrastructureException;
}
