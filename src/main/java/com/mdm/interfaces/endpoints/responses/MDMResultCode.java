package com.mdm.interfaces.endpoints.responses;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** Enum representing status call of web services */
@AllArgsConstructor(access=AccessLevel.PRIVATE)
@Getter
public enum MDMResultCode {
	CONNECTION_OK(1, "Success connection to MDM"),
	CONNECTION_KO(2, "Fail connection to MDM"),
	SEND_ACTION_OK(3, "Success send action"),
	SEND_ACTION_KO(4, "Fail send action");
	
	private int code;
	private String content;

}
