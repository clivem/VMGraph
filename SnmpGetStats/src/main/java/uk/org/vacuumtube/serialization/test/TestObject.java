package uk.org.vacuumtube.serialization.test;

import java.io.Serializable;

/**
 * @author clivem
 *
 */
public class TestObject implements Serializable {
	
	static final long serialVersionUID = -3193357271891865972L;
	
	// This counter must only be incremented when creating the new object - not from deserialization
	private static long INSTANCE_COUNT = 0;
	
	private char c;
	private byte b;
	private short s;
	private int i;
	private long l;
	private String string;
	private String[] string_array;
	private long instanceCount;
	
	@SuppressWarnings("unused")
	private TestObject() {
		super();
	}
	
	public TestObject(char c, byte b, short s, int i, long l, 
			String string, String[] string_array, long instanceCount) {
		this.c = c;
		this.b = b;
		this.s = s;
		this.i = i;
		this.l = l;
		this.string = string;
		this.string_array = string_array;
		this.instanceCount = instanceCount;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TestObject)) {
			return false;
		}
		
		TestObject other = (TestObject) obj;
		if (other.string_array == null || this.string_array == null) {
			return false;
		}
		if (other.string_array.length != this.string_array.length) {
			return false;
		}
		for (int  i = 0; i < other.string_array.length; i++) {
			if (!other.string_array[i].equals(this.string_array[i])) {
				return false;
			}
		}
		
		if (other.instanceCount == this.instanceCount &&
			other.c == this.c &&
			other.b == this.b &&
			other.s == this.s &&
			other.i == this.i &&
			other.l == this.l &&
			other.string.equals(this.string)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @return the instance count
	 */
	public final static long getInstanceCount() {
		return INSTANCE_COUNT;
	}
	
	public final static TestObject create() {
		return new TestObject((char) 'c', (byte) 0x1, (short) 10, (int) 1000, (long) 1000000,
		  "This is a test. This is a test. This is a test. This is a test. ", 
		  new String[] {
				"This is a test. This is a test. This is a test. This is a test. ",
				"This is a test. This is a test. This is a test. This is a test. ",
				"This is a test. This is a test. This is a test. This is a test. ",
				"This is a test. This is a test. This is a test. This is a test. ",
				"This is a test. This is a test. This is a test. This is a test. ",
				"This is a test. This is a test. This is a test. This is a test. ",
				"This is a test. This is a test. This is a test. This is a test. ",
				"This is a test. This is a test. This is a test. This is a test. ",
				"This is a test. This is a test. This is a test. This is a test. ",
				"This is a test. This is a test. This is a test. This is a test. ",
				"This is a test. This is a test. This is a test. This is a test. ",
				"This is a test. This is a test. This is a test. This is a test. ",
				"This is a test. This is a test. This is a test. This is a test. "},
			++INSTANCE_COUNT);
	}
	
	public final static TestObject[] createArray(int size) {
		TestObject[] objects = new TestObject[size];
		for (int i = 0; i < size; i++) {
			objects[i] = create();
		}
		return objects;
	}
}
