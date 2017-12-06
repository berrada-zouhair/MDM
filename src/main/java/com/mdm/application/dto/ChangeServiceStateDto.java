package com.mdm.application.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChangeServiceStateDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String serviceName;
	
	private boolean checked;

}
