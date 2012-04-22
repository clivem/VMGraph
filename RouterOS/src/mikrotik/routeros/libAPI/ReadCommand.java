package mikrotik.routeros.libAPI;

/*
 * CommandRead.java
 *
 * Created on 19 June 2007, 10:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.io.*;
import java.util.concurrent.*;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
//import java.util.logging.Level;
//import java.util.logging.Logger;

/**
 * 
 * @author janisk
 */
public class ReadCommand implements Runnable {

	private static final Logger logger = Logger.getLogger(ReadCommand.class);
	
	private DataInputStream in = null;
	LinkedBlockingQueue<Response> queue = null;

	/**
	 * Creates a new instance of CommandRead
	 * 
	 * @param in
	 *            - Data input stream of socket
	 * @param queue
	 *            - data output inteface
	 */
	public ReadCommand(DataInputStream in, LinkedBlockingQueue<Response> queue) {
		this.in = in;
		this.queue = queue;
	}

	@Override
	public void run() {
		byte b = 0;
		String s = "";
		//char ch;
		int a = 0;
		while (true) {
			int sk = 0;
			try {
				//int avail = in.available();
				//System.out.println("Available: " + avail);
				a = in.read();
			} catch (IOException ex) {
				//ex.printStackTrace();
				//System.out.println("ReadCommand Exiting...");
				logger.log(Level.WARN, "run() exiting...", ex);
				return;
			}
			if (a != 0 && a > 0) {
				if (a < 0x80) {
					sk = a;
				} else {
					if (a < 0xC0) {
						a = a << 8;
						try {
							a += in.read();
						} catch (IOException ex) {
							//ex.printStackTrace();
							//System.out.println("ReadCommand Exiting...");
							logger.log(Level.WARN, "run() exiting...", ex);
							return;
						}
						sk = a ^ 0x8000;
					} else {
						if (a < 0xE0) {
							try {
								for (int i = 0; i < 2; i++) {
									a = a << 8;
									a += in.read();
								}
							} catch (IOException ex) {
								//Logger.getLogger(ReadCommand.class.getName()).log(Level.SEVERE, null, ex);
								//ex.printStackTrace();
								//System.out.println("ReadCommand Exiting...");
								logger.log(Level.WARN, "run() exiting...", ex);
								return;
							}
							sk = a ^ 0xC00000;
						} else {
							if (a < 0xF0) {
								try {
									for (int i = 0; i < 3; i++) {
										a = a << 8;
										a += in.read();
									}
								} catch (IOException ex) {
									//Logger.getLogger(ReadCommand.class.getName()).log(Level.SEVERE, null, ex);
									//ex.printStackTrace();
									//System.out.println("ReadCommand Exiting...");
									logger.log(Level.WARN, "run() exiting...", ex);
									return;
								}
								sk = a ^ 0xE0000000;
							} else {
								if (a < 0xF8) {
									try {
										a = 0;
										for (int i = 0; i < 5; i++) {
											a = a << 8;
											a += in.read();
										}
									} catch (IOException ex) {
										//Logger.getLogger(ReadCommand.class.getName()).log(Level.SEVERE, null, ex);
										//ex.printStackTrace();
										//System.out.println("ReadCommand Exiting...");
										logger.log(Level.WARN, "run() exiting...", ex);
										return;
									}
								} else {
								}
							}
						}
					}
				}
				s += "\n";
				byte[] bb = new byte[sk];
				try {
					a = in.read(bb, 0, sk);
				} catch (IOException ex) {
					a = 0;
					//ex.printStackTrace();
					//System.out.println("ReadCommand Exiting...");
					logger.log(Level.WARN, "run() exiting...", ex);
					return;
				}
				if (a > 0) {
					s += new String(bb);
				}
			} else if (b == -1) {
				//System.out.println("Error, it should not happen ever, or connected to wrong port");
				logger.log(Level.WARN, "Error, it should not happen ever, or connected to wrong port!");
			} else {
				if (s.length() > 0) {
					try {
						queue.put(new Response(s));
					} catch (InterruptedException ex) {
						//ex.printStackTrace();
						//System.out.println("ReadCommand Exiting...");
						logger.log(Level.WARN, "run() exiting...", ex);
						return;
					}
					s = "";
				}
			}
		}
	}
}
