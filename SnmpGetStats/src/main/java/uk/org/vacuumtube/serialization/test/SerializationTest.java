package uk.org.vacuumtube.serialization.test;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

/**
 * @author clivem
 *
 */
public class SerializationTest {

	private static final Logger logger = Logger.getLogger(SerializationTest.class);
	/*
	private final static List<SerializationFactory> FACTORY_LIST = new ArrayList<SerializationFactory>();
	static {
		FACTORY_LIST.add(new HessianFactory());
		FACTORY_LIST.add(new BurlapFactory());
		FACTORY_LIST.add(new XStreamFactory());
		FACTORY_LIST.add(new SunJavaFactory());
		FACTORY_LIST.add(new JBossFactory());		
	}
	*/
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SerializationFactory[] factories = new SerializationFactory[] {
				new HessianFactory(), 
				new BurlapFactory(),
				new XStreamFactory(),
				new SunJavaFactory(),
				new JBossFactory()};
		
		test(false, true, factories);
		test(true, true, factories);
	}
	
	public static void test(boolean compress, boolean buffer, SerializationFactory... factoryList) {
		for (SerializationFactory factory : factoryList) {
			factory.setBuffer(buffer);
			factory.setCompress(compress);
			test(factory, 100);
		}
	}
	
	public static void test(SerializationFactory factory, int number) {
		int count = 0;
		int byteCount = 0;
		long start = -1;
		try {
			for (int i = 0; i < number + 100; i++) {
				if (i == 100) {
					start = System.currentTimeMillis();
				}
				
				/*
				 * TestObject
				 */
				Object object = TestObject.create();
				long instanceCount = TestObject.getInstanceCount();
				byte[] bytes = factory.serialize(object);
				Object object1 = factory.deserialize(bytes);
				Assert.isTrue(object.equals(object1), "!object.equals(object1)");
				Assert.isTrue(TestObject.getInstanceCount() == instanceCount, 
						"TestObject.getInstanceCount() != instanceCount");

				if (i >= 100) {
					count++;
					byteCount += bytes.length;
				}
			}
			long time = System.currentTimeMillis() - start;
			logger.info(factory.getName() + ".\n\tFinished in " + time + "ms. Count=" + count + 
					", Serialized Object Size=" + (byteCount / count) + 
					" bytes, Avg=" + (time / (double) count) + "ms");
		} catch (Exception e) {
			logger.error("", e);
		}
	}
}
