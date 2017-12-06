package com.mdm.interfaces.endpoints.responses;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Result included in response got from calling MDM web services.
 */

@AllArgsConstructor
@Getter
@Setter
public class MDMResultWebServiceCall implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** Response code */
	private int code;
	
	/** Response content */
	private Object content;

}
