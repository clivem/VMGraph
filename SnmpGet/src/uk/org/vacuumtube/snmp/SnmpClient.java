/**
 * 
 */
package uk.org.vacuumtube.snmp;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 * @author clivem
 *
 */
public class SnmpClient {

	static final Logger LOGGER = Logger.getLogger(SnmpClient.class);

	private String address;
	private Snmp snmp;

	/**
	 * @param address
	 * @throws IOException
	 */
	public SnmpClient(String address) throws IOException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("SnmpClient(address=" + address + ")");
		}
 		this.address = address;
		start();
	}
	
	/**
	 * @throws IOException
	 */
	private void start() throws IOException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("SnmpClient.start(): START");
		}
		
		TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
		//TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping(new UdpAddress("192.168.0.60/52222"), true);
		snmp = new Snmp(transport);
		transport.listen();		
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("SnmpClient.start(): END");
		}
	}
	
	/**
	 * @throws IOException
	 */
	public void stop() throws IOException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("SnmpClient.stop()");
		}
		snmp.close();
	}
	
	/**
	 * @param oids
	 * @return
	 * @throws IOException
	 */
	public ResponseEvent get(OID oids[]) throws IOException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("SnmpClient.get(oids=" + oids + ")");
		}
		
		return(snmp.send(getPDU(oids), getTarget(), null));
	}

	/**
	 * @return
	 */
	private Target getTarget() {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("SnmpClient.getTarget()");
		}
		Address targetAddress = GenericAddress.parse(address);
		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString("public"));
		target.setAddress(targetAddress);
		target.setRetries(2);
		target.setTimeout(1500);
		target.setVersion(SnmpConstants.version2c);
		return target;
	}

	/**
	 * @param oids
	 * @return
	 */
	private PDU getPDU(OID oids[]) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("SnmpClient.getPDU(oids=" + oids + ")");
		}

		PDU pdu = new PDU();
		for (OID oid : oids) {
			pdu.add(new VariableBinding(oid));
		}
	 	   
		pdu.setType(PDU.GET);
		return pdu;
	}	
}
