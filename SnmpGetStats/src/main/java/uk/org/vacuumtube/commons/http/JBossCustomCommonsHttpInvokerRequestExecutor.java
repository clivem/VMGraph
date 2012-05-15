package uk.org.vacuumtube.commons.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.Map;

import org.apache.commons.httpclient.HttpConnectionManager;
import org.jboss.serial.io.JBossObjectInputStream;
import org.jboss.serial.io.JBossObjectOutputStream;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;

/**
 * @author clivem
 *
 */
public class JBossCustomCommonsHttpInvokerRequestExecutor extends CustomCommonsHttpInvokerRequestExecutor {

	/**
	 * @param httpConnectionManager
	 */
	public JBossCustomCommonsHttpInvokerRequestExecutor(HttpConnectionManager httpConnectionManager, 
			Map<String, TransportClientProperties> transportClientPropertiesMap) {
		super(httpConnectionManager, transportClientPropertiesMap);
	}

	/* (non-Javadoc)
	 * @see org.springframework.remoting.httpinvoker.AbstractHttpInvokerRequestExecutor#writeRemoteInvocation(org.springframework.remoting.support.RemoteInvocation, java.io.OutputStream)
	 */
	protected void writeRemoteInvocation(RemoteInvocation invocation, OutputStream os) throws IOException {
		JBossObjectOutputStream oos = new JBossObjectOutputStream(os);
		try {
			oos.writeObject(invocation);
			oos.flush();
		} finally {
			oos.close();
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.remoting.httpinvoker.AbstractHttpInvokerRequestExecutor#readRemoteInvocationResult(java.io.InputStream, java.lang.String)
	 */
	protected RemoteInvocationResult readRemoteInvocationResult(InputStream is, String codebaseUrl) throws IOException, ClassNotFoundException {
		JBossObjectInputStream ois = new JBossObjectInputStream(is);
		try {
			Object obj = ois.readObject();
			if (!(obj instanceof RemoteInvocationResult)) {
				throw new RemoteException(
						"Deserialized object needs to be assignable to type ["
								+ RemoteInvocationResult.class.getName() + "]: "
								+ obj);
			}
			return (RemoteInvocationResult) obj;
		} finally {
			ois.close();
		}
	}
}
