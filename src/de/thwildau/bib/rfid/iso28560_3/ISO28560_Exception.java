package de.thwildau.bib.rfid.iso28560_3;

/**
 * Exception for ISO28560_3.
 * 
 * @author <a href="mailto:patrick.rogalla@th-koeln.de">Patrick Rogalla</a>
 *
 */
public class ISO28560_Exception extends Exception {
	
	
	/**
	 * Initialize an exception with a message.
	 * 
	 * @param msg the message describing the exception 
	 */
	public ISO28560_Exception(String msg) {
		super(msg);
	}
	
	/**
	 * Initialize an exception with a message and a throwable source.
	 * 
	 * @param msg the message describing the exception
	 * @param source the exception source
	 */
	public ISO28560_Exception(String msg, Throwable source) {
		super(msg, source);
	}
}
