package com.mdm.infrastructure.exception;

/** 
 * Exception thrown in case of a runtime exception
 */
public class MDMTechnicalException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public MDMTechnicalException( String message ) {
		super(message);
	}

}
