package com.mdm.application.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LocationDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String ip;
	private String country_code;
	private String country_name;
	private String region_code;
	private String region_name;
	private String city;
	private String zip_cod;
	private String time_zone;
	private Double latitude;
	private Double longitude;
	private Integer metro_code;

}
