package com.mdm.domain.models.entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity that represents a Device in database
 */
@Entity(name="DEVICE")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Device {
	
	@Id
	@Column(name="IMEI", nullable=false)
	private String imei;
	
	@Column(name="IP")
	private String ip;
	
	@Column(name="LATITUDE")
	private Double latitude;
	
	@Column(name="LONGITUDE")
	private Double longitude;
	
	@Column(name="CONNECTED")
	private boolean connected;
	
	/** An id generated and used by GCM so receive notifications */
	@Column(name="REGISTER_TOKEN")
	private String registerTokek;
	
	/** Installed apps stored in database in JSON format*/
	@Column(name="INSTALLED_APPS")
	private String installedApps;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastConnection;
	
	public Device(String ip, String imei, Double latitude, Double longitude, String registerTokek) {
		this.ip = ip;
		this.imei = imei;
		this.latitude = latitude;
		this.longitude = longitude;
		this.registerTokek = registerTokek;
	}
	
	/** Convert apps from JSON format to list of strings */
	@SuppressWarnings("unchecked")
	public List<String> getInstalledAppsList() throws JsonParseException, JsonMappingException, IOException {
		if( installedApps == null ) return new ArrayList<>(0);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(installedApps.getBytes(), List.class);
	}
	
}
