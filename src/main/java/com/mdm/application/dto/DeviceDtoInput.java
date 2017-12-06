package com.mdm.application.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

/** 
 * DTO that represents device information sent by the device itself
 */
@Data
@AllArgsConstructor
public class DeviceDtoInput implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String ip;
	
	private String imei;
	
	private Double latitude;
	
	private Double longitude;
	
	private String registerToken;

}
