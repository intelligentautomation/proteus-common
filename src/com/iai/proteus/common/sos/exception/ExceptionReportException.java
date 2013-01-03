package com.iai.proteus.common.sos.exception;

/**
 * A SOAP Exception Report 
 * 
 * @author Jakob Henriksson 
 *
 */
public class ExceptionReportException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ExceptionReportException(String msg) {
		super(msg);
	}

}
