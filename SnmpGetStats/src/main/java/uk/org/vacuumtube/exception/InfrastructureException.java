/**
 * 
 */
package uk.org.vacuumtube.exception;

/**
 * @author clivem
 *
 */
public class InfrastructureException extends RuntimeException {

	static final long serialVersionUID = -3193357271891865972L;

	public InfrastructureException() {
	}

	public InfrastructureException(String message) {
		super(message);
	}

	public InfrastructureException(String message, Throwable cause) {
		super(message, cause);
	}

	public InfrastructureException(Throwable cause) {
		super(cause);
	}
}
