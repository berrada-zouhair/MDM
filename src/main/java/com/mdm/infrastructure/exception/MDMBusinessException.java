package com.mdm.infrastructure.exception;

/** 
 * Exception thrown in case of an illegal business process
 */
public class MDMBusinessException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public MDMBusinessException( String message ) {
		super(message);
	}

}
