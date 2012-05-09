/**
 * 
 */
package uk.org.vacuumtube.exception;

/**
 * @author clivem
 *
 */
public class DaoRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -2566586246859699215L;

	public DaoRuntimeException() {
        super();
    }

    public DaoRuntimeException(String message) {
        super(message);
    }

    public DaoRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoRuntimeException(Throwable cause) {
        super(cause);
    }
}
